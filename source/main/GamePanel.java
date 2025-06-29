package main;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.FloatControl;
import javax.swing.JPanel;
import javax.swing.JLabel;
import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.text.DecimalFormat;
import entity.Player;
import main.tile.TileManager;
import object.SuperObject;

/**
 * GamePanel is the main game screen where all rendering, input handling,
 * game loop, state management, and game mechanics (e.g., player movement,
 * customer request handling, recipe logic) take place.
 *
 * It handles the core game update loop, drawing the player, tile map,
 * objects, timer, UI text, and coordinating between game levels.
 */
public class GamePanel extends JPanel implements Runnable {

    final int originalTileSize = 16;
    final int scale = 3;
    public final int tileSize = originalTileSize * scale;
    public final int maxScreenCol = 16;
    public final int maxScreenRow = 12;
    public final int screenWidth = tileSize * maxScreenCol;
    public final int screenHeight = tileSize * maxScreenRow;

    int FPS = 60;

    // The screen and world coordinates are the same since the map only
    // exists of what is visible on the screen.
    public final int maxWorldCol = maxScreenCol;
    public final int maxWorldRow = maxScreenRow;
    public final int worldWidth = screenWidth;
    public final int worldHeight = screenHeight;

    int level = 1;
    int customersServed = 0; // Track customer requests fulfilled
    int totalRequests = 0; // Track total requests given
    boolean levelComplete = false;
    boolean gameOver = false;
    private long levelCompleteTime = 0;
    private static final int LEVEL_COMPLETE_DELAY = 2000;
    // 2-second delay to display Level Complete
    private static final int GAME_OVER_DISPLAY_TIME = 3000;
    // 3-second delay to display "You Win"
    private long gameOverDisplayStartTime = 0;
    // The delays also help the game play to match with the sound effects.

    // Number of customers to serve for each level
    final int LEVEL_1_CUSTOMERS = 2;
    final int LEVEL_2_CUSTOMERS = 3;
    final int LEVEL_3_CUSTOMERS = 5;

    TileManager tileM = new TileManager(this);
    KeyHandler keyH = new KeyHandler();
    Thread gameThread;
    public CollisionChecker cChecker = new CollisionChecker(this);
    public AssetSetter aSetter = new AssetSetter(this);
    public Player player = new Player(this, keyH);
    public SuperObject[] obj = new SuperObject[17];

    public int gameState;
    public final int playState = 1;
    public final int pauseState = 2;
    public final int levelCompleteState = 3;
    public final int gameOverState = 4;

    //
    public int deliveryX;
    public int deliveryY;

    private Inventory inventory;
    private PickUp pickUp;

    public Recipe recipe;

    public PickUp getPickUp() {
        return pickUp;
    }

    private CustomerRequest customerRequest;

    // Timer elements
    JLabel counterLabel;
    Font font1 = new Font("Arial", Font.PLAIN, 70);
    Timer timer;
    int second;
    int minute;
    String ddSecond;
    String ddMinute;
    DecimalFormat dFormat = new DecimalFormat("00");

    // Field for customer loss message display
    private boolean displayCustomerLossMessage = false;
    private long lossMessageStartTime = 0;

    /**
     * * Constructs the main game panel, initializes game components like tile map,
     * player, inventory, recipes, objects, and starts the countdown timer.
     */
    public GamePanel() {
        this.setPreferredSize(new Dimension(screenWidth, screenHeight));
        this.setBackground(Color.black);
        this.setDoubleBuffered(true);
        this.addKeyListener(keyH);
        this.setFocusable(true);

        customerRequest = new CustomerRequest();
        inventory = new Inventory(tileSize, screenWidth - tileSize - 20,
                screenHeight - tileSize - 20);
        this.setLayout(null);

        pickUp = new PickUp(this);
        recipe = new Recipe(this);

        player = new Player(this, keyH);
        setupGame();

        // Initialize timer values for countdown
        second = 0;
        minute = 1;
        countdownTimer();
        timer.start();
    }

    public void countdownTimer() {
        if (timer != null && timer.isRunning()) {
            timer.stop(); // Stop any previous timer instance to prevent stacking
        }

        timer = new Timer(1000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                second--;
                ddSecond = dFormat.format(second);
                ddMinute = dFormat.format(minute);

                if (second == -1) {
                    second = 59;
                    minute--;
                    ddSecond = dFormat.format(second);
                    ddMinute = dFormat.format(minute);
                }
                if (minute == 0 && second == 0) {
                    timer.stop();

                    // Display the "You lost the customer" message
                    displayCustomerLossMessage = true;
                    lossMessageStartTime = System.currentTimeMillis();
                    customerRequest.generateNewRequest();

                    // Restart timer with initial values for next round
                    minute = 1;
                    second = 0;
                    timer.restart();
                }
            }
        });
        timer.start();
    }

    public void setupGame() {
        setupLevel(level);
        aSetter.setObject();
        gameState = playState;
    }

    /**
     * Configures player speed, request count, and timer based on the specified
     * level.
     * 
     * @param level the game level to set up (1, 2, or 3)
     */
    public void setupLevel(int level) {
        switch (level) {
            case 1:
                player.setSpeed(4);
                customersServed = 0;
                totalRequests = 0;
                minute = 1;
                second = 0;
                break;
            case 2:
                // The speed increases in level 2.
                player.setSpeed(6);
                customersServed = 0;
                totalRequests = 0;
                minute = 1;
                second = 0;
                break;
            case 3:
                player.setSpeed(6);
                customersServed = 0;
                totalRequests = 0;
                minute = 0;
                second = 30;
                // Set timer to 30 seconds for Level 3 to make it more challenging.
                break;
            default:
                break;
        }
        countdownTimer();
        timer.start();
    }

    public void startGameThread() {
        gameThread = new Thread(this);
        gameThread.start();
    }

    @Override
    public void run() {
        double drawInterval = 1000000000 / FPS;
        double nextDrawTime = System.nanoTime() + drawInterval;

        while (gameThread != null) {
            update();
            repaint();

            try {
                double remainingTime = nextDrawTime - System.nanoTime();
                remainingTime = remainingTime / 1000000;
                if (remainingTime < 0) {
                    remainingTime = 0;
                }

                Thread.sleep((long) remainingTime);
                nextDrawTime += drawInterval;
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public void update() {
        if (gameState == playState && !gameOver) {
            player.update();

            // final int TARGET_X = 144;
            // final int TARGET_Y = 40;

            final int DEFLECTION = 40;

            recipe.replaceItemsWithDonerWOnTile8();
            recipe.cuttingTomato();
            recipe.cuttinglettuce();
            recipe.replaceItemsWithDonerLT();

            for (int i = 0; i < obj.length; i++) {
                SuperObject currentObject = obj[i];
                if (currentObject != null) {
                    int objectX = currentObject.worldX;
                    int objectY = currentObject.worldY;

                    boolean isCloseX = objectX >= deliveryX - DEFLECTION && objectX <= deliveryX + DEFLECTION;
                    boolean isCloseY = objectY >= deliveryY - DEFLECTION && objectY <= deliveryY + DEFLECTION;

                    // System.out.println("Object dropped at: " + objectX + ", " + objectY);

                    if (isCloseX && isCloseY && customerRequest.checkRequest(currentObject)) {
                        obj[i] = null;
                        customersServed++;
                        totalRequests++;
                        customerRequest.generateNewRequest();

                        minute = level == 3 ? 0 : 1; // Reset timer as per level
                        second = level == 3 ? 30 : 0;
                        timer.restart();

                        checkLevelCompletion();
                    }
                }
            }
        }

        if (gameState == levelCompleteState && System.currentTimeMillis() - levelCompleteTime > LEVEL_COMPLETE_DELAY) {
            level++;
            if (level <= 3) {
                setupLevel(level);
                gameState = playState;
            } else {
                gameState = gameOverState;
                gameOverDisplayStartTime = System.currentTimeMillis();
                playWinSound("assets/sounds/win_sound.wav");
            }
        }

        if (gameState == gameOverState
                && System.currentTimeMillis() - gameOverDisplayStartTime >= GAME_OVER_DISPLAY_TIME) {
            closeGameWindow();
            stopGameThread();
        }

        if (displayCustomerLossMessage && System.currentTimeMillis() - lossMessageStartTime >= 2000) {
            displayCustomerLossMessage = false;
        }
    }

    private void checkLevelCompletion() {
        int requiredCustomers = getRequiredCustomersForLevel(level);

        if (customersServed >= requiredCustomers) {
            levelComplete = true;
            gameState = levelCompleteState;
            levelCompleteTime = System.currentTimeMillis();
            playSoundEffect("assets/sounds/level_passed.wav\\", false);
        }
    }

    private int getRequiredCustomersForLevel(int level) {
        switch (level) {
            case 1:
                return LEVEL_1_CUSTOMERS;
            case 2:
                return LEVEL_2_CUSTOMERS;
            case 3:
                return LEVEL_3_CUSTOMERS;
            default:
                return 0;
        }
    }

    private void playSoundEffect(String filePath, boolean isGameOver) {
        try {
            File soundFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);

            FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float range = volumeControl.getMinimum();
            float gain = (float) ((StartScreen.volumeLevel / 100.0) * (0 - range) + range);
            volumeControl.setValue(gain);

            clip.start();
        } catch (Exception e) {
            System.out.println("Error playing sound effect: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void playWinSound(String filePath) {
        try {
            File soundFile = new File(filePath);
            AudioInputStream audioStream = AudioSystem.getAudioInputStream(soundFile);
            Clip clip = AudioSystem.getClip();
            clip.open(audioStream);

            FloatControl volumeControl = (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            float range = volumeControl.getMinimum();
            float gain = (float) ((StartScreen.volumeLevel / 100.0) * (0 - range) + range);
            volumeControl.setValue(gain);

            clip.start();
        } catch (Exception e) {
            System.out.println("Error playing win sound effect: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void closeGameWindow() {
        javax.swing.SwingUtilities.getWindowAncestor(this).dispose();
    }

    private void stopGameThread() {
        gameThread = null;
    }

    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;

        tileM.draw(g2);
        for (SuperObject superObject : obj) {
            if (superObject != null) {
                superObject.draw(g2, this);
            }
        }

        player.draw(g2);
        inventory.draw(g2, this);

        g2.setColor(Color.WHITE);
        g2.setFont(new Font("Arial", Font.BOLD, 16));

        // 30 58 82: old y-axis for each string

        g2.drawString("Level: " + level, 10, 82);

        g2.drawString("Time: " + ddMinute + ":" + ddSecond, 10, 56);

        if (customerRequest != null) {
            g2.setFont(new Font("Arial", Font.BOLD, 17));
            g2.drawString("Customer Request: " + customerRequest.getCurrentRequest(), 10, 30);
        }

        if (displayCustomerLossMessage) {
            g2.setColor(Color.RED);
            g2.setFont(new Font("Arial", Font.BOLD, 30));
            g2.drawString("You lost the customer!", screenWidth / 2 - 150, screenHeight / 2 - 50);
        }

        if (gameState == levelCompleteState) {
            g2.setColor(Color.YELLOW);
            g2.setFont(new Font("Arial", Font.BOLD, 30));
            g2.drawString("Level " + level + " Complete!", screenWidth / 2 - 100, screenHeight / 2);
        } else if (gameState == gameOverState) {
            g2.setColor(Color.GREEN);
            g2.setFont(new Font("Arial", Font.BOLD, 40));
            g2.drawString("YOU WIN!!!!", screenWidth / 2 - 120, screenHeight / 2);
        }

        g2.dispose();
    }

    public Inventory getInventory() {
        return inventory;
    }
}
