package game;

import javax.swing.ImageIcon;
import javax.swing.JLabel;

public class Cell  extends JLabel{
	
	public String cellRune= "notRune";
	public int cellXPosition=0;
	public int cellYPosition=0;
	public boolean isEmpty=true;
	
	
	public boolean getIsEmpty() {
		return isEmpty;
	}




	public void setEmpty(boolean isEmpty) {
		this.isEmpty = isEmpty;
	}




	public int getCellXPosition() {
		return cellXPosition;
	}




	public void setCellXPosition(int xPosition) {
		cellXPosition = xPosition;
	}




	public int getCellYPosition() {
		return cellYPosition;
	}




	public void setCellYPosition(int yPosition) {
		cellYPosition = yPosition;
	}




	public String getCellRune() {
		return cellRune;
	}
	public void setCellRune(String rune) {
		this.cellRune = rune;
	}
	
	
	
	
	
	
	
	

}
