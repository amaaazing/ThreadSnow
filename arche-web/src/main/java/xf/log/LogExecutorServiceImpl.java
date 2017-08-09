package xf.log;

import java.io.IOException;
import java.util.Date;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

import org.springframework.util.CollectionUtils;

import xf.utility.CommonUtils;

public class LogExecutorServiceImpl implements LogExecutorService {

    private BackendLogService grouponBackendLogService;

    private RecordLogService grouponRecordLogService;

    private ExecutorService pool;

    public void init() {
        pool = Executors.newFixedThreadPool(10);
    }

    public void close() {
        try {
            if (!pool.awaitTermination(10, TimeUnit.SECONDS)) {
                pool.shutdownNow();
            }
        } catch (InterruptedException e) {
            // log.error("notifyApiWhenOderPayed. threadpool : " + "close." + " message: " + e.getMessage());
        }
    }

    public void executorDBSql(final LogSqlVo logSqlVo) {

        // 创建一个可重用固定线程数的线程池
        pool.execute(new Runnable() {
            @Override
            public void run() {
                saveDBSql(logSqlVo);

            }
        });
    }

    private void saveDBSql(final LogSqlVo logSqlVo) {

        SqlVo sqlVo = SqlCommonUtil.getInstance().getSqlVo(logSqlVo);
        
        if (!CollectionUtils.isEmpty(sqlVo.getIdList())) {
            for (Long id : sqlVo.getIdList()) {
                saveSql(sqlVo, id, logSqlVo.getUuid());
            }
        } else {
        	if (sqlVo.getId() != null) {
                saveSql(sqlVo, (Long) sqlVo.getId(), logSqlVo.getUuid());
            } else {
                saveSql(sqlVo, 0L, logSqlVo.getUuid());
            }
        }
        
        

    }

    private void saveSql(SqlVo sqlVo, Long id, String uuid) {

        /**
         * TODO 团购附属信息表日志错乱BUG
         * 产生原因：
         * logSqlVo.id存入表groupon_backend_log_buss字段operate_id
         * 当为insert时，logSqlVo.id取的ID为groupon_backend_log_buss表ID字段，
         * 实际取值应该为ResutlUtil.getId(tableName)字段对应的值，改值返回到logSqlVo对应的idList属性
         * 根据上下处理的方式，此处应该修改为：
         * 当logSqlVo.idList为空时，取logSqlVo.id
         * 当logSqlVo.idList不为空时，优先取logSqlVo.idList第一条数据
         */
        GrouponBackendLogData grouponBackendLogData = new GrouponBackendLogData();
        grouponBackendLogData.setUuid(uuid);
        grouponBackendLogData.setOperateId(id);
        grouponBackendLogData.setMethodPara(sqlVo.getJsonValue());
        grouponBackendLogData.setOperateDataName(sqlVo.getSqlPar());
        grouponBackendLogData.setOperateDataValue(sqlVo.getParValue());
        grouponBackendLogData.setTableName(sqlVo.getTableName());
        grouponBackendLogData.setOperateType(sqlVo.getType());
        grouponBackendLogData.setSqlStr(sqlVo.getSql());
        grouponBackendLogData.setCreateTime(new Date());
        grouponBackendLogData.setWhereValue(sqlVo.getWhereValue());

        if (!ResutlUtil.checkTable(sqlVo.getTableName())) {
            return;
        }

        // if (StringUtils.isBlank(grouponBackendLogData.getOperateDataName())
        // || StringUtils.isBlank(grouponBackendLogData.getOperateDataValue())) {
        // return;
        // }

        // 先插入日志业务表
        grouponBackendLogService.insertBussLog(grouponBackendLogData);

        grouponBackendLogService.saveGrouponBackendLogData(grouponBackendLogData);

    }

    public void executorActionSql(final BackendLog grouponLog) {
        // 创建一个可重用固定线程数的线程池
        pool.execute(new Runnable() {
            @Override
            public void run() {
                saveActionSql(grouponLog);

            }
        });
    }

    public void executorLog(final RecordLog grouponRecordLog) {
        // 创建一个可重用固定线程数的线程池
        pool.execute(new Runnable() {
            @Override
            public void run() {
                saveRecordLog(grouponRecordLog);

            }
        });
    }

    /**
     * 
     * @Description: 保存日志
     * @param grouponRecordLog
     */
    private void saveRecordLog(RecordLog grouponRecordLog) {

        //String poolName = YccGlobalPropertyConfigurer.getMainPoolId();

        String clientIp = CommonUtils.getLocalIpAddr();

        if (grouponRecordLog.getThrowable() != null) {
            try {
                String exceptionInfo = GrouponLogCommonUtil.getThrowableStack(grouponRecordLog.getThrowable());
                grouponRecordLog.setExceptionInfo(exceptionInfo);
            } catch (IOException e) {
                // log.debug(arg0)
            }
        }

        grouponRecordLog.setClientIp(clientIp);
        //grouponRecordLog.setInvokeBy(poolName);
        grouponRecordLog.setExtraInfo(grouponRecordLog.getClazz().getName());

        grouponRecordLogService.saveGrouponRecordLog(grouponRecordLog);
    }

    private void saveActionSql(BackendLog grouponLog) {
//        grouponBackendLogService.saveGrouponBackendLog(grouponLog);
    }

    public void setGrouponBackendLogService(BackendLogService grouponBackendLogService) {
        this.grouponBackendLogService = grouponBackendLogService;
    }

    public void setGrouponRecordLogService(RecordLogService grouponRecordLogService) {
        this.grouponRecordLogService = grouponRecordLogService;
    }


}
