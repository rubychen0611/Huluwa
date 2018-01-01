package huluwa.Background;

import huluwa.Thing2D;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class MonstersWinReplay extends Thing2D
{
    public MonstersWinReplay()
    {

        URL loc = this.getClass().getClassLoader().getResource("yj_win_replay.png");
        ImageIcon iia = new ImageIcon(loc);
        Image image = iia.getImage();
        this.setImage(image);
    }
}
