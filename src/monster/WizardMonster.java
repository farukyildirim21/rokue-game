package monster;

import java.util.Timer;
import java.io.Serializable;
import java.util.ArrayList;
import javax.swing.JLabel;
import player.Player;
import game.Cell;
import game.GameManager;
import game.ObjectOverlay;

public class WizardMonster extends Monster  implements Serializable{
	
	
	// we use strategy pattern
	 private static final long serialVersionUID = 1L;
    private Cell[][] grid;
    private ArrayList<Cell> objectList;
   
    private ObjectOverlay objectOverlay = new ObjectOverlay();
   
    private boolean isActive = true;
    private Timer behaviorTimer; // Reference to the timer used in behaviors
    private int lastBehaviorZone = -1; // Keeps track of the last executed behavior zone
    private ArrayList<Monster> monsters;
    public void setLastBehaviorZone(int lastBehaviorZone) {
		this.lastBehaviorZone = lastBehaviorZone;
	}

	public WizardMonster(int row, int col, ArrayList<Cell> objectList, GameManager gameManager,ArrayList<Monster> monsters) {
        super(row, col, gameManager);
        this.grid = gameManager.getGridLabels();
        this.objectList = objectList;
        this.row = row;
        this.col = col;
        this.monsters= monsters;
        
    }
    
    public Cell[][] getGrid() {
        return grid; // Ensure `grid` is of type `Cell[][]`
    }

    public ArrayList<Cell> getObjectList() {
        return objectList;
    }

    @Override
    protected void performAction(Player player) {
        if (!gameManager.isPaused()) {

        }
    }
    
    public boolean isActive() {
        return isActive;
    }

    
    public void act(Player player, GameManager gameManager) {
    	
        if (!isActive) return;
        System.out.println("passed");
        int totalTime = 100; //gameManager.countObjectsInCurrentHall()*5; 
        int remainingTime =gameManager.getTimeLeftWrapper()[0];
        int percentage = (remainingTime * 100) / totalTime;

        
        int currentZone = (percentage < 30) ? 0 : (percentage > 70 ? 2 : 1);
      
        if (currentZone == lastBehaviorZone) return;

        
        lastBehaviorZone = currentZone;
        
        switch (currentZone) {
            case 0 -> new LowTimeBehavior().executeBehavior(this,gameManager);
            case 1 -> new MidTimeBehavior().executeBehavior(this,gameManager);
            case 2 -> new HighTimeBehavior().executeBehavior(this, gameManager);
        }
    }

    
    public void setBehaviorTimer(Timer timer) {
        if (behaviorTimer != null) {
            behaviorTimer.cancel();
            behaviorTimer.purge();
        }
        this.behaviorTimer = timer;
    }
    
    public void disappear(Monster monster) {
    	
        isActive = false; // Mark the wizard as inactive
        if (behaviorTimer != null) {
            behaviorTimer.cancel();
            behaviorTimer.purge();
        }
        objectOverlay.revertToGround(grid[row][col]);
        if(!monsters.isEmpty()) {
        	//monsters.remove(monster);
        }
        
        System.out.println("Wizard should disaappear.");
    }
}
