package game;

import java.awt.Color;
import java.awt.Dimension;
import java.util.ArrayList;

import javax.swing.BorderFactory;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingConstants;

public class InitialHallPanel {
	private int gridSize= 12;
	public Cell cell;
	private  String groundPath="src/assets/items/floor_mud_e.png";
	private String wallPath= "src/assets/items/wall_gargoyle_red_1.png";
	private CreateImageIcon iconCreator = new CreateImageIcon();
	private String floorName="floor";
 	private String wallName= "wall";
 	private   String closeDoorPath = "src/assets/items/door_closed.png";
 	Cell [][] hall  = new Cell[gridSize][gridSize];
  	public ArrayList<Cell[][]> hallList= new ArrayList();
 	

 	public void initializeHallPanel(int cellSize) throws Exception {
 	    ImageIcon floorIcon = iconCreator.getImageIcon(groundPath,cellSize, cellSize);
 	    ImageIcon wallIcon = iconCreator.getImageIcon(wallPath,cellSize,cellSize);
 	    ImageIcon doorIcon = iconCreator.getImageIcon(closeDoorPath,cellSize,cellSize);

 	  // Dimension cellSizee = new Dimension(40, 40);
 	    for (int i = 0; i < 4; i++) {
 	        Cell[][] hall = new Cell[gridSize][gridSize]; // Create a new hall array for each iteration

 	        for (int row = 0; row < gridSize; row++) {
 	            for (int col = 0; col < gridSize; col++) {
 	                cell = new Cell();
 	                cell.setOpaque(true);
 	                cell.setBackground(new Color(80, 60, 70));
 	                cell.setHorizontalAlignment(SwingConstants.CENTER);
 	                cell.setVerticalAlignment(SwingConstants.CENTER);
 	             
 	                cell.setCellXPosition(row);
 	                cell.setCellYPosition(col);
 	            
 	                cell.setCellRune("noRune");
 	                
 	                cell.setName(floorName);
 	                cell.setIcon(floorIcon);
 	                

 	                if (row == 0 || col == 0 || row == gridSize - 1 || col == gridSize - 1) {
 	                    cell.setIcon(wallIcon);
 	                    cell.setName(wallName);
 	                    cell.setEmpty(false);
 	                }
 	               if (row == gridSize - 1 && col == 3) {
 	                    
 	                    cell.setIcon(doorIcon);
 	                    cell.setName("door");
 	                    cell.setEmpty(false);
 	                }
 	             
 	                hall[row][col] = cell;
 	            }
 	        }

 	        hallList.add(hall); 
 	    }
 	}
 	public ArrayList<Cell[][]> getHallList() {
 		
		return hallList;
	}

}
