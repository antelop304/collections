package org.antelop.utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.commons.net.ftp.FTP;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPClientConfig;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;


/**
 * FTP连接管理(使用apache commons-net-1.4.1 lib) 016
 */
public class FtpClientUtil
{
    private FTPClient mFtpClient = null;

    // 服务器IP
    private String mServer;

    // 端口
    private Integer mPort;

    // 用户
    private String mUserName;

    // 密码
    private String mUserPassword;

    public String GetServer()
    {
        return mServer;
    }

    public Integer GetPort()
    {
        return mPort;
    }

    public FtpClientUtil(
        String server,
        int port,
        String userName,
        String userPassword)
    {
        mServer = server;
        mPort = port;
        mUserName = userName;
        mUserPassword = userPassword;
    }

    /**
     * 连接到服务器
     */
    public boolean Connect()
    {
        try {
            if (mFtpClient == null)
                mFtpClient = new FTPClient();

            if (mFtpClient.isConnected())
                return true;

            // 连接登录
            mFtpClient.connect(mServer, mPort);
            mFtpClient.login(mUserName, mUserPassword);

            // 检测连接是否成功
            int reply = mFtpClient.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                DisConnect();
//                Tracker.i("FTP server refused connection.");
                return false;
            }

            // 设置上传模式.binally
            mFtpClient.setFileType(FTP.BINARY_FILE_TYPE);

            return true;
        }
        catch (Exception e) {
            DisConnect();
//            Tracker.e(e);
            return false;
        }
    }

    /**
     * 关闭链接
     */
    public void DisConnect()
    {
        try {
            if (mFtpClient != null && mFtpClient.isConnected())
                mFtpClient.disconnect();
        }
        catch (Exception e) {
//            Tracker.e(e);
//            Tracker.e("Close Server Failure:" + mServer + ";port:" + mPort);
        }
    }

    /**
     * 判断目录下文件是否存在
     * 
     * @param filePath
     *            文件路径
     * @param fileName
     *            文件名称
     * 
     * @throws IOException
     */
    public boolean IsFileExist(String filePath, String fileName)
        throws IOException
    {
        Connect();

        ChangeWorkingDirectory(filePath);

        FTPFile[] files = mFtpClient.listFiles();
        for (FTPFile file : files) {
            String f = new String(file.getName().getBytes("ISO-8859-1"));
            if (file.isFile() && f.equals(fileName))
                return true;
        }

        return false;
    }

    /**
     * 从FTP服务器上下载文件并返回下载文件长度
     * @param list
     * filePath
     *           FTP服务器上文件所在路径
     * fileName
     *           FTP服务器上文件名
     * localFullFileName
     *            本地文件全名称
     * @return
     */
    public boolean GetFtpFile(List<Map<String,String>> list)
    {
        if (!Connect())
            return false;

        try {
            for (Iterator<Map<String,String>> iterator = list.iterator(); iterator.hasNext();) {
                Map<String, String> map = iterator.next();
                String fileName = map.get("fileName");
                String filePath = map.get("filePath");
                String localFullFileName = map.get("localFullFileName");

                mFtpClient.enterLocalPassiveMode();
    
                if (!IsFileExist(filePath, fileName)) {
//                    Tracker.i(String.format(
//                        "FTP server (Server:%s,Port:%s) can not find file:%s",
//                        mServer, mPort, ftpFullFileName));
                    return false;
                }
    
                ChangeWorkingDirectory(filePath);
//                Tracker.i("Copying '" + fileName + "' to server,wait a moment!");
                boolean ok = mFtpClient.retrieveFile(new String(
                    fileName.getBytes(), "ISO-8859-1"), new FileOutputStream(
                    localFullFileName));
                if (!ok) {
//                    Tracker
//                        .i("Fail to get file " + localFullFileName + " from " + ftpFullFileName + " and " + mFtpClient
//                            .getReplyString());
                    return ok;
                }
//                Tracker
//                    .i("Success get file " + localFullFileName + " from " + ftpFullFileName + " and " + mFtpClient
//                        .getReplyString());
            }
            mFtpClient.logout();
            return true;
        }
        catch (Exception e) {
//            Tracker.e(e);
            return false;
        }
        finally {
            DisConnect();
        }
    }

    /**
     * 上传文件到FTP服务器
     * 
     * @param localFullFileName
     *            本地文件目录和文件名
     * @param ftpFileName
     *            上传后的文件名
     * @param ftpDirectory
     *            FTP目录如:/path1/pathb2/,如果目录不存在则自动创建目录
     * 
     * @return 是否上传成功
     */
    public boolean PutFileToFtp(
        String localFullFileName,
        String ftpFileName,
        String ftpDirectory)
    {
        if (!Connect())
            return false;

        File srcFile = new File(localFullFileName);
        FileInputStream fis = null;
        boolean flag = false;

        try {
            FTPClientConfig conf = new FTPClientConfig(FTPClientConfig.SYST_NT);
            conf.setServerLanguageCode("zh");
            fis = new FileInputStream(srcFile);
            // 设置被动模式上传文件
            mFtpClient.enterLocalPassiveMode();
            // 设置缓冲区
            mFtpClient.setBufferSize(1024);
            mFtpClient.setControlEncoding("UTF-8");
            // 设置文件类型（二进制）
            mFtpClient.setFileType(FTP.BINARY_FILE_TYPE);

            ChangeWorkingDirectory(ftpDirectory);

            // 上传
            flag = mFtpClient.storeFile(new String(
                ftpFileName.getBytes("GBK"), "ISO-8859-1"), fis);
            if (flag)
//                Tracker
//                    .i("Success put file" + localFullFileName + " to " + ftpDirectory);
//            else
//                Tracker
//                    .i("Failed put file" + localFullFileName + " to " + ftpDirectory);

            fis.close();

            return flag;
        }
        catch (Exception e) {
//            Tracker.e(e);
            return false;
        }
        finally {
            IOUtils.closeQuietly(fis);
            DisConnect();
        }
    }

    /**
     * 切换到工作目录
     * 
     * @param ftpPath
     *            需切换的目录
     * 
     * @return 是否切换成功
     */
    private boolean ChangeWorkingDirectory(String ftpPath)
    {
        if ((mFtpClient == null) || (!mFtpClient.isConnected()))
            return false;

        try {
            StringBuffer sbStr = new StringBuffer(256);
            char[] chars = ftpPath.toCharArray();
            for (int i = 0; i < chars.length; i++) {
                if ('\\' == chars[i])
                    sbStr.append('/');
                else
                    sbStr.append(chars[i]);
            }

            ftpPath = new String(sbStr.toString().getBytes(), "ISO-8859-1");
            mFtpClient.makeDirectory(ftpPath);

            return mFtpClient.changeWorkingDirectory(ftpPath);
        }
        catch (Exception e) {
//            Tracker.e(e);
            return false;
        }
    }
}