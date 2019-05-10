package org.antelop.utils;

import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.List;

/**
 * 文件扫描器
 * 
 * @author Alex
 * 
 */
public class FileScanner
{
    /**
     * 获取指定目录下所有文件
     * 
     * @param root
     *            根目录
     * 
     * @return 指定目录下所有文件列表
     * 
     * @throws Exception
     *             内存不足
     */
    public static List<File> ScanFiles(File root) throws Exception
    {
        List<File> fileInfo = new ArrayList<File>();

        File[] files = root.listFiles(new FileFilter() {
            public boolean accept(File pathname)
            {
                if (pathname.isDirectory() && pathname.isHidden()) {
                    return false;
                }

                if (pathname.isFile() && pathname.isHidden()) {
                    return false;
                }

                return true;
            }
        });

        for (File file : files) {
            if (file.isDirectory()) {
                List<File> ff = ScanFiles(file);
                fileInfo.addAll(ff);
            }
            else {
                fileInfo.add(file);
            }
        }

        return fileInfo;
    }
}