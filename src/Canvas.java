import javax.swing.*;
import java.awt.*;
import java.awt.event.MouseEvent;
import java.awt.event.MouseMotionAdapter;

public class Canvas extends JPanel {

    Canvas() {
        addMouseMotionListener(new MList());
    }

    public void clear() {
        setBackground(Color.WHITE);
        repaint();
    }

    class MList extends MouseMotionAdapter {
        public void mouseDragged(MouseEvent e) {
            Graphics g = getGraphics();
            g.setColor(Color.black);
            g.fillOval(e.getX(), e.getY(), 20, 20);
        }
    }
}