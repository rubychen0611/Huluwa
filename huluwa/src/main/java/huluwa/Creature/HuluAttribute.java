package huluwa.Creature;

public enum HuluAttribute//葫芦娃属性
{
    RED("①","红色",1), ORANGE("②", "橙色",2), YELLOW("③", "黄色",3), GREEN("④","绿色",4),
    CYAN("⑤","青色",5), BLUE("⑥", "蓝色", 6), PURPLE("⑦", "紫色", 7);
    private String NoName; //老X
    private String ColorName; //颜色
    private int index; //序号
    private HuluAttribute(String NoName, String ColorName, int index) //构造函数
    {
        this.NoName = NoName;
        this.ColorName = ColorName;
        this.index = index;
    }
    public  String getNoName()
    {
        return NoName;
    }
    public String getColorName()
    {
        return  ColorName;
    }
    public int getIndex() {return index;}
}