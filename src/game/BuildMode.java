package game;

import javax.swing.*;
import javax.swing.TransferHandler.TransferSupport;

import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

public class BuildMode {
    private JFrame frame;
    private JPanel  objectPanel, bottomPanel;

    private JLabel dragSourceCell; 
  
    private final int gridSize = 12;
    

    private String[] hallNames = {"Hall Of Earth", "Hall Of Air", "Hall Of Water", "Hall of Fire"};
    private int[] minRequirements = {3, 3, 3, 3};
    ArrayList<Cell[][]> hallList;

    
   
    private ArrayList<Cell[][]> completedHalls = new ArrayList<>(); // List to store all completed halls
 
    private int[] currentObjectCounts = new int[hallNames.length]; // Array to store object counts for each hall



   
    private InitialHallPanel initialHallPanel;
    
    private JPanel mainHallContainer;

    public ArrayList<Cell[][]> getCompletedHalls() { // getter for completed halls arraylist
		return completedHalls;
	}

	public BuildMode() {
		
		//the BuildMode class acts as a Façade for handling multiple subsystems like mainHallContainer, objectPanel, and bottomPanel.
        frame = new JFrame("Build Mode");
        frame.setSize(1200, 800);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setLayout(new BorderLayout());
        
        frame.getContentPane().setBackground(new Color(50, 34, 40));

       // Façade pattern is used.
      
        initialHallPanel = new InitialHallPanel();
        initializeHallPanels(); // Initialize the main container panel
        initializeObjectPanel(); // Add the object panel
        initializeBottomPanel(); // Add the bottom panel
       

        frame.add(mainHallContainer, BorderLayout.CENTER);  
        frame.pack(); // Forces the layout to recalculate
        
        frame.setVisible(true); // 

        try {
			//loadCurrentHall();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
       
       
    }

	private void initializeHallPanels() {
	    mainHallContainer = new JPanel(new GridLayout(2, 2, 20, 20));
	  
	    mainHallContainer.setBackground(new Color(50, 34, 40));
	  
	    try {
	        initialHallPanel.initializeHallPanel(40); // Initialize hallList
	    } catch (Exception e) {
	        e.printStackTrace();
	    }

	    hallList  = initialHallPanel.getHallList();
	    
	    if (hallList == null || hallList.size() < 4) {
	        System.err.println("Error: hallList is not properly initialized. Expected 4 halls.");
	        return;
	    }
	    Dimension cellSize = new Dimension(40, 40);

	    for (int i = 0; i < hallNames.length; i++) {
	       

	        JPanel hallPanel = new JPanel(new BorderLayout());
	        JPanel gridPanel = new JPanel(new GridLayout(12, 12));
	        gridPanel.setBorder(BorderFactory.createLineBorder(Color.black));
	        gridPanel.setBackground(new Color(60, 40, 50));

	        Cell[][] hallCells = hallList.get(i);

	        for (int row = 0; row < hallCells.length; row++) {
	            for (int col = 0; col < hallCells[row].length; col++) {
	                Cell cell = hallCells[row][col];
	                cell.setPreferredSize(cellSize);
	                addDragAndDropHandlers(cell);// Pass hallIndex to track objects
	                gridPanel.add(cell);// Composite: Adding child Cell to grid JPanel
	            }
	        }
	        JLabel hallTitleLabel = new JLabel(hallNames[i] + " " + currentObjectCounts[i] + "/" + minRequirements[i], SwingConstants.CENTER);
	        hallTitleLabel.setFont(new Font("Arial", Font.BOLD, 16));
	        hallTitleLabel.setForeground(Color.black);

	        hallPanel.add(hallTitleLabel, BorderLayout.NORTH);
	        hallPanel.add(gridPanel, BorderLayout.CENTER);

	        // Add title label to the hall panel as a property
	        hallPanel.putClientProperty("hallTitleLabel", hallTitleLabel);


	        mainHallContainer.add(hallPanel);  // Add gridPanel as a composite child composite pattern 

	       
	    }

	    mainHallContainer.revalidate();
	    mainHallContainer.repaint();
	}


    private void initializeObjectPanel() {
        // Custom JPanel with a background image that scales while maintaining aspect ratio
           objectPanel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                // Draw the background image with scaling while maintaining aspect ratio
                try {
                    ImageIcon backgroundIcon = new ImageIcon("src/assets/items/build_mode.png");
                    Image backgroundImage = backgroundIcon.getImage();

                    // Get original image dimensions
                    int imageWidth = backgroundImage.getWidth(this);
                    int imageHeight = backgroundImage.getHeight(this);

                    // Calculate the scaling factor to fit the panel dimensions while preserving aspect ratio
                    double scale = Math.min((double) getWidth() / imageWidth, (double) getHeight() / imageHeight);

                    // Calculate new dimensions
                    int scaledWidth = (int) (imageWidth * scale);
                    int scaledHeight = (int) (imageHeight * scale);

                    // Center the image within the panel
                    int x = (getWidth() - scaledWidth) / 2;
                    int y = (getHeight() - scaledHeight) / 2;

                    // Draw the scaled image
                    g.drawImage(backgroundImage, x, y, scaledWidth, scaledHeight, this);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        };
        objectPanel.setBackground(new Color(0, 0, 0, 0));
        objectPanel.setPreferredSize(new Dimension(250, 600)); // 
        objectPanel.setLayout(null); // Absolute positioning for precise alignment

        // Add the Exit button at the top center
        JButton exitButton = new JButton();
        exitButton.setIcon(new ImageIcon("src/assets/items/exit16.png")); // Exit button image path
        exitButton.setBounds((250 - 32) / 2, 10, 32, 32); // Centered horizontally in a 250px wide panel
        exitButton.setContentAreaFilled(false); // Remove background
        exitButton.setOpaque(false);           // Ensure transparency
        exitButton.setBorderPainted(false);    // Remove border
        exitButton.setFocusPainted(false);     // Disable focus outline
        exitButton.setBackground(new Color(0, 0, 0, 0)); // Fully transparent background
        exitButton.addActionListener(e -> System.exit(0)); // Exit on click
        
        objectPanel.add(exitButton);

        // Add draggable objects
        addDraggableObject("src/assets/items/box.png", 110, 130);
        addDraggableObject("src/assets/items/boxes_stacked.png", 110, 170);
        addDraggableObject("src/assets/items/chest_closed.png", 110, 210);
        addDraggableObject("src/assets/items/column_wall.png", 110, 250);
        addDraggableObject("src/assets/items/floor_ladder.png", 110, 290);
        addDraggableObject("src/assets/items/skull.png", 110, 330);
        addDraggableObject("src/assets/items/torch_no_flame.png", 110, 370);
        addDraggableObject("src/assets/items/flask_big_blue.png", 110, 410);
        addDraggableObject("src/assets/items/flask_big_green.png", 110, 450);

        frame.add(objectPanel, BorderLayout.EAST);
    }

    private void initializeBottomPanel() {
        bottomPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
        bottomPanel.setPreferredSize(new Dimension(1200, 50));
        bottomPanel.setBackground(new Color(50, 34, 40));

        JButton playButton = new JButton("Play");
        playButton.setFont(new Font("Arial", Font.BOLD, 16));
        playButton.setBackground(new Color(50, 30, 60));
        playButton.setForeground(Color.black);
        playButton.setFocusPainted(false);
        
        playButton.addActionListener(e -> {
			try {
				checkHallCompletion();
			} catch (Exception e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			}
		});

        bottomPanel.add(playButton);

        frame.add(bottomPanel, BorderLayout.SOUTH);
    }

    private void goToPlayMode() {
    	
    	 SwingUtilities.invokeLater(() -> {
    		 frame.dispose();
    		 
             GameManager game = new GameManager(getCompletedHalls()); // Transition to GameManager
             try {
				game.startGame();
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
            
         });
    }

   
    private void addDragAndDropHandlers(Cell cell) {
    	// The drag-and-drop handling (addDragAndDropHandlers) can be extended to use different drag-and-drop strategies.
        cell.setTransferHandler(new TransferHandler("icon") {

            @Override
            public boolean canImport(TransferSupport support) {
                Component component = support.getComponent();
                while (component != null && !(component instanceof Cell)) {
                    component = component.getParent();
                }

                if (component == null || !(component instanceof Cell)) {
                   
                    return false;
                }

                Cell targetCell = (Cell) component;
                boolean isIconFlavor = support.isDataFlavorSupported(TransferableIcon.ICON_FLAVOR);
                boolean isEmpty = targetCell.getIsEmpty();

               
                if (dragSourceCell != null && dragSourceCell.getParent() != objectPanel) {
                    return false; // Block dragging within the hall
                }

                return isIconFlavor && isEmpty;
            }

            @Override
            public boolean importData(TransferSupport support) {
               

                Component component = support.getComponent();
                while (component != null && !(component instanceof Cell)) {
                    component = component.getParent();
                }

                if (component == null || !(component instanceof Cell)) {
                    
                    return false;
                }

                Cell targetCell = (Cell) component;

                if (!canImport(support)) {
                    
                    return false;
                }

                try {
                    Transferable transferable = support.getTransferable();
                    ImageIcon icon = (ImageIcon) transferable.getTransferData(TransferableIcon.ICON_FLAVOR);

                    if (icon != null) {
                        // Check if the drag source is the objectPanel
                        if (dragSourceCell != null && dragSourceCell.getParent() == objectPanel) {
                            // Increment object count for the corresponding hall
                            updateObjectCountForHall(targetCell,1);
                        }
                    }

                    if (icon != null && targetCell != null) {
                      
                        addObjectOverlay(targetCell, icon);

                        if (icon != null) {
                            addObjectOverlay(targetCell, icon);

                            // Mark the target cell as occupied
                            targetCell.setEmpty(false);
                            targetCell.revalidate();
                            targetCell.repaint();

                            // Reset dragSourceCell
                            dragSourceCell = null;
                            return true;
                        }
                        
                        
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return false;
            }

            @Override
            protected Transferable createTransferable(JComponent c) {
            	// Disable drag from cells in the hall
                if (c instanceof Cell) {
                    Cell sourceCell = (Cell) c;

                    // Only allow dragging if the source is from the object panel
                    if (dragSourceCell != null && dragSourceCell.getParent() == objectPanel) {
                        Icon icon = sourceCell.getIcon();
                        if (icon instanceof ImageIcon) {
                            return new TransferableIcon((ImageIcon) icon);
                        }
                    }
                }
                return null;
            };

            @Override
            public int getSourceActions(JComponent c) {
                return MOVE; // Allow MOVE action
            }
        });

        cell.addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                JComponent comp = (JComponent) e.getSource();
                TransferHandler handler = comp.getTransferHandler();
                if (handler != null) {
                    dragSourceCell = cell; // Set the source cell for drag operation
                    handler.exportAsDrag(comp, e, TransferHandler.MOVE);
                }
            }
            

            @Override
            public void mouseClicked(MouseEvent e) {
            	System.out.println("it is clicked");
                if (e.getClickCount() == 2 && !cell.getIsEmpty()) {
                    // Double-click to remove the object
                    cell.removeAll();
                    cell.setEmpty(true); // Mark the cell as empty
                    updateObjectCountForHall(cell, -1);
                    cell.revalidate();
                    cell.repaint();
                    
                    System.out.println("Object removed from cell.");
                }
            }
        });
    }

    
    
    
    private void checkHallCompletion() throws Exception {
        StringBuilder errorMessage = new StringBuilder();
        boolean allHallsCompleted = true;

        for (int i = 0; i < hallNames.length; i++) {
            if (currentObjectCounts[i] < minRequirements[i]) {
                allHallsCompleted = false;
                errorMessage.append(hallNames[i])
                            .append(" requires at least ")
                            .append(minRequirements[i])
                            .append(" objects, but currently has ")
                            .append(currentObjectCounts[i])
                            .append(".\n");
            }
        }

        if (allHallsCompleted) {
        	
        	saveCurrentHall(); 
        	//JOptionPane.showMessageDialog(frame, "All halls are completed! Click Play to start!", "Congratulations", JOptionPane.INFORMATION_MESSAGE);
        	removeDragAndDropHandlers(); 
        	goToPlayMode();

         
               
            
        } else {
            JOptionPane.showMessageDialog(frame, errorMessage.toString(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }


    private void saveCurrentHall() {
        ArrayList<Cell[][]> hallCellsList = new ArrayList<>();

        for (int i = 0; i < mainHallContainer.getComponentCount(); i++) {
            Component hallComponent = mainHallContainer.getComponent(i);

            if (hallComponent instanceof JPanel) {
                JPanel hallPanel = (JPanel) hallComponent;

                // Get the grid panel of the hall
                JPanel gridPanel = (JPanel) hallPanel.getComponent(1); // Assumes gridPanel is the second child
                Cell[][] savedHall = new Cell[gridSize][gridSize];

                int row = 0, col = 0;

                for (Component comp : gridPanel.getComponents()) {
                    if (comp instanceof Cell) {
                        Cell cell = (Cell) comp;
                        
                        // Safeguard to ensure row and column are within bounds
                        if (row < gridSize && col < gridSize) {
                            // Check if the cell has an overlay object
                            ImageIcon overlayIcon = getOverlayIcon(cell);
                            
                            if (overlayIcon != null) {
                            	
                                cell.setName("object");
                                cell.setIcon(overlayIcon);
                                
                                
                                savedHall[row][col] = cell;
                            } else {
                                // Clone the cell without an overlay
                                Cell clonedCell = new Cell();
                                
                                clonedCell.setIcon(cell.getIcon());
                                clonedCell.setName(cell.getName());
                               
                                savedHall[row][col] = cell;
                            }
                        } else {
                            System.err.println("Error: Exceeded grid bounds. Check the gridPanel components.");
                            return;
                        }

                        // Update column and row counters
                        col++;
                        if (col >= gridSize) {
                            col = 0;
                            row++;
                        }
                    }
                }

                // Ensure the grid was fully populated
                if (row != gridSize || col != 0) {
                    System.err.println("Error: Grid not fully populated. Check the gridPanel components.");
                    return;
                }
                

                hallCellsList.add(savedHall);
            }
        }

        completedHalls = hallCellsList;

       
    }

    

    private void addDraggableObject(String imagePath, int x, int y) {
        try {
            ImageIcon objectIcon = new ImageIcon(imagePath);

            JLabel objectLabel = new JLabel();
            objectLabel.setIcon(objectIcon);
            objectLabel.setBounds(x, y, 32, 32);
          //  objectLabel.putClientProperty("isFromObjectPanel", true);
            objectLabel.setTransferHandler(new TransferHandler("icon") {
                @Override
                protected Transferable createTransferable(JComponent c) {
                	
                    return new TransferableIcon((ImageIcon) ((JLabel) c).getIcon());
                }

                @Override
                public int getSourceActions(JComponent c) {
                    return COPY;
                }
            });

            objectLabel.addMouseListener(new MouseAdapter() {
                @Override
                public void mousePressed(MouseEvent e) {
                	
                    JComponent comp = (JComponent) e.getSource();
                    TransferHandler handler = comp.getTransferHandler();
                    if (handler != null) {
                        dragSourceCell = (JLabel) comp; // Save the source cell
                        handler.exportAsDrag(comp, e, TransferHandler.COPY);
                    }                }
            });

            objectPanel.add(objectLabel);
            objectPanel.revalidate();
            objectPanel.repaint();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
    private void addObjectOverlay(Cell groundLabel, ImageIcon objectIcon) {

        groundLabel.setLayout(null);

        // Create the overlay label for the object
        JLabel objectOverlay = new JLabel();
        objectOverlay.setIcon(objectIcon);
        objectOverlay.setBounds(10, 0, groundLabel.getWidth() , groundLabel.getHeight() );
        groundLabel.add(objectOverlay);
        
        groundLabel.setEmpty(false); // Mark the cell as occupied
       
        groundLabel.setName("object");
       
        groundLabel.revalidate();
       
        groundLabel.repaint();
        
     
    }
    // Observer Pattern
    private void updateObjectCountForHall(Cell targetCell,int  countDelta) {
        for (int i = 0; i < hallNames.length; i++) {
            JPanel hallPanel = (JPanel) mainHallContainer.getComponent(i);
            JPanel gridPanel = (JPanel) hallPanel.getComponent(1);

            // Check if the targetCell belongs to this hall
            for (Component comp : gridPanel.getComponents()) {
                if (comp == targetCell) {
                	currentObjectCounts[i] += countDelta;
                    updateHallTitle(i);
                    return;
                }
            }
        }
    }
    private ImageIcon getOverlayIcon(JLabel cell) {
        for (Component child : cell.getComponents()) {
            if (child instanceof JLabel) {
                JLabel overlayLabel = (JLabel) child;
                ImageIcon overlayIcon = (ImageIcon) overlayLabel.getIcon();
                if (overlayIcon != null) {
                	
                	return overlayIcon;
                }
                }
        }
        return null; // No overlay icon present
    }



    private void updateHallTitle(int hallIndex) {
        JPanel hallPanel = (JPanel) mainHallContainer.getComponent(hallIndex);
        JLabel hallTitleLabel = (JLabel) hallPanel.getClientProperty("hallTitleLabel");

        if (hallTitleLabel != null) {
            hallTitleLabel.setText(hallNames[hallIndex] + " " + currentObjectCounts[hallIndex] + "/" + minRequirements[hallIndex]);
        }
    }
    
    private void removeDragAndDropHandlers() {
        for (int i = 0; i < mainHallContainer.getComponentCount(); i++) {
            Component hallComponent = mainHallContainer.getComponent(i);

            if (hallComponent instanceof JPanel) {
                JPanel hallPanel = (JPanel) hallComponent;

                // Get the grid panel of the hall
                JPanel gridPanel = (JPanel) hallPanel.getComponent(1); // Assumes gridPanel is the second child

                for (Component comp : gridPanel.getComponents()) {
                    if (comp instanceof Cell) {
                        Cell cell = (Cell) comp;

                        // Remove the TransferHandler
                        cell.setTransferHandler(null);

                        // Remove all MouseListeners
                        for (MouseListener mouseListener : cell.getMouseListeners()) {
                            cell.removeMouseListener(mouseListener);
                        }
                        //cell.removeAll();
                        cell.revalidate();
                        cell.repaint();
                    }
                }
            }
        }

        System.out.println("Removed drag-and-drop functionalities from all cells.");
    }


 

}
    