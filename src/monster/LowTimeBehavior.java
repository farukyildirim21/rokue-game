package monster;

import player.PlayerManager;
import player.Player;
import game.Cell;
import game.ObjectOverlay;

import javax.swing.*;

import java.io.Serializable;
import java.util.Random;
import game.GameManager;

public class LowTimeBehavior implements WizardBehavior ,Serializable {
	 private static final long serialVersionUID = 1L;
	  private  ObjectOverlay objectOverlay = new ObjectOverlay();
  

    @Override
    public void executeBehavior(WizardMonster monster, GameManager gameManager) {
    	
    	if (!monster.isActive()) return; // Prevent execution if the wizard is inactive
    	
        try {
            // Clear the wizard's cell using revertToGround
          //  objectOverlay.revertToGround(monster.getGrid()[monster.row][monster.col]);
            
         // Clear the player's current position using revertToGround
            PlayerManager playerManager = gameManager.getPlayerManager();
     
            objectOverlay.revertToGround(playerManager.gridLabels[monster.gameManager.getPlayerManager().getPlayer().getRow()][monster.gameManager.getPlayerManager().getPlayer().getCol()]);

            // Teleport the player to a new random position
            playerManager.placePlayerRandomly();

            System.out.println("Wizard teleported the player to a new random location!");
            
            // Mark the wizard as inactive and remove it
            monster.disappear(monster);
            
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("Error during teleportation: " + e.getMessage());
        }
    }
}
