package panel;


import javax.swing.*;

import enchantment.Enchantment;

import java.awt.*;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import game.GameManager;
import game.ImageResizer;
import game.MainMenu;

public class InventoryPanel extends JPanel {
	private static final String inventory = "src/assets/rokue-like assets/Inventory.png";
	private JLabel timeLabel; // Label for displaying the remaining time
   // private JPanel pauseScreen;
    private boolean isPaused = false;
    private JFrame parentFrame; // Reference to the main game frame
    private GameManager gameManager; // Reference to the GameManager
    private JLabel heartLabel;
    private JLabel livesLabel;
    private ImageResizer imageResizer = new ImageResizer();
    private LinkedHashMap<String, ImageIcon> inventoryItems = new LinkedHashMap<>();
    private String luringGemPath =  "src/assets/items/luring2.png";
    private String cloakOfProtectionPath = "src/assets/items/cloak2.png";
    private String revealPath = "src/assets/items/Reveal2.png";
   private  JLabel inventoryLabel ;
  // private JPanel slotPanel;
    //private String enchantmentName;
   
    public InventoryPanel(JFrame parentFrame, GameManager gameManager) {
        this.parentFrame = parentFrame;
        this.gameManager = gameManager; // Store the reference
        
        setLayout(null); // Use absolute layout
        setBackground(new Color(60, 40, 50)); // Set the dark background color
        setBounds(800, 50, 200, 570); // Position on the right side

            }
    public void putItemsOnInventory(int live) {
    	// Add Inventory UI Components
        addControlButtons();
        addTitle();
        System.out.println("live in inventory panel:"+live);
        initializeHearts(live);
        try {
        	addInventory();
        	addItemtoMap();
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

    	
    }
    
    private void addControlButtons() {
        // Add Exit button
        JButton exitButton = new JButton();
        exitButton.setIcon(new ImageIcon("src/assets/items/exit16.png")); // Exit button image path
        exitButton.setBounds(20, 10, 32, 32);
        exitButton.setContentAreaFilled(false);
        exitButton.setBorderPainted(false);
        exitButton.setFocusPainted(false);
        exitButton.addActionListener(e -> goBackToMenuMenu(gameManager.getGameFrame()));
        add(exitButton);

        // Add Pause button
        JButton pauseButton = new JButton();
        pauseButton.setIcon(new ImageIcon("src/assets/items/pause16.png")); // Pause button image path
        pauseButton.setBounds(60, 10, 32, 32);
        pauseButton.setContentAreaFilled(false);
        pauseButton.setBorderPainted(false);
        pauseButton.setFocusPainted(false);
        pauseButton.addActionListener(e -> showPauseScreen());
        add(pauseButton);
    }

    private void addTitle() {
        // Remove the existing timeLabel if it exists
        if (timeLabel != null) {
            remove(timeLabel);
        }

        // Create a new timeLabel with the desired properties
        timeLabel = new JLabel("Left Time: ");
        timeLabel.setForeground(Color.WHITE);
        timeLabel.setFont(new Font("Arial", Font.BOLD, 16));
        timeLabel.setBounds(20, 50, 200, 30);

        // Add the new timeLabel to the panel
        add(timeLabel);

        // Refresh the panel to reflect the changes
        revalidate();
        repaint();
    }


    

    private void initializeHearts(int live) {
    	
        // Create and scale the heart icon
        ImageIcon heartIcon = new ImageIcon("src/assets/rokue-like assets/heart.png");
        Image scaledHeart = heartIcon.getImage().getScaledInstance(30, 30, Image.SCALE_SMOOTH);
        ImageIcon scaledHeartIcon = new ImageIcon(scaledHeart);

        // Create the heart label to display the heart icon
        heartLabel = new JLabel(scaledHeartIcon);
        heartLabel.setBounds(20, 90, 30, 30);
        add(heartLabel);

        
        updateLives(live);
        
        
     // Refresh the panel to reflect the changes
        revalidate();
        repaint();
    }

    // Method to update the number of lives displayed
    public void updateLives(int live) {
    	System.out.println("playerLives:" + live);

        // Remove the existing livesLabel if it exists
        if (livesLabel != null) {
            remove(livesLabel);
        }

        // Create a new livesLabel with the updated text
        livesLabel = new JLabel("x " + live);
        livesLabel.setBounds(60, 90, 50, 30); // Adjust position next to the heart icon
        livesLabel.setFont(new Font("Arial", Font.BOLD, 20));
        livesLabel.setForeground(Color.WHITE);

        // Add the new livesLabel to the panel
        add(livesLabel);

        // Refresh the panel to reflect the changes
        revalidate();
        repaint();
        
    }
    public void addItemtoMap() throws Exception {
    	BufferedImage resizedImage = imageResizer.convertImage(luringGemPath,24,24);
        ImageIcon luringIcon = new ImageIcon(resizedImage);
        inventoryItems.put("luring gem", luringIcon);
        
        
        BufferedImage resizedImage2 = imageResizer.convertImage(cloakOfProtectionPath,24,24);
        ImageIcon cloakIcon = new ImageIcon(resizedImage2);
        inventoryItems.put("cloak of protection", cloakIcon);
        
        BufferedImage resizedImage3 = imageResizer.convertImage(revealPath,24,24);
        ImageIcon revealIcon = new ImageIcon(resizedImage3);
        inventoryItems.put("reveal", revealIcon);
        
        
    	
    }
    public void addInventory() throws Exception {
    	BufferedImage resizedImage = imageResizer.convertImage(inventory,250,300);
        ImageIcon bufferedIcon = new ImageIcon(resizedImage);
        inventoryLabel = new JLabel(bufferedIcon);
        
        
        inventoryLabel.setForeground(Color.WHITE);
        inventoryLabel.setFont(new Font("Arial", Font.BOLD, 20));
        inventoryLabel.setBounds(-100, 120, 400, 400 );
        
        add(inventoryLabel);
    }

   
        
    
       
    public void addInventoryItems(LinkedHashMap<String, List<Enchantment>> enchantmentsMap) throws Exception {
    	clearInventoryLabel();

        int size = enchantmentsMap.size();
        JPanel slotPanel = new JPanel();
        slotPanel.setLayout(null);
        slotPanel.setOpaque(false);
        slotPanel.setBounds(-100, 120, 400, 400); // Adjust based on slots

        List<String> enchantmentNames = new ArrayList<>(enchantmentsMap.keySet());

        for (int i = 0; i < size; i++) {
            int count = enchantmentsMap.get(enchantmentNames.get(i)).size();
            JLabel slotLabel = new JLabel(inventoryItems.get(enchantmentNames.get(i)));
            int slotPanelWidth = 400; // Width of the slotPanel
            int slotPanelHeight = 400; // Height of the slotPanel
            int slotLabelWidth = 80; // Width of the slotLabel
            int slotLabelHeight = 80; // Height of the slotLabel

            // Calculate the centered position
            int x = ((slotPanelWidth - slotLabelWidth) / 2) + 50;
            int y = ((slotPanelHeight - slotLabelHeight) / 2) - 90;

            // Set the bounds for slotLabel
            slotLabel.setBounds(x + (i * 50), y, slotLabelWidth, slotLabelHeight);
            
            slotPanel.add(slotLabel);

            // Add count label if count > 1
            if (count > 1) {
                JLabel countLabel = new JLabel();
                countLabel.setText(null);
                countLabel.setText("x " + count);
                countLabel.setFont(new Font("Arial", Font.BOLD, 18));
                countLabel.setForeground(Color.WHITE); // White color for visibility
                countLabel.setBounds(235+(i*50), 50, 50, 20); // Position the count below the icon
                
                slotPanel.add(countLabel);
            }
        }

        // Add the slot panel to the inventory label
        inventoryLabel.add(slotPanel);
        inventoryLabel.revalidate();
        inventoryLabel.repaint();
    }
    public void clearInventoryLabel() {
        inventoryLabel.removeAll();  // Remove all components
        inventoryLabel.revalidate(); // Revalidate the container to reflect changes
        inventoryLabel.repaint();    // Repaint to update the UI
    }



    // Method to update the time label
    public void updateTimeLabel(int timeLeft) {
        timeLabel.setText("Time Left: " + timeLeft + " seconds");
    }

    private void showPauseScreen() {
        if (isPaused) return;

        isPaused = true;
        gameManager.pauseGame();  // Pause game logic

        JDialog pauseDialog = new JDialog(parentFrame, "Paused", true);
        pauseDialog.setSize(400, 400);
        pauseDialog.setLayout(new BorderLayout());
        pauseDialog.setLocationRelativeTo(parentFrame);

        JPanel panel = new JPanel();
        panel.setBackground(new Color(0, 0, 0, 200));
        panel.setLayout(new GridLayout(2, 1, 10, 10)); // Adjust layout for two buttons

        // Resume Button
        JButton resumeButton = new JButton("Resume");
        resumeButton.setFont(new Font("Arial", Font.BOLD, 20));
        resumeButton.setFocusPainted(false);
        resumeButton.addActionListener(e -> {
            pauseDialog.dispose();  // Close the pause dialog
            isPaused = false;
            gameManager.resumeGame();  // Resume game logic
        });
        panel.add(resumeButton);

        // Save Game Button
        JButton saveButton = new JButton("Save Game");
        saveButton.setFont(new Font("Arial", Font.BOLD, 20));
        saveButton.setFocusPainted(false);
        saveButton.addActionListener(e -> {
            String filePath = "savefile.dat"; // Set the save file path
            gameManager.saveGame(filePath);   // Save the game state
            JOptionPane.showMessageDialog(pauseDialog, "Game saved successfully!", "Save Game", JOptionPane.INFORMATION_MESSAGE);

        });
        panel.add(saveButton);

        pauseDialog.add(panel, BorderLayout.CENTER);
        pauseDialog.setVisible(true);  // Show the pause screen
    }
    
    
    private void goBackToMenuMenu(Frame frame) {
    	List<Timer> timers = gameManager.getTimers();
    	for(Timer timer: timers) {
    		timer.stop();
    	}
    	
    	frame.dispose();
    	new MainMenu();
    	
    }


    
   
}
