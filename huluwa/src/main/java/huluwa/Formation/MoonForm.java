package huluwa.Formation;

import huluwa.Space.Position;
import huluwa.Space.Space;

import java.util.ArrayList;

public class MoonForm extends Formation //偃月形
{
    public MoonForm()
    {
        length = 19;
        posTuples  = new ArrayList<Tuple>(length);
        posTuples.add(new Tuple(6,4));
        posTuples.add(new Tuple(6,3));
        posTuples.add(new Tuple(6,5));
        int i, k = 3;
        for(i = 2; i <= 6; i++)
            posTuples.add(new Tuple(7,i));
        for(i = 1; i <= 7; i++)
            posTuples.add(new Tuple(8,i));
        posTuples.add(new Tuple(9,1));
        posTuples.add(new Tuple(9,7));
        posTuples.add(new Tuple(10,0));
        posTuples.add(new Tuple(10,8));

    }
}