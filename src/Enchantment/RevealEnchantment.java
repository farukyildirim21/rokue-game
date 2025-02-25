package enchantment;

import java.awt.Color;
import java.io.Serializable;
import java.util.LinkedHashMap;


import javax.swing.BorderFactory;
import javax.swing.Timer;

import game.Cell;
import game.GameManager;

public class RevealEnchantment extends Enchantment implements Serializable{
	private static final long serialVersionUID = 1L;
    Cell [][] gridLabels; 
    Cell cell;
    int runeRow,  runeCol;
    GameManager gameManager;
    int gridSize= 12;
    Timer timer;
    int startRow,endRow,startCol,endCol;
 	public RevealEnchantment(String type, GameManager gameManager, int runeRow, int runeCol ) {
		super(type);
		this.gridLabels= gameManager.getGridLabels();
		this.gameManager= gameManager;
		this.runeRow= runeRow;
		this.runeCol= runeCol;
				
	}

	@Override
	public void useEnchantment() {
		 startRow = Math.max(0, runeRow - 1);
	     endRow = Math.min(gridSize - 1, runeRow + 2);
	     startCol = Math.max(0, runeCol - 1);
	     endCol = Math.min(gridSize - 1, runeCol + 2);

	    // Highlight the 4x4 rectangle,
	     System.out.println("in reveal highghted");
	    for (int i = startRow; i <= endRow; i++) {
	        for (int j = startCol; j <= endCol; j++) {
	        	System.out.println("we create line border");
	            gridLabels[i][j].setBorder(BorderFactory.createLineBorder(Color.RED, 10));
	        }
	    }

	    scheduleHighlightRemoval(startRow, endRow, startCol, endCol);
	   
	}
	private void scheduleHighlightRemoval(int startRow, int endRow, int startCol, int endCol) {
		timer = new Timer(10000, e -> removeHighlight(startRow, endRow,startCol,endCol));
		timer.setRepeats(false); // Ensure the timer runs only once
	    timer.start(); 
	   
	}

	private void removeHighlight(int startRow, int endRow, int startCol, int endCol) {
	    for (int i = startRow; i <= endRow; i++) {
	        for (int j = startCol; j <= endCol; j++) {
	            gridLabels[i][j].setBorder(null); // Reset borders
	        }
	    }
	    System.out.println("Highlight removed.");
	}

	
	

	

}
