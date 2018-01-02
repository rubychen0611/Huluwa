package huluwa.Replay;

public class DisplayElement
{
    private int creatureNo;
    private boolean isAlive;
    private int x;
    private int y;
    public DisplayElement(int creatureNo, boolean isAlive, int x, int y)
    {
        this.creatureNo = creatureNo;
        this.isAlive = isAlive;
        this.x = x;
        this.y = y;
    }

    public int getCreatureNo() {
        return creatureNo;
    }

    public int getX() {
        return x;
    }
    public int getY()
    {
        return y;
    }

    public boolean getIsAlive() {
        return isAlive;
    }
}
