package frames;

import javax.swing.*;
import java.awt.*;

public class GameFrame extends JFrame {
    
    public GameFrame(String title, int width, int height, Color backgroundColor) {
        super(title); // Set the frame title using the parent JFrame constructor
        setSize(width, height);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(null); // Set the layout manager
        getContentPane().setBackground(backgroundColor); // Set the background color
    }

    public void addComponent(JComponent component) {
        add(component); // Add the component to the frame
    }

    public void makeVisible() {
        setFocusable(true);
        setVisible(true);
        requestFocus();
    }
}
