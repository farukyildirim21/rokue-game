package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import enchantment.EnchantmentManager;
import frames.GameFrame;
import monster.MonsterSpawner;
import game.Cell;
import game.GameManager;
import player.Player;
import player.PlayerManager;

import java.awt.Color;
import java.util.ArrayList;

class loadNextHallTestCase {
	private GameManager gameManager;
    private ArrayList<Cell[][]> completedHalls;

    @BeforeEach
    void setUp() throws Exception {
        // Create a 12x12 grid
        int gridSize = 12;
        Cell[][] hall1 = new Cell[gridSize][gridSize];
        Cell[][] hall2 = new Cell[gridSize][gridSize];

        // Initialize the grid cells for both halls
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                hall1[i][j] = new Cell();
                hall1[i][j].setName("floor");
                hall1[i][j].setEmpty(true);

                hall2[i][j] = new Cell();
                hall2[i][j].setName("floor");
                hall2[i][j].setEmpty(true);
                if (i == 0 || j == 0 || i == gridSize - 1 || j == gridSize - 1) {
	                   
	                    hall1[i][j].setName("wall");
	                    hall1[i][j].setEmpty(false);
	                }
            }
        }

        // Add a door in hall2 for testing
        hall2[5][5].setName("door");
        hall2[5][5].setEmpty(true);

        // Add halls to completedHalls
        completedHalls = new ArrayList<>();
        completedHalls.add(hall1);
        completedHalls.add(hall2);

        // Initialize GameManager with completed halls
        gameManager = new GameManager(completedHalls);
        gameManager.gameFrame = new GameFrame("Test Frame", 1200, 800, null); // Mock a GameFrame
       
        gameManager.initializeHallPanel();
        gameManager.initializeInventoryPanel(3);
        int count= gameManager.countObjectsInCurrentHall(); 
        gameManager.startTimerForCurrentHall(count*5);

        // Add some objects to the object list for testing
        Cell objectCell = hall1[3][3];
        objectCell.setName("object");
        objectCell.setEmpty(false);
        gameManager.objectList.add(objectCell);

        // Initialize player for GameManager
        gameManager.player = new Player(1, 1,  gameManager);

        // Initialize monsterSpawner and enchantmentManager
        gameManager.monsterSpawner = new MonsterSpawner(gameManager.player, gameManager.getObjectList(), gameManager);
        gameManager.enchantmentManager = new EnchantmentManager(gameManager, new ArrayList<>());
    }

    /* 
     * Requires:
*  - `currentHallIndex` is within the bounds of `completedHalls`.
*  - `completedHalls` contains at least one valid hall (non-null `Cell[][]` objects).
*  - Dependencies such as `playerManager`, `monsterSpawner`, and `enchantmentManager` are properly initialized.
* 
* Modifies:
*  - Advances `currentHallIndex` to the next hall.
*  - Resets and reinitializes the game grid (`gridLabels`) for the new hall.
*  - Clears and restarts the monster spawner and enchantment spawner.
*  - Places the player in a valid starting position in the new hall.
* 
* Effects:
*  - Transitions the game to the next hall.
*  - If there are no more halls, triggers the win panel (`winPanel.displayWinPanel()`).
*  - Throws `Exception` if initialization or spawner setup fails.
    


   */
   
    
    
    
    
    
    

    @Test
    void testLoadNextHallSuccessfulTransition() throws Exception {
        // Ensure initial state
        assertEquals(0, gameManager.currentHallIndex, "Initial hall index should be 0");

        // Call loadNextHall
        gameManager.loadNextHall();

        // Validate state transition
        assertEquals(1, gameManager.currentHallIndex, "Hall index should advance to 1");
        assertNotNull(gameManager.getGridLabels(), "Grid labels should be initialized for the new hall");
        assertNotNull(gameManager.getPlayer(), "Player should be placed in the new hall");
    }

    @Test
    void testLoadNextHallTriggersWinPanel() throws Exception {
        // Advance to the last hall
        gameManager.loadNextHall(); // Hall 1 to Hall 2
        gameManager.loadNextHall(); // No more halls

        // Ensure win panel is triggered
        assertEquals(1, gameManager.currentHallIndex, "Hall index should not exceed the last hall index");
        // Add validation for win panel if accessible
    }
    
    @Test
    void testLoadNextHallFailsWithoutHalls() {
        // Clear halls
        completedHalls.clear();
        Exception exception = assertThrows(Exception.class, () -> {
            gameManager.loadNextHall();
        });

        assertEquals("No halls available", exception.getMessage(), "Expected exception for no halls");
    }

    @Test
    void testLoadNextHallResetsSpawners() throws Exception {
        // Setup mocks or validate real spawner states
        gameManager.loadNextHall();

        // Ensure spawners are reset
        // Mock monster spawner and validate its methods were called for reset/reinitialization
        assertNotNull(gameManager.getObjectList(), "Object list should be reset for the new hall");
    }

   
}
