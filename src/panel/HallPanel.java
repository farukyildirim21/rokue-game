package panel;

import javax.swing.*;
import java.awt.*;

public class HallPanel extends JPanel {
    private JLabel hallTitleLabel;

    public HallPanel() {
        setLayout(null); // Use null layout for precise positioning
        setBounds(50, 50, 800, 600);
        setBorder(BorderFactory.createLineBorder(Color.black));
        setBackground(new Color(60, 40, 50));

        initializeTitleLabel();
    }

    private void initializeTitleLabel() {
        hallTitleLabel = new JLabel();
        hallTitleLabel.setFont(new Font("Arial", Font.BOLD, 20));
        hallTitleLabel.setForeground(Color.white);
        hallTitleLabel.setBounds(300, 10, 400, 30);
        add(hallTitleLabel); // Add the title label to this panel
    }

    public JLabel getHallTitleLabel() {
        return hallTitleLabel;
    }

    public void updateTitle(String title) {
        hallTitleLabel.setText(title);
    }
}
