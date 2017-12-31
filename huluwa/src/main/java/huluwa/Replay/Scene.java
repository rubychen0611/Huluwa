package huluwa.Replay;

import java.util.ArrayList;

public class Scene
{
    ArrayList<DisplayElement> elements;  //此时所有显示的元素，注意尸体图片需要排在活着的生物图片之前
}
class DisplayElement
{
    String imageName;
    int x;
    int y;
    public DisplayElement(String imageName, int x, int y)
    {
        this.imageName = imageName;
        this.x = x;
        this.y = y;
    }
}
