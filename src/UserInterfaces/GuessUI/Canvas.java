package UserInterfaces.GuessUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class Canvas extends JPanel {
    private int[] x, y;
    private int points = 0;

    Canvas() {
        x = new int[1920];
        y = new int[1080];
        addMouseMotionListener(new MouseMA());
    }

    /*
     * Paints a circle at the location of the mouse
     */
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setRenderingHint(RenderingHints.KEY_ANTIALIASING, RenderingHints.VALUE_ANTIALIAS_ON);
        g2.setStroke(new BasicStroke(34, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setPaint(Color.white);
        g.drawPolyline(x, y, points);
    }

    /*
     * Action Listener for the mouse
     */
    class MouseMA extends MouseMotionAdapter {
        /*
         * Updates the mouse's x and y position
         */
        public void mouseDragged(MouseEvent e) {
            x[points] = e.getX();
            y[points] = e.getY();
            points++;
            repaint();
        }
    }
}