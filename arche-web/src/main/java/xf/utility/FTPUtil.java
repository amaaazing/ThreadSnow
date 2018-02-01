package xf.utility;

import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPFile;
import org.apache.commons.net.ftp.FTPReply;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class FTPUtil {


    /**
     * Description: 向FTP服务器上传文件
     * @Version1.0 Jul 27, 2008 4:31:09 PM by 崔红保（cuihongbao@d-heaven.com）创建
     * @param url FTP服务器hostname
     * @param port FTP服务器端口
     * @param username FTP登录账号
     * @param password FTP登录密码
     * @param path FTP服务器保存目录
     * @param filename 上传到FTP服务器上的文件名
     * @param input 输入流
     * @return 成功返回true，否则返回false
     */
    public static boolean uploadFile(String url,String username, String password, int port,String path, String filename, InputStream input) {
        boolean success = false;
        FTPClient ftp = new FTPClient();
        try {
            int reply;
            //可以使用ftp.connect(url,port)的方式直接连接FTP服务器
            ftp.connect(url,port); // 使用默认端口
            ftp.login(username, password);//登录
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return success;
            }
            ftp.changeWorkingDirectory(path);
            success = ftp.storeFile(filename, input);

            input.close();
            ftp.logout();
            success = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return success;
    }


    /**
     * Description: 从FTP服务器下载文件
     * @Version. Jul , :: PM by 崔红保（cuihongbao@d-heaven.com）创建
     * @param url FTP服务器hostname
     * @param port FTP服务器端口
     * @param username FTP登录账号
     * @param password FTP登录密码
     * @param remotePath FTP服务器上的相对路径
     * @param fileName 要下载的文件名  ,当指定文件名为"" 时，表示下载所以文件
     * @param localPath 下载后保存到本地的路径
     * @return
     */
    public static boolean downFile(String url, String username, String password,int port, String remotePath,String fileName,String localPath) {
        boolean success = false;
        FTPClient ftp = new FTPClient();
        try {
            int reply;
            //可以使用ftp.connect(url,port)的方式直接连接FTP服务器
//			ftp.connect(url); // 使用默认端口
            ftp.connect(url,port);
            ftp.login(username, password);//登录
            reply = ftp.getReplyCode();
            if (!FTPReply.isPositiveCompletion(reply)) {
                ftp.disconnect();
                return success;
            }
            ftp.changeWorkingDirectory(remotePath);//转移到FTP服务器目录
            FTPFile[] fs = ftp.listFiles();
            if(fs==null || fs.length ==0){
                return true;
            }
            makeDir(localPath);
            for(FTPFile ff:fs){
                if(fileName=="" || ff.getName().equals(fileName)){
                    File localFile = new File(localPath+ff.getName());
                    OutputStream is = null;
                    try{
                        is = new FileOutputStream(localFile);
                        ftp.retrieveFile(ff.getName(), is);
                        is.close();
                    }catch(Exception e){
                    }finally {
                        if(is!=null){
                            is.close();
                        }
                    }
                }
            }
            ftp.logout();
            success = true;
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (ftp.isConnected()) {
                try {
                    ftp.disconnect();
                } catch (IOException ioe) {
                }
            }
        }
        return success;
    }


    private static boolean makeDir(String dicPath){
        File dic = new File(dicPath);
        boolean flag=false;
        try{
            if(!dic.exists()){
                dic.mkdirs();
            }
            flag=true;
        }catch(Exception e){
            e.printStackTrace();
        }
        return flag;
    }
//
//	public static void main(String[] args){
//		String fileName ="F:\\testLLW\\fromyhd\\20170804\\yhd_20170804_2017080411241115_01.txt";
//        File localFile = new File(fileName);
//		FileInputStream in;
//		try {
//			in = new FileInputStream(localFile);
//			uploadFile("127.0.0.1", "zty", "123456", "/yhd/fromyhd", localFile.getName(), in);
//			try {
//				in.close();
//			} catch (IOException e) {
//				// TODO Auto-generated catch block
//				e.printStackTrace();
//			}
//		} catch (FileNotFoundException e) {
//
//			e.printStackTrace();
//		}
//
//		downFile("127.0.0.1", "zty", "123456", "/yhd/toyhd", "", "F:\\testLLW\\toyhd\\");
//	}
}

