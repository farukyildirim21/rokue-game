package game;

import java.io.*;
import java.util.List;
import javax.swing.*;

import enchantment.Enchantment;
import enchantment.EnchantmentManager;
import frames.GameFrame;
import monster.Monster;
import monster.MonsterSpawner;
import panel.HallPanel;
import panel.InventoryPanel;
import panel.WinPanel;
import player.Player;
import player.PlayerManager;
import save.GameState;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Random;

public class GameManager implements Serializable {
	private static final long serialVersionUID = 1L;

	
    private  final int gridSize = 12; // size of grid
    private  final String rune = "src/assets/items/icons8-key-16.png"; // rune path
    private  final String openDoor = "src/assets/items/door_open.png";
    private String wallPath= "src/assets/items/wall_gargoyle_red_1.png";
    private String groundPath= "src/assets/items/floor_mud_e.png";
    private  String closeDoorPath = "src/assets/items/door_closed.png";
   private  String playerPath= "src/assets/rokue-like assets/player.png";
  
    
    public List<Timer> getTimers() {
		return timers;
	}

    public transient List<Timer> timers = new ArrayList<>();
    public transient Timer hallTimer;
    private transient ImageIcon playerIcon = new ImageIcon(playerPath);
    private transient InventoryPanel inventoryPanel;
    private transient HallPanel hallPanel;
    public transient GameFrame gameFrame;
    private transient CreateImageIcon iconCreator = new CreateImageIcon();
    private transient ObjectOverlay objectOverlay = new ObjectOverlay();
    private transient WinPanel winPanel = new WinPanel();
    private transient MouseListener objectMouseListener;
    public int[] TimeLeftWrapper= new int[] {0};
   
    
	public int currentHallIndex = 0; // Tracks the current hall
    public  Player player;
    private transient Random random = new Random();
    public  Cell[][] gridLabels = new Cell[gridSize][gridSize];
    private Cell[][] grid;
    
    public MonsterSpawner monsterSpawner;
    public boolean isPaused = false; // Variable to track the game's pause state

    
    public  ArrayList<Cell >objectList= new ArrayList();
    private ArrayList<Cell> doorList= new ArrayList();
    public  ArrayList<Cell[][]> completedHalls;
    private String[] hallNames = {"Earth Hall", "Air Hall", "Water Hall", "Fire Hall"};
    
    public  EnchantmentManager enchantmentManager;
    
    public LinkedHashMap<String, List<Enchantment>> enchantmentsMap ;


    private PlayerManager playerManager = new PlayerManager(this,gridLabels);
    private ArrayList<Monster> monsters ;
    
    // constructor;
    public GameManager(ArrayList<Cell[][]> completedHalls ) {
        this.completedHalls= completedHalls;
    }
   
    public int[] getTimeLeftWrapper() {
		return TimeLeftWrapper;
	}

    // Save the game state
    public void saveGame(String filePath) {
       
        System.out.println("in save game: spawn count:"+monsterSpawner.getSpawnCount());
        System.out.println("in save game: player lives:"+player.getLives());
        System.out.println("in save game: current hall index count:"+currentHallIndex);
        System.out.println("in save game: timeLeft:"+ TimeLeftWrapper[0]);
        System.out.println("in save game: enchantmentManager.getEnchantmentsMap(),:"+enchantmentManager.getEnchantmentsMap());
        System.out.println("in save game: monsterSpawner.getMonsters(),:"+ monsterSpawner.getMonsters());
        System.out.println("in save game:player row ,:"+ player.getRow());
        System.out.println("in save game: enchantmentManager.getRemovalCount(),:"+ enchantmentManager.getRemovalCount());
        System.out.println("in save game: enchnatment cell,:"+ enchantmentManager.getEnchantmentCell());
        //pauseGame();
        isPaused= false;
        System.out.println("is paused or not:"+ isPaused);
        
        GameState gameState = new GameState(
            completedHalls,
            currentHallIndex,
            TimeLeftWrapper[0],
            player,
            enchantmentManager.getEnchantmentsMap(),
            monsterSpawner.getMonsters(),
            enchantmentManager.getSpawnCount(),
            enchantmentManager.getRemovalCount(),
            monsterSpawner.getSpawnCount(),
            objectList,
            doorList,
            enchantmentManager.getEnchantmentCell(),
           
            enchantmentManager.getEnchantment()
            
            
        );

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath))) {
            oos.writeObject(gameState);
            System.out.println("Game saved successfully!");
        } catch (NotSerializableException e) {
            e.printStackTrace();
            System.err.println("Problematic class: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error saving game: " + e.getMessage());
        }

    }
    
 // Load the game state
    public void loadGame(String filePath) throws Exception {
    	this.timers = new ArrayList<Timer>();
    	System.out.println("timers:"+timers);
    	
    	resetAllTimers();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(filePath))) {
            GameState gameState = (GameState) ois.readObject();

            // Restore the saved state
            this.completedHalls = gameState.getHallStates();
            this.currentHallIndex = gameState.getCurrentHallIndex();
            this.TimeLeftWrapper[0] = gameState.getTimeLeft();
            this.player = gameState.getPlayer();
            this.objectList= gameState.getObjectList();
            this.doorList= gameState.getDoorList();
            this.isPaused= false;

            // Reinitialize transient fields
            this.iconCreator = new CreateImageIcon();
            this.objectOverlay = new ObjectOverlay();
            this.random = new Random();
            this.winPanel = new WinPanel();
            this.timers = new ArrayList<>();
            this.hallPanel = new HallPanel();
            this.gameFrame = new GameFrame("Game", 1200, 800, new Color(50, 34, 40));
            this.gameFrame.requestFocusInWindow();
            this.inventoryPanel = new InventoryPanel(gameFrame, this);
            this.inventoryPanel.putItemsOnInventory(player.getLives());
         // Reinitialize player
            if (player != null) {
                player.reinitialize(this); // Reinitialize transient fields of Player
            }

            this.playerManager.reinitialize(this);
            playerManager.setPlayer(player);
            System.out.println("in load game: player lives:"+player.getLives());
            System.out.println("is there any problem here:");
            
           // resumeGame();  
            System.out.println("after resumeGame");
            // Reinitialize the UI and timers
            System.out.println("is paused or not:"+isPaused());
            System.out.println("is paused or not:"+isPaused);
            initializeHallPanel();
           // initializeInventoryPanel(player.getLives());
            loadHallState();
            startTimerForCurrentHall(gameState.getTimeLeft());
            updateHallTitle();
           
            gameFrame.addComponent(hallPanel.getHallTitleLabel());
            gameFrame.addComponent(inventoryPanel);

            gameFrame.setVisible(true);
            gameFrame.makeVisible();
            
            // Restore monsters
            System.out.println("object list:"+ objectList);
            this.monsterSpawner= new MonsterSpawner(this.player, this.objectList, this);
            System.out.println("spawn count:"+gameState.getMonsterSpawnCount());
            System.out.println("monsters :"+gameState.getMonsters());
            monsterSpawner.loadState(
                gameState.getMonsters(),
                gameState.getMonsterSpawnCount()
            );

          //   Restore enchantments
            this.enchantmentManager= new EnchantmentManager(this, this.monsterSpawner.getFighterMonsters());
            enchantmentManager.loadState(
            
                gameState.getEnchantmentsMap(),
                gameState.getSpawnCount(),
                gameState.getRemovalCount(),
                gameState.getEnchantmentCell(),
                gameState.getEnchantment()
                
            );
            enchantmentManager.addKeyListener(gameFrame);
            inventoryPanel.addInventoryItems(gameState.getEnchantmentsMap());
            playerManager.addGameKeyListener(gameFrame);

           
            
            System.out.println("Game loaded successfully!");
        } catch (IOException | ClassNotFoundException e) {
            System.err.println("Error loading game: " + e.getMessage());
        }
    }
    
    private void loadHallState() {
    	
    	hallPanel.removeAll();
    	
    	//hallPanel.add(completedHalls.get(currentHallIndex));

        hallPanel.setLayout(new GridLayout(gridSize, gridSize)); // Use a grid layout for the hallPanel
        hallPanel.setPreferredSize(new Dimension(gridSize * 48, gridSize * 48)); // Set the preferred size
        hallPanel.setBounds(70, 50, gridSize * 48, gridSize * 48); // Set precise bounds
    	
    	for(int i=0; i<12; i++) {
    		for( int j=0; j<12;j++) {
    			Cell cell = completedHalls.get(currentHallIndex)[i][j];
    			 gridLabels[i][j] = cell;
    			cell.setBounds(j * 48, i * 48, 48, 48);
    			cell.setOpaque(true);
                cell.setHorizontalAlignment(SwingConstants.CENTER);
                cell.setVerticalAlignment(SwingConstants.CENTER);
    			hallPanel.add(cell);
    			if(cell.getName()=="object") {
    				 objectList.add(cell);
    			}
    			if(cell.getName()=="door") {
    				doorList.add(cell);
    			}
    			if(cell.getName()=="monster") {
    				
    			}
    			 addMouseListener(i, j);
    		}
    	}
    	
  //  hallPanel.setBackground(Color.blue);
    hallPanel.revalidate();
    hallPanel.validate();
    }



    public void startGame() throws Exception {
    	//  Façade pattern is used
        gameFrame = new GameFrame("Play Mode", 1200, 800, new Color(50, 34, 40));
        gameFrame.requestFocusInWindow();

        System.out.println("Completed halls: " + completedHalls.get(currentHallIndex));
        initializeHallPanel();

        initializeUI();
        addRuneRandomly();
        initializeInventoryPanel(3);
        player = playerManager.placePlayerRandomly();
        playerManager.addGameKeyListener(gameFrame);

        monsterSpawner = new MonsterSpawner(getPlayer(), getObjectList(), this);
        monsterSpawner.startSpawning(0);
       
        startTimerForCurrentHall(100);
        enchantmentManager = new EnchantmentManager(this, monsterSpawner.getFighterMonsters());

        enchantmentManager.startEnchantmentSpawning(0,0);
       
        enchantmentManager.addKeyListener(gameFrame);
        



        // Add all other components to the GameFrame
        gameFrame.addComponent(hallPanel.getHallTitleLabel());
        gameFrame.addComponent(hallPanel.getHallTitleLabel());
        gameFrame.addComponent(inventoryPanel);

        gameFrame.setVisible(true);
        gameFrame.makeVisible();


    }

    public void initializeHallPanel() {
        hallPanel = new HallPanel(); // Create the HallPanel instance
        gameFrame.add(hallPanel); // Add the HallPanel directly to the frame
    }

    public GameFrame getGameFrame() {
        return gameFrame;
    }

   

    public PlayerManager getPlayerManager() {
        return playerManager;
    }



    public  Player getPlayer() {
        return player;
    }



    public  Cell[][] getGridLabels() {
        return gridLabels;
    }



    public  ArrayList<Cell> getObjectList() {
        return objectList;
    }

   
    //PAUSE RESUME LOGIC
    public void addTimer(Timer timer) {
        timers.add(timer);
    }
    public void resetAllTimers() {
    	for (Timer timer : timers) {
            if (timer.isRunning()) {
               timer =null;
            }
        }
    }

    // Pause all timers
    public void pauseAllTimers() {
        for (Timer timer : timers) {
            if (timer.isRunning()) {
                timer.stop();
            }
        }
        if (hallTimer != null) hallTimer.stop();
        if (monsterSpawner != null) monsterSpawner.pauseSpawning();
        if (enchantmentManager != null) enchantmentManager.pauseSpawning();
    }


    // Resume all timers
    public void resumeAllTimers() {
        for (Timer timer : timers) {
            timer.start();
        }
        if (hallTimer != null) hallTimer.start();
        if (monsterSpawner != null) monsterSpawner.resumeSpawning();
        if (enchantmentManager != null) enchantmentManager.resumeSpawning();
    }

    // Remove a timer from the central list
    public void removeTimer(Timer timer) {
        timers.remove(timer);
    }



    // Pause the game
    public void pauseGame() {
        isPaused = true;
        pauseAllTimers(); // Pause all registered timers and components
        System.out.println("Game paused.");
    }

    public void resumeGame() {
        isPaused = false;
        resumeAllTimers(); // Resume all registered timers
        gameFrame.requestFocusInWindow(); // Regain focus
        System.out.println("Game resumed.");
    }

    public boolean isPaused() {
        return isPaused;
    }


    // Handle game over logic
    public void gameOver(String message) {
        isPaused = true; // Pause the game
        pauseAllTimers(); // Pause all registered timers and components
        JOptionPane.showMessageDialog(gameFrame, message, "Game Over", JOptionPane.INFORMATION_MESSAGE);
        new MainMenu(); // Navigate to the main menu
        gameFrame.dispose(); // Close the game frame
        System.out.println("Game over: " + message);
    }



    public void initializeInventoryPanel(int live) {
        System.out.println("Initializing Inventory Panel");
        
        System.out.println("live in initialize inventory panel:"+live);
        if (inventoryPanel == null) {
        	 System.out.println("yes");
            inventoryPanel = new InventoryPanel(gameFrame, this); // Pass the GameManager instance
        }

        // Remove and re-add to ensure proper layout
        gameFrame.getContentPane().remove(inventoryPanel);
        gameFrame.getContentPane().add(inventoryPanel);

        inventoryPanel.setBounds(800, 50, 200, 570); // Ensure proper positioning
        //inventoryPanel.setBackground(Color.blue);
        inventoryPanel.setVisible(true);
        inventoryPanel.putItemsOnInventory(live);
        gameFrame.revalidate();
        gameFrame.repaint();

        System.out.println("Inventory Panel Initialized: " + inventoryPanel);
    }

    public InventoryPanel getInventoryPanel() {
        System.out.println("panel in gamemanager:"+inventoryPanel);
        return inventoryPanel;
    }




    private void updateHallTitle() {
    	//hallPanel.setBackground(Color.red); // do not forget to delete this:
    	System.out.println("panel widht:"+hallPanel.getWidth());
    	System.out.println("panel height:"+hallPanel.getHeight());
        String title = hallNames[currentHallIndex];
        hallPanel.updateTitle(title);
    }

    public int countObjectsInCurrentHall() {
        Cell[][] selectedHall = completedHalls.get(currentHallIndex);
        int count = 0;

        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (selectedHall[i][j].getName()=="object"){
                    count++;
                }
            }
        }
        return count;
    }
   
    private void initializeUI() throws Exception {
    	System.out.println(" panel initializeUI:"+hallPanel.getWidth());
    	System.out.println(" panel height in initlaizeUi"+hallPanel.getHeight());

        int cellSize = 48;

        // Ensure there are completed halls available
        if (completedHalls == null || completedHalls.isEmpty()) {
            return;
        }


        Cell[][] selectedHall = completedHalls.get(currentHallIndex);

        hallPanel.setLayout(new GridLayout(gridSize, gridSize)); // Use a grid layout for the hallPanel
        hallPanel.setPreferredSize(new Dimension(gridSize * cellSize, gridSize * cellSize)); // Set the preferred size
        hallPanel.setBounds(70, 50, gridSize * cellSize, gridSize * cellSize); // Set precise bounds

        hallPanel.removeAll();
        ImageIcon floorIcon = iconCreator.getImageIcon(groundPath,cellSize,cellSize);
        ImageIcon wallIcon = iconCreator.getImageIcon(wallPath,cellSize,cellSize);
        ImageIcon doorIcon = iconCreator.getImageIcon(closeDoorPath,cellSize,cellSize);


        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                Cell cell = selectedHall[i][j];

                gridLabels[i][j] = cell;
                gridLabels[i][j].setBounds(j * cellSize, i * cellSize, cellSize, cellSize);
                gridLabels[i][j].setOpaque(true);
                gridLabels[i][j].setHorizontalAlignment(SwingConstants.CENTER);
                gridLabels[i][j].setVerticalAlignment(SwingConstants.CENTER);


                if("wall".equals(gridLabels[i][j].getName())){
                    gridLabels[i][j].setIcon(wallIcon);

                }
                if("floor".equals(gridLabels[i][j].getName())){
                    gridLabels[i][j].setIcon(floorIcon);

                }

                if ("object".equals( gridLabels[i][j].getName())) {
                    System.out.println("yes this is object");

                    ImageIcon objectIcon= (ImageIcon) cell.getIcon();

                    objectOverlay.revertToGround(gridLabels[i][j]);
                    ImageIcon floorIconn = iconCreator.getImageIcon(groundPath, 48,48);
                    gridLabels[i][j].setIcon(floorIconn);
                    objectOverlay.addObjectOverlay(gridLabels[i][j], objectIcon);

                    objectList.add(gridLabels[i][j]);
                    System.out.println("in initialize");
                    checkMouseListeners(gridLabels[i][j]);
                    removeAllMouseListeners(gridLabels[i][j]);
                    addMouseListener(i, j);
                }
                if("door".equals(gridLabels[i][j].getName())) {
                    gridLabels[i][j].setIcon(doorIcon);

                    doorList.add(gridLabels[i][j]);
                }


                hallPanel.add(gridLabels[i][j]); // Treat cells as part of a composite grid composite pattern.
            }
        }

        updateHallTitle();
        System.out.println("afte panel widht initializeUI:"+hallPanel.getWidth());
    	System.out.println("after panel height in initlaizeUi"+hallPanel.getHeight());
        // Refresh the hallPanel
        hallPanel.revalidate();
        hallPanel.repaint();
    }


    public void addExtraTime(int seconds) {
    	 TimeLeftWrapper[0] += seconds; // Add the extra time
        inventoryPanel.updateTimeLabel(TimeLeftWrapper[0]); // Update the UI to reflect the new time
        System.out.println("Added " + seconds + " seconds. New time left: " + TimeLeftWrapper[0]);



    }
    public void startTimerForCurrentHall(int timeLeft) {
        // Calculate time based on the number of objects in the hall
       // int objectCount = countObjectsInCurrentHall();
      //  timeLeft = 100;//objectCount * 5; // 5 seconds per object

        TimeLeftWrapper[0]= timeLeft;
        inventoryPanel.updateTimeLabel(timeLeft);
        hallTimer = new Timer(1000, e -> {
            if (TimeLeftWrapper[0] > 0) {
            	TimeLeftWrapper[0]--;
                inventoryPanel.updateTimeLabel(TimeLeftWrapper[0]);

            } else {
                ((Timer) e.getSource()).stop();

                gameOver("Time's up! Game Over."); //new addition
                new MainMenu();
                gameFrame.dispose(); // End the game
            }
        });
        hallTimer.start();
    }

    private void addRuneRandomly() {

        int length = objectList.size();
        if (length == 0) {
            System.out.println("No objects available to add a rune.");
            return; // Skip if there are no objects
        }

        int num= random.nextInt(length);
        System.out.println("before add rune:"+objectList.get(num).getCellRune());
        System.out.println("empty before add rune:"+objectList.get(num).getIsEmpty());
        objectList.get(num).setCellRune("rune");

        System.out.println("after add rune:"+objectList.get(num).getCellRune());
        System.out.println("name of rune object:"+objectList.get(num).getIcon());


    }


    public void cleanHall() {
        // Remove all components from the hall panel
        hallPanel.removeAll();

        // Clear gridLabels and reset each JLabel
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                if (gridLabels[i][j] != null) {
                    gridLabels[i][j].setIcon(null); // Remove any icons
                    gridLabels[i][j].setName(null); // Reset the name
                    gridLabels[i][j].removeAll();  // Remove any overlays
                }
            }
        }

        objectList.clear();
        doorList.clear();

        hallPanel.revalidate();
        hallPanel.repaint();
    }
 // facade pattern is used here as well.
    public void loadNextHall() throws Exception {
    	System.out.println("next hall");
    	if (completedHalls == null || completedHalls.isEmpty()) {
            throw new Exception("No halls available"); // Throw an exception if no halls are available
        }

        if (currentHallIndex < completedHalls.size() - 1) {
            if (monsterSpawner != null) {
                monsterSpawner.stopSpawning();
            }

            currentHallIndex++;

            updateHallTitle();
            initializeUI();
            initializeInventoryPanel(3);
            inventoryPanel.clearInventoryLabel();
            addRuneRandomly();
            player = playerManager.placePlayerRandomly();
            System.out.println("second get player:" + getPlayer());

            monsterSpawner.getFighterMonsters().clear();
            monsterSpawner = new MonsterSpawner(getPlayer(), getObjectList(), this);
            monsterSpawner.startSpawning(0);
            
            startTimerForCurrentHall(100);

            enchantmentManager.stopSpawning();
            enchantmentManager.clearEnchantmentsMap();
            enchantmentManager = new EnchantmentManager(this, monsterSpawner.getFighterMonsters());
            enchantmentManager.startEnchantmentSpawning(0,0);
         
            enchantmentManager.addKeyListener(gameFrame);
        } else {
            if (monsterSpawner != null) {
                monsterSpawner.stopSpawning();
            }
            winPanel.displayWinPanel(gameFrame, this);
        }
    }

    private Cell findDoorLabel() {
        return doorList.get(0);
    }
//// Observer/Publish-Subscribe Pattern
    private void checkMouseListeners(JComponent component) {
        MouseListener[] listeners = component.getMouseListeners();

        if (listeners.length > 0) {
            System.out.println("MouseListeners found on: " + component.getName());
            for (MouseListener listener : listeners) {
                System.out.println("Listener: " + listener);
            }
        } else {
            System.out.println("No MouseListeners found on: " + component.getName());
        }
    }
   // Observer/Publish-Subscribe Pattern
    private void addMouseListener(int row, int col) {
        objectMouseListener = new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                System.out.println("in mouse clicked");
                if (SwingUtilities.isLeftMouseButton(e)) { // BUTTON1 for left click
                    System.out.println("Cell clicked: (" + row + ", " + col + ")");
                    try {
                        handleCellClick(row, col);
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            }
        };

        gridLabels[row][col].setVisible(true);
        gridLabels[row][col].setEnabled(true);
        gridLabels[row][col].addMouseListener(objectMouseListener);
    }
//Observer/Publish-Subscribe pattern
    public void handleCellClick(int row, int col) throws Exception {
        int distance = Math.abs(player.getRow() - row) + Math.abs(player.getCol() - col);
        String RuneName = gridLabels[row][col].getCellRune();
        System.out.println("distance:" + distance);
        System.out.println("runeName:" + RuneName);

        if (distance == 1) {
            if ("rune".equals(RuneName)) {
                System.out.println("You found the hidden rune!");
                SoundPlayer.playSound("src/assets/sounds/door-opening.wav");
                Cell runeLabel = gridLabels[row][col];
                objectOverlay.revertToGround(runeLabel);

                runeLabel.setEmpty(false);
                ImageIcon runeIcon = new ImageIcon(rune);
                objectOverlay.addObjectOverlay(runeLabel, runeIcon);

                ImageIcon icon = new ImageIcon(openDoor);
                Cell door = findDoorLabel();
                ImageIcon floorIcon = iconCreator.getImageIcon(groundPath, 48, 48);

                door.setIcon(floorIcon);
                door.setName("door");

                objectOverlay.overlayLabel(door, icon, 48);
                door.setEmpty(true);
            }
        } else {
            System.out.println("This object is empty.");
        }
    }

    private void removeAllMouseListeners(JComponent component) {
        MouseListener[] listenersBefore = component.getMouseListeners();

        for (MouseListener listener : listenersBefore) {
            component.removeMouseListener(listener);
        }

        MouseListener[] listenersAfter = component.getMouseListeners();
        if (listenersAfter.length > 0) {
            System.out.println("Failed to remove all mouse listeners.");
        } else {
            System.out.println("All mouse listeners removed.");
        }
    }
}
