package main;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;

/**
 * Creates the start screen with start, rules, and exit button.
 * Has volume on/off control.
 */
public class StartScreen extends JFrame {

    private Clip musicClip; // To store the background music for volume control
    private JPanel panel; // Main panel for the start screen
    private BufferedImage backgroundImage; // Background image
    private boolean playMusicFlag = true; // Set to true initially to play music

    // change later!!!
    public static int volumeLevel = 100; // Default volume level is 100%

    private JButton soundToggleButton;
    private ImageIcon volumeOnIcon;
    private ImageIcon volumeOffIcon;
    private boolean isMuted = true;

    public StartScreen() {
        // Load the background image
        try {
            backgroundImage = ImageIO
                    .read(new File("assets/undercooked_start_screen.png"));
        } catch (IOException e) {
            System.out.println("Background image could not be loaded.");
            e.printStackTrace();
        }

        // Set the window title, size, and non-resizable property
        setTitle("Undercooked");
        setSize(768, 576);
        setResizable(false); // Make the window non-resizable
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null);
        setLocationRelativeTo(null);

        // Create a custom panel for background
        panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (backgroundImage != null) {
                    g.drawImage(backgroundImage, 0, 0, getWidth(), getHeight(), this);
                }
            }
        };
        panel.setLayout(null); // Use absolute positioning
        setContentPane(panel);

        // Build the initial screen
        buildInitialScreen();

        // Make the window visible
        setVisible(true);

        // Play background music if the flag is set to true
        if (playMusicFlag) {
            playMusic("assets/sounds/start_screen_sound.wav");
        }
    }

    // Method to play background music with error handling
    public void playMusic(String musicPath) {
        try {
            if (musicClip != null && musicClip.isRunning()) {
                musicClip.stop();
                musicClip.close();
            }

            File musicFile = new File(musicPath);
            if (!musicFile.exists()) {
                System.out.println("Music file not found at path: " + musicPath);
                return;
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(musicFile);
            musicClip = AudioSystem.getClip();
            musicClip.open(audioStream);
            musicClip.loop(Clip.LOOP_CONTINUOUSLY);
            adjustVolume(volumeLevel);
            musicClip.start();
        } catch (Exception e) {
            System.out.println("Error playing background music.");
            e.printStackTrace();
        }
    }

    // Stop music playback
    public void stopMusic() {
        if (musicClip != null && musicClip.isRunning()) {
            musicClip.stop();
        }
    }

    // Method to build the initial screen with buttons
    public void buildInitialScreen() {
        panel.removeAll();

        // now in the image:

        int buttonWidth = 200;
        int buttonHeight = 50;
        int buttonX = 360;
        int initialY = 190; // Starting Y position for the buttons
        int spacing = 80; // Spacing between buttons

        // Create the "Start" button
        JButton startButton = createRoundedButton("<  Start  >");
        startButton.setBounds(buttonX, initialY, buttonWidth, buttonHeight);
        startButton.addActionListener(e -> {
            playMusicFlag = false;
            stopMusic();
            openGamePanel();
        });

        // Create "Rules" button
        JButton rulesButton = createRoundedButton("<  Rules  >");
        rulesButton.setBounds(buttonX, initialY + spacing, buttonWidth, buttonHeight);
        rulesButton.addActionListener(e -> openRulesPage());

        // Create the "Exit" button
        JButton exitButton = createRoundedButton("<  Exit  >");
        exitButton.setBounds(buttonX, initialY + spacing * 2, buttonWidth, buttonHeight);
        exitButton.addActionListener(e -> System.exit(0));

        // Create Mute Button

        volumeOnIcon = new ImageIcon("assets/sounds/volume_up.png");
        volumeOffIcon = new ImageIcon("assets/sounds/volume_off.png");

        soundToggleButton = new JButton(volumeOnIcon);
        soundToggleButton.setBounds(710, 10, 32, 32); // Top-right corner
        soundToggleButton.setBorderPainted(false);
        soundToggleButton.setContentAreaFilled(false);
        soundToggleButton.setFocusPainted(false);
        soundToggleButton.setOpaque(false);

        soundToggleButton.addActionListener(e -> {
            isMuted = !isMuted;
            soundToggleButton.setIcon(isMuted ? volumeOffIcon : volumeOnIcon);

            if (isMuted) {
                stopMusic();
            } else if (musicClip == null || !musicClip.isRunning()) {
                playMusic("assets/sounds/start_screen_sound.wav");
            }
        });
        
        panel.add(soundToggleButton);

        panel.add(startButton);
        // panel.add(soundControlButton);
        panel.add(rulesButton);
        panel.add(exitButton);

        panel.revalidate();
        panel.repaint();
    }

    // Method to open the rules page
    public void openRulesPage() {
        panel.removeAll();

        // Create a custom panel for background

        JPanel rulesBackground = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g;

                // smooth edges:
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Background color 112, 154, 209
                g2.setColor(new Color(112, 154, 209));

                g2.fillRect(0, 0, getWidth(), getHeight());
            }
        };

        rulesBackground.setLayout(null); // Manual positioning inside this background
        rulesBackground.setBounds(80, 20, 608, 400);
        rulesBackground.setOpaque(false); // So paintComponent is visible

        // Actual Rules Text
        JTextArea rulesText = new JTextArea(
                "Press WASD to move your character. Press e to pick up and place an item.\n\n" +
                        "The kitchen has two different kinds of stations.\n" +
                        "The stations with squares on their centers allow you to combine different items." +
                        "However, don't forget to follow the rules of cooking: create the base first with doner meat and wrap.\n\n"
                        +
                        "Then, to add tomatoes and/or lettuce, you need to cut beforehand." +
                        "Use the second station with the knife and cutting board for this.\n\n" +
                        "Oh and you need to remember that the waiter uses some abbreviations when taking orders!\n\n" +

                        "Abbreviations:\n" +
                        "donerw: doner + wrap\n" +
                        "donerl: doner + wrap + lettuce\n" +
                        "donert: doner + wrap + tomato\n" +
                        "donerlt: doner + wrap + lettuce + tomato");

        rulesText.setBounds(60, 25, 468, 370);
        rulesText.setFont(new Font("Serif", Font.PLAIN, 14));
        rulesText.setEditable(false);
        rulesText.setWrapStyleWord(true);
        rulesText.setLineWrap(true);
        rulesText.setOpaque(false); // Let background panel show through

        TitledBorder titledBorder = new TitledBorder(BorderFactory.createLineBorder(new Color(88, 123, 168), 5),
                "RULES:");
        rulesText.setBorder(titledBorder);

        rulesBackground.add(rulesText);

        panel.add(rulesBackground);

        // Create and add a "Back" button to return to StartScreen
        JButton backButton = createRoundedButton("Back");
        backButton.setBounds(275, 424, 200, 50);
        backButton.addActionListener(e -> buildInitialScreen());
        panel.add(backButton);

        panel.revalidate();
        panel.repaint();
    }

    // Method to create a rounded button
    private JButton createRoundedButton(String text) {
        JButton button = new JButton(text) {
            // private boolean hovered = false;

            @Override
            protected void paintComponent(Graphics g) {
                // super.paintComponent(g);
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                // Change background only on hover
                if (getModel().isRollover()) {
                    g2.setColor(new Color(200, 220, 255)); // on hover
                } else {
                    g2.setColor(Color.WHITE); // normal background
                }

                g2.fill(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 30, 30));

                // Text
                FontMetrics fm = g2.getFontMetrics();
                int stringWidth = fm.stringWidth(getText());
                int stringHeight = fm.getAscent();
                g2.setColor(getForeground());
                g2.drawString(getText(), (getWidth() - stringWidth) / 2, (getHeight() + stringHeight) / 2 - 1);
                g2.dispose();
            }

            @Override
            protected void paintBorder(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);

                g2.setColor(new Color(88, 123, 168));
                g2.setStroke(new BasicStroke(5));

                g2.draw(new RoundRectangle2D.Float(0, 0, getWidth(), getHeight(), 30, 30));
                g2.dispose();
            }
        };

        // styling of button
        // light blue: 112, 154, 209
        // shadow blue: 88 123 168
        button.setOpaque(false);
        button.setContentAreaFilled(false);
        button.setFocusPainted(false);
        button.setBorderPainted(false);

        button.setBackground(Color.LIGHT_GRAY);
        button.setForeground(new Color(112, 154, 209));

        return button;
    }

    // Method to open the game screen in a new JFrame
    public void openGamePanel() {
        JFrame gameFrame = new JFrame("Undercooked");
        gameFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        gameFrame.setResizable(false);

        // Initialize and add GamePanel to the new frame
        GamePanel gamePanel = new GamePanel();
        gameFrame.add(gamePanel);
        gameFrame.pack();
        gameFrame.setLocationRelativeTo(null); // Center the new window
        gameFrame.setVisible(true);
        gamePanel.requestFocusInWindow();
        SwingUtilities.invokeLater(() -> gamePanel.requestFocusInWindow());

        // Start the game thread
        gamePanel.startGameThread();

        // Close the StartScreen
        dispose();
    }

    // Method to adjust the volume of the music
    public void adjustVolume(int volume) {
        if (musicClip != null) {
            FloatControl volumeControl = (FloatControl) musicClip.getControl(FloatControl.Type.MASTER_GAIN);
            float range = volumeControl.getMinimum();
            float gain = (float) ((volume / 100.0) * (0 - range) + range);
            volumeControl.setValue(gain);
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(StartScreen::new);
    }
}
