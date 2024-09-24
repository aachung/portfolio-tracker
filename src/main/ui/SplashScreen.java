package ui;

import javax.swing.*;
import java.awt.*;

// Represents the splash screen when user opens app
// source: https://www.c-sharpcorner.com/article/developing-a-splash-screen-using-java/
public class SplashScreen extends JWindow {
    private Image image;

    // EFFECTS: constructs a splash screen
    public SplashScreen() {
        String sep = System.getProperty("file.separator");
        image = Toolkit.getDefaultToolkit().getImage(System.getProperty("user.dir") + sep
                + "data" + sep + "splash.png");
        ImageIcon icon = new ImageIcon(image);
        setSize(icon.getIconWidth(), icon.getIconHeight());
        Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (screenSize.width - getSize().width) / 2;
        int y = (screenSize.height - getSize().height) / 2;
        setLocation(x,y);
        setVisible(true);
    }

    // MODIFIES: this
    // EFFECTS: paints image onto window
    public void paint(Graphics g) {
        super.paint(g);
        g.drawImage(image, 0, 0, this);
    }
}
