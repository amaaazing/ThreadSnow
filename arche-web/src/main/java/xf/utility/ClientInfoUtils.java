package xf.utility;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.InputStream;
import java.util.Properties;

public class ClientInfoUtils {
    private static final Log LOG = LogFactory.getLog(ClientInfoUtils.class);

    /** 默认的客户端名称*/
    private static final String UNKNOWN_CLIENT_APP_NAME="unknown_client_app_name";

    /** 客户端当前的pool名字 */
    private static final String CLIENT_POOL_NAME = null;

    /** 客户端jar包当前的版本号*/
    private static final String CLIENT_APP_VERSION;

    /** 客户端jar包当前POM文件路径*/
    // 此路径可参照maven打包后的路径修改
    private static final String CLIENT_APP_POM_PATH = "/META-INF/maven/com.*.front/passport-service-client/pom.properties";

    static{
        // 初始化 静态变量
        //CLIENT_POOL_NAME = YccGlobalPropertyConfigurer.getMainPoolId();//获取当前客户端的pool名称
        String clientAppVersion = null;
        try {
            InputStream is = ClientInfoUtils.class.getResourceAsStream(CLIENT_APP_POM_PATH);//读取当前jar包的pom文件
            Properties pt = new Properties();
            pt.load(is);
            pt.getProperty("version");
            clientAppVersion = pt.getProperty("version");
        } catch (Exception e) {
            LOG.error(e.getMessage(),e);
        }
        CLIENT_APP_VERSION = clientAppVersion;
    }

    /**
     * 获得当前pool的名称
     */
    public static String getClientPoolName(){
        return StringUtils.isBlank(CLIENT_POOL_NAME)?UNKNOWN_CLIENT_APP_NAME:CLIENT_POOL_NAME;
    }

    /**
     * 获得当前客户端jar包的版本号
     */
    public static String getClientAppVersion(){
        return CLIENT_APP_VERSION;
    }
}
