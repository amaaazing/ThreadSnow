package xf.utility;

import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.imageio.ImageIO;
import javax.imageio.ImageReadParam;
import javax.imageio.ImageReader;
import javax.imageio.stream.ImageInputStream;
import java.awt.image.BufferedImage;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.Arrays;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 * @classname: FileUploadUtil
 * @description: 文件上传
 */
public class FileUploadUtil {

    /** 图片上传服务器地址 */
    public static final String UPLOAD_POST_URL = "http://upload.yihaodian.com/upload/UploadAction";
    /** 来源类型 {36:会员图片} */
    public static final String RESOURCE_TYPE_MEMBER = "36";
    /** 图片类型 {36:会员图片} */
    public static final String PIC_TYPE_MEMBER = "36";

    private static final Long ONE_M = 1024*1024L;

    private static String DEFAULT_CUT_PREVFIX = "cut_";
//	private static Boolean DEFAULT_FORCE = false;

    //建临时文件夹，存放剪切后的图片文件
    private static final String DEST_IMG_PATH = ServletActionContext.getServletContext().getRealPath("/")+ File.separator+"tmp"+File.separator;

    /** log4j 日志 */
    static Log log = LogFactory.getLog(FileUploadUtil.class);


    public static String uploadFile(Long endUserId, File picFile, String picName) {
        String imgPath = null;
        Long creatorId = endUserId;
        JSONObject jo = new JSONObject();
        PostMethod method = null;
        try {
            DESPlus jsonKey = new DESPlus(String.valueOf(creatorId));
            DESPlus defaultKey = new DESPlus();
            JSONArray ja = new JSONArray();
            JSONObject jo1 = new JSONObject();
            jo1.put("action", "upload");
            jo1.put("backup", "0");
            jo1.put("name", picName);
            jo1.put("position", "1");
            jo1.put("creator_id", creatorId);
            jo1.put("resource_type", RESOURCE_TYPE_MEMBER);
            jo1.put("resource_id", 0);
            jo1.put("pic_type", PIC_TYPE_MEMBER);
            jo1.put("mc_site_id", 1);
            jo1.put("auth_server", "backend");
            jo1.put("session", ServletActionContext.getRequest().getSession());

            JSONArray jb = new JSONArray();
            JSONObject job1 = new JSONObject();
            job1.put("img_scale", "0");
            job1.put("img_wm", "0");
            job1.put("is_major", "1");

            JSONObject job2 = new JSONObject();
            job2.put("img_scale", "200#200");
            job2.put("img_wm", "0");
            job2.put("is_major", "0");
            jb.put(job1);
            jb.put(job2);
            jo1.put("img_series", jb);
            ja.put(jo1);
            jo.put("items", ja);

            HttpClient client = new HttpClient();
            // 设置相关参数
            client.getHttpConnectionManager().getParams().setConnectionTimeout(3000);
            client.getHttpConnectionManager().getParams().setSoTimeout(10000);

            // 提交url
            String joString = java.net.URLEncoder.encode(jo.toString(), "UTF-8");
            method = new PostMethod(UPLOAD_POST_URL);
            Part[] parts = { new StringPart("upload_request", jsonKey.encrypt(joString)), new StringPart("creator_id", defaultKey.encrypt(String.valueOf(creatorId))),
                    new FilePart(picFile.getName(), picFile) };
            method.setRequestEntity(new MultipartRequestEntity(parts, method.getParams()));
            int statusCode = client.executeMethod(method);
            if (HttpStatus.SC_OK == statusCode) {// sc_ok=200
                String returnInfo = method.getResponseBodyAsString();
                if (returnInfo != null && !returnInfo.equalsIgnoreCase("")) {
                    JSONObject rjo = new JSONObject(returnInfo);
                    JSONArray items = rjo.getJSONArray("upload_response");
                    for (int i = 0; i < items.length(); i++) {
                        JSONObject to = (JSONObject) items.get(i);
                        String result = to.getString("result");
                        if ("success".equals(result)) {
                            JSONArray details = to.getJSONArray("details");
                            JSONObject detail = (JSONObject) details.get(0);
                            // 返回新图片地址
                            imgPath = detail.getString("url");
                        } else {
                            break;
                        }
                    }
                }
            }
            if (picFile.exists()) {
                picFile.delete();
            }
        } catch (Exception e) {
            log.error("uploadFile error: ", e);
        } finally {
            if (method != null) {
                method.releaseConnection();
            }
        }
        return imgPath;
    }

    /**
     * 验证图片格式
     * @param
     * @return
     */
    public static boolean checkJPGAndPNG(String fileName){
        if(StringUtils.isBlank(fileName)){
            return false;
        }
        int imgType=fileName.lastIndexOf(".");
        String imgTypeStr=fileName.substring(imgType);
        if(!imgTypeStr.equalsIgnoreCase(".jpg") && !imgTypeStr.equalsIgnoreCase(".jpeg")&&!imgTypeStr.equalsIgnoreCase(".png")){
            return false;
        }
        return true;
    }
    /**
     * 验证图片大小
     * @param picFile
     * @param maxSize  单位M
     * @return
     */
    public static boolean checkPicSize(File picFile,Long maxSize){
        if(null==picFile){
            return false;
        }
        if(!picFile.exists()){
            return false;
        }
        Long size = maxSize * ONE_M;
        if(size.compareTo(picFile.length())<=0){
            return false;
        }
        return true;
    }



    public static void cutImage(InputStream fis, String srcName, OutputStream output, java.awt.Rectangle rect){
        ImageInputStream iis = null;
        try {
            // ImageIO 支持的图片类型 : [BMP, bmp, jpg, JPG, wbmp, jpeg, png, PNG, JPEG, WBMP, GIF, gif]
            String types = Arrays.toString(ImageIO.getReaderFormatNames()).replace("]", ",");
            String suffix = null;
            // 获取图片后缀
            if(srcName.indexOf(".") > -1) {
                suffix = srcName.substring(srcName.lastIndexOf(".") + 1);
            }// 类型和图片后缀全部小写，然后判断后缀是否合法
            if(suffix == null || types.toLowerCase().indexOf(suffix.toLowerCase()+",") < 0){
                log.error("Sorry, the image suffix is illegal. the standard image suffix is {}." + types);
                return ;
            }
            // 将FileInputStream 转换为ImageInputStream
            iis = ImageIO.createImageInputStream(fis);
            // 根据图片类型获取该种类型的ImageReader
            ImageReader reader = ImageIO.getImageReadersBySuffix(suffix).next();
            reader.setInput(iis,true);
            ImageReadParam param = reader.getDefaultReadParam();
            param.setSourceRegion(rect);
            BufferedImage bi = reader.read(0, param);
            ImageIO.write(bi, suffix, output);
            output.flush();
        } catch (IOException e) {
            throw new RuntimeException("图片裁剪异常",e);
        } finally {
            try {
                if(fis != null) fis.close();
                if(iis != null) iis.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    public static String cutImage(InputStream stream, String srcName,java.awt.Rectangle rect){
        File destImg = new File(DEST_IMG_PATH);
        if(!destImg.exists()){
            destImg.mkdir();
        }
        String cutImgPath = null;
        FileOutputStream fos = null;
        if(destImg.exists()){
            String p = destImg.getPath();
            try {
                if(!destImg.isDirectory()){
                    p = destImg.getParent();
                }
                if(!p.endsWith(File.separator)){
                    p = p + File.separator;
                }
                cutImgPath = p + DEFAULT_CUT_PREVFIX + "_" + srcName;
                fos = new java.io.FileOutputStream(cutImgPath);
                cutImage(stream,srcName,fos, rect);
            } catch (Exception e) {
                log.warn("cutImage has error! info:"+e.getStackTrace()[0].toString());
            }finally{
                try {
                    if(fos != null) {
                        fos.flush();
                        fos.close();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }else {
            log.warn("the dest image folder is not exist.");
        }
        return cutImgPath;
    }

    /**
     * 图片剪切后上传至文件服务器
     * @param fileUrl
     * @param x
     * @param y
     * @param width
     * @param height
     * @param endUserId
     * @param picName
     * @return
     */
    public static String getCutImageUrl(String fileUrl,String picName,Long endUserId, int x, int y, int width, int height){
        InputStream inputStream = null;
        File cropFile = null;
        try {
            inputStream = getRemoteImgInputStream(fileUrl);
            if(null == inputStream){
                return null;
            }

            String localPath =  cutImage(inputStream, picName,new java.awt.Rectangle(x, y, width, height));
            if(StringUtils.isBlank(localPath)){
                return localPath;
            }
            //图片上传成功后删除本地临时文件
            cropFile = new File(localPath);
            return uploadFile(endUserId, cropFile, cropFile.getName());
        } catch (Exception e) {
            return null;
        }finally{
            try {
                if(null != inputStream){
                    inputStream.close();
                }
                if(null != cropFile && cropFile.exists()){
                    cropFile.delete();
                }
            } catch (Exception e) {
            }
        }
    }

    public static InputStream getRemoteImgInputStream(String imgUrl){
        try {
            //new一个URL对象
            URL url = new URL(imgUrl);
            // 打开链接
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            // 设置请求方式为"GET"
            conn.setRequestMethod("GET");
            // 超时响应时间为5秒
            conn.setConnectTimeout(5 * 1000);
            // 通过输入流获取图片数据
            return conn.getInputStream();
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 删除临时剪切图片
     * @param file
     * @return
     */
    public static boolean deleteFromCutPath(String file){
        File f = new File(file);
        if(f.exists()){
            return f.delete();
        }else{
            return true;
        }
    }
}

