package huluwa.Creature;
import huluwa.Space.Space;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class Scorpion extends Creature //蝎子精类
{
    public Scorpion(int x, int y, Space space)
    {
        super(x, y, space);
        this.species = Species.SCORPION;
        this.rank = 7;
        this.group = Group.EVIL;
        URL loc = this.getClass().getClassLoader().getResource("scorpion.png");
        ImageIcon iia = new ImageIcon(loc);
        Image image = iia.getImage().getScaledInstance(100,100,Image.SCALE_DEFAULT);

        livingImage = image;
        loc = this.getClass().getClassLoader().getResource("scorpion_dead.png");
        iia = new ImageIcon(loc);
        image = iia.getImage().getScaledInstance(100,100,Image.SCALE_DEFAULT);
        deadImage = image;
        sendImages();
    }

}
