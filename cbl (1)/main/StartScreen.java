package main;

import java.awt.*;
import java.awt.geom.RoundRectangle2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.sound.sampled.*;
import javax.swing.*;

public class StartScreen extends JFrame {

    private Clip musicClip; // To store the background music for volume control
    private JPanel panel; // Main panel for the start screen
    private BufferedImage backgroundImage; // Background image
    private boolean playMusicFlag = true; // Set to true initially to play music
    public static int volumeLevel = 100; // Default volume level is 100%

    private JButton soundToggleButton;
    private ImageIcon volumeOnIcon;
    private ImageIcon volumeOffIcon;
    private boolean isMuted = false;

    public StartScreen() {
        // Load the background image
        try {
            backgroundImage = ImageIO
                    .read(new File("cbl asset/undercooked_start_screen.png"));
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
            playMusic("cbl asset/chill vibes_10.wav");
        }
    }

    // Method to play background music with error handling
    public void playMusic(String musicPath) {
        try {
            File musicFile = new File(musicPath);
            if (!musicFile.exists()) {
                System.out.println("Music file not found at path: " + musicPath);
                return;
            }

            AudioInputStream audioStream = AudioSystem.getAudioInputStream(musicFile);
            musicClip = AudioSystem.getClip();
            musicClip.open(audioStream);
            musicClip.loop(Clip.LOOP_CONTINUOUSLY); // Play the music in a loop
            musicClip.start();
            adjustVolume(volumeLevel); // Use the stored volume level
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
        int buttonHeight = 60;
        int buttonX = 300; // Centered horizontally
        int initialY = 180; // Starting Y position for the buttons
        int spacing = 80; // Spacing between buttons

        // Create the "Start" button
        JButton startButton = createRoundedButton("Start");
        startButton.setBounds(buttonX, initialY, buttonWidth, buttonHeight);
        startButton.addActionListener(e -> {
            playMusicFlag = false;
            stopMusic();
            openGamePanel();
        });


        // Create "Rules" button
        JButton rulesButton = createRoundedButton("Rules");
        rulesButton.setBounds(buttonX, initialY + spacing, buttonWidth, buttonHeight);
        rulesButton.addActionListener(e -> openRulesPage());

        // Create the "Exit" button
        JButton exitButton = createRoundedButton("Exit");
        exitButton.setBounds(buttonX, initialY + spacing * 2, buttonWidth, buttonHeight);
        exitButton.addActionListener(e -> System.exit(0));

        // Create Mute Button

        volumeOnIcon = new ImageIcon("cbl asset/volume_up.png");
        volumeOffIcon = new ImageIcon("cbl asset/volume_off.png");

        soundToggleButton = new JButton(volumeOnIcon);
        soundToggleButton.setBounds(710, 10, 32, 32); // Top-right corner
        soundToggleButton.setBorderPainted(false);
        soundToggleButton.setContentAreaFilled(false);
        soundToggleButton.setFocusPainted(false);
        soundToggleButton.setOpaque(false);

        soundToggleButton.addActionListener(e -> {
            isMuted = !isMuted;
            soundToggleButton.setIcon(isMuted ? volumeOffIcon : volumeOnIcon);
            if (isMuted)
                stopMusic();
            else
                playMusic("cbl asset/chill vibes_10.wav");
        });

        panel.add(soundToggleButton);

        panel.add(startButton);
        //panel.add(soundControlButton);
        panel.add(rulesButton);
        panel.add(exitButton);

        panel.revalidate();
        panel.repaint();
    }

    // Method to open the rules page
    public void openRulesPage() {
        panel.removeAll();

        // Create and add a label with Lorem Ipsum text
        JTextArea rulesText = new JTextArea("Rules:\n" + //
                "\n" + //
                "Press WASD to move your character. Press e to pick up and place an item\n" + //
                "\n" + //
                "The kitchen has two different kinds of stations." + //
                "\n" + //
                "The stations with squares on their centeirs allows you to combine different items.However, to do so you must follow the rules of cooking itself. To make a proper doner wrap, you must create the base first which consists the doner meat and wrap. Then you can add the tomatoes and/or the lettuce.\n"
                + //
                "\n" + //
                "The tomatoes and lettuce must be cut on the cutting stations before using them on the combining stations."
                + "\n" + //
                "The waiter uses these abbreviations for the recipes:" + //
                "donerw: doner and wrap, donerl: donerwrap and lettuce, donert: donerwrap and lettuce," + "\\n" + //
                "donerlt: donerwrap lettuce and tomato"

        );
        rulesText.setBounds(100, 100, 568, 300);
        rulesText.setFont(new Font("Serif", Font.PLAIN, 14));
        rulesText.setEditable(false); // Make it read-only
        rulesText.setWrapStyleWord(true);
        rulesText.setLineWrap(true);
        panel.add(rulesText);

        // Create and add a "Back" button to return to StartScreen
        JButton backButton = createRoundedButton("Back");
        backButton.setBounds(300, 440, 200, 60);
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
                g2.drawString(getText(), (getWidth() - stringWidth) / 2, (getHeight() + stringHeight) / 2 - 5);
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
        // light blue: 112 154 209
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
        JFrame gameFrame = new JFrame("Game Panel");
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
