package huluwa.Formation;
import java.util.ArrayList;
public class LongSnakeForm extends Formation //长蛇形
{
    public LongSnakeForm()
    {
        length = 7;
        posTuples = new ArrayList<Tuple>(length);//new Position[length];
        for(int i = 0; i < length; i++)
        {
            posTuples.add(new Tuple(2, 1+i));
        }
    }
}