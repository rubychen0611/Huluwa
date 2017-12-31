package huluwa.Creature;
import huluwa.Space.Space;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class Huluwa extends Creature
{
    private HuluAttribute huluAttribute;
    private int index;

    public HuluAttribute getAttribute()
    {
        return this.huluAttribute;
    }
    public int getIndex()
    {
        return this.index;
    }
    public Huluwa(int x, int y, Space space, int index, HuluAttribute huluAttribute)
    {
        super(x, y, space);
        this.species = Species.HULUWA;
        this.index = index;
        this.rank = 9 - index;
        this.group = Group.GOOD;
        this.huluAttribute = huluAttribute;
        URL loc = this.getClass().getClassLoader().getResource("huluwa" + index +".png");
        ImageIcon iia = new ImageIcon(loc);
        Image image = iia.getImage().getScaledInstance(100,100,Image.SCALE_DEFAULT);
        this.setImage(image);
        livingImage = image;

        loc = this.getClass().getClassLoader().getResource("huluwa" + index +"_dead.png");
        iia = new ImageIcon(loc);
        image = iia.getImage().getScaledInstance(100,100,Image.SCALE_DEFAULT);
        deadImage = image;
        sendImages();
    }

}