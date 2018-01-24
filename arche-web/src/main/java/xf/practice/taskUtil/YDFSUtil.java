package xf.practice.taskUtil;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.httpclient.HttpClient;
import org.apache.commons.httpclient.HttpStatus;
import org.apache.commons.httpclient.methods.PostMethod;
import org.apache.commons.httpclient.methods.multipart.FilePart;
import org.apache.commons.httpclient.methods.multipart.MultipartRequestEntity;
import org.apache.commons.httpclient.methods.multipart.Part;
import org.apache.commons.httpclient.methods.multipart.StringPart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpSession;
import java.io.File;

public class YDFSUtil {

    /** 图片上传服务器地址 */
    public static final String UPLOAD_POST_URL = "http://upload.yihaodian.com/upload/UploadAction";
    private static Logger logger          = LoggerFactory.getLogger(YDFSUtil.class);

    /**
     * 文件上传
     *
     * @param picFile 文件
     * @param picName 文件名
     * @param resourceType 资源类型
     * @param picType 文件类型
     * @param resourceId
     * @param httpSession
     * @return
     */
    public static String uploadFile(File picFile, String picName, String resourceType,
                                    String picType, String resourceId, HttpSession httpSession,
                                    Long creatorId) {
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
            jo1.put("resource_type", resourceType);
            jo1.put("resource_id", resourceId);
            jo1.put("pic_type", picType);
            jo1.put("mc_site_id", 1);
            jo1.put("auth_server", "backend");
            jo1.put("session", httpSession);

            JSONArray jb = new JSONArray();
            JSONObject job1 = new JSONObject();
            job1.put("img_scale", "0");
            job1.put("img_wm", "0");
            job1.put("is_major", "1");

            JSONObject job2 = new JSONObject();
            job2.put("img_scale", "200#200");
            job2.put("img_wm", "0");
            job2.put("is_major", "0");
            jb.add(job1);
            jb.add(job2);
            jo1.put("img_series", jb);

            ja.add(jo1);
            jo.put("items", ja);

            HttpClient client = new HttpClient();
            // 设置相关参数
            client.getHttpConnectionManager().getParams().setConnectionTimeout(20000);
            client.getHttpConnectionManager().getParams().setSoTimeout(20000);

            // 提交url
            String joString = java.net.URLEncoder.encode(jo.toString(), "UTF-8");
            method = new PostMethod(UPLOAD_POST_URL);
            Part[] parts = { new StringPart("upload_request", jsonKey.encrypt(joString)),
                    new StringPart("creator_id", defaultKey.encrypt(String.valueOf(creatorId))),
                    new FilePart(picFile.getName(), picFile) };
            method.setRequestEntity(new MultipartRequestEntity(parts, method.getParams()));
            int statusCode = client.executeMethod(method);
            if (HttpStatus.SC_OK == statusCode) {// sc_ok=200
                String returnInfo = method.getResponseBodyAsString();
                if (returnInfo != null && !returnInfo.equalsIgnoreCase("")) {
                    JSONObject rjo = JSONObject.parseObject(returnInfo);
                    JSONArray items = rjo.getJSONArray("upload_response");
                    for (int i = 0; i < items.size();) {
                        JSONObject to = (JSONObject) items.get(i);
                        String result = to.getString("result");
                        if ("success".equals(result)) {
                            JSONArray details = to.getJSONArray("details");
                            JSONObject detail = (JSONObject) details.get(0);
                            return detail.getString("url");
                        } else {
                            break;
                        }
                    }
                } else {
                    logger.error("图片上传至文件服务器失败，HttpStatus:" + statusCode);
                }
            }
        } catch (Exception e) {
            logger.error("上传错误：" + e);
        } finally {
            if (method != null) {
                method.releaseConnection();
            }
        }
        return null;
    }
}

