package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.swing.Timer;

import static org.junit.jupiter.api.Assertions.*;

import game.Cell;
import game.GameManager;
import frames.GameFrame;

import java.awt.Color;
import java.util.ArrayList;

/**
 * Test suite for GameManager's pauseGame and resumeGame methods.
 * Focuses on verifying the correct pausing and resuming of hallTimer.
 */
public class GameManagerPauseResumeTest {
    private GameManager gameManager;
    private Timer hallTimer;
    private ArrayList<Cell[][]> completedHalls;

    @BeforeEach
    public void setUp() throws Exception {
        // Initialize a 5x5 grid
        int gridSize = 5;
        Cell[][] grid = new Cell[gridSize][gridSize];
        for (int i = 0; i < gridSize; i++) {
            for (int j = 0; j < gridSize; j++) {
                grid[i][j] = new Cell();
                grid[i][j].setEmpty(true);
            }
        }

        // Add grid to completed halls
        completedHalls = new ArrayList<>();
        completedHalls.add(grid);

        // Initialize GameManager
        gameManager = new GameManager(completedHalls);

        // Initialize gameFrame
        gameManager.gameFrame = new GameFrame("Play Mode", 1200, 800, new Color(50, 34, 40));

        // Initialize and register hallTimer
        hallTimer = new Timer(1000, e -> {});
        gameManager.hallTimer = hallTimer;
        gameManager.addTimer(hallTimer); // Register hallTimer with GameManager
    }

    /**
     * Method Specification for pauseGame and resumeGame:
     * 
     * @requires
     * - hallTimer is initialized and registered with GameManager.
     * - GameManager's `isPaused` state is accurately tracked.
     * 
     * @modifies
     * - Stops the `hallTimer` when `pauseGame` is called.
     * - Restarts the `hallTimer` when `resumeGame` is called.
     * - Updates GameManager's `isPaused` state.
     * 
     * @effects
     * - Stops `hallTimer` when the game is paused.
     * - Resumes `hallTimer` when the game is resumed.
     */

    @Test
    public void testPauseGamePausesHallTimer() {
        // Start the hall timer
        hallTimer.start();

        // Pause the game
        gameManager.pauseGame();

        // Verify hall timer is stopped
        assertFalse(hallTimer.isRunning(), "Hall timer should be paused.");
        assertTrue(gameManager.isPaused(), "GameManager's isPaused flag should be true.");
    }

    @Test
    public void testResumeGameResumesHallTimer() {
        // Start the hall timer
        hallTimer.start();

        // Pause and then resume the game
        gameManager.pauseGame();
        gameManager.resumeGame();

        // Verify hall timer is running
        assertTrue(hallTimer.isRunning(), "Hall timer should be running after resuming.");
        assertFalse(gameManager.isPaused(), "GameManager's isPaused flag should be false.");
    }

    @Test
    public void testPauseGameDoesNotPauseAlreadyPausedTimer() {
        // Pause the game
        gameManager.pauseGame();

        // Verify hall timer is stopped
        assertFalse(hallTimer.isRunning(), "Hall timer should be paused.");

        // Pause the game again
        gameManager.pauseGame();

        // Verify no change in state
        assertFalse(hallTimer.isRunning(), "Hall timer should remain paused.");
        assertTrue(gameManager.isPaused(), "GameManager's isPaused flag should remain true.");
    }

    @Test
    public void testResumeGameDoesNotResumeAlreadyRunningTimer() {
        // Start the hall timer
        hallTimer.start();

        // Resume without pausing
        gameManager.resumeGame();

        // Verify hall timer remains running
        assertTrue(hallTimer.isRunning(), "Hall timer should remain running.");
        assertFalse(gameManager.isPaused(), "GameManager's isPaused flag should remain false.");
    }
}

