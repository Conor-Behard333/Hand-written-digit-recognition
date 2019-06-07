import UserInterfaces.Other.InfoUI;

import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;

public class Run {
    public static void main(String[] args) {
        InfoUI infoUI = new InfoUI();
        infoUI.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
            }

            @Override
            public void windowClosing(WindowEvent e) {
            }

            public void windowClosed(WindowEvent e) {
                try {
                    new Start().run();
                } catch (Exception ex) {
                    System.out.println(ex);
                }
            }

            @Override
            public void windowIconified(WindowEvent e) {
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
            }

            @Override
            public void windowActivated(WindowEvent e) {
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
            }
        });
    }
}
