package UserInterfaces.Other;

import javax.swing.*;
import java.awt.*;

public class LoadingUI extends JFrame {
    public LoadingUI() {
        setTitle("Loading - Please Wait");
        setSize(300, 50);
        setBackground(Color.black);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setResizable(false);
        setVisible(true);
        addLoadingBar();
    }

    private void addLoadingBar() {
        JPanel panel = new JPanel();
        panel.setSize(getSize());

        JProgressBar load = new JProgressBar();
        load.setSize(getSize());
        load.setLocation(0, 0);
        load.setIndeterminate(true);

        getContentPane().add(load);
        getContentPane().add(panel);
    }
}
