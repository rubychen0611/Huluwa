package huluwa.Space;
import huluwa.Creature.Creature;

import java.awt.*;
import java.util.ArrayList;

public class Position //队列位置类
{
    private int x, y;
    private Creature holder; //位置的占有者
    private boolean empty; //位置是否为空
    public ArrayList<Image> corpseImages; //尸体图片
    public Position(int x, int y)
    {
        super();
        this.x = x;
        this.y = y;
        empty = true;
        corpseImages = new ArrayList<Image>();
    }
    public synchronized boolean ifEmpty()
    {
        return empty;
    }
    public int getX()
    {
        return x;
    }
    public void setX(int x)
    {
        this.x = x;
    }
    public int getY()
    {
        return y;
    }
    public void setY(int y)
    {
        this.y = y;
    }
    public synchronized void setHolder(Creature holder) //进入位置
    {
        this.holder = holder;
        empty = false;
    }
    public synchronized void out() //从位置离开
    {
        this.holder = null;
        empty = true;
        notifyAll();
    }
    public synchronized Creature getHolder() //获得当前角色的引用
    {
        return this.holder;
    }
    public synchronized void waitForPos() throws InterruptedException
    {
        while(!ifEmpty())
            wait();
    }
    public synchronized void addCorpseImages(Image image)
    {
        corpseImages.add(image);
    }
    public synchronized ArrayList<Image> getCorpseImages()
    {
        return corpseImages;
    }

}