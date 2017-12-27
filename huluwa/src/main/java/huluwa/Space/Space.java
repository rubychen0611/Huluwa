package huluwa.Space;

import huluwa.Creature.*;
import huluwa.Formation.LongSnakeForm;
import huluwa.Formation.SwordForm;
import huluwa.Queue.HuluwaQueue;
import huluwa.Queue.ScorpionQueue;
import huluwa.Tile;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.swing.JPanel;
import javax.xml.ws.Holder;

import static java.lang.Thread.sleep;

public class Space extends JPanel//二维坐标表示的空间
{
    public static final int N = 11;
    public static final int M = 9;
    public static final int GOOD_GROUP_NUM= 8;
    public static final int EVIL_GROUP_NUM= 12;
    private Position [][]positions;

    private final int SPACE = 100;

    //private ArrayList tiles = new ArrayList();      //背景
   // private Player player;                          //玩家

    private int w = 0;
    private int h = 0;
    private boolean completed = false;

    private int goodGroupCount = GOOD_GROUP_NUM;
    private int evilGroupCount = EVIL_GROUP_NUM;

    HuluwaQueue huluwaqueue = null;
    ScorpionQueue scorpionqueue = null;
    Oldman oldman = null;
    Snake snake = null;
    private boolean battleEnds = false;
    public Space()
    {
        addKeyListener(new Space.TAdapter()); //键盘监视器
        goodGroupCount = GOOD_GROUP_NUM;
        evilGroupCount = EVIL_GROUP_NUM;
        battleEnds = false;
        setFocusable(true);
        try {initWorld();}
        catch (Exception e)
        {
            System.out.println("Error1");
        }
    }
    public synchronized boolean ifBattleEnds()
    {
        return battleEnds;
    }
    public synchronized void endBattle()
    {
        battleEnds = true;
    }
    public synchronized Position getPosition(int x, int y)
    {
        if(!ifPositionLegal(x, y))
            return null;
        return positions[x][y];
    }

    public int getBoardWidth() {
        return this.w;
    }

    public int getBoardHeight() {
        return this.h;
    }

    public boolean ifPositionLegal(int x, int y)
    {
        if(x >= 0 && x < 11 && y >= 0 && y < 9)
            return true;
        else return false;
    }
    private boolean decideWinner(Creature a, Creature b)   //决定战斗输赢，返回true则a赢，false则b赢
    {
        Random random = new Random();
        int rand = random.nextInt(100) + 1; //生成1-100之间的随机数
        int p = 50 + (a.rank - b.rank) * 5;    //强者赢的概率：p = 0.5 +(两者等级之差) * 0.05  则同等级输赢改了各为10%, 等级悬殊最大的两者相遇后强者赢的概率为85%
      //  System.out.println(rand + " " + p);
        if (rand <= p) return true;    //随机数小于p则a赢
        else return false;
    }
    private synchronized void battle(Creature good, Creature evil)
    {

                if (decideWinner(good, evil)) //葫芦娃赢
                {
                    evil.die();
                    evilGroupCount--;
                    System.out.println("妖精："+ evilGroupCount);
                    if (evilGroupCount == 0)
                    {
                        endBattle();
                        System.out.println("葫芦娃胜利");
                    }
                } else //妖精赢
                {
                    good.die();
                    goodGroupCount--;
                    System.out.println("好人："+ goodGroupCount);
                    if (goodGroupCount == 0)
                    {
                        endBattle();
                        System.out.println("妖精胜利");
                    }
                }

    }
    private synchronized boolean ifMeetEnemy(Creature c)
    {
        int x, y;
        Creature holder;
        assert c.isAlive();
        assert !c.getPosition().ifEmpty();
        x = c.getPosition().getX();
        y = c.getPosition().getY();
        if(ifPositionLegal(x+1, y) && !getPosition(x+1, y).ifEmpty())
        {
            holder = getPosition(x+1,y).getHolder();
            if(holder.group != c.group) //前方有敌人
            {
                assert !holder.ifPositionEmpty();
                assert(holder.isAlive());
                //synchronized (holder) {
                battle(c, holder);
                // }
                return true;
            }

        }

        if(ifPositionLegal(x-1, y) && !getPosition(x-1, y).ifEmpty())
        {
            //holder = getPosition(x-1, y).getHolder();

            holder = getPosition(x-1,y).getHolder();
            if(holder.group != c.group) //后方有敌人
            {
                assert !holder.ifPositionEmpty();
                assert(holder.isAlive());
                //synchronized (holder) {
                    battle(c, holder);
               // }
                return true;
            }

        }

        if(ifPositionLegal(x, y+1) && !getPosition(x, y+1).ifEmpty())
        {
            //holder = getPosition(x-1, y).getHolder();
            holder = getPosition(x,y+1).getHolder();
            if(holder.group != c.group) //下方有敌人
            {
                assert !holder.ifPositionEmpty();
                assert(holder.isAlive());
                //synchronized (holder) {
                battle(c, holder);
                // }
                return true;
            }

        }

        if(ifPositionLegal(x, y-1) && !getPosition(x, y-1).ifEmpty())
        {
            holder = getPosition(x,y-1).getHolder();
            if(holder.group != c.group) //上方有敌人
            {
                assert !holder.ifPositionEmpty();
                assert(holder.isAlive());
                //synchronized (holder) {
                battle(c, holder);
                // }
                return true;
            }

        }
        return false;
    }
    public synchronized boolean ifBattleExists()
    {
        //遍历葫芦娃方的四周有无敌人
        boolean flag = false;
        for(Creature c: huluwaqueue.getCreatures())
        {
            //synchronized (c) {
                if (c.isAlive()) {
                    if (ifMeetEnemy(c))
                        flag = true;
                }
           // }
        }
        if(oldman.isAlive() && ifMeetEnemy(oldman))
            flag = true;
        return flag;
    }

    public final void initWorld()
    {
        w = SPACE * N;
        h = SPACE * M;
        positions = new Position[N][M];
        int i, j;
        for(i = 0; i < N; i++)
            for(j = 0; j < M; j++)
            {
                positions[i][j] = new Position(i, j);//初始化N*M个位置
            }
        huluwaqueue = new HuluwaQueue(this, new LongSnakeForm()); //葫芦娃
        scorpionqueue = new ScorpionQueue(this,new SwordForm()); //蝎子精和喽啰
        snake = new Snake(10, 5,this);
        oldman = new Oldman(1,4,this);
        //repaint();
    }

    public void start() throws Exception
    {
        //Thread.sleep(1500);//sleep
        ExecutorService exec = Executors.newCachedThreadPool();
        for(Creature c :huluwaqueue.getCreatures())
            exec.execute(c);
        for(Creature c: scorpionqueue.getCreatures())
            exec.execute(c);
        exec.execute(snake);//蛇精
        exec.execute(oldman); //老爷爷
        exec.execute(           //刷新屏幕线程，λ表达式
                ()->
                {
                    while(!ifBattleEnds())
                     {
                        repaint();
                        try {
                        sleep(100);
                        synchronized (this) {
                            ifBattleExists();
                        }
                        } catch(Exception e)
                       {
                        System.out.println("error3");
                      }
                    }
                    repaint();
                }
        );
    }

    public void buildWorld(Graphics g)
    {

       // g.setColor(new Color(250, 240, 170));
        //g.fillRect(0, 0, this.getWidth(), this.getHeight());

        Tile tile = new Tile();
        g.drawImage(tile.getImage(),0, 0, this);
        int i,j ;

        for( i = 0; i < N; i++)
        {
            for (j = 0; j < M; j++)
            {
                if(!positions[i][j].corpseImages.isEmpty())
                {
                    for(Image im: positions[i][j].corpseImages)
                    {
                        g.drawImage(im,i * SPACE, j * SPACE, this ); //绘制尸体图片
                    }
                }
                if (!positions[i][j].ifEmpty())
                {
                    g.drawImage(positions[i][j].getHolder().getImage(), i * SPACE, j * SPACE, this);  //绘制生物图片
                }
            }
        }

    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        buildWorld(g);
    }

    class TAdapter extends KeyAdapter
    {

        @Override
        public void keyPressed(KeyEvent e)
        {

            if (completed)
            {
                return;
            }
            int key = e.getKeyCode();

            if (key == KeyEvent.VK_SPACE)
            {
               try
               {
                   start();
               }catch (Exception ex)
               {
                   System.out.println("error2");
               }
            }

        }
    }


/*    public void restartLevel() {

        tiles.clear();
        initWorld();
        if (completed) {
            completed = false;
        }
    }
    */
}
