package UserInterfaces.GuessUI;

import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class Canvas extends JPanel {
    private int[] x, y;
    private int points = 0;

    Canvas() {
        x = new int[10000];
        y = new int[10000];
        addMouseMotionListener(new MList());
    }

    /*
     * Paints a circle at the location of the mouse
     */
    public void paint(Graphics g) {
        super.paint(g);
        Graphics2D g2 = (Graphics2D) g;
        g2.setStroke(new BasicStroke(37, BasicStroke.CAP_ROUND, BasicStroke.JOIN_ROUND));
        g2.setPaint(Color.white);
        g.drawPolyline(x, y, points);
    }

    /*
     * Action Listener for the mouse
     */
    class MList extends MouseMotionAdapter {
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