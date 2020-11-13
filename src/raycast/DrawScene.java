package raycast;

import java.awt.*;
import javax.swing.*;

@SuppressWarnings("serial")
public class DrawScene extends JPanel {
    private Point playerLocation = new Point();

    public DrawScene(int x, int y) {
        this.playerLocation.setLocation(x, y);
    }

    public void setLocation(int x, int y) {
        playerLocation.setLocation(x, y);
    }

    public void renderFrame() {
        repaint();
    }
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        this.setBackground(Color.BLACK);
        // Graphics2D g2 = (Graphics2D) g;
        // g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g.setColor(Color.WHITE);
        g.fillRect((int)playerLocation.getX(), (int)playerLocation.getY(), 10, 10);
    }

}
