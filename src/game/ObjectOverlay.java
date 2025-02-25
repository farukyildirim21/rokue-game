package game;

import java.awt.Dimension;
import java.awt.Image;
import java.awt.event.MouseListener;
import java.io.Serializable;
import java.util.Arrays;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.SwingUtilities;

public class ObjectOverlay implements Serializable {
	 private static final long serialVersionUID = 1L;
	
	CreateImageIcon imageCreator= new CreateImageIcon();
	
	
	 public void addObjectOverlay(Cell groundLabel, ImageIcon icon) throws Exception {
		 ImageIcon objectIcon;
		 if(icon.getIconHeight()<48) {
			 String objectPath= icon.getDescription();
			 objectIcon = imageCreator.getImageIcon(objectPath, 32,32);
      
			 
		 }
		 else {
			 String objectPath= icon.getDescription();
			 objectIcon = imageCreator.getImageIcon(objectPath, 32,48);
			 
		 }
		   
	      
	       
	        groundLabel.setLayout(null); // Allow precise positioning of components
	        
	     
	      
	        
	        Cell objectOverlay = new Cell();
	        objectOverlay.setIcon(objectIcon);
	    
	       ;
	        int xOffset = (groundLabel.getWidth() - 32) / 2;
	        int yOffset = (groundLabel.getHeight() - 32) / 2;
	        objectOverlay.setBounds( xOffset,yOffset, 32, 32); // Center the object
	       
	        groundLabel.add(objectOverlay); // Add object overlay
	        groundLabel.setEmpty(false);
	        groundLabel.revalidate();
	        groundLabel.repaint();


	    }
	 public void overlayLabel(Cell groundLabel, ImageIcon enchantmentIcon, int targetSize) throws Exception {
		   Cell enchantmentLabel = new Cell();
	       // monsterIcon = new ImageIcon(monsterIcon.getImage().getScaledInstance(16, 16, Image.SCALE_SMOOTH)); // Resize monster icon
	       ImageIcon icon= imageCreator.getImageIcon(enchantmentIcon.getDescription(), targetSize, targetSize);
	       enchantmentLabel.setIcon(icon);

	        // Set bounds to center the monster on the ground label

	        int xOffset = (groundLabel.getWidth() - targetSize) / 2;
	        int yOffset = (groundLabel.getHeight() - targetSize) / 2;
	        enchantmentLabel.setBounds( xOffset,yOffset, targetSize, targetSize); //
	        // Add the monster label to the ground label
	        groundLabel.setLayout(null); // Allow precise positioning
	        groundLabel.add(enchantmentLabel); // Overlay the monster
	        groundLabel.setEmpty(false); // Update the label's state to indicate it's occupied by a monster
	       
	        
	        groundLabel.revalidate();
	        groundLabel.repaint();
	    }
	    
	 



	    public void revertToGround(Cell label) {
	    	label.removeAll(); 
	        ImageIcon groundIcon = (ImageIcon) label.getIcon(); // Get the current icon
	        if (groundIcon != null) {
	            label.setIcon(groundIcon); // Revert to ground icon
	            
	            label.setEmpty(true);
	            label.removeAll(); // Remove any overlays
	            
	            label.revalidate();
	            label.repaint();
	        }
	    }
	    

}
