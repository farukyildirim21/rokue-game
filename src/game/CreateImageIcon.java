package game;

import java.awt.image.BufferedImage;
import java.io.Serializable;

import javax.swing.ImageIcon;

public class CreateImageIcon implements Serializable {
	 private static final long serialVersionUID = 1L;
	private ImageResizer resizer = new ImageResizer();
	
	
	
	public ImageIcon getImageIcon (String path, int targetWidth,int targetHeight) throws Exception {
		
		BufferedImage image = resizer.convertImage(path,targetWidth, targetHeight);
        ImageIcon icon = new ImageIcon(image);
        return icon;
		
		
	}
	
	

}
