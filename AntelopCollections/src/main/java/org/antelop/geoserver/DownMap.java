package org.antelop.geoserver;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;

/**
 * 下载瓦片地图.
 * 
 * @author antelop
 * @version 2014.08.19
 */
public class DownMap
{
    /**
     * 
     * @param url
     *         地图下载地址
     * @param savePath
     *         保存路径
     * @param type
     *         图层类型
     * @param xmin
     * @param xmax
     * @param ymin
     * @param ymax
     * @param levelMin
     *        图层最小级别
     * @param levelMax
     *        图层最大级别
     */
    public static void downTianditu(
        String url,
        String savePath,
        String type,
        int xmin,
        int xmax,
        int ymin,
        int ymax,
        int levelMin,
        int levelMax)
    {
        // url = http://t0.tianditu.com/DataServer?T=vec_c&X=6&Y=1&L=3
        for (int l = levelMin; l <= levelMax; l++) {
            for (int x = xmin; x <= xmax; x++) {
                for (int y = ymin; y <= ymax; y++) {
                    String imageUrl =url+"?T="+type + "&X=" + x + "&Y=" + y + "&L=" + l;
                    String newImageName = savePath + "/" + l + "/" + x + "-" + y + "-" + l + ".jpg";
                    if (!new File(newImageName).exists()) {
                        try {
                            URL url_type = new URL(imageUrl);
                            
                            // 打开网络输入流
                            DataInputStream dis_type = new DataInputStream(
                                url_type.openStream());

                            // 建立一个新的文件
                            File dir_type = new File(
                                newImageName.substring(
                                    0, newImageName.lastIndexOf("/")));
                            if (!dir_type.exists()) {
                                dir_type.mkdirs();
                            }
                            FileOutputStream fos_type = new FileOutputStream(
                                newImageName);
                            byte[] buffer_type = new byte[1024];

                            int length;

                            // 开始填充数据

                            while ((length = dis_type.read(buffer_type)) > 0) {
                                fos_type.write(buffer_type, 0, length);
                            }

                            dis_type.close();
                            fos_type.close();
                        }
                        catch (IOException e) {
                            e.printStackTrace();
                        }
                        catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            xmin = xmin * 2;
            xmax = xmax * 2;
            ymin = ymin * 2;
            ymax = ymax * 2;
        }
    }
    
    public static void main(String[] args)
    {
        downTianditu("http://t0.tianditu.com/DataServer","D:/aa","vec_c",1600, 1680, 338, 388, 11, 18);
    }
}
