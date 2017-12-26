package huluwa.Creature;
import huluwa.Space.*;
import huluwa.Thing2D;

import java.util.Random;

enum Species //物种
{
    HULUWA, OLDMAN, SNAKE, SCORPION, MINION;
}
public class Creature extends Thing2D implements Runnable//抽象类：生物
{
    public Species species; //物种
    protected Position position; //位置
    private Space space;
    private boolean alive;      //是否死亡
    public int rank;    //战斗力

    public Creature(int x, int y, Space space)
    {
        this.space = space;
        setPosition(space.getPosition(x, y));
        alive = true;
    }
    public Position getPosition()
    {
        return this.position;
    }
    public void setPosition(Position position) //设置位置
    {
        if(position.ifEmpty())
        {
            leavePosition();
            position.setHolder(this);
            this.position = position;
        }
        else assert(false);
    }
    public void leavePosition() //离开位置
    {
        if(this.position != null)
        {
            (this.position).out();
            this.position = null;
        }

    }

    public void run() {
        while (!Thread.interrupted())
        {
          //  Random rand = new Random();
            int x = this.position.getX();
            int y = this.position.getY();
            y= (y+1)%9;
            leavePosition();
            setPosition(space.getPosition(x, y));
            try {

                Thread.sleep(2000);
                this.space.repaint();

            } catch (Exception e)
            {

            }
        }
    }
   /* public void move(int x, int y)
    {
        int nx = this.x() + x;
        int ny = this.y() + y;
        this.setX(nx);
        this.setY(ny);
    }
*/
}
