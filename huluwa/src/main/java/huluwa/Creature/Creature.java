package huluwa.Creature;
import huluwa.Space.*;
import huluwa.Thing2D;
import javafx.geometry.Pos;

import java.awt.*;
import java.util.Random;

enum Species //物种
{
    HULUWA, OLDMAN, SNAKE, SCORPION, MINION;
}
enum Group //阵营
{
    GOOD, EVIL; //正义与邪恶
}
public class Creature extends Thing2D implements Runnable//抽象类：生物
{
    public Species species; //物种
    protected Position position; //位置
    private Space space;
    private boolean alive;      //是否死亡
    public int rank;    //战斗力
    public Group group;     //阵营
    public Image deadImage;
    public Creature(int x, int y, Space space)
    {
        this.space = space;
        setPosition(space.getPosition(x, y));
        alive = true;
    }
    public synchronized Position getPosition()
    {
        return this.position;
    }

    public synchronized boolean isAlive()
    {
        return alive;
    }

    public synchronized void setPosition(Position position) //设置位置
    {
        synchronized (position) {
            if (position.ifEmpty()) {
                leavePosition();
                position.setHolder(this);
                this.position = position;
            } else assert (false);
        }
    }
    public synchronized void leavePosition() //离开位置
    {
        if(this.position != null)
        {
            (this.position).out();
            this.position = null;
        }
    }
    public synchronized boolean ifPositionEmpty()
    {
        return this.position == null;
    }
    public synchronized void die() //死亡
    {
        assert this.isAlive();
        assert !this.ifPositionEmpty();
        this.position.addCorpseImages(deadImage);
        leavePosition();
        this.alive = false;
    }
    private synchronized Position decideNextPos() //决定下一步
    {
        int x = position.getX();
        int y = position.getY();
        Position nextPos;
        if(group == Group.GOOD)
        {
             nextPos = space.getPosition(x + 1, y);
        }
        else
        {
            nextPos = space.getPosition(x-1,y);
        }
            return nextPos;
    }
    public void run()
    {
        //改线程名
        String name = species.toString();
        if(species == Species.HULUWA)
            name += ((Huluwa)this).getIndex();
        if(species == Species.MINION)
            name += ((Minion)this).getIndex();
        Thread.currentThread().setName( name );

        while (this.isAlive() && !this.space.ifBattleEnds()) //仍活着且战斗未结束
            {
                Position nextPos = decideNextPos();//判断下一步移动位置
                //synchronized (this.space) {
                    if (nextPos != null) {
                        try {
                            nextPos.waitForPos();       //如果该位置有人，等待
                        } catch (Exception e) {
                            System.out.println("interrupt1");
                        }
                        if(!this.isAlive()) break;
                        synchronized (nextPos)
                        {
                            synchronized (this) {
                                leavePosition();    //两句之间可能出现位置为空
                                setPosition(nextPos);
                            }
                       }
                    }
               // }
                try {
                    Thread.sleep(1000);//sleep
                }catch (Exception e)
                {
                    System.out.println("interrupt2");
                }
              /* if (this.space.ifBattleExists())//space判断是否有战斗，决定结果
                {
                    try {
                        Thread.sleep(100);//sleep
                    }catch (Exception e)
                    {
                        System.out.println("interrupt3");
                    }
                }*/
            }
    }

}

