package monster;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

import javax.swing.Timer;
import javax.swing.*;
import java.util.ArrayList;
import java.util.Random;
import java.util.TimerTask;
import player.Player;
import game.Cell;
import game.GameManager;

public class HighTimeBehavior implements WizardBehavior , Serializable{
	 private static final long serialVersionUID = 1L;
     private Timer behaviorTimer; // Timer for moving the rune
    
    @Override
    public void executeBehavior(WizardMonster monster , GameManager gameManager) {
    	System.out.println("in high time behavior:");
    	
    	 if (!monster.isActive()) return;

         // If a timer is already running, do not create a new one
         if (behaviorTimer != null && behaviorTimer.isRunning()) {
             return;
         }
        
         ArrayList<Cell> objectList = monster.getObjectList();
         behaviorTimer = new Timer(3000, new ActionListener() {
             @Override
             public void actionPerformed(ActionEvent e) {
            	 System.out.println("hight time action performed should work");
                 if (!monster.isActive()) {
                     behaviorTimer.stop();
                     gameManager.removeTimer(behaviorTimer); // Remove the timer from GameManager
                     behaviorTimer = null; // Clear the reference to avoid reuse
                     return;
                 }

                 // Clear the old rune location
                 for (Cell obj : objectList) {
                     if ("rune".equals(obj.getCellRune())) {
                         obj.setCellRune("noRune");
                     }
                 }

                 // Move the rune to a new random location
                 int randomIndex = new Random().nextInt(objectList.size());
                 System.out.println("random index:"+randomIndex );
                 objectList.get(randomIndex).setCellRune("rune");
                 

                 System.out.println("Wizard moved the rune to a new location!");
             }
         });

         behaviorTimer.setRepeats(true);
         behaviorTimer.start();
        if(behaviorTimer!=null) {
        	System.out.println("yes");
        	System.out.println("behaviourTimer:"+behaviorTimer);
        	System.out.println("behaviourTimer gameManager:"+ gameManager);
        	System.out.println("timers in gameManager:"+ gameManager.getTimers());
        	gameManager.addTimer(behaviorTimer); 
        	
        }
       
       
        
    }
}