package huluwa.Background;

import huluwa.Thing2D;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class MonstersWin extends Thing2D
{
    public MonstersWin() {

        URL loc = this.getClass().getClassLoader().getResource("yj_win.png");
        ImageIcon iia = new ImageIcon(loc);
        Image image = iia.getImage();
        this.setImage(image);
    }
}