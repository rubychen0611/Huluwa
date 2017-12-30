package huluwa.Background;

import huluwa.Thing2D;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class HuluwaWin extends Thing2D
{
    public HuluwaWin() {

        URL loc = this.getClass().getClassLoader().getResource("hlw_win.png");
        ImageIcon iia = new ImageIcon(loc);
        Image image = iia.getImage();
        this.setImage(image);
    }
}
