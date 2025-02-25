package enchantment;

import java.io.Serializable;
import java.util.LinkedHashMap;

import javax.swing.Timer;

import player.Player;

public class CloakOfProtectionEnchantment extends Enchantment implements Serializable {
	 private static final long serialVersionUID = 1L;
    Player player;
    Timer timer;
	public CloakOfProtectionEnchantment(String type, Player player) {
		super(type);
		this.player= player;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void useEnchantment() {
		timer = new Timer(20000, e -> {
		        player.setWearCloak() ;
		        System.out.println("Cloak of Protection has worn off. Hero is visible to archers again.");
		    });
		timer.setRepeats(false); // Run only once
		timer.start();
		player.setWearCloak();
		
	}

	
	

}
