package monster;

import player.Player;
import game.GameManager;
import game.ObjectOverlay;

import javax.swing.Timer;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;

public class MidTimeBehavior implements WizardBehavior,Serializable {
	private static final long serialVersionUID = 1L;
   
    private Timer behaviorTimer; // Declare the timer as a class-level field

    @Override
    public void executeBehavior(WizardMonster monster, GameManager gameManager) {
    	
    	System.out.println("midTime behaviour works");
    	System.out.println("midTime midtime is active or not:"+monster.isActive());
        if (!monster.isActive()) return; // Prevent execution if the wizard is inactive

        System.out.println("Wizard is indecisive. It will disappear in 2 seconds.");
        System.out.println("behaviourTimer:"+behaviorTimer);
        behaviorTimer = new Timer(2000, new ActionListener() {
        	
            @Override
            public void actionPerformed(ActionEvent e) {
            	System.out.println("behaviour timer is work");
                monster.disappear(monster);
                gameManager.removeTimer(behaviorTimer); // Remove the timer from GameManager
            }
        });
        System.out.println("after behaviour timer");
        behaviorTimer.setRepeats(false);
        behaviorTimer.start();

        // Register the timer with GameManager for pause/resume support
       gameManager.addTimer(behaviorTimer);
    }
}

