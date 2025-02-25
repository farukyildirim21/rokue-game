package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import java.util.ArrayList;

import game.Cell;
import game.GameManager;

/**
 * Test suite for GameManager's handleCellClick method.
 * Tests the functionality of clicking cells in the game grid,
 * particularly focusing on rune discovery mechanics.
 */
public class HandleClickTest {
    private GameManager gameManager;
    private ArrayList<Cell[][]> completedHalls;

    @BeforeEach
    public void setUp() {
        completedHalls = new ArrayList<>();
        Cell[][] testHall = new Cell[12][12];
        
        // Initialize basic hall
        for (int i = 0; i < 12; i++) {
            for (int j = 0; j < 12; j++) {
                testHall[i][j] = new Cell();
                testHall[i][j].setName("floor");
                testHall[i][j].setEmpty(true);
            }
        }
        
        // Add walls and door
        for (int i = 0; i < 12; i++) {
            testHall[0][i].setName("wall");
            testHall[11][i].setName("wall");
            testHall[i][0].setName("wall");
            testHall[i][11].setName("wall");
            testHall[0][i].setEmpty(false);
            testHall[11][i].setEmpty(false);
            testHall[i][0].setEmpty(false);
            testHall[i][11].setEmpty(false);
        }
        testHall[6][11].setName("door");
        completedHalls.add(testHall);
        
        try {
            gameManager = new GameManager(completedHalls);
            gameManager.startGame();
        } catch (Exception e) {
            fail("Failed to create GameManager: " + e.getMessage());
        }
    }

    /**
     * Method Specification for handleCellClick:
     * 
     * @requires
     * - row and col are valid grid coordinates (0 <= row, col < gridSize)
     * - player object is initialized and has valid position
     * - gridLabels array is properly initialized
     * 
     * @modifies
     * - state of the clicked cell (isEmpty, cellRune properties)
     * - door cell's appearance if rune is found
     * - object overlay appearances
     * 
     * @effects
     * - if distance to player is 1 and cell contains rune:
     *   - reveals the rune
     *   - changes door appearance
     *   - plays sound
     * - if conditions not met, cell remains unchanged
     */

    @Test
    public void testHandleCellClick_ValidRuneDiscovery() throws Exception {
        // Setup adjacent rune cell
        Cell clickedCell = gameManager.getGridLabels()[1][1];
        clickedCell.setCellRune("rune");
        gameManager.getPlayer().setRow(1);
        gameManager.getPlayer().setCol(2);

        // Execute click
        gameManager.handleCellClick(1, 1);

        // Verify rune was discovered
        assertFalse(clickedCell.getIsEmpty(), "Cell should not be empty after rune discovery");
    }

    @Test
    public void testHandleCellClick_TooFarFromPlayer() throws Exception {
        // Setup distant rune cell
        Cell clickedCell = gameManager.getGridLabels()[5][5];
        clickedCell.setCellRune("rune");
        gameManager.getPlayer().setRow(1);
        gameManager.getPlayer().setCol(1);

        // Execute click
        gameManager.handleCellClick(5, 5);

        // Verify rune remains hidden
        assertEquals("rune", clickedCell.getCellRune(), 
                    "Rune should not be revealed when player is too far");
    }

    @Test
    public void testHandleCellClick_NonRuneCell() throws Exception {
        // Setup adjacent non-rune cell
        Cell clickedCell = gameManager.getGridLabels()[1][1];
        clickedCell.setCellRune("");
        gameManager.getPlayer().setRow(1);
        gameManager.getPlayer().setCol(2);

        // Execute click
        gameManager.handleCellClick(1, 1);

        // Verify cell remains unchanged
        assertTrue(clickedCell.getIsEmpty(), "Non-rune cell should remain empty");
    }
}
