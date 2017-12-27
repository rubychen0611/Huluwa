package huluwa.Queue;
import huluwa.Creature.*;
import huluwa.Formation.Formation;
import huluwa.Space.Space;

public class ScorpionQueue extends Queue<Creature> //蝎子精及其喽啰阵型
{
    public ScorpionQueue(Space space, Formation form)
    {
        this.form = form;
        num = 11;
        creatures = new Creature[num];
        creatures[0] = new Scorpion(form.getPosTuples().get(0).getX(),form.getPosTuples().get(0).getY(),space); //蝎子精初始化
        for(int i= 1 ; i < num  ; i++)
        {
            creatures[i] = new Minion(form.getPosTuples().get(i).getX(),form.getPosTuples().get(i).getY(),space,i); //小喽啰初始化
        }
    }
}
