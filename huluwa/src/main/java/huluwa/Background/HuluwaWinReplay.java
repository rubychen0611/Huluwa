package huluwa.Background;

import huluwa.Thing2D;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class HuluwaWinReplay extends Thing2D
{
    public HuluwaWinReplay() {

        URL loc = this.getClass().getClassLoader().getResource("hlw_win_replay.png");
        ImageIcon iia = new ImageIcon(loc);
        Image image = iia.getImage();
        this.setImage(image);
    }
}

