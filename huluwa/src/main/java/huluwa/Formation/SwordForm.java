package huluwa.Formation;

import huluwa.Space.Position;
import huluwa.Space.Space;

import java.util.ArrayList;

public class SwordForm extends Formation //偃月形
{
    public SwordForm()
    {
        length = 11;
        posTuples  = new ArrayList<Tuple>(length);
        posTuples.add(new Tuple(6,4));
        posTuples.add(new Tuple(6,3));
        posTuples.add(new Tuple(6,5));
        int i;
        for(i = 7; i <= 10; i++)
            posTuples.add(new Tuple(i,4));
        posTuples.add(new Tuple(7,2));
        posTuples.add(new Tuple(8,1));
        posTuples.add(new Tuple(7,6));
        posTuples.add(new Tuple(8,7));

    }
}