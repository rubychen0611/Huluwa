package huluwa.Space;

import huluwa.Creature.Oldman;
import huluwa.Creature.Snake;
import huluwa.Formation.LongSnakeForm;
import huluwa.Formation.MoonForm;
import huluwa.Queue.HuluwaQueue;
import huluwa.Queue.ScorpionQueue;
import huluwa.Tile;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import javax.swing.JPanel;
public class Space extends JPanel//二维坐标表示的空间
{
    public static final int N = 11;
    public static final int M = 9;
    private Position [][]positions;

    private final int SPACE = 100;

    //private ArrayList tiles = new ArrayList();      //背景
   // private Player player;                          //玩家

    private int w = 0;
    private int h = 0;
    private boolean completed = false;

    public Space()
    {
      //  addKeyListener(new Space.TAdapter()); //键盘监视器
        setFocusable(true);
        try {initWorld();}
        catch (Exception e)
        {}
    }
    public Position getPosition(int x, int y)
    {
        return positions[x][y];
    }

    public int getBoardWidth() {
        return this.w;
    }

    public int getBoardHeight() {
        return this.h;
    }

    public final void initWorld()  throws Exception
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
        HuluwaQueue huluwaqueue = new HuluwaQueue(this, new LongSnakeForm()); //葫芦娃
        ScorpionQueue scorpionqueue = new ScorpionQueue(this,new MoonForm()); //蝎子精和喽啰

        ExecutorService exec = Executors.newCachedThreadPool();
      //  exec.execute(new Snake(6, 2,this));//蛇精
        exec.execute(new Oldman(1,4,this)); //老爷爷

        //TimeUnit.SECONDS.sleep(10); // Run for a while...
       // exec.shutdownNow(); // Interrupt all tasks

    }

    public void buildWorld(Graphics g)
    {

        g.setColor(new Color(250, 240, 170));
        g.fillRect(0, 0, this.getWidth(), this.getHeight());

        Tile tile = new Tile();
        g.drawImage(tile.getImage(),0, 0, this);
        int i,j ;

        for( i = 0; i < N; i++)
        {
            for (j = 0; j < M; j++)
            {
                if (!positions[i][j].ifEmpty())
                {
                    g.drawImage(positions[i][j].getHolder().getImage(), i * SPACE, j * SPACE, this);
                }
            }
        }
       /* ArrayList world = new ArrayList();
        world.addAll(tiles);

        world.add(player);


        for (int i = 0; i < world.size(); i++)
        {

            Thing2D item = (Thing2D) world.get(i);

            if (item instanceof Player)
            {
                g.drawImage(item.getImage(), item.x() + 2, item.y() + 2, this);
            }
            else
            {
                g.drawImage(item.getImage(), item.x(), item.y(), this);
            }

            if (completed) {
                g.setColor(new Color(0, 0, 0));
                g.drawString("Completed", 25, 20);
            }

        }*/
    }

    @Override
    public void paint(Graphics g) {
        super.paint(g);
        buildWorld(g);
    }

   /* class TAdapter extends KeyAdapter
    {

       / @Override
        public void keyPressed(KeyEvent e)
        {

            if (completed) {
                return;
            }


            int key = e.getKeyCode();


            if (key == KeyEvent.VK_LEFT) {


                player.move(-SPACE, 0);

            } else if (key == KeyEvent.VK_RIGHT) {


                player.move(SPACE, 0);

            } else if (key == KeyEvent.VK_UP) {


                player.move(0, -SPACE);

            } else if (key == KeyEvent.VK_DOWN) {


                player.move(0, SPACE);

            } else if (key == KeyEvent.VK_S) {

                new Thread(player).start();

            } else if (key == KeyEvent.VK_R) {
                restartLevel();
            }

            repaint();
        }
    }
*/

/*    public void restartLevel() {

        tiles.clear();
        initWorld();
        if (completed) {
            completed = false;
        }
    }
    */
}
