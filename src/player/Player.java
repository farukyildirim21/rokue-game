package player;

import java.io.Serializable;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import frames.GameFrame;
import panel.InventoryPanel;
import game.GameManager;

public class Player implements Serializable  {
	 private static final long serialVersionUID = 1L; 
    private int row, col;
    private int lives = 3;//
    private boolean wearingCloak = false;
    private transient GameManager gameManager;
    private transient InventoryPanel panel;

    
    public Player(int row, int col, GameManager gameManager) {
    	
        this.row = row;
        this.col = col;
        this.gameManager = gameManager;
        
        panel= gameManager.getInventoryPanel();
        
    }



    public void addLive(int live) {
    	lives+=live;
    	panel.updateLives(lives);
    }

    public void setLives(int lives) {
        this.lives = lives;
    }

    public void loseLife() {
        lives--;
        panel.updateLives(lives);
        
        if (lives <= 0) {
            gameManager.gameOver("You Lose All Lives"); // Notify GameManager of game over
        }
    }
    public int getRow() {
        return row;
    }

    public int getLives() {
		return lives;
	}

	public void setRow(int row) {
		this.row = row;
	}
	
    public GameManager getGameManager() {
        return gameManager;
    }

	public void setCol(int col) {
		this.col = col;
	}

	public int getCol() {
        return col;
    }

    public boolean getWearingCloak() {
        return wearingCloak;
    }

    public void setWearCloak() {
        this.wearingCloak = true;
    }
    // Reinitialize transient fields
    public void reinitialize(GameManager gameManager) {
        this.gameManager = gameManager;
        this.panel = gameManager.getInventoryPanel();
    }


}
