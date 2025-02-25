package player;

import javax.swing.*;

import frames.GameFrame;
import game.Cell;
import game.GameManager;
import game.ObjectOverlay;

import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.Serializable;
import java.util.Random;

public class PlayerManager implements Serializable{
	private static final long serialVersionUID = 1L; // Add this for serialization
    private  Player player;
    private transient  GameFrame gameFrame;
    private transient  Random random = new Random();
    private final int gridSize= 12;
   
    private transient ObjectOverlay objectOverlay= new ObjectOverlay();
    private final String playerPath= "src/assets/rokue-like assets/player.png";
    private transient ImageIcon playerIcon= new ImageIcon(playerPath);
    public  Cell[][] gridLabels ;
    private transient GameManager gameManager;
    
    
    
  
    
    public PlayerManager(GameManager gameManager, Cell[][] gridLabels) {
    	this.gridLabels= gridLabels;
        this.gameManager = gameManager;
        this.gameFrame= gameManager.getGameFrame();
    }

    public Player getPlayer() {
        return player;
    }
    public void setPlayer(Player player) {
		this.player = player;
	}

	public void reinitialize(GameManager gameManager) {
        this.gameManager = gameManager;
        this.gameFrame = gameManager.getGameFrame();
        this.objectOverlay = new ObjectOverlay();
        this.playerIcon = new ImageIcon(playerPath);
        this.random = new Random();
     
    }

    public Player placePlayerRandomly() throws Exception {
    	
        int playerRow; // Player's starting position
        int playerCol;

        // Generate initial random position
        playerRow = random.nextInt(gridSize - 2) + 1; 
        playerCol = random.nextInt(gridSize - 2) + 1; 
        System.out.println("playerRow randomly: " + playerRow);
        System.out.println("playerCol randomly: " + playerCol);

        // Initialize player with the initial random position
       
        player = new Player(playerRow, playerCol,  gameManager);
        player.setCol(playerCol);
        player.setRow(playerRow);

        Cell playerLabel = gridLabels[playerRow][playerCol]; // Get the label for the initial position
        System.out.println("playerLabel.isEmpty: " + playerLabel.getIsEmpty());
        
        // Check if the label is not empty and update the position if needed
        while (!playerLabel.getIsEmpty()) { 
            playerRow = random.nextInt(gridSize - 2) + 1; 
            playerCol = random.nextInt(gridSize - 2) + 1; 
            System.out.println("loop");

            // Update player position
            player.setCol(playerCol);
            player.setRow(playerRow);
           // playerLabel.setEmpty(false);
            // Update playerLabel to the new random position
            playerLabel = gridLabels[playerRow][playerCol];
        }

        // Add the player overlay to the final valid position
        
        objectOverlay.overlayLabel(playerLabel, playerIcon,32);
        System.out.println("is here");
        return player;
    }
  //Strategy Pattern
  // player-specific behaviors
    //There is Command Pattern as well.
    public void addGameKeyListener(JFrame frame) {
        KeyListener gameKeyListener = new KeyListener() {
            @Override
            public void keyPressed(KeyEvent e) {
                try {
                    switch (e.getKeyCode()) {
                        case KeyEvent.VK_UP -> {
                            try {
                                movePlayer(-1, 0); // Move up
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                System.out.println("Error while moving player up: " + ex.getMessage());
                            }
                        }
                        case KeyEvent.VK_LEFT -> {
                            try {
                                movePlayer(0, -1); // Move left
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                System.out.println("Error while moving player left: " + ex.getMessage());
                            }
                        }
                        case KeyEvent.VK_DOWN -> {
                            try {
                                movePlayer(1, 0); // Move down
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                System.out.println("Error while moving player down: " + ex.getMessage());
                            }
                        }
                        case KeyEvent.VK_RIGHT -> {
                            try {
                                movePlayer(0, 1); // Move right
                            } catch (Exception ex) {
                                ex.printStackTrace();
                                System.out.println("Error while moving player right: " + ex.getMessage());
                            }
                        }
                    }
                } catch (Exception ex) {
                    ex.printStackTrace();
                    System.out.println("Unexpected error in key handling: " + ex.getMessage());
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {}

            @Override
            public void keyTyped(KeyEvent e) {}
        };

        frame.addKeyListener(gameKeyListener);
    }

  
 public void movePlayer(int rowChange, int colChange) throws Exception {
	 if (gameManager.isPaused()) return;
    	
    	int playerRow=  player.getRow();
        int playerCol= player.getCol();
      
        int newRow = playerRow + rowChange;
        int newCol = playerCol + colChange;
        
       
        if(isEmpty(newRow,newCol)==true) {
        	

              Cell currentLabel = gridLabels[playerRow][playerCol];
              objectOverlay. revertToGround(currentLabel);
      
              Cell newLabel = gridLabels[newRow][newCol];
              newLabel.setEmpty(false);
              objectOverlay. overlayLabel(newLabel,playerIcon,32);
            
            player.setCol(newCol);
            player.setRow(newRow);
            currentLabel.setEmpty(true);
            
            
            CheckPlayerInDoor(newRow, newCol);   
            
           
        }
        
    }
 public void CheckPlayerInDoor(int newRow, int newCol) {
	 // Observer Pattern
	 //Interaction with GameManager and game elements like doors or timers.
	 if ("door".equals(gridLabels[newRow][newCol].getName())) {
         System.out.println("Player reached the open door. Loading next hall...");
         
         if (gameManager.hallTimer != null) {
        	 gameManager.hallTimer.stop();
         }
      
            gameManager.cleanHall();
    	    gridLabels[player.getRow()][player.getCol()].setIcon(null); 
    	    
         try {
        	 // if player in door. şt w
        	 gameManager.loadNextHall();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // Transition to the next hall
     }
	 

	 
 }
 public boolean isEmpty(int row, int col) {
 	//boolean empty= false;
 	Cell cell= gridLabels[row][col];
 	
 	if(cell.getIsEmpty()==true) {
 		
 		return true;
 	}
 	return false;
 	
 }
 public boolean repOk() {
	    if (gridLabels == null || gridLabels.length < gridSize || gridLabels[0].length < gridSize) {
	        return false; // Grid must be initialized and large enough
	    }

	    // Check for null entries in the grid
	    for (int i = 0; i < gridSize; i++) {
	        for (int j = 0; j < gridSize; j++) {
	            if (gridLabels[i][j] == null) {
	                return false; // No cell in the grid should be null
	            }
	        }
	    }

	    if (player != null) {
	        int row = player.getRow();
	        int col = player.getCol();
	        if (row < 0 || row >= gridSize || col < 0 || col >= gridSize) {
	            return false; // Player's position must be within bounds
	        }
	        if (gridLabels[row][col].getIsEmpty()) {
	            return false; // Player's current cell must not be empty
	        }
	    }

	    return true;
	}

    
    
    
    

   
}
