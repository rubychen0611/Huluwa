package huluwa.Space;

import huluwa.Creature.*;
import huluwa.Formation.LongSnakeForm;
import huluwa.Formation.SwordForm;
import huluwa.Queue.HuluwaQueue;
import huluwa.Queue.ScorpionQueue;
import huluwa.Replay.DisplayElement;
import huluwa.Replay.FileManager;
import huluwa.Replay.Scene;
import huluwa.Thing2D;

import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import javax.swing.*;

import static java.lang.Thread.interrupted;
import static java.lang.Thread.sleep;

enum State
{
    BEGIN,RUNNING,REPLAY,END,REPLAYEND
}
public class Space extends JPanel//二维坐标表示的空间
{
    public static final int N = 11;
    public static final int M = 9;
    private static final int GOOD_GROUP_NUM= 8;
    private static final int EVIL_GROUP_NUM= 12;
    private Position [][]positions;

    private final int SPACE = 100;
    private State state = State.BEGIN;


    private int w = 0;
    private int h = 0;

    private int goodGroupCount = GOOD_GROUP_NUM;
    private int evilGroupCount = EVIL_GROUP_NUM;
    private ExecutorService exec;
    private FileManager fileManager;
    private Scene scene;

    private HuluwaQueue huluwaqueue = null;
    private ScorpionQueue scorpionqueue = null;
    private Oldman oldman = null;
    private Snake snake = null;
    private Map<Integer, Image> livingImages;
    private Map<Integer, Image> corpseImages;
    private boolean battleEnds = false;

    public Space()
    {
        addKeyListener(new Space.TAdapter()); //键盘监视器
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
    private synchronized void endBattle()
    {
        battleEnds = true;
    }
    public  Position getPosition(int x, int y)
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

    public int[][] getCurrentSituation() //获得当前局势表：0为空，1为正义方，-1为邪恶方
    {
        int[][] s = new int [Space.N][Space.M];
        for(int i = 0; i < N; i++)
        {
            for(int j = 0; j < M; j++)
            {
                try{
                synchronized (positions[i][j])
                {
                    if (!positions[i][j].ifEmpty()) {
                        if (positions[i][j].getHolder().group == Group.GOOD)
                            s[i][j] = 1;
                        else s[i][j] = -1;
                    } else {
                        s[i][j] = 0;
                    }
                }
                }
                catch (NullPointerException e)
                {
                    s[i][j] = 0;
                }
            }
        }
        return s;
    }
    public void addImages(int creatureNo, Image livingImage, Image deadImage)
    {
        this.livingImages.put(creatureNo, livingImage);
        this.corpseImages.put(creatureNo,deadImage);
    }

    private boolean ifPositionLegal(int x, int y)
    {
        return x >= 0 && x < 11 && y >= 0 && y < 9;
    }
    private boolean decideWinner(Creature a, Creature b)   //决定战斗输赢，返回true则a赢，false则b赢
    {
        Random random = new Random();
        int rand = random.nextInt(100) + 1; //生成1-100之间的随机数
        int p = 50 + (a.rank - b.rank) * 5;    //强者赢的概率：p = 0.5 +(两者等级之差) * 0.05  则同等级输赢改了各为10%, 等级悬殊最大的两者相遇后强者赢的概率为85%
        return rand <= p; //随机数小于p则a赢
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
                battle(c, holder);
                return true;
            }

        }

        if(ifPositionLegal(x-1, y) && !getPosition(x-1, y).ifEmpty())
        {
            holder = getPosition(x-1,y).getHolder();
            if(holder.group != c.group) //后方有敌人
            {
                assert !holder.ifPositionEmpty();
                assert(holder.isAlive());
                    battle(c, holder);
                return true;
            }

        }

        if(ifPositionLegal(x, y+1) && !getPosition(x, y+1).ifEmpty())
        {
            holder = getPosition(x,y+1).getHolder();
            if(holder.group != c.group) //下方有敌人
            {
                assert !holder.ifPositionEmpty();
                assert(holder.isAlive());
                battle(c, holder);
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
                battle(c, holder);
                return true;
            }

        }
        return false;
    }
    private synchronized boolean ifBattleExists()
    {
        //遍历葫芦娃方的四周有无敌人
        boolean flag = false;
        for(Creature c: huluwaqueue.getCreatures())
        {
                if (c.isAlive())
                {
                    if (ifMeetEnemy(c))
                        flag = true;
                }
        }
        if(oldman.isAlive() && ifMeetEnemy(oldman))
            flag = true;
        return flag;
    }

    private void initWorld()
    {
        goodGroupCount = GOOD_GROUP_NUM;
        evilGroupCount = EVIL_GROUP_NUM;
        battleEnds = false;
        state = State.BEGIN;
        setFocusable(true);
        Creature.setCountZero();
        livingImages = new HashMap<>();
        corpseImages = new HashMap<>();
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
        fileManager = new FileManager();
    }

    private void start() throws Exception
    {
        state = State.RUNNING;
        exec = Executors.newCachedThreadPool();
        for(Creature c :huluwaqueue.getCreatures())
            exec.execute(c);
        for(Creature c: scorpionqueue.getCreatures())
            exec.execute(c);
        exec.execute(snake);//蛇精
        exec.execute(oldman); //老爷爷
        exec.execute(           //刷新屏幕线程，λ表达式
                ()->
                {
                    while(!ifBattleEnds() && !interrupted())
                     {
                        repaint();
                        try {
                        sleep(200);
                        ifBattleExists();
                        } catch(Exception e)
                       {
                        System.out.println("error3");
                      }
                    }
                    state = State.END;  //大战结束
                    repaint();

                }
        );
    }
    private void replay()
    {
        ArrayList<Scene> scenes = fileManager.getRecord().getScenes();
        Iterator it = scenes.iterator();
        exec = Executors.newCachedThreadPool();
        exec.execute(           //刷新屏幕线程，λ表达式
                ()->
                {
                    try {
                        while (it.hasNext())
                        {
                            scene = (Scene) it.next();
                            repaint();
                            sleep(150);
                        }
                        state = State.REPLAYEND;
                        repaint();
                    } catch (InterruptedException ie) {
                        System.out.println("replay: interrupt");
                    }
                });
    }
    private void drawRecord(Graphics g)
    {
        ArrayList<DisplayElement>elements = scene.getElements();
        for(DisplayElement element: elements)
        {
            if(element.getIsAlive())
            {
                g.drawImage(livingImages.get(element.getCreatureNo()), element.getX() * SPACE, element.getY() * SPACE, this); //绘制生物图片
            }
            else
            {
                g.drawImage(corpseImages.get(element.getCreatureNo()), element.getX() * SPACE, element.getY() * SPACE, this);
            }
        }

    }
    private synchronized void drawSituation(Graphics g)
    {
        int i, j;
        scene = new Scene();
        for (i = 0; i < N; i++) {
            for (j = 0; j < M; j++) {
                    if (!positions[i][j].corpseImagesNo.isEmpty()) {
                        for (Integer no : positions[i][j].corpseImagesNo) {
                            g.drawImage(corpseImages.get(no), i * SPACE, j * SPACE, this); //绘制尸体图片
                            scene.addDisplayElement(no,false,i,j);
                        }
                    }
                    if (!positions[i][j].ifEmpty()) {
                        synchronized (positions[i][j].getHolder()) {
                            g.drawImage(livingImages.get(positions[i][j].getHolder().getCreatureNo()), i * SPACE, j * SPACE, this);  //绘制生物图片
                            scene.addDisplayElement(positions[i][j].getHolder().getCreatureNo(),true,i,j);
                        }
                    }
            }
        }
        fileManager.writeRecord(scene);
    }
    private void buildWorld(Graphics g)
    {
        switch (state)
        {
            case BEGIN :
                Thing2D sbg = new Thing2D("start.png");
                g.drawImage(sbg.getImage(), 0, 0, this);
                break;
            case RUNNING:
            {
                Thing2D bg = new Thing2D("bg.png");
                g.drawImage(bg.getImage(), 0, 0, this);
                drawSituation(g);
            } break;
            case END:
            {
                Thing2D bg = new Thing2D("bg.png");
                g.drawImage(bg.getImage(), 0, 0, this);
                drawSituation(g);
                if(goodGroupCount == 0)
                {
                    Thing2D mw = new Thing2D("yj_win.png");
                    g.drawImage(mw.getImage(), 0, 0, this);
                }
                else
                {
                    Thing2D hw = new Thing2D("hlw_win.png");
                    g.drawImage(hw.getImage(), 0, 0, this);
                }
            } break;
            case REPLAY:
            {

                Thing2D bg = new Thing2D("bg.png");
                g.drawImage(bg.getImage(), 0, 0, this);
                drawRecord(g);

            } break;
            case REPLAYEND:
            {
                Thing2D bg = new Thing2D("bg.png");
                g.drawImage(bg.getImage(), 0, 0, this);
                drawRecord(g);
                if(fileManager.getRecord().getResult())
                {
                    Thing2D hw = new Thing2D("hlw_win_replay.png");
                    g.drawImage(hw.getImage(), 0, 0, this);
                }
                else
                {
                    Thing2D mw = new Thing2D("yj_win_replay.png");
                    g.drawImage(mw.getImage(), 0, 0, this);
                }
                break;
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
            int key = e.getKeyCode();

            switch (key)
            {
                case KeyEvent.VK_SPACE:
                    if(state == State.BEGIN)
                    {
                        try {
                            start();
                        } catch (Exception ex) {
                            System.out.println("error2");
                        }
                    }
                    else if(state == State.END )
                    {
                        exec.shutdownNow();
                        repaint();
                        try {
                            sleep(1000);
                        }catch (InterruptedException ie)
                        {
                            System.out.println("wating for all threads being shut down: interrupt");
                        }
                        try {initWorld();}
                        catch (Exception ex)
                        {
                            System.out.println("Error appears when initWorld");
                        }
                        state = State.BEGIN;
                        repaint();
                    }
                    else if(state == State.REPLAYEND)
                    {
                        try {initWorld();}
                        catch (Exception ex)
                        {
                            System.out.println("Error appears when initWorld");
                        }
                        state = State.BEGIN;
                        repaint();
                    }
                    break;
                case KeyEvent.VK_L:
                    if(state == State.BEGIN)
                    {
                        JFileChooser fd = new JFileChooser();
                        fd.showOpenDialog(null);
                        File file = fd.getSelectedFile();
                        if (file != null)
                        {
                            try {
                                fileManager.readRecord(file);
                            }catch (FileNotFoundException ffe)
                            {
                                JOptionPane.showMessageDialog(null, "出错啦", "找不到文件！", JOptionPane.ERROR_MESSAGE);
                            }catch (IOException ioe)
                            {
                                JOptionPane.showMessageDialog(null, "出错啦", "文件读取失败！", JOptionPane.ERROR_MESSAGE);
                            }
                            System.out.println("文件读取成功");
                            state = State.REPLAY;
                            replay();
                        }
                    }
                    break;
                case KeyEvent.VK_S:
                    if(state == State.END)
                    {
                        fileManager.writeFile(goodGroupCount != 0);
                        JOptionPane.showMessageDialog(null, "保存成功！");
                    }
                    break;
                default:break;
            }


        }
    }
}
