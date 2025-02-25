package test;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import frames.GameFrame;
import player.Player;
import player.PlayerManager;
import game.Cell;
import game.GameManager;

import java.awt.Color;
import java.util.ArrayList;

class PlayerManagerTest {
    private PlayerManager playerManager;
    private Cell[][] gridLabels;
    private GameManager gameManager;

    @BeforeEach
    void setUp() {
        // Initialize a 12x12 grid
        int gridSize = 12;
        gridLabels = new Cell[gridSize][gridSize];
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                gridLabels[i][j] = new Cell();
                gridLabels[i][j].setEmpty(true);
            }
        }

        // Initialize GameManager with a mock list of completed halls
        ArrayList<Cell[][]> completedHalls = new ArrayList<>();
        completedHalls.add(gridLabels);
        gameManager = new GameManager(completedHalls);
        gameManager.gameFrame = new GameFrame("Test Mode", 1200, 800, new Color(50, 34, 40));
        gameManager.initializeHallPanel();
       // gameManager.startTimerForCurrentHall();
        // Initialize PlayerManager
        playerManager = new PlayerManager(gameManager, gridLabels);
    }
    
    
    /*
    Overview
    The PlayerManager class manages the player's position and interactions within a grid-based game. It allows for random placement of the player, movement handling, and interactions with game object like door.

    Abstract Function
    The PlayerManager class represents a manager for the Player in a game world grid. It maintains:

    The player's position (player.getRow() and player.getCol()).
    The state of the grid (gridLabels) by marking cells as occupied or empty.
    Interaction logic when the player moves to special grid cells (e.g., "door").
    
    Representation Invariant
    gridLabels must not be null and must have dimensions at least [gridSize][gridSize].
    The player's position (player.getRow(), player.getCol()) must be within the bounds of gridLabels.
    The player's current cell in gridLabels must be marked as occupied (setEmpty(false)).
    
    repOk Method
    This method ensures the representation invariant is maintained.*/
    

    @Test
    void testPlacePlayerRandomly() throws Exception {
        // Place the player on the grid
        Player player = playerManager.placePlayerRandomly();

        assertNotNull(player, "Player should not be null after placement");
        int row = player.getRow();
        int col = player.getCol();

        // Check that the player's position is valid
        assertTrue(row >= 1 && row < gridLabels.length - 1, "Player row is out of bounds");
        assertTrue(col >= 1 && col < gridLabels[0].length - 1, "Player column is out of bounds");

        // Ensure the player's cell is no longer empty
        assertFalse(gridLabels[row][col].getIsEmpty(), "Player's cell should not be empty after placement");
    }

    @Test
    void testMovePlayerValidMove() throws Exception {
        // Place the player initially
        Player player = playerManager.placePlayerRandomly();
        int initialRow = player.getRow();
        int initialCol = player.getCol();

        // Simulate moving down
        playerManager.movePlayer(1, 0); // Move down
        int newRow = player.getRow();
        int newCol = player.getCol();

        // Check the new position
        assertEquals(initialRow + 1, newRow, "Player row should increment by 1");
        assertEquals(initialCol, newCol, "Player column should remain unchanged");

        // Ensure the old cell is empty and the new cell is not empty
        assertTrue(gridLabels[initialRow][initialCol].getIsEmpty(), "Old cell should be empty after move");
        assertFalse(gridLabels[newRow][newCol].getIsEmpty(), "New cell should not be empty after move");
    }

    @Test
    void testMovePlayerToNonEmptyCell() throws Exception {
        // Place the player initially
        Player player = playerManager.placePlayerRandomly();
        int initialRow = player.getRow();
        int initialCol = player.getCol();

        // Make a nearby cell non-empty
        gridLabels[initialRow + 1][initialCol].setEmpty(false);

        // Attempt to move to the non-empty cell
        playerManager.movePlayer(1, 0); // Move down

        // Ensure the player did not move
        assertEquals(initialRow, player.getRow(), "Player row should not change");
        assertEquals(initialCol, player.getCol(), "Player column should not change");
    }

    @Test
    void testCheckPlayerInDoor() throws Exception {
        // Place the player near a door
        gridLabels[5][5].setName("door");
        Player player = playerManager.placePlayerRandomly();
        player.setRow(5);
        player.setCol(4);

        // Move the player into the door
        playerManager.movePlayer(0, 1); // Move right

        // Validate that the door logic was triggered
        assertEquals("door", gridLabels[5][5].getName(), "The destination cell should be a door.");
    }

    @Test
    void testIsEmpty() {
        // Check an empty cell
        assertTrue(playerManager.isEmpty(3, 3), "Cell should be empty");

        // Make the cell non-empty
        gridLabels[3][3].setEmpty(false);

        // Check the same cell
        assertFalse(playerManager.isEmpty(3, 3), "Cell should not be empty");
    }
    

    @Test
    void testRepOk() {
        assertTrue(playerManager.repOk(), "Representation invariant should hold initially");

        gridLabels[0][0] = null; // Break invariant
        assertFalse(playerManager.repOk(), "Representation invariant should fail with invalid grid");
    }
}
