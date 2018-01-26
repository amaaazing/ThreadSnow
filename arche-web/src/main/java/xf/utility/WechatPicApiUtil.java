package xf.utility;

import com.alibaba.fastjson.JSONObject;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;
import sun.misc.BASE64Decoder;

import javax.net.ssl.HttpsURLConnection;
import java.io.DataOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;

public class WechatPicApiUtil {

    private static Logger LOG = Logger.getLogger(WechatPicApiUtil.class);

    private static final String API_URL = null;

    private static final String API_KEY = null;

    //连接超时时间
    private static final Integer CONNECTION_TIMEOUT;
    //处理超时时间
    private static final Integer SO_TIMEOUT;

    //山姆图片服务器gg了默认显示（防止返回为null，又提示顾客需要上传）
    private static final String CONNECTION_ERROR_PIC_URL = "http://vip.samsclub.cn/statics-gen/samclub/images/passport/logo.png";

    static {
//        API_URL = YccGlobalPropertyConfigurer.getPropertyByKey("vip_config.properties", "wechat_photo_api_url");
//        API_KEY = YccGlobalPropertyConfigurer.getPropertyByKey("vip_config.properties", "wechat_photo_api_key");
//
//        String connectionTimeOut = YccGlobalPropertyConfigurer.getPropertyByKey("vip_config.properties", "wechat_photo_api_connection_time_out");
//        String soTimeOut = YccGlobalPropertyConfigurer.getPropertyByKey("vip_config.properties", "wechat_photo_api_socket_time_out");
        String connectionTimeOut = null;
        String soTimeOut = null;
        if (StringUtils.isBlank(connectionTimeOut)) {
            CONNECTION_TIMEOUT = 5000;
        } else {
            CONNECTION_TIMEOUT = Integer.valueOf(connectionTimeOut);
        }

        if (StringUtils.isBlank(soTimeOut)) {
            SO_TIMEOUT = 5000;
        } else {
            SO_TIMEOUT = Integer.valueOf(soTimeOut);
        }
    }

    /**
     * 头像查询
     *
     * @param samCardNo
     * @return
     */
    public static String queryPic(String samCardNo) {

        InputStream reponseInputStream = null;

        try {
            URL url = new URL(API_URL + samCardNo);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setSSLSocketFactory(new TLSSocketConnectionFactory());
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setReadTimeout(SO_TIMEOUT);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("GET");
            conn.setRequestProperty("Authorization", API_KEY);

            int statusCode = conn.getResponseCode();

            if (statusCode < HttpsURLConnection.HTTP_BAD_REQUEST) {
                reponseInputStream = conn.getInputStream();
            } else {
                reponseInputStream = conn.getErrorStream();
            }

            String res = IOUtils.toString(reponseInputStream);

            JSONObject jsonObject = JSONObject.parseObject(res);

            if (200 == conn.getResponseCode() && jsonObject.getJSONObject("data") != null) {
                return String.valueOf(jsonObject.getJSONObject("data").get("url"));
            }
            LOG.warn("查询头像失败: samcardno=" + samCardNo + ",response=" + jsonObject.toJSONString());
        } catch (Exception e) {
            LOG.error("调用微信API查询头像异常，samCardNo=" + samCardNo, e);
            return CONNECTION_ERROR_PIC_URL;
        } finally {
            IOUtils.closeQuietly(reponseInputStream);
        }
        return null;
    }

    /**
     * 头像删除
     *
     * @param samCardNo
     * @return
     */
    public static boolean deletePic(String samCardNo) {

        InputStream reponseInputStream = null;

        try {
            URL url = new URL(API_URL + samCardNo);
            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
            conn.setSSLSocketFactory(new TLSSocketConnectionFactory());
            conn.setConnectTimeout(CONNECTION_TIMEOUT);
            conn.setReadTimeout(SO_TIMEOUT);
            conn.setDoOutput(true);
            conn.setDoInput(true);
            conn.setUseCaches(false);
            conn.setRequestMethod("DELETE");
            conn.setRequestProperty("Authorization", API_KEY);

            int statusCode = conn.getResponseCode();

            if (statusCode < HttpsURLConnection.HTTP_BAD_REQUEST) {
                reponseInputStream = conn.getInputStream();
            } else {
                reponseInputStream = conn.getErrorStream();
            }

            String res = IOUtils.toString(reponseInputStream);

            JSONObject jsonObject = JSONObject.parseObject(res);

            if (200 == conn.getResponseCode()) {
                return true;
            }

            LOG.error("删除头像失败: samcardno=" + samCardNo + ",response=" + jsonObject.toJSONString());
        } catch (Exception e) {
            LOG.error("调用微信API删除头像失败,samCardNo=" + samCardNo, e);
        } finally {
            IOUtils.closeQuietly(reponseInputStream);
        }
        return false;
    }

    /**
     * 头像上传
     * 选用HttpsURLConnection是为了支持jdk1.6+TLS1.2
     *
     * @param samClubUploadPicIn
     * @return
     */
//    public static String upLoadPic(SamClubUploadPicIn samClubUploadPicIn) {
//
//        OutputStream out = null;
//
//        try {
//            String BOUNDARY = "---------------------------" + UUID.randomUUID().toString().replace("-", "");
//            URL url = new URL(API_URL + samClubUploadPicIn.getSamCardNo());
//            HttpsURLConnection conn = (HttpsURLConnection) url.openConnection();
//            conn.setSSLSocketFactory(new TLSSocketConnectionFactory());
//            conn.setConnectTimeout(CONNECTION_TIMEOUT);
//            conn.setReadTimeout(SO_TIMEOUT);
//            conn.setDoOutput(true);
//            conn.setDoInput(true);
//            conn.setUseCaches(false);
//            conn.setRequestMethod("POST");
//            conn.setRequestProperty("Authorization", API_KEY);
//            conn.setRequestProperty("Content-Type", "multipart/form-data; boundary=" + BOUNDARY);
//            out = new DataOutputStream(conn.getOutputStream());
//
//            StringBuffer strBuf = new StringBuffer();
//            strBuf.append("\r\n").append("--").append(BOUNDARY).append("\r\n");
//            String fileName = samClubUploadPicIn.getSamCardNo() + "." + samClubUploadPicIn.getRealFileType();
//            strBuf.append("Content-Disposition: form-data; name=\"avatar\"; filename=\"" + fileName + "\"\r\n");
//            strBuf.append("Content-Type:" + samClubUploadPicIn.getContentType() + "\r\n\r\n");
//
//            IOUtils.write(strBuf.toString().getBytes(), out);
//            IOUtils.write(new BASE64Decoder().decodeBuffer(samClubUploadPicIn.getPicStr()),out);
//            IOUtils.write(("\r\n--" + BOUNDARY + "--\r\n").getBytes(),out);
//
//            out.flush();
//
//            int statusCode = conn.getResponseCode();
//            InputStream reponseInputStream = null;
//
//            if (statusCode < HttpsURLConnection.HTTP_BAD_REQUEST) {
//                reponseInputStream = conn.getInputStream();
//            } else {
//                reponseInputStream = conn.getErrorStream();
//            }
//
//            String res = IOUtils.toString(reponseInputStream);
//
//            JSONObject jsonObject = JSONObject.parseObject(res);
//
//            if (200 == statusCode && jsonObject.getJSONObject("data") != null) {
//                return String.valueOf(jsonObject.getJSONObject("data").get("url"));
//            }
//
//            if (jsonObject.getJSONObject("error") != null && jsonObject.getJSONObject("error").getInteger("code") != null &&
//                    jsonObject.getJSONObject("error").getInteger("code") == 1004) {
//                throw new Exception("重复上传");
//            }
//
//            LOG.error("上传头像失败: samcardno=" + samClubUploadPicIn.getSamCardNo() + ",response=" + jsonObject.toJSONString());
//        } catch (Exception e) {
//            LOG.error("调用微信API上传头像失败,samcardno=" + samClubUploadPicIn.getSamCardNo(), e);
//        } finally {
//            IOUtils.closeQuietly(out);
//        }
//        return null;
//    }

}







