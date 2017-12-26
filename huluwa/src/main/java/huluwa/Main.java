package huluwa;
import javax.swing.JFrame;
import huluwa.Space.Space;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public final class Main extends JFrame
{

    public Main()
    {
        InitUI();
    }

    public void InitUI() {
        Space space = new Space();
        add(space);

        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(space.getBoardWidth() + 15,
                space.getBoardHeight() + 55);
        setLocationRelativeTo(null);
        setTitle("葫芦娃大战妖精");
    }


    public static void main(String[] args)
    {
        Main ground = new Main();
        ground.setVisible(true);
    }
}