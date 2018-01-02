package huluwa.Creature;
import huluwa.Space.Space;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class Oldman extends Creature //生物：老爷爷
{
    public Oldman(int x, int y, Space space)
    {
        super(x, y, space);
        this.species = Species.OLDMAN;
        this.rank = 2;
        this.group = Group.GOOD;
        URL loc = this.getClass().getClassLoader().getResource("oldman.png");
        ImageIcon iia = new ImageIcon(loc);
        Image image = iia.getImage().getScaledInstance(100,100,Image.SCALE_DEFAULT);
        livingImage = image;
        loc = this.getClass().getClassLoader().getResource("oldman_dead.png");
        iia = new ImageIcon(loc);
        image = iia.getImage().getScaledInstance(100,100,Image.SCALE_DEFAULT);
        deadImage = image;
        sendImages();
    }

}

