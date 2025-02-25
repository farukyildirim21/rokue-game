package monster;

import javax.swing.*;

import player.Player;

import java.awt.Color;
import java.awt.Image;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import game.Cell;
import game.GameManager;
import game.ObjectOverlay;

public class MonsterSpawner  implements Serializable {
	 private static final long serialVersionUID = 1L;
	
    private Cell[][] grid;
    private  Random random = new Random();
    private Player player;
    private ArrayList<Monster> monsters = new ArrayList<>();
    private ArrayList<Monster> savedMonsterList = new ArrayList<>();
    private ArrayList<FighterMonster> fighterMonsters = new ArrayList<>();
    private ArrayList<Cell> objectList;
    private String archer = "src/assets/rokue-like assets/archer.png";
    private String fighter = "src/assets/rokue-like assets/fighter.png";
    private String wizard = "src/assets/rokue-like assets/wizard.png";
    private  Timer spawnTimer;
    private  Timer newTimer;
    private  Timer actionTimer;
    private boolean isPaused = false;
    private  GameManager gameManager;
    private  FighterMonster fighterMonster;
   
    private  ObjectOverlay objectOverlay= new ObjectOverlay();
    private int[] spawnCountWrapper= new int[] {0};
    Monster monster;
  
	public ArrayList<Monster> getMonsters() {
		return monsters;
	}

    public int getSpawnCount() {
        return spawnCountWrapper[0];
    }


    public MonsterSpawner( Player player, ArrayList<Cell> objectList, GameManager gameManager) {
        this.grid = gameManager.getGridLabels();
        this.player = player;
        this.objectList=  objectList;
        this.gameManager = gameManager;
       
    }
    
 // Restore monsters and timer state
    public void loadState(ArrayList<Monster> savedMonsters, int savedSpawnCount) {
    	monsters.clear();
    for(int i=0; i<savedMonsters.size();i++) {
    	//savedMonsterList.add(savedMonsters.get(i));
    	monsters.add(savedMonsters.get(i));
    }
    	
    System.out.println("saved monsters:"+monsters);
    
     
        gameManager.isPaused= false;
       
        this.spawnTimer = null; // Reset timers (will be restarted later)
        this.actionTimer = null; // Reset timers (will be restarted later)
       
        this.grid = gameManager.getGridLabels();
        this.objectOverlay = new ObjectOverlay(); // Reinitialize
        this.random = new Random(); // Reinitialize
        this.newTimer= null;
        gameManager.timers= new ArrayList<Timer>();
       
        System.out.println("normal gameManager for wizard:"+gameManager);
        System.out.println("timers in monster Spawner load state:"+gameManager.getTimers());
        
        act(monsters);
        System.out.println("after start Act");
        startSpawning(savedSpawnCount);
        System.out.println("soawning starts");
    }
   
   
   
    public void startSpawning(int spawnCount) {
    	
        if (spawnTimer == null) {
        	spawnCountWrapper[0] = spawnCount;
            spawnTimer = new Timer(1000, e -> {
            	spawnCountWrapper[0]++;
            	
            	
               if(spawnCountWrapper[0]==8) {
            	   try {
            		  
            		   gameManager.isPaused= false;
					spawnMonster();
					spawnCountWrapper[0] = 0; // Reset the count
					
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
               }
            });
            gameManager.addTimer(spawnTimer);
           
        }
        spawnTimer.start();
        act(monsters);
       

        
    }
   
    public void act(ArrayList<Monster> monsters) {
    	System.out.println("time  left:"+gameManager.getTimeLeftWrapper()[0]);
    	for(Monster monster: monsters) {
    		
    		if(monster instanceof WizardMonster) {
    			System.out.println("is active or not of monsters:"+((WizardMonster) monster).isActive());
    		}
    		
    	}
    	
    	
    	if (actionTimer == null) {
    		System.out.println("we are in actionTimer");
    		
            actionTimer = new Timer(1000, e -> {
            	 System.out.println("actionTimer starts");
               
                monsters.removeIf(monster -> monster instanceof WizardMonster && !((WizardMonster) monster).isActive());
                System.out.println("after monster is changed or not:"+monsters);
                
                if(!monsters.isEmpty()) {
                for (Monster monster :  monsters) {
                    if (monster instanceof WizardMonster wizardMonster) {
                       //Observer Pattern
                    	//WizardMonster and potentially other monsters interact with the game state (GameManager) and other components like the player.
                    	//They act as observers of the game state, reacting to changes and performing actions accordingly.

                        wizardMonster.act(player, gameManager);
                    } else {
                    	
                        monster.act(player); // Strategy patern: Other monsters act normally
                    }
                }
                }
            });
           
            actionTimer.start();
            gameManager.addTimer(actionTimer);
        }
    	
    	}
    	
    
   
    public void stopSpawning() {
        if (spawnTimer != null) {
            spawnTimer.stop();
            System.out.println("Monster spawning stopped.");
        }
        if (actionTimer != null) {
            actionTimer.stop();
        }
        monsters.clear();
    }
    
    public void pauseSpawning() {
        if (!isPaused) {
          
            if (spawnTimer != null) {
                
                spawnTimer.stop();
            }

            if (actionTimer != null) {
              
                actionTimer.stop();
            }

            isPaused = true;
        }
    }


    public void resumeSpawning() {
        if (isPaused) {
            if (spawnTimer != null) {
               
            	
                spawnTimer.start();
            }

            if (actionTimer != null) {
              
                actionTimer.start();
            }

            isPaused = false;
        }
    }

    private void spawnMonster() throws Exception {
    	gameManager.isPaused= false;

    	//int cellSize = 50; 
        int row, col;

        // Find a random empty cell
        row = random.nextInt(grid.length-1);
        col = random.nextInt(grid[0].length-1);
       
        boolean checkEmpty= grid[row][col].getIsEmpty();
          // playerin hareketiyle ayni zamanda gelebilirler.w
       
        while(checkEmpty != true) {
        	 
        	row = random.nextInt(grid.length-1);
            col = random.nextInt(grid[0].length-1);
            checkEmpty= grid[row][col].getIsEmpty();
           
           
        }
        
       
        int type = random.nextInt(3);
        
        Cell groundLabel = grid[row][col];
        // Factory Method Pattern: the method determines the type of monster to create (ArcherMonster, FighterMonster, or WizardMonster) and 
        //encapsulates the logic for their creation. 
        //This abstracts the instantiation details from the rest of the class.

        switch (type) {
            case 0 -> {
                monster = new ArcherMonster(row, col, gameManager);
                groundLabel.setName("monster");
                objectOverlay.overlayLabel(groundLabel, new ImageIcon(archer), 32);
                
               // monsterLabel.setForeground(Color.red);
            }
            case 1 -> {
                monster = new FighterMonster(row, col,  gameManager);
                System.out.println("fighter in  monster spawner");
                System.out.println("monste in spawninginr:"+monster);
                System.out.println("is it puase or not in fighter:"+gameManager.isPaused);
                fighterMonster= (FighterMonster) monster;
                fighterMonsters.add(fighterMonster);
              //  System.out.println("fighter in  monster spawner:"+fighterMonster);
              //  groundLabel.setName("monster");
                objectOverlay.overlayLabel(groundLabel, new ImageIcon(fighter), 32);
                //monsterLabel.setForeground(Color.orange);
            }
            case 2 -> {
                monster = new WizardMonster(row, col, objectList, gameManager,monsters);
                
                objectOverlay.overlayLabel(groundLabel, new ImageIcon(wizard), 32);
            }
            default -> throw new IllegalStateException("Unexpected monster type: " + type);
        }

      
        
        monsters.add(monster);
       
        
    }

	public ArrayList<FighterMonster> getFighterMonsters() {
		return fighterMonsters;
	}

	

   
}
