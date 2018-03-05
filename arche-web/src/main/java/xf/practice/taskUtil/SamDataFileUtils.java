package xf.practice.taskUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONException;
import net.sf.json.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.multipart.MultipartFile;

import javax.crypto.Cipher;
import javax.crypto.CipherInputStream;
import javax.crypto.CipherOutputStream;
import javax.crypto.KeyGenerator;
import javax.servlet.http.HttpServletRequest;
import java.net.URLEncoder;
import java.security.Key;
import java.security.SecureRandom;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @description  sam会员手动同步文件处理相关操作
 * @author hekui
 * @version $Id: SamDataFileUtils.java, v 0.1 2016年8月29日 下午8:34:30  Exp $
 */
public class SamDataFileUtils {

    /*
     * 盐，文件加密和解密用的
     */
    private static final String SALT = "hello,sam!";

    private static Logger log  = LoggerFactory.getLogger(SamDataFileUtils.class);

    /**
     *
     * @description 把文件保存至临时目录
     * @param txtfile txt文件
     * @param taskId taskJob唯一id
     * @param request
     * @return
     * @throws IOException
     */
    public static File saveTemPath(MultipartFile txtfile, String taskId, HttpServletRequest request) throws IOException {

        //获取临时文件
        File temfile = SamDataFileUtils.getSTemFile(request, taskId);
        if (!temfile.exists()) {
            temfile.createNewFile();
        }

        //写入临时文件
        txtfile.transferTo(temfile);
        return temfile;
    }

    /**
     *
     * @description 上传文件到YDFS
     * @param file
     * @param taskId
     * @return
     * @throws Exception
     */
    public static String upload2Ydfs(File file, String taskId) throws Exception {
        File destFile = getUpTemfile(file);
        encrypt(file, destFile, taskId);
        return _upload(destFile);
    }

    public static File download4Ydfs(HttpServletRequest request, String url, String taskId) throws Exception {
        synchronized (taskId.intern()) {
            File file = SamDataFileUtils.getDTemFile(request, taskId);
            if (!file.exists()) {
                _down(file, url);
            }

            File destFile = getUpTemfile(file);

            if (!destFile.exists()) {
                decrypt(file, destFile, taskId);
            }
            return destFile;
        }
    }

    /**
     *
     * @description 获取上传文件临时文件
     * @param request
     * @param taskId
     * @return
     */
    private static File getSTemFile(HttpServletRequest request, String taskId) {

        return getTemFile(request, taskId, "S");
    }

    /**
     *
     * @description 获取下载临时文件
     * @param request
     * @param taskId
     * @return
     */
    private static File getDTemFile(HttpServletRequest request, String taskId) {
        return getTemFile(request, taskId, "D");
    }

    /**
     *
     * @description 获取临时文件
     * @param request
     * @param taskId
     * @param preffix
     * @return
     */
    private static File getTemFile(HttpServletRequest request, String taskId, String preffix) {

        String path = request.getSession().getServletContext().getRealPath("/") + "/samdata/";
        File dicpath = new File(path);
        if (!dicpath.exists()) {
            dicpath.mkdirs();
        }

        //文件名
        String filename = preffix + "_" + taskId + ".txt";

        return new File(dicpath, filename);

    }

    /**
     *
     * @description 获取加密和解密所需盐
     * @param strKey
     * @return
     * @throws Exception
     */
    private static Key getKey(String strKey) throws Exception {
        KeyGenerator _generator = KeyGenerator.getInstance("DES");
        _generator.init(new SecureRandom(strKey.getBytes()));
        return _generator.generateKey();
    }

    /**
     *
     * @description 加密文件
     * @param srcfile 源文件
     * @param destFile 加密后的文件
     * @param taskId
     * @return
     * @throws Exception
     */
    private static void encrypt(File srcfile, File destFile, String taskId) throws Exception {

        String salt = SALT + taskId;

        Cipher cipher = Cipher.getInstance("DES");
        cipher.init(Cipher.ENCRYPT_MODE, getKey(salt));

        InputStream is = null;
        OutputStream out = null;
        CipherInputStream cis = null;
        try {
            is = new FileInputStream(srcfile);
            out = new FileOutputStream(destFile);
            cis = new CipherInputStream(is, cipher);
            byte[] buffer = new byte[1024];
            int r;
            while ((r = cis.read(buffer)) > 0) {
                out.write(buffer, 0, r);
            }
        } catch (Exception e) {
            throw e;
        } finally {

            //别骂我sb  你删掉try catch执行一下sonar就懂了  --hk
            try {
                if (cis != null)
                    cis.close();
            } catch (Exception e) {

            }
            try {
                if (is != null)
                    is.close();
            } catch (Exception e) {

            }

            try {
                if (out != null)
                    out.close();
            } catch (Exception e) {

            }

        }

    }

    /**
     *
     * @description 解密文件
     * @param srcFile 源文件
     * @param destFile 解密后的文件
     * @param taskId
     * @throws Exception
     */
    private static void decrypt(File srcFile, File destFile, String taskId) throws Exception {

        if (!destFile.exists()) {
            destFile.createNewFile();
        }

        String salt = SALT + taskId;

        InputStream is = null;
        OutputStream out = null;
        CipherOutputStream cos = null;
        try {
            Cipher cipher = Cipher.getInstance("DES");
            cipher.init(Cipher.DECRYPT_MODE, getKey(salt));
            is = new FileInputStream(srcFile);
            out = new FileOutputStream(destFile);
            cos = new CipherOutputStream(out, cipher);
            byte[] buffer = new byte[1024];
            int r;
            while ((r = is.read(buffer)) >= 0) {
                cos.write(buffer, 0, r);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            //别骂我sb  你删掉try catch执行一下sonar就懂了  --hk
            try {
                if (cos != null)
                    cos.close();
            } catch (Exception e) {

            }
            try {
                if (is != null)
                    is.close();
            } catch (Exception e) {

            }

            try {
                if (out != null)
                    out.close();
            } catch (Exception e) {

            }
        }

    }

    /**
     *
     * @description 一句话说明这个方法是干什么的
     * @param file
     * @return
     */
    private static File getUpTemfile(File file) {
        File parent = file.getParentFile();
        String filename = "tem_" + file.getName();
        return new File(parent, filename);
    }

    /**
     *
     * @description 下载操作
     * @param destFile
     * @param url
     * @throws Exception
     */
    private static void _down(File destFile, String url) throws Exception {

        InputStream in = null;
        OutputStream out = null;
        try {
            DefaultHttpClient httpClient = new DefaultHttpClient();
            HttpGet httpGet = new HttpGet(url);
            HttpResponse httpResponse = httpClient.execute(httpGet);
            HttpEntity entity = httpResponse.getEntity();
            in = entity.getContent();
            long length = entity.getContentLength();
            if (length <= 0) {
                throw new Exception("文件不存在");
            }
            out = new FileOutputStream(destFile);
            byte[] data = new byte[1024];
            int index = 0;
            while ((index = in.read(data)) != -1) {
                out.write(data, 0, index);
            }
        } catch (Exception e) {
            throw e;
        } finally {
            try {
                if (in != null)
                    in.close();
            } catch (Exception e) {

            }

            try {
                if (out != null)
                    out.close();
            } catch (Exception e) {

            }

        }

    }

    /**
     *
     * @description 上传操作
     * @param f
     * @return
     * @throws Exception
     */
    private static String _upload(File f) throws Exception {

        String name = f.getName();

        String creator = "159538310";

        DESPlus defaultKey = null;
        DESPlus jsonKey = null;
        defaultKey = new DESPlus();
        jsonKey = new DESPlus(creator);

        JSONObject data = new JSONObject();
        try {
            JSONArray items = new JSONArray();

            JSONObject item = new JSONObject();
            item.put("position", "1");
            item.put("mc_site_id", 1);// arg0.getParameter("mcSiteId"));
            item.put("file_or_image", "video");
            item.put("creator_id", creator);
            item.put("resource_id", "1");
            item.put("pic_type", "9");
            item.put("auth_server", "front");
            item.put("session", "myyhd");
            item.put("name", name);
            item.put("resource_type", "9");
            item.put("action", "upload");
            item.put("backup", "1");
            items.add(item);
            data.put("items", items);
        } catch (Exception e) {

        }

        String request = URLEncoder.encode(data.toString(), "UTF-8");
        request = jsonKey.encrypt(request);
        creator = defaultKey.encrypt(creator);

        HttpClient client = new HttpClient();
        //设置相关参数
        client.getHttpConnectionManager().getParams().setConnectionTimeout(20000);
        client.getHttpConnectionManager().getParams().setSoTimeout(100000);

        PostMethod method = null;
        //提交url
        method = new PostMethod(YDFSUtil.UPLOAD_POST_URL);

        Part[] parts = { new StringPart("upload_request", request), new StringPart("creator_id", creator), new FilePart(f.getName(), f) };
        method.setRequestEntity(new MultipartRequestEntity(parts, method.getParams()));

        int statusCode = client.executeMethod(method);

        String url = null;
        if (HttpStatus.SC_OK == statusCode) {// sc_ok=200
            String returnInfo = method.getResponseBodyAsString();
            JSONObject rjo = JSONObject.fromObject(returnInfo);
            JSONArray items = rjo.getJSONArray("upload_response");

            for (int i = 0; i < items.size(); i++) {
                JSONObject to = (JSONObject) items.get(i);

                System.out.println(to.toString());
                String result = to.getString("result");
                if (returnInfo != null
                        && "success".equals(result)
                        && (returnInfo.contains("d11.yihaodianimg.com") || returnInfo.contains("d12.yihaodianimg.com") || returnInfo
                        .contains("d13.yihaodianimg.com"))) {
                    log.error(returnInfo);
                    throw new Exception("返回url含有d11,域名不一致错误");
                }

                if ("success".equals(result)) {
                    url = to.getString("backup_url");
                } else {
                    if (result.startsWith("error-")) {
                        throw new Exception(result);
                    }
                }
            }
        } else {
            throw new Exception("上传文件CODE-" + statusCode);
        }

        if (method != null) {
            method.releaseConnection();
        }

        return url;

    }

}

