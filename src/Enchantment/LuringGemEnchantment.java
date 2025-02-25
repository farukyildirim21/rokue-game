package enchantment;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;

import javax.swing.Timer;

import monster.FighterMonster;
import monster.Monster;
import player.Player;

public class LuringGemEnchantment extends Enchantment implements Serializable{
	private static final long serialVersionUID = 1L;
    private ArrayList<FighterMonster> fighterMonsters;
    private Boolean activation;
    Timer lureTimer;
    Player player;
	public LuringGemEnchantment(String type,ArrayList<FighterMonster> fighterMonsters,Player player) {
		super(type);
		this.fighterMonsters= fighterMonsters;
		this.player= player;
		// TODO Auto-generated constructor stub
	}

	public void useEnchantment(String direction) {
		
		 // Activate luring mode for all fighters
        for (FighterMonster fighter : fighterMonsters) {
            fighter.setDirection(direction);
            fighter.setLuringActive(true); // Pause regular actions
        }

        // Schedule deactivation after 5 seconds
        if (lureTimer != null) {
            lureTimer.stop();
        }

        lureTimer = new Timer(5000, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deactivateLure();
            }

        });

        lureTimer.setRepeats(false);
        lureTimer.start();
    

	}
	
		
		
	private void deactivateLure() {
        System.out.println("Deactivating lure effect...");
        for (FighterMonster fighter : fighterMonsters) {
        	 fighter.stopLuringMovement();// Resume regular actions
        }
    }

	@Override
	public void useEnchantment() {
		// TODO Auto-generated method stub
		
	}
	public void setLuringActiveOfFighter(Boolean active) {
		activation= active;
		System.out.println("setLuringActiveOfFighter:"+activation);
		
		//return activation;
	}
	

	

}
