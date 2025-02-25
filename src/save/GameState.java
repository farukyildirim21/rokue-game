// GameState.java
package save;

import game.Cell;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import enchantment.Enchantment;
import enchantment.EnchantmentManager;
import monster.Monster;
import monster.MonsterSpawner;
import player.Player;
import player.PlayerManager;

public class GameState implements Serializable {
    private static final long serialVersionUID = 1L;

    private ArrayList<Cell[][]> hallStates;
    private int currentHallIndex;
    Player player;
    private ArrayList<Monster> monsters;
    private int spawnCount;    // Enchantment spawn count
    private int removalCount;  // Enchantment removal timer count
    private int monsterSpawnCount; // Monster spawn timer count
	private int timeLeft;
	private LinkedHashMap<String, List<Enchantment>> enchantmentsMap;
	private ArrayList<Cell >objectList;
     private  ArrayList<Cell> doorList;
    private  Cell enchantmentCell;
    
      private  Enchantment enchantment;
   
	
	public GameState(
			ArrayList<Cell[][]> hallStates, 
			int currentHallIndex, 
			int timeLeft, 
			Player player,
            LinkedHashMap<String, 
            List<Enchantment>> enchantmentsMap,
            ArrayList<Monster> monsters,
            int spawnCount,
            int removalCount, 
            int monsterSpawnCount,
            ArrayList<Cell >objectList,
            ArrayList<Cell> doorList,
            Cell enchantmentCell,
           
            Enchantment enchantment
			
			) {
		this.hallStates = hallStates;
        this.currentHallIndex = currentHallIndex;
        this.timeLeft = timeLeft;
        this.player = player;
        this.enchantmentsMap = enchantmentsMap;
        this.monsters = monsters;
        this.spawnCount = spawnCount;
        this.removalCount = removalCount;
        this.monsterSpawnCount = monsterSpawnCount;
        this.objectList= objectList;
        this.doorList= doorList;
        this.enchantmentCell= enchantmentCell;
       
       this.enchantment=enchantment;
    }

	 
		public Cell getEnchantmentCell() {
			return enchantmentCell;
		}

		public Enchantment getEnchantment() {
			return enchantment;
		}



    
    public ArrayList<Cell> getObjectList() {
		return objectList;
	}



	public ArrayList<Cell> getDoorList() {
		return doorList;
	}



	public LinkedHashMap<String, List<Enchantment>> getEnchantmentsMap() {
		return enchantmentsMap;
	}

    public Player getPlayer() {
        return player;
    }
    public int getSpawnCount() {
        return spawnCount;
    }

    public int getRemovalCount() {
        return removalCount;
    }

    public int getMonsterSpawnCount() {
        return monsterSpawnCount;
    }
	

	public ArrayList<Monster> getMonsters() {
		return monsters;
	}


	
    public ArrayList<Cell[][]> getHallStates() {
        return hallStates;
    }

    public int getCurrentHallIndex() {
        return currentHallIndex;
    }
    public int getTimeLeft() {
        return timeLeft;
    }

    
}