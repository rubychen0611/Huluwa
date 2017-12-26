package huluwa.Creature;
import huluwa.Space.Space;

import javax.swing.*;
import java.awt.*;
import java.net.URL;

public class Snake extends Creature //生物：蛇精
{
    public Snake(int x, int y, Space space)
    {
        super(x, y, space);
        this.species = Species.SNAKE;

        URL loc = this.getClass().getClassLoader().getResource("snake.png");
        ImageIcon iia = new ImageIcon(loc);
        Image image = iia.getImage().getScaledInstance(100,100,Image.SCALE_DEFAULT);
        this.setImage(image);
    }
   /* public void report()
    {
        System.out.print("🐍");
    }*/
}