package monster;

import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.Timer;

import player.Player;
import game.Cell;
import game.GameManager;
import game.ObjectOverlay;

public class FighterMonster extends Monster {
	private boolean isLuringActive= false;
	private Timer luringTimer; 
	private ObjectOverlay objectOverlay= new ObjectOverlay();
	
    
	private Cell[][] grid;
    private String fighterIconPath = "src/assets/rokue-like assets/fighter.png";
    private ImageIcon fighterIcon= new ImageIcon(fighterIconPath);
    private String direction="";  // look at here do not forget:
    int newRow, newCol;
    int[] selectedMove;
    

    public void setDirection(String direction) {
		this.direction = direction;
	}

	public FighterMonster(int row, int col, GameManager gameManager) {
        super(row, col, gameManager);
        grid= gameManager.getGridLabels();
    }

    @Override
    public void performAction(Player player) {
    	
        int distance = Math.abs(player.getRow() - row) + Math.abs(player.getCol() - col);
        if (distance == 1) {
            player.loseLife();
        }
        
        if (!isLuringActive) {
            try {
				moveRandomly();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // Regular movement if not lured
        }
        else {
            try {
				moveFighter();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} // Handle luring movement if active
        }
       
       
    }
  

    private void moveFighter() throws Exception {
    	if (!isLuringActive) {
            moveRandomly();
        } else {
            // Handle luring movement
            if (luringTimer == null) {
                luringTimer = new Timer(1000, e -> goLuringDirection());
                luringTimer.start();
            }
        }
 }
    private void  moveRandomly() throws Exception{
    	    System.out.println("move randomly:");
		   int[][] moves = {{0, 1}, {1, 0}, {-1, 0}, {0, -1}, }; // Possible moves
	       selectedMove = moves[new java.util.Random().nextInt(moves.length)];
	       newRow = row + selectedMove[0];
	       newCol = col + selectedMove[1];
           System.out.println("newRow"+newRow);
           System.out.println("newcol"+newCol);
	      if (grid[newRow][newCol].getIsEmpty()) {
	          Cell currentLabel = grid[row][col];
	          objectOverlay.revertToGround(currentLabel);

	          Cell newLabel = grid[newRow][newCol];
	          objectOverlay.overlayLabel(newLabel,fighterIcon,32);

	          row = newRow;
	          col = newCol;
	      }
	      else {
	      	selectedMove = moves[new java.util.Random().nextInt(moves.length)];
	      	newRow = row + selectedMove[0];
	          newCol = col + selectedMove[1];
	      }
	  	
		   
	   }	

    	
    	
    public void setLuringActive(boolean active) {
		isLuringActive = active;
	}

   

   
    private void goLuringDirection() {
    	System.out.println("we are in luring direction ");
    	
    	
    	switch (direction) {
        case "A":
            try {
				goLeftDirection(-1);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
            break;
        case "D":
            try {
				goRightDirection(1);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            break;
        case "W":
            try {
				goUpDirection(-1);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            break;
        case "S":
            try {
				goDownDirection(1);
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            break;
        default:
            System.out.println("Invalid direction key! Please press A, D, W, or S.");
    }
    	
    }
    private void goRightDirection(int step) throws Exception {
        int newCol = col + step; // Initial column to check

        // Loop until the next cell is non-empty
        if (newCol < grid.length && grid[row][newCol].getIsEmpty()) {
        	
            
            // Clear the current cell
            Cell currentLabel = grid[row][col];
            objectOverlay.revertToGround(currentLabel);

            // Move to the new cell
            col = newCol; // Update the hero's position
            Cell newLabel = grid[row][col];
            objectOverlay. overlayLabel(newLabel,fighterIcon, 32);

            // Update the next column
          //  newCol += step;
        }

        System.out.println("Reached a nonempty cell at: " + row + ", " + newCol);
    }

    private void goLeftDirection(int step) throws Exception {
        int newCol = col + step; // Initial column to check

        // Loop until the next cell is non-empty
        if (newCol >= 0 && grid[row][newCol].getIsEmpty()) { // Ensure within bounds
        	
            

            // Clear the current cell
            Cell currentLabel = grid[row][col];
            objectOverlay.revertToGround(currentLabel);

            // Move to the new cell
            col = newCol; // Update the hero's position
            Cell newLabel = grid[row][col];
            objectOverlay.overlayLabel(newLabel,fighterIcon, 32);

            // Update the next column
          //  newCol += step; // Continue in the left direction (step should be negative)
        }

        System.out.println("Reached a nonempty cell or boundary at: " + row + ", " + newCol);
    }

    private void goDownDirection(int step) throws Exception {
        int newRow = row + step; // Initial row to check

        // Loop until the next cell is non-empty
        if (newRow < grid.length && grid[newRow][col].getIsEmpty()) { // Ensure within bounds
        	

            // Clear the current cell
            Cell currentLabel = grid[row][col];
            objectOverlay.revertToGround(currentLabel);

            // Move to the new cell
            row = newRow; // Update the hero's position
            Cell newLabel = grid[row][col];
            objectOverlay. overlayLabel(newLabel,fighterIcon,32);

            // Update the next row
          //  newRow += step; // Continue moving downward (step should be positive)
        }

        System.out.println("Reached a nonempty cell or boundary at: " + newRow + ", " + col);
    }

    private void goUpDirection(int step) throws Exception {
        int newRow = row + step; // Calculate the next row to move

        // Loop until the next cell is non-empty or the top boundary is reached
        if (newRow >= 0 && grid[newRow][col].getIsEmpty()) { // Ensure within bounds
        	
            // Clear the current cell
            Cell currentLabel = grid[row][col];
            objectOverlay. revertToGround(currentLabel);

            // Move to the new cell
            row = newRow; // Update the hero's current position
            Cell newLabel = grid[row][col];
            objectOverlay. overlayLabel(newLabel,fighterIcon, 32);

            // Update the next row
           // newRow += step; // Continue moving upward (step should be negative)
        }

        System.out.println("Reached a nonempty cell or boundary at: " + newRow + ", " + col);
    }
    public void stopLuringMovement() {
        if (luringTimer != null) {
            luringTimer.stop();
            luringTimer = null;
        }
        isLuringActive = false;
    }
}
