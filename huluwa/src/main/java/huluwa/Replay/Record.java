package huluwa.Replay;

import java.io.DataOutputStream;
import java.io.IOException;
import java.util.ArrayList;

public class Record
{
    ArrayList<Scene> scenes; //定时记录场景，每隔0.2s显示
    boolean result; //记录大战结果，0为妖精胜，1为葫芦娃胜
    public Record()
    {
        scenes = new ArrayList<Scene>();
        result = false;
    }
    public void addScene(Scene scene)
    {
        scenes.add(scene);
    }
    public ArrayList<Scene> getScenes()
    {
        return scenes;
    }
    public void write(DataOutputStream out) throws IOException
    {
        out.writeInt(scenes.size());//场景数
        out.writeBoolean(result);   //比赛结果
        for (Scene scene : scenes)
        {
            scene.write(out);
        }
    }
    public void setResult(boolean result)
    {
        this.result = result;
    }
}
