package test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.antelop.utils.FtpClientUtil;

public class FtpTest
{

    public static void main(String[] args)
    {
        // TODO Auto-generated method stub
        new FtpTest().downloadFromFtp();
    }

    public void downloadFromFtp()
    {
        try {
            String ip = "127.0.0.1";
            String port = "21";
            String userName = "antelop";
            String password = "antelop";
            FtpClientUtil ftpClient = new FtpClientUtil(ip,Integer.parseInt(port),userName, password);
            List<Map<String, String>> list = new ArrayList<Map<String, String>>();
            
            for (int i=0;i<1;i++) {
                Map<String, String> map = new HashMap<String, String>();
                String fileName = "Point.xlsx";
                String filePath = "/EasyWlan2";
                String localFullFileName = "E:/Point.xlsx";
                map.put("fileName", fileName);
                map.put("filePath", filePath);
                map.put("localFullFileName", localFullFileName);
                list.add(map);
            }
            // 从FTP服务器上下载文件
            boolean ok = ftpClient.GetFtpFile(list);
            if(ok){
                System.out.println("下载成功");
            }else{
                System.out.println("下载失败");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
//            Tracker.e(e);
        }
    }
}
