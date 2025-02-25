package enchantment;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Random;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.SwingUtilities;
import javax.swing.Timer;

import monster.ArcherMonster;
import monster.FighterMonster;
import monster.Monster;
import monster.WizardMonster;
import panel.InventoryPanel;
import player.Player;
import game.Cell;
import game.GameManager;
import game.ObjectOverlay;
import game.SoundPlayer;

import java.util.ArrayList;

public class EnchantmentManager  implements Serializable{
	
	private transient Timer spawnTimer, actionTimer, removalTimer; // Marked as transient
    private transient ObjectOverlay objectOverlay = new ObjectOverlay(); // Marked as transient
    private transient MouseListener enchantmentMouseListener; // Marked as transient
    private transient Random random = new Random(); // Marked as transient
    private transient InventoryPanel inventoryPanel; // Marked as transient
	 
	 
   	private Cell enchantmentCell;
	private boolean isPaused = false;
	
	private Cell[][] grid;
	private int enchantmentRow, enchantmentCol;
	private GameManager gameManager;
	
	private String timePath = "src/assets/items/time2.png";
    private String livePath = "src/assets/rokue-like assets/heart.png";
    private String luringGemPath = "src/assets/items/luring2.png";
    private String cloakOfProtectionPath = "src/assets/items/cloak2.png";
    private String revealPath = "src/assets/items/Reveal2.png";
    private  Enchantment enchantment;
    private boolean enchantmentOnHallWhenSaved= true;
   


	private String direction="";
    private boolean isLureModeActive = false; 
   
    private  ArrayList<Cell >objectList;
    private  Player player;
    private  LinkedHashMap<String, List<Enchantment>> enchantmentsMap ;
    private  LinkedHashMap<String, List<Enchantment>> savedEnchantmentsMap ;
	private  ArrayList<FighterMonster> fighterMonsters;
   
    private String time="time";
    private String live="live";
    private String luringGem= "luring gem";
    private String cloakOfProtection="cloak of protection";
    private String reveal= "reveal";
    private int[] spawnCountWrapper= new int[] {0};
    private int[] removalCountWrapper = new int[] {0} ;
    
	
	public EnchantmentManager(GameManager gameManager, ArrayList<FighterMonster> fighterMonsters) {
		System.out.println("in enchantment maanger");
		this.gameManager= gameManager;
		grid= gameManager.getGridLabels();
		this.fighterMonsters= fighterMonsters;
		player= gameManager.getPlayer();
		inventoryPanel= gameManager.getInventoryPanel();
		
		objectList= gameManager.getObjectList();
		enchantmentsMap= new LinkedHashMap<>();
	}
	public Cell getEnchantmentCell() {
		return enchantmentCell;
	}

	 public Enchantment getEnchantment() {
			return enchantment;
		}
    
    public void setEnchantmentCell(Cell enchantmentCell) {
		this.enchantmentCell = enchantmentCell;
	}
	public LinkedHashMap<String, List<Enchantment>> getEnchantmentsMap() {
		return enchantmentsMap;
	}
 // Save timer states
    public int getSpawnCount() {
        return  spawnCountWrapper[0];
    }

    public int getRemovalCount() {
        return removalCountWrapper[0];
    }


    
 // Restore timer states and reinitialize timers
    public void loadState(LinkedHashMap<String, List<Enchantment>> savedMap, int savedSpawnCount, int savedRemovalCount, Cell enchantmentCell, Enchantment ench) {
        clearEnchantmentsMap();
        System.out.println("saved map in load state:"+savedMap);
        enchantmentsMap.putAll(savedMap);
        System.out.println("enchnatmentsMap in load state:"+enchantmentsMap);
        System.out.println("enchnatment cel in load state:"+enchantmentCell);
        System.out.println("enchnatment  in load state:"+ench);
       
        this.spawnTimer= null;
        this.actionTimer= null;
        this.removalTimer= null;
        this.grid = gameManager.getGridLabels();
        this.objectList = gameManager.getObjectList();
        this.player = gameManager.getPlayer();
        this.inventoryPanel = gameManager.getInventoryPanel();
        this.objectOverlay = new ObjectOverlay();
        this.random = new Random();
        
        // Restart timers
       
        if(enchantmentCell!=null) {
        	System.out.println("yes I am here:");
        	 startTimerForRemoveEnchantment(savedRemovalCount, enchantmentCell);
        	 
        	 addEnchantmentMouseListener(enchantmentCell, ench);
        	 enchantmentOnHallWhenSaved= false;
        }
        startEnchantmentSpawning(savedSpawnCount, 0);
       
        
    }
	
    public void startEnchantmentSpawning(int spawnCount , int removalCount) {
    	System.out.println("in enchantment spawning");
        if (spawnTimer != null) {
            spawnTimer.stop();
        }
        // Use a mutable wrapper for spawnCount
        spawnCountWrapper[0]= spawnCount ;
        spawnTimer = new Timer(1000, e -> {
        	
            spawnCountWrapper[0]++;
        	if(spawnCountWrapper[0] == 12) {
        		try {
					spawnEnchantment(removalCount);
					spawnCountWrapper[0]=0;
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
        	}
        });

        gameManager.addTimer(spawnTimer);
        spawnTimer.start();
       
    }
    public void startTimerForRemoveEnchantment(int removalCount, Cell enchantmentCell) {
    	System.out.println("time is started for remove enchantment");
    	
        if (removalTimer != null) {
        	System.out.println("it should stop:");
            removalTimer.stop();
        }
        removalCountWrapper[0] = removalCount;
        removalTimer = new Timer(1000, e -> {
        	 removalCountWrapper[0]++;
        	System.out.println("removal timer:"+ removalCountWrapper[0]);
        	if(removalCountWrapper[0]==6) {
        		 removeEnchantmentfromHall(enchantmentCell);
        		 removalCountWrapper[0]=0;
        		
        	}
           
            
        });

        gameManager.addTimer(removalTimer);
        removalTimer.start();
       
    }
    
    
    public void clearEnchantmentsMap() {
    	System.out.println("in clear enchantment");
    	enchantmentsMap.clear();
    }
    public void resetSpawning() {
    	System.out.println("in reset spawning");
        if (spawnTimer != null) {
            spawnTimer.stop(); // Stop the existing timer
            System.out.println("Spawning timer reset.");
        }
        spawnTimer = new Timer(12000, e -> {
            try {
                spawnEnchantment(0);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        });
        spawnTimer.start(); // Start a fresh timer
    }

    
   

	public void stopSpawning() {
		System.out.println("in stop spawning");
        if (spawnTimer != null) {
            spawnTimer.stop();
            System.out.println("Monster spawning stopped.");
            gameManager.removeTimer(spawnTimer); // Remove the spawn timer from GameManager
        }
        if (actionTimer != null) {
            actionTimer.stop();
            gameManager.removeTimer(actionTimer); // Remove the action timer from GameManager
        }
        if (removalTimer != null) {
        	removalTimer.stop();
        	gameManager.removeTimer(removalTimer); // Remove the removal timer from GameManager
        }
       
    }
    
	// Pause all timers
    public void pauseSpawning() {
    	System.out.println("in pause spawning");
        if (!isPaused) {
            
            if (spawnTimer != null) {
                
                spawnTimer.stop();
            }

            if (removalTimer != null) {
               
                removalTimer.stop();
            }

            isPaused = true;
            
        } else {
            System.out.println("Timers are already paused.");
        }
    }


 // Resume all timers
    public void resumeSpawning() {
    	System.out.println("in resume spawning");
        if (isPaused) {
            if (spawnTimer != null) {
                
                spawnTimer.start();
                
            }

            if (removalTimer != null) {
                
                removalTimer.start();
               
            }

            isPaused = false;
           
        } else {
            System.out.println("Timers are not paused. No need to resume.");
        }
    }

    public Cell findRuneCell() {
    	System.out.println("in find runeCell");
    	
		for(Cell cell: objectList) {
			if(cell.getCellRune().equals("rune")) {
				return cell;
			}
			
			
				
			
		}
		System.out.println("there is no rune in object list");
		return null;
	}

    
    
    public  void spawnEnchantment(int removalCount) throws Exception {

    	
    	System.out.println("in spawn Enchnatmnet");

        // Find a random empty cell
    	enchantmentRow = random.nextInt(grid.length-1);
    	enchantmentCol = random.nextInt(grid[0].length-1);
       
        boolean checkEmpty= grid[enchantmentRow][enchantmentCol].getIsEmpty();
        System.out.println("enchantment cell empty or not:"+checkEmpty);
       
        while(checkEmpty != true) {
        	 
        	enchantmentRow = random.nextInt(grid.length-1);
        	enchantmentCol = random.nextInt(grid[0].length-1);
            checkEmpty= grid[enchantmentRow][enchantmentCol].getIsEmpty();
           
           
        }
        
        
        
        int type =random.nextInt(5);
        System.out.println("enchantment row:"+enchantmentRow);
        System.out.println("enchantment col:"+enchantmentCol);
        enchantmentCell = grid[enchantmentRow][enchantmentCol];
        
        System.out.println("type:"+type);
        switch (type) {
            case 0 -> {
               enchantment = new ExtraTimeEnchantment(time, gameManager);
               objectOverlay.overlayLabel(enchantmentCell, new ImageIcon(timePath),32);
               enchantmentCell.setEmpty(false);
              
               System.out.println("is in time");
               // monsterLabel.setForeground(Color.red);
            }
            case 1 -> {
            	 enchantment= new ExtraLiveEnchantment(live, gameManager.getPlayer());
            	 objectOverlay.overlayLabel(enchantmentCell, new ImageIcon(livePath),32);
            	 enchantmentCell.setEmpty(false);
            	// enchantmentCell.setText("live");
            	 System.out.println("is in live");
            }
            case 2 -> {
            	 enchantment= new RevealEnchantment(reveal, gameManager, findRuneCell().getCellXPosition(), findRuneCell().getCellYPosition());
            	 objectOverlay.overlayLabel(enchantmentCell, new ImageIcon(revealPath),32);
            	 enchantmentCell.setEmpty(false);
            	 //enchantmentCell.setText("reveal");
            	 System.out.println("is in reveal");
            	 
            }
            case 3 -> {
            	 enchantment= new CloakOfProtectionEnchantment(cloakOfProtection, gameManager.getPlayer());
            	 objectOverlay.overlayLabel(enchantmentCell, new ImageIcon(cloakOfProtectionPath),32);
            	 enchantmentCell.setEmpty(false);
            	// enchantmentCell.setText("cloak");
            	 System.out.println("is in cloak");
             }
            case 4 -> {
            	 enchantment= new LuringGemEnchantment(luringGem, fighterMonsters,player );
            	 objectOverlay.overlayLabel(enchantmentCell, new ImageIcon(luringGemPath),32);
            	 enchantmentCell.setEmpty(false);
            	// enchantmentCell.setText("luring");
            	 System.out.println("is in luring");
             }
            default -> throw new IllegalStateException("Unexpected monster type: " + type);
        }
        
        addEnchantmentMouseListener(enchantmentCell, enchantment );
        startTimerForRemoveEnchantment(removalCount, enchantmentCell);
		
	}
    
    
    
    
    private void removeEnchantment(String type) throws Exception {
    	System.out.println("in remove Enchantment");
        if (checkEnchantmentInMap(type)) {
            List<Enchantment> enchantmentList = enchantmentsMap.get(type);
            enchantmentList.remove(0); // Remove the first enchantment of the specified type
            inventoryPanel.addInventoryItems(enchantmentsMap);
            
            if (enchantmentList.isEmpty()) {
                enchantmentsMap.remove(type);
                inventoryPanel.addInventoryItems(enchantmentsMap);
            }
            System.out.println("Removed one " + type + " enchantment. Updated map: " + enchantmentsMap);
        } else {
            System.out.println("No enchantments of type: " + type + " to remove.");
        }
    }
    private boolean checkEnchantmentInMap(String enchantName) {
    	System.out.println("in check checkEnchantmentInMap");
    	System.out.println("hashmap in check:"+enchantmentsMap);
    	System.out.println("type in check:"+enchantName);    
    	if(enchantmentsMap.containsKey(enchantName)) {
    		return true;
    	}
    	return false;
    }
    
    private Enchantment getEnchantmentFromMap(String type) {
    	System.out.println("in  getEnchantmentFromMap");
        if (checkEnchantmentInMap(type)) {
            return enchantmentsMap.get(type).get(0); // Return the first enchantment of the specified type
        }
        System.out.println("No enchantments of type: " + type + " found.");
        return null;
    }
    
    	
    
    
    

    private void addEnchantment(Cell cell, Enchantment enchantment) throws Exception {
        System.out.println("Adding enchantment: " + enchantment.getType());
        
        enchantmentsMap.computeIfAbsent(enchantment.getType(), k -> new ArrayList<>()).add(enchantment);
        System.out.println("map in add enchantment:"+enchantmentsMap);
        
        inventoryPanel.addInventoryItems(enchantmentsMap);

        System.out.println("Updated enchantments map: " + enchantmentsMap);
        removeEnchantmentfromHall(cell);
    }
    
    
    public void addEnchantmentMouseListener(Cell cell, Enchantment ench) {
        // Add a MouseListener to detect clicks on the enchantment cell
    	enchantmentMouseListener = new MouseAdapter() {
        
            @Override
            public void mouseClicked(MouseEvent e) {
                if (SwingUtilities.isLeftMouseButton(e)) {
                    System.out.println("Enchantment clicked: " + ench);
                    System.out.println("name of enchantment is:"+ench.getType());
                     
                    
                    
              	  if (ench instanceof ExtraTimeEnchantment) {
              		  System.out.println("yes this is time enchantment");
                        ((ExtraTimeEnchantment) ench).useEnchantment(); // am not sure. look at here:
                        removeEnchantmentfromHall(cell);
                    }
              	  else if (ench instanceof ExtraLiveEnchantment) {
              		System.out.println("yes this is live enchantment");
                     ((ExtraLiveEnchantment) ench).useEnchantment(); // am not sure. look at here:
                       removeEnchantmentfromHall(cell);
                 }
              	  else {
              		  System.out.println("ench not time or live");
              		  try {
						addEnchantment(cell, ench);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
              		  
              	  }
              	
                }
            }
            
        };
        cell.addMouseListener(enchantmentMouseListener);
        
        
    }

    private void handleDirectionalKey(KeyEvent e) throws Exception {
    	System.out.println("Lure mode activated. Press A, D, W, or S to throw the lure.");
        switch (e.getKeyCode()) {
            case KeyEvent.VK_A:
            	System.out.println("A");
                direction = "A"; // Left
                break;
            case KeyEvent.VK_D:
            	System.out.println("d");
                direction = "D"; // Right
                break;
            case KeyEvent.VK_W:
            	System.out.println("w");
                direction = "W"; // Up
                break;
            case KeyEvent.VK_S:
            	System.out.println("s");
                direction = "S"; // Down
                break;
            default:
                System.out.println("Invalid key pressed in lure mode. Press A, D, W, or S.");
                return;
        }

        if (!direction.isEmpty()) {
            System.out.println("Lure thrown in direction: " + direction);
            SoundPlayer.playSound("src/assets/sounds/luringGemSound.wav");
            Enchantment enchantment = getEnchantmentFromMap(luringGem);
            if (enchantment instanceof LuringGemEnchantment) {
            	((LuringGemEnchantment) enchantment).setLuringActiveOfFighter(true);
                ((LuringGemEnchantment) enchantment).useEnchantment(direction); // Use the enchantment
               
            }
            removeEnchantment(luringGem); // Remove the used enchantment from the bag
            
            isLureModeActive = false; // Exit lure mode
            direction = ""; // reset direction;
        }
    }

public void addKeyListener(JFrame frame) {
    	
        frame.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
            	System.out.println("before key pressed hash map:"+enchantmentsMap);
                if (e.getKeyCode() == KeyEvent.VK_R) {
                    System.out.println("R key pressed. Highlighting the area...");
                        if(checkEnchantmentInMap(reveal)) {
                        	System.out.println("yes in may bag");
                        	getEnchantmentFromMap(reveal).useEnchantment(); 
                            try {
								removeEnchantment(reveal);
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
                        }
                   
                    }
                   
                
                if (e.getKeyCode() == KeyEvent.VK_P) {
                    System.out.println("P key pressed. protect the player...");
                   if(checkEnchantmentInMap(cloakOfProtection)) {
                	   getEnchantmentFromMap(cloakOfProtection).useEnchantment();
                       try {
						removeEnchantment(cloakOfProtection);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
                    	
                    }

                }

                if (e.getKeyCode() == KeyEvent.VK_B) {
                    System.out.println("B key pressed. Entering lure mode...");
                    System.out.println("enc map: in press:"+enchantmentsMap);
                    if (checkEnchantmentInMap(luringGem)) {
                        isLureModeActive = true; // Activate lure mode
                        
                        System.out.println("Lure mode activated. Press A, D, W, or S to throw the lure.");
                    } 

              
                }
                if (isLureModeActive) {
                	try {
						handleDirectionalKey(e);
					} catch (Exception e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} // Handle directional keys while in lure mode
                }

                
                
                }
        });
    }

    
    public void removeEnchantmentfromHall(Cell cell) {
    	//inactiveEnchantments.add(cell)
    	
    	if (enchantmentMouseListener != null) {
            cell.removeMouseListener(enchantmentMouseListener);
            enchantmentMouseListener = null; // Clean up the reference
        }
    	objectOverlay.revertToGround(cell);
    	
    	
    }

}
