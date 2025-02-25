package enchantment;

import java.io.Serializable;
import java.util.LinkedHashMap;

import player.Player;

public class ExtraLiveEnchantment extends Enchantment implements Serializable {
	
	 private static final long serialVersionUID = 1L;
	
	Player player;
	
	public ExtraLiveEnchantment(String type, Player player) {
		super(type);
		this.player= player;
		// TODO Auto-generated constructor stub
	}
	@Override
	public void useEnchantment() {
            player.addLive(1);		
		
	}
	
	
	


	

}
