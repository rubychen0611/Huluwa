package huluwa.Replay;


import java.io.*;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileManager
{
    private Record record;
    private File file;
   // private Space space;
    public FileManager()
    {
         record = new Record();
    }
    public void readRecord(File file) throws IOException//读文件 存入record
    {
        this.file = file;
        DataInputStream in = new DataInputStream(new BufferedInputStream(new FileInputStream(file)));
        record.read(in);
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
            DataOutputStream out = new DataOutputStream(new BufferedOutputStream(new FileOutputStream(file)));
            record.write(out);

            out.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    public Record getRecord()
    {
        return record;
    }//供Space 回放调用

}
