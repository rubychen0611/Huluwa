package huluwa.Creature;
//import huluwa.Sorter.Comparable;
import huluwa.Space.Space;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class Huluwa extends Creature //implements Comparable
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
        this.huluAttribute = huluAttribute;
        URL loc = this.getClass().getClassLoader().getResource("huluwa" + index +".png");
        ImageIcon iia = new ImageIcon(loc);
        Image image = iia.getImage().getScaledInstance(100,100,Image.SCALE_DEFAULT);
        this.setImage(image);
    }

   /* @Override
    public boolean biggerThan(Comparable brother)
    {
        if (brother instanceof Huluwa)
        {
            return (this.index > ((Huluwa) brother).getIndex());
        }
        else return false;
    }*/
}