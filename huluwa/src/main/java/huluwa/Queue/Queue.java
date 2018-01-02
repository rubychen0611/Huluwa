package huluwa.Queue;
import huluwa.Creature.Creature;
import huluwa.Formation.*;
import huluwa.Space.*;
import java.util.ArrayList;
abstract public class Queue<T extends Creature> //抽象类：某生物队列
{
    protected int num; //队列人数
    protected T creatures[];//队列中的生物数组
    protected Formation form; //表示队列当前队形的对象

    public Creature[] getCreatures()
    {
        return creatures;
    }

}

