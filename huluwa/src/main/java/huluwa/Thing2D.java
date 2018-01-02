package huluwa;

import javax.swing.*;
import java.awt.Image;
import java.net.URL;

public class Thing2D
{

    private Image image;

    public Thing2D(String imageName)
    {
        URL loc = this.getClass().getClassLoader().getResource(imageName);
        ImageIcon iia = new ImageIcon(loc);
        this.image = iia.getImage();
    }
    public Image getImage()
    {
        return this.image;
    }

   // public void setImage(Image img) {
   //     image = img;
    //}

} 