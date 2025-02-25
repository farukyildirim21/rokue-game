package enchantment;

import java.io.Serializable;
import java.util.LinkedHashMap;

import game.GameManager;

public class ExtraTimeEnchantment extends Enchantment implements Serializable {
	 private static final long serialVersionUID = 1L;
	GameManager gameManager;
	public ExtraTimeEnchantment(String type, GameManager gameManager) {
		super(type);
		this.gameManager= gameManager;
		// TODO Auto-generated constructor stub
	}

	@Override
	public void useEnchantment() {
		gameManager.addExtraTime(5);
		
	}

	
	
	
	

}
