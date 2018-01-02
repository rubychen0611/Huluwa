package huluwa.Creature;
import huluwa.Formation.Tuple;
import huluwa.Space.*;

import java.awt.*;
import java.util.Random;

import static java.lang.Thread.interrupted;
import static java.lang.Thread.yield;

enum Species //物种
{
    HULUWA, OLDMAN, SNAKE, SCORPION, MINION
}

public class Creature implements Runnable//抽象类：生物
{
    public Species species; //物种
    protected Position position; //位置
    private Space space;
    private boolean alive;      //是否死亡
    public int rank;    //战斗力
    public Group group;     //阵营
    public Image livingImage;
    public Image deadImage;
    private static int count = 0;  //记录生物创建个数，用于编号，回放时根据编号获取应显示的图片
    private final int creatureNo;
    public Creature(int x, int y, Space space)
    {
        this.space = space;
        setPosition(space.getPosition(x, y));
        alive = true;
        this.creatureNo = count++;
    }
    public static void setCountZero()
    {
        count = 0;
    }
    public int getCreatureNo()
    {
        return creatureNo;
    }

    protected void sendImages()
    {
        space.addImages(creatureNo, livingImage, deadImage);
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
        this.position.addCorpseImagesNo(creatureNo);
        leavePosition();
        this.alive = false;
    }
    private Tuple nearestEnemy(int x, int y, int g, int[][] s)
    {
       // boolean [][] f;
        int N = Space.N;
        int M = Space.M;
        int i, j;
        int minx = x, miny = y;
        int mind = N * N + M * M;
        for(i = 0; i < N; i++)
        {
            for (j = 0; j < M; j++) {
                if (s[i][j] == -g) {
                    int d = (x - i) * (x - i) + (y - j) * (y - j);
                    if (d < mind) {
                        mind = d;
                        minx = i;
                        miny = j;
                    }
                }
            }
        }
        return new Tuple(minx, miny);
    }
    private synchronized Position decideNextPos() //决定下一步
    {
        int x = position.getX();
        int y = position.getY();
        int g = this.group == Group.GOOD ? 1 : -1;
        int[][] s = this.space.getCurrentSituation();
       // Position nextPos;
        int i;
        //先看前方(相对)有无敌人: 有则前进
        for(i = x + g; i < Space.N && i >= 0; i += g)
        {
            if(s[i][y] == -g)
                return this.space.getPosition(x+g, y);
        }
        //再看后方(相对)有无敌人： 有则后退
        for(i = x-g; i < Space.N && i >= 0; i -= g)
        {
            if(s[i][y] == -g)
                return this.space.getPosition(x-g, y);
        }
        //再看距离最近的敌人位置（广度优先搜索）决定上或者下
        Tuple ep = nearestEnemy(x, y, g, s);
        if(ep.getY() < y)
            return this.space.getPosition(x,y-1);
        else return this.space.getPosition(x, y+1);

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

        while (this.isAlive() && !this.space.ifBattleEnds() && !interrupted()) //仍活着且战斗未结束
            {
                try {
                    Position nextPos = decideNextPos();//判断下一步移动位置
                    synchronized (nextPos) {
                            nextPos.waitForPos();       //如果该位置有人，等待
                            if (!this.isAlive()) break;
                            yield();
                            synchronized (this) {
                                leavePosition();    //两句之间可能出现位置为空
                                setPosition(nextPos);
                            }
                    }
                    Random rand = new Random();
                   Thread.sleep(rand.nextInt(1000) + 800);
                    }catch (NullPointerException npe)
                {
                    return;
                }
                catch (InterruptedException ie)
                {
                    System.out.println("interrupt" + name);
                }
            }
    }

}

