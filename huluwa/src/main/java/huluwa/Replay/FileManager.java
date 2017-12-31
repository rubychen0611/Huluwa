package huluwa.Replay;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

public class FileManager
{
    private Record record;
    private File file;
    public FileManager()
    {
    }
    public void newRecord() //新建记录，新建文件，以时间命名
    {
        record = new Record();
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");//"Huluwa"+"yyyyMMddHHmmss");//设置日期格式
        String fileName = "Huluwa"+ df.format(new Date()) + ".txt";
        file = new File(fileName);
        try {
            if (!file.exists())
                file.createNewFile();
        }catch(Exception e)
        {
            System.out.println("文件创建失败");
        }
     }
    public void readRecord(File file)
    {
        this.file = file;
        //读文件 存入record
    }
    public void writeRecord()
    {
        //每次刷新时调用，向文件中添加一个场景

    }
    public Record getRecord()
    {
        return record; //供Space 回放调用
    }
}
