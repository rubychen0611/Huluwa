package huluwa.Background;

import huluwa.Thing2D;
import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class StartBackground extends Thing2D
{

    public StartBackground()
    {
        URL loc = this.getClass().getClassLoader().getResource("start.png");
        ImageIcon iia = new ImageIcon(loc);
        Image image = iia.getImage();
        this.setImage(image);
    }
}