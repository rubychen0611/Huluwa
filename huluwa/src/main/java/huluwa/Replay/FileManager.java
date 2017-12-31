package huluwa.Replay;

import huluwa.Space.Space;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

import static java.lang.Thread.interrupted;
import static java.lang.Thread.sleep;

public class FileManager
{
    private Record record;
    private File file;
    private Space space;
    public FileManager(Space space)
    {
        this.space = space;
    }
    public void newRecord() //新建记录，新建文件，以时间命名
    {
        record = new Record();

     }
    public void readRecord(File file)
    {
        this.file = file;
        //读文件 存入record
    }
    public void writeRecord(Scene scene)//每次刷新时调用，向文件中添加一个场景
    {
        record.addScene(scene);
    }
    public void writeFile(boolean result)
    {
        record.setResult(result);
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//"Huluwa"+"yyyyMMddHHmmss");//设置日期格式
        String fileName = "Huluwa"+ df.format(new Date()) + ".dat";
        file = new File(fileName);
        try {
            if (!file.exists())
                file.createNewFile();
        }catch(Exception e)
        {
            System.out.println("文件创建失败");
        }
        try
        {
            DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(fileName)));
            record.write(out);

            out.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
        for(Scene scene : record.getScenes())
        {

        }
    }
    public Record getRecord()
    {
        return record; //供Space 回放调用
    }

   /* @Override
    public void run()
    {
        while(!interrupted())
        {
            try{
            space.waitingForNewScene();
            writeRecord(space.getScene());//添加场景
              //  sleep(500);
            }catch (InterruptedException ie)
            {
                System.out.println("FileManager: interrupt");
            }
        }
    }*/
}
