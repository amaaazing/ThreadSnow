package xf.log;

import org.apache.commons.lang3.StringUtils;
import org.apache.log4j.Logger;

public class LogUtils {

    public static final Logger log = Logger.getLogger(LogUtils.class);

    private static LogUtils instance = null;

    private LogExecutorService logExecutorService = null;

    private GrouponCodeConfigService grouponCodeConfigService = null;

    private Class<?> clazz;

    private LogUtils() {
        try {
            logExecutorService = (LogExecutorService) SpringUtil.getBackendBean("logExecutorService");
            grouponCodeConfigService = (GrouponCodeConfigService) SpringUtil.getBackendBean("grouponCodeConfigService");
        } catch (Exception e) {
            log.error("logExecutorService init is error", e);
        }

    }

    public static synchronized LogUtils getInstance() {
        if (instance == null) {
            instance = new LogUtils();
        }
        return instance;
    }

    public static synchronized LogUtils getLog(Class<?> clazz) {
        if (instance == null) {
            instance = new LogUtils();
        }
        instance.setClazz(clazz);
        return instance;
    }

    private String getLogEnable() {
        GrouponCodeConfig grouponCodeConfig = grouponCodeConfigService.getGrouponCodeConfigByKeyAndModule(
                "groupon.log.enable", null);

        // 开关
        if (grouponCodeConfig != null) {
            return grouponCodeConfig.getValue();
        }
        return "0";
    }

    /**
     * 
     * @Description: TODO
     * @param sql
     * @param parameterObject
     * @param id 如果是insert 则为返回的id
     */
    public void insertDB(String sql, ParameterMapping[] paramMappSet, Object parameterObject, Object id, String uuid) {
        if ("0".equals(getLogEnable())) {
            return;
        }
        if (StringUtils.isBlank(uuid)) {
            return;
        }
        LogSqlVo logSqlVo = new LogSqlVo();
        logSqlVo.setSql(sql);
        logSqlVo.setParamMappSet(paramMappSet);
        logSqlVo.setParameterObject(parameterObject);
        logSqlVo.setId(id);
        logSqlVo.setUuid(uuid);
        logExecutorService.executorDBSql(logSqlVo);
    }

    public void insertActionData(BackendLog grouponLog) {
        if ("0".equals(getLogEnable())) {
            return;
        }
        logExecutorService.executorActionSql(grouponLog);
    }

    /******************************* 操作日志 ************************************************/

    public void info(String info) {
        info(info, null);
    }


    public <T> void info(String info, T t) {
        error(info, t, null);
    }

    public void error(String info) {
        error(info, null);
    }

    public void error(String info, Throwable throwable) {
        error(info, throwable, 0);
    }

    public void error(String info, Throwable throwable, int isWarning) {
        error(info, null, throwable, isWarning);
    }

    public <T> void error(String info, T t, Throwable throwable) {
        error(info, t, throwable, 0);
    }

    public <T> void error(String info, T t, Throwable throwable, int isWarning) {
        GrouponRecordLog grouponRecordLog = new GrouponRecordLog();
        
        grouponRecordLog.setContext(info);
        grouponRecordLog.setThrowable(throwable);
        grouponRecordLog.setIsWarning(isWarning);
        saveRecordLog(grouponRecordLog);
    }

    /**
     * 
     * @Description: 保存日志
     * @param grouponRecordLog
     */
    private void saveRecordLog(GrouponRecordLog grouponRecordLog) {
        grouponRecordLog.setClazz(clazz);
        logExecutorService.executorLog(grouponRecordLog);
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

}
