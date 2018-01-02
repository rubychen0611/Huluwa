package huluwa.Queue;
import huluwa.Creature.*;
import huluwa.Formation.Formation;
import huluwa.Space.*;
import java.util.ArrayList;
import java.util.Collections;


public class HuluwaQueue extends Queue<Huluwa>
{
    public HuluwaQueue(Space space, Formation form)
    {
        this.form = form;
        num = 7;
        creatures = new Huluwa[num];
        ArrayList<Integer> array = new ArrayList<>(num);
        int i;
        //任意站队
        for(i = 0; i < num; i++)
            array.add(i);
        Collections.shuffle(array);

        for (i = 0; i < num; i++)
        {
            HuluAttribute huluAttribute;
            switch (i+1)
            {
                case 1: huluAttribute = HuluAttribute.RED; break;
                case 2: huluAttribute = HuluAttribute.ORANGE; break;
                case 3: huluAttribute = HuluAttribute.YELLOW; break;
                case 4: huluAttribute = HuluAttribute.GREEN; break;
                case 5: huluAttribute = HuluAttribute.CYAN; break;
                case 6: huluAttribute = HuluAttribute.BLUE; break;
                case 7: huluAttribute = HuluAttribute.PURPLE; break;
                default: huluAttribute = null; assert(false);
            }

            creatures[i] = new Huluwa( form.getPosTuples().get(array.get(i)).getX(), form.getPosTuples().get(array.get(i)).getY(), space, i + 1, huluAttribute);
        }
    }


}
