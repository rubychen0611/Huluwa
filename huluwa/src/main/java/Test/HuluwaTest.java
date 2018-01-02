package Test;

import huluwa.Replay.FileManager;
import org.junit.Test;

import java.io.File;
import java.io.IOException;

public class HuluwaTest
{
    @Test
    public void testReadRecord1() throws IOException        //测试读入正常数据文件
    {
            File file = new File("example.dat");
            FileManager fileManager = new FileManager();
            fileManager.readRecord(file);
    }
    @Test(expected = Exception.class)
    public void testReadRecord2() throws IOException        //测试读入非正常文件
    {
        File file = new File("huluwa.iml");
        FileManager fileManager = new FileManager();
        fileManager.readRecord(file);
    }
}
