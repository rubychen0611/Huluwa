package huluwa.Replay;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Scene
{
    private ArrayList<DisplayElement> elements;  //此时所有显示的元素，注意尸体图片需要排在活着的生物图片之前
    public Scene()
    {
        elements = new ArrayList<DisplayElement>();
    }
    public void addDisplayElement(int creatureNo, boolean isAlive, int x, int y)
    {
        DisplayElement e = new DisplayElement(creatureNo,isAlive, x, y);
        elements.add(e);
    }
    public void write(DataOutputStream out) throws IOException
    {
        out.writeInt(elements.size());
        for (DisplayElement element : elements)
        {
            out.writeInt(element.getCreatureNo());
            out.writeBoolean(element.getIsAlive());
            out.writeInt(element.getX());
            out.writeInt(element.getY());
        }
    }
}
class DisplayElement
{
    int creatureNo;
    boolean isAlive;
    int x;
    int y;
    public DisplayElement(int creatureNo, boolean isAlive, int x, int y)
    {
        this.creatureNo = creatureNo;
        this.isAlive = isAlive;
        this.x = x;
        this.y = y;
    }

    public int getCreatureNo() {
        return creatureNo;
    }

    public int getX() {
        return x;
    }
    public int getY()
    {
        return y;
    }

    public boolean getIsAlive() {
        return isAlive;
    }
}
