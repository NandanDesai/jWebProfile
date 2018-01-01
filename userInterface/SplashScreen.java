/*
 * Year: 2017
 */
package userInterface;

import java.awt.Container;
import java.net.URL;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JWindow;

/**
 *
 * @author nandan
 */
public class SplashScreen extends JWindow {

    public SplashScreen() {
        Container container = getContentPane();
        container.setLayout(null);
        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 638, 360);
        container.add(panel);
        JLabel label = new JLabel();
        URL iconURL = getClass().getResource("Resources/Splash640x360.png");
        ImageIcon icon = new ImageIcon(iconURL);
        label.setIcon(icon);
        panel.add(label);
        setSize(640, 360);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    public SplashScreen(String path) {
        Container container = getContentPane();
        container.setLayout(null);
        JPanel panel = new JPanel();
        panel.setBounds(0, 0, 550, 330);
        container.add(panel);
        JLabel label = new JLabel();
        URL iconURL = getClass().getResource(path);
        ImageIcon icon = new ImageIcon(iconURL);
        label.setIcon(icon);
        panel.add(label);
        setSize(550, 330);
        setLocationRelativeTo(null);
        setVisible(true);
    }
    /*
    public static void main(String[] args){
        SplashScreen execute=new SplashScreen();
    }
     */
}
