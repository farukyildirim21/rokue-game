package game;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.geom.AffineTransform;
import java.io.File;

public class MainMenu {

    private JFrame frame;
   // private String rokuePath= "src/assets/rokue-like assets/Rokue-like logo 4.png";
    private int selectedIndex = 0;	
    private JButton[] buttons;
    private JLabel[] selectorLabels;  // Array to store selector labels
    private JPanel[] buttonContainers;  // Array to store button containers
    private Timer glitterTimer;  // Timer for glitter effect
    private boolean selectorVisible = true;  // Track selector visibility  
    private Color[] glitterColors = {  // Array of colors for glitter effect
        new Color(255, 255, 255),  // White
        new Color(255, 215, 0),    // Gold
        new Color(255, 255, 224)   // Light yellow
    };
    private int currentColorIndex = 0;
    
    
    // Animation related fields
    private Timer animationTimer;
    private int playerX = -50;  // Start off-screen
    private final int PLAYER_Y = 250;  // Fixed Y position
    private final int SCREEN_WIDTH = 800;
    private ImageIcon playerSprite;
    private boolean animationRunning = true;
    
    public MainMenu() {
        frame = new JFrame("Main Menu");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(800, 600);
        frame.setLocationRelativeTo(null);
        
     // Create custom panel for background and animation
        JPanel mainPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                Graphics2D g2d = (Graphics2D) g;
                
                // Set background
                g2d.setColor(new Color(50, 34, 40));
                g2d.fillRect(0, 0, getWidth(), getHeight());
                
                // Draw main title with shadow
                g2d.setFont(new Font("Comic Sans MS", Font.BOLD, 48));
                drawShadowedText(g2d, "ROKUE LIKE GAME", getWidth()/2, 100);
                
                // Draw welcome text with shadow
                g2d.setFont(new Font("Comic Sans MS", Font.BOLD, 36));
                drawShadowedText(g2d, "Welcome to the Game", getWidth()/2, 160);
                
                // Draw player sprite
                if (playerSprite != null) {
                    g2d.drawImage(playerSprite.getImage(), playerX, PLAYER_Y, null);
                }
            }
            
            private void drawShadowedText(Graphics2D g2d, String text, int x, int y) {
                FontMetrics fm = g2d.getFontMetrics();
                int textWidth = fm.stringWidth(text);
                
                // Draw shadow
                g2d.setColor(new Color(0, 0, 0, 128));
                g2d.drawString(text, x - textWidth/2 + 3, y + 3);
                
                // Draw main text
                g2d.setColor(Color.WHITE);
                g2d.drawString(text, x - textWidth/2, y);
            }
        };
        
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        frame.add(mainPanel);
        
        // Load player sprite
        playerSprite = new ImageIcon("src/assets/rokue-like assets/player4x.png");
        
        // Add spacing for titles
        mainPanel.add(Box.createVerticalStrut(350));
        
        // Initialize glitter timer
        glitterTimer = new Timer(150, e -> updateGlitterEffect());
        glitterTimer.start();
        
        // Initialize animation timer
        animationTimer = new Timer(16, e -> updatePlayerAnimation());
        animationTimer.start();
        
       

        
       


        // Button panel
        JPanel buttonPanel = new JPanel();
        buttonPanel.setLayout(new BoxLayout(buttonPanel, BoxLayout.Y_AXIS)); // Vertical layout
        buttonPanel.setOpaque(false);

        // Create buttons
        JButton startButton = createPlayfulButton("START NEW GAME");
        JButton loadButton = createPlayfulButton("LOAD GAME");
        JButton helpButton = createPlayfulButton("HELP");
        JButton exitButton = createPlayfulButton("EXIT");
        
        
        // Initialize arrays
        buttons = new JButton[]{startButton, loadButton, helpButton, exitButton};
        selectorLabels = new JLabel[buttons.length];
        buttonContainers = new JPanel[buttons.length];

        // Create button containers with selectors
        for (int i = 0; i < buttons.length; i++) {
            // Create a fixed-width container
            buttonContainers[i] = new JPanel(new FlowLayout(FlowLayout.CENTER, 0, 0));
            buttonContainers[i].setOpaque(false);
            
            // Create a sub-panel for the selector and button with fixed width
            JPanel innerContainer = new JPanel();
            innerContainer.setLayout(new BoxLayout(innerContainer, BoxLayout.X_AXIS));
            innerContainer.setOpaque(false);
            
            // Set fixed size for the selector area
            JPanel selectorArea = new JPanel();
            selectorArea.setLayout(new BorderLayout());
            selectorArea.setOpaque(false);
            selectorArea.setPreferredSize(new Dimension(30, 40));
            
            // Create selector label with custom font and initial color
            selectorLabels[i] = new JLabel(" ", SwingConstants.CENTER);
            selectorLabels[i].setForeground(glitterColors[0]);
            selectorLabels[i].setFont(new Font("Comic Sans MS", Font.BOLD, 40));
            
            // Add selector to its fixed-width area
            selectorArea.add(selectorLabels[i], BorderLayout.CENTER);
            
            // Add components to inner container
            innerContainer.add(selectorArea);
            innerContainer.add(buttons[i]);
            
            // Set preferred size for the entire container
            buttonContainers[i].setPreferredSize(new Dimension(300, 50));
            buttonContainers[i].add(innerContainer);
            
            // Add to button panel
            buttonPanel.add(buttonContainers[i]);
            buttonPanel.add(Box.createVerticalStrut(10));
        }

        // Center the button panel
        JPanel centeringPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        centeringPanel.setOpaque(false);
        centeringPanel.add(buttonPanel);
        
        mainPanel.add(centeringPanel);

        //updateSelectorPosition();
        
        // Add action listeners to buttons
        startButton.addActionListener(e -> {
        	animationRunning = false;
            animationTimer.stop();
        	glitterTimer.stop();  // Stop the timer before disposing
            frame.dispose(); // Close the main menu
            launchGame();    // Start the game
        });

        loadButton.addActionListener(e -> {
            String filePath = "savefile.dat"; // Path to the saved game file
            File saveFile = new File(filePath);
            if (saveFile.exists()) {
                try {
                    GameManager gameManager = new GameManager(null); // Initialize GameManager
                    gameManager.loadGame(filePath); // Load the saved game
                  //  gameManager.initializeGameFromState(); // Ensure the game is fully initialized
                   // gameManager.getGameFrame().setVisible(true); // Make the game frame visible
                    frame.dispose(); // Close the main menu
                } catch (Exception ex) {
                    JOptionPane.showMessageDialog(frame, "Failed to load game: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
                }
            } else {
                JOptionPane.showMessageDialog(frame, "No saved game found!", "Error", JOptionPane.ERROR_MESSAGE);
            }
        });


        helpButton.addActionListener(e -> showHelpDialog());
        exitButton.addActionListener(e -> {
        	animationTimer.stop();
        	glitterTimer.stop();  // Stop the timer before exiting
            System.exit(0);
        });
        
     // Add key listener for arrow navigation
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                switch (e.getKeyCode()) {
                    case KeyEvent.VK_UP:
                        navigateButtons(-1);
                        break;
                    case KeyEvent.VK_DOWN:
                        navigateButtons(1);
                        break;
                    case KeyEvent.VK_ENTER:
                        buttons[selectedIndex].doClick();
                        break;
                }
            }
        });

        frame.setFocusable(true);
        frame.requestFocus();
        frame.setVisible(true);
    }
    private void updatePlayerAnimation() {
        if (!animationRunning) return;
        
        playerX += 2;  // Move 2 pixels per frame
        if (playerX > SCREEN_WIDTH) {
            playerX = -50;  // Reset to start
        }
        frame.repaint();
    }
    
    private void updateGlitterEffect() {
        // Change color
        currentColorIndex = (currentColorIndex + 1) % glitterColors.length;
        Color currentColor = glitterColors[currentColorIndex];
        
        // Update the selected selector's color
        if (selectorVisible) {
            selectorLabels[selectedIndex].setForeground(currentColor);
            selectorLabels[selectedIndex].setText(">");
        } else {
            selectorLabels[selectedIndex].setText(" ");
        }
        
        // Toggle visibility for next update
        selectorVisible = !selectorVisible;
    }
    
    
    // Method to navigate buttons using arrow keys
    private void navigateButtons(int direction) {
    	 selectorLabels[selectedIndex].setText(" ");
    	 selectedIndex = (selectedIndex + direction + buttons.length) % buttons.length;
         updateSelectorPosition();
    }

    // Update the position of the selector label
    private void updateSelectorPosition() {
    	for (int i = 0; i < selectorLabels.length; i++) {
            if (i == selectedIndex) {
                selectorLabels[i].setForeground(glitterColors[currentColorIndex]);
                selectorLabels[i].setText(">");
            } else {
                selectorLabels[i].setText(" ");
            }
        }
        frame.requestFocusInWindow();
    }
    

    
 // Create a playful styled button
    private JButton createPlayfulButton(String text) {
        JButton button = new JButton(text) {
            @Override
            protected void paintComponent(Graphics g) {
                Graphics2D g2 = (Graphics2D) g.create();
                g2.setColor(Color.BLACK);
                // Make the button transparent
                g2.setBackground(new Color(50, 34, 40));
                g2.clearRect(0, 0, getWidth(), getHeight());
                
                // Draw the text
                FontMetrics fm = g2.getFontMetrics();
                int textWidth = fm.stringWidth(getText());
                int textHeight = fm.getHeight();
                g2.setColor(Color.WHITE);  // Set text color to white
                g2.drawString(getText(), 
                    (getWidth() - textWidth) / 2, 
                    (getHeight() + textHeight / 2) / 2);
                
//                // Draw nested rectangles
//                g2.setColor(Color.gray);
//                g2.setStroke(new BasicStroke(2));
//                
//                // Outer rectangle
//                g2.drawRect(2, 2, getWidth() - 4, getHeight() - 4);
//                
                g2.dispose();
            }
        };
        
        // Basic button setup
        button.setOpaque(false);  // Make button transparent
        button.setContentAreaFilled(false);  // Don't fill the content area
        button.setBorderPainted(false);  // Don't paint the default border
        button.setFocusPainted(false);  // Don't paint focus indicator
        button.setFont(new Font("Comic Sans MS", Font.BOLD, 18));
        button.setForeground(Color.WHITE);
        button.setPreferredSize(new Dimension(250, 40));
        button.setMaximumSize(new Dimension(250, 40));
        
        // Add hover effect
        button.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseEntered(MouseEvent e) {
                button.setForeground(new Color(255, 215, 0));  // Gold color on hover
            }
            
            @Override
            public void mouseExited(MouseEvent e) {
                button.setForeground(Color.WHITE);  // Back to white when not hovered
            }
            
            @Override
            public void mousePressed(MouseEvent e) {
                button.setForeground(new Color(255, 165, 0));  // Orange when pressed
            }
            
            @Override
            public void mouseReleased(MouseEvent e) {
                button.setForeground(button.getModel().isRollover() ? 
                    new Color(255, 215, 0) : Color.WHITE);  // Back to hover or normal color
            }
        });
        
        return button;
    }

		        

		    
    // Launch the game
    private void launchGame() {
        SwingUtilities.invokeLater(() -> {
            BuildMode buildMode= new BuildMode();
        });
    }

    // Show Help Dialog
    private void showHelpDialog() {
    	JTextArea textArea = new JTextArea(
                
                "Welcome to the Game!\n\n" +
                        "- Start a New Game: Launches the game with empty halls where you can design and place objects.\n" +
                        "- How to Play:\n" +
                        "   1. Navigate through the dungeon halls (Earth, Air, Water, Fire) to find the hidden runes.\n" +
                        "   2. Use the arrow keys to move the hero:\n" +
                        "      - Arrow Up: Move up.\n" +
                        "      - Arrow Down: Move down.\n" +
                        "      - Arrow Left: Move left.\n" +
                        "      - Arrow Right: Move right.\n" +
                        "   3. Click on objects to search for runes.\n" +
                        "   4. Avoid monsters that try to stop you:\n" +
                        "      - Archer: Shoots arrows if you're too close.\n" +
                        "      - Fighter: Attacks with a dagger when adjacent.\n" +
                        "      - Wizard: Teleports the rune to confuse you.\n" +
                        "   5. Collect enchantments to gain advantages:\n" +
                        "      - Extra Time: Adds 5 seconds to the timer immediately upon collection.\n" +
                        "      - Extra Life: Increases the hero’s lives by 1 immediately upon collection.\n" +
                        "      - Reveal: Press 'R' to highlight a 4x4 area where the rune might be hidden.\n" +
                        "      - Cloak of Protection: Press 'P' to become invisible to archer monsters for 20 seconds.\n" +
                        "      - Luring Gem: Press 'B' to throw the gem and use 'A', 'D', 'W', or 'S' to choose the direction:\n" +
                        "         - A: Throw left.\n" +
                        "         - D: Throw right.\n" +
                        "         - W: Throw up.\n" +
                        "         - S: Throw down.\n" +
                        "- Hall Design Requirements:\n" +
                        "   - Hall of Earth: At least 6 objects.\n" +
                        "   - Hall of Air: At least 9 objects.\n" +
                        "   - Hall of Water: At least 13 objects.\n" +
                        "   - Hall of Fire: At least 17 objects.\n" +
                        "- Controls:\n" +
                        "   - Movement: Arrow keys (Up, Down, Left, Right).\n" +
                        "   - Pause: Press 'Pause' to stop and resume the game as needed.\n" +
                        "   - Enchantments:\n" +
                        "      - Reveal: 'R'\n" +
                        "      - Cloak of Protection: 'P'\n" +
                        "      - Luring Gem: 'B' + Direction (A, D, W, S)\n" +
                        "- Exit: Quits the game and returns to the main menu.\n\n" +
                        "Enjoy your adventure and good luck!");
     // Configure the text area
        textArea.setEditable(false);
        textArea.setWrapStyleWord(true);
        textArea.setLineWrap(true);
        textArea.setMargin(new Insets(10, 10, 10, 10));
        textArea.setBackground(new Color(50, 34, 40));  // Match the main menu background
        textArea.setForeground(Color.WHITE);  // White text
        textArea.setFont(new Font("Comic Sans MS", Font.PLAIN, 14));
        textArea.setCaretPosition(0);  // Scroll to top

        // Create a scroll pane and add the text area to it
        JScrollPane scrollPane = new JScrollPane(textArea);
        scrollPane.setPreferredSize(new Dimension(600, 400));  // Set a reasonable size
        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        scrollPane.getVerticalScrollBar().setUnitIncrement(16);  // Smooth scrolling

        // Create and show the dialog
        JDialog helpDialog = new JDialog(frame, "Help", true);
        helpDialog.setLayout(new BorderLayout());
        helpDialog.add(scrollPane, BorderLayout.CENTER);
        
        // Add a close button at the bottom
        JButton closeButton = new JButton("Close");
        closeButton.addActionListener(e -> helpDialog.dispose());
        JPanel buttonPanel = new JPanel();
        buttonPanel.setBackground(new Color(50, 34, 40));
        buttonPanel.add(closeButton);
        helpDialog.add(buttonPanel, BorderLayout.SOUTH);

        // Configure and show the dialog
        helpDialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
        helpDialog.pack();
        helpDialog.setLocationRelativeTo(frame);
        helpDialog.setResizable(true);
        helpDialog.setVisible(true);
    }
    
    private void cleanup() {
        if (animationTimer != null) animationTimer.stop();
        if (glitterTimer != null) glitterTimer.stop();
    }



}
