package monster;


import java.io.Serializable;
import java.util.ArrayList;
import java.util.Random;
import javax.swing.JLabel;
import javax.swing.Timer;
import player.Player;
import game.GameManager;

public abstract class Monster  implements Serializable{
	private static final long serialVersionUID = 1L;
    protected int row, col;
    protected GameManager gameManager;

    public Monster(int row, int col, GameManager gameManager) {
        this.row = row;
        this.col = col;
        this.gameManager = gameManager;
       
    }

    public void act(Player player) {
    	
        if (gameManager.isPaused()) return; // Stop acting if the game is paused
       
        performAction(player);
    }

    protected abstract void performAction(Player player);
}
