package huluwa.Formation;
import java.util.ArrayList;


public class Formation  //队形基类
{
    protected ArrayList<Tuple> posTuples = null; //存储队形的位置元组数据
    protected int length;

   public ArrayList<Tuple> getPosTuples()
    {
        return posTuples;
    }

    public int getLength()
    {
        return length;
    }
}

