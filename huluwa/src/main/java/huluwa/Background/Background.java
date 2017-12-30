package huluwa.Background;

import huluwa.Thing2D;

import java.awt.Image;
import java.net.URL;
import javax.swing.ImageIcon;

public class Background extends Thing2D {

    public Background() {

        URL loc = this.getClass().getClassLoader().getResource("bg.png");
        ImageIcon iia = new ImageIcon(loc);
        Image image = iia.getImage();
        this.setImage(image);
    }
}