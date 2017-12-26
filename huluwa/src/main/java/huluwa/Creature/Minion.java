package huluwa.Creature;
import huluwa.Space.Space;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class Minion extends Creature //小喽啰类
{
    int index;
    public Minion(int x, int y, Space space, int index)
    {
        super(x, y, space);
        this.index = index;
        this.species = Species.MINION;

        URL loc = this.getClass().getClassLoader().getResource("minion.png");
        ImageIcon iia = new ImageIcon(loc);
        Image image = iia.getImage().getScaledInstance(100,100,Image.SCALE_DEFAULT);
        this.setImage(image);
    }
    /*public void report()
    {
        System.out.print("🐜");
    }*/
}
