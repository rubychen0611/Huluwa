package huluwa.Replay;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;

import java.io.DataInputStream;
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

    public ArrayList<DisplayElement> getElements()
    {
        return elements;
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
    public void read(DataInputStream in) throws IOException
    {
        int elementsSize = in.readInt();
        DisplayElement element;
        int creatureNo, x, y;
        boolean isAlive;
        for(int i = 0; i < elementsSize; i++)
        {
            creatureNo = in.readInt();
            isAlive = in.readBoolean();
            x = in.readInt();
            y = in.readInt();
            elements.add( new DisplayElement(creatureNo, isAlive, x, y));
        }
    }
}
