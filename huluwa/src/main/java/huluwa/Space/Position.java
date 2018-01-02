package huluwa.Space;
import huluwa.Creature.Creature;

import java.awt.*;
import java.util.ArrayList;

public class Position //队列位置类
{
    private int x, y;
    private Creature holder; //位置的占有者
    private boolean empty; //位置是否为空
    public ArrayList<Integer> corpseImagesNo; //尸体图片
    public Position(int x, int y)
    {
        super();
        this.x = x;
        this.y = y;
        empty = true;
        corpseImagesNo = new ArrayList<>();
    }
    public synchronized boolean ifEmpty()
    {
        return empty;
    }
    public int getX()
    {
        return x;
    }
    public int getY()
    {
        return y;
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
    public synchronized void addCorpseImagesNo(int creatureNo)
    {
        corpseImagesNo.add(new Integer(creatureNo));
    }

}