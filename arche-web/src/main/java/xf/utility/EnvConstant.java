package xf.utility;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class EnvConstant {

    private static final Log log      = LogFactory.getLog(EnvConstant.class);
    private static Properties prop_env = null;
    private final static String STG      = "staging";

    static {
        loadPropertiesFile(System.getProperty("global.config.path") + "/env.properties");
        prop_env = loadPropertiesFile(System.getProperty("global.config.path") + "/env.ini");
    }

    public static Properties loadPropertiesFile(String fileName) {
        Properties props = new Properties();
        InputStream is = null;
        try {
            File file = new File(fileName);
            if (!file.exists()) {
                is = EnvConstant.class.getResourceAsStream(fileName);
            } else {
                is = new FileInputStream(file);
            }
            if (is != null) {
                props.load(is);
            }
        } catch (IOException ex) {
            log.error("Load properties is error");

        } finally {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }
        }
        return props;
    }

    public static String getEnv() {
        String env = null;
        if (null == prop_env) {
            env = "prod";
        } else {
            env = prop_env.getProperty("env");
            if (null == env || env.trim().length() == 0) {
                env = "prod";
            }
            if (STG.equals(env.trim())) {
                env = "staging";
            }else if ("prod".equals(env.trim())){
                env = "prod";
            }else if ("base".equals(env.trim())){
                env = "base";
            }
        }
        return env;
    }

    public static String getProperty(String flag){
        return (String) prop_env.get(flag);
    }
}

