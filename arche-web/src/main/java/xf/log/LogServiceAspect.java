package xf.log;

import com.alibaba.fastjson.JSONObject;
import org.aspectj.lang.ProceedingJoinPoint;
import com.alibaba.fastjson.JSONObject;

import java.util.Date;

public class LogServiceAspect {


    private static Logger logger = LoggerFactory.getLogger(LogServiceAspect.class);

    public Object doAroundMethodCall(ProceedingJoinPoint pjp) throws Throwable {
        Object returnValue = null;
        Throwable caughtThrowable = null;
        long startLong = System.currentTimeMillis();
        Date startTime = new Date();
        String methodName = null;
        Object[] args = null;
        try {
            methodName = pjp.getSignature().getName();
            args = pjp.getArgs();
            returnValue = pjp.proceed();
        } catch (GrouponException e) {
            caughtThrowable = e.getThrowable();
            // throw caughtThrowable;
        } finally {
            try {
                if (args != null && args.length == 1) {
                    GrouponInterfaceLog grouponInterfaceLog = new GrouponInterfaceLog();

                    // 接口处理
                    if (args[0] instanceof GrouponBaseIn) {
                        GrouponBaseIn baseIn = (GrouponBaseIn) args[0];
                        long timeLong = System.currentTimeMillis() - startLong;
//                        if (timeLong >= 1000) {
//                            grouponLog.info("method is " + methodName + ", interface time is " + timeLong,
//                                    baseIn, 1);
//                        }
                        if (null != caughtThrowable) {
                            logger.error("GrouponServiceAspect catched Throwable. Details: {}", caughtThrowable);
                            grouponInterfaceLog.setInvokeBy(baseIn.getPoolName());
                            grouponInterfaceLog.setMethodName(methodName);
                            grouponInterfaceLog.setParameter(baseIn.toString());
                            grouponInterfaceLog.setClientVersion(baseIn.getClientVersion());
                            grouponInterfaceLog.setType(2);
                            grouponInterfaceLog.setExceptionInfo(GrouponException.getThrowableStack(caughtThrowable));
                            grouponInterfaceLog.setStatus(2);
                            grouponInterfaceLog.setTimeLong(timeLong);
                            grouponInterfaceLog.setCreateTime(new Date());
                            grouponInterfaceLog.setEndTime(new Date());
                            grouponInterfaceLog.setClientIp("127.0.0.1");
                            grouponInterfaceLog.setServerIp(CommonUtil.getLocalIpAddr());
                            grouponInterfaceLog.setStartTime(startTime);
                            GrouponInterfaceLogService grouponInterfaceLogService = (GrouponInterfaceLogService) SpringBeanProxy
                                    .getBean("grouponInterfaceLogService");
                            grouponInterfaceLogService.save(grouponInterfaceLog);
                        }
                    } else if (args[0] instanceof GrouponMessageIn) {
                        GrouponMessageIn grouponMessageIn = (GrouponMessageIn) args[0];
                        grouponInterfaceLog.setMethodName(methodName);
                        grouponInterfaceLog.setParameter(grouponMessageIn.getMessage());
                        grouponInterfaceLog.setType(1);
                        grouponInterfaceLog.setStatus(2);
                        grouponInterfaceLog.setTimeLong(System.currentTimeMillis() - startLong);
                        grouponInterfaceLog.setCreateTime(new Date());
                        grouponInterfaceLog.setEndTime(new Date());
                        grouponInterfaceLog.setClientIp("127.0.0.1");
                        grouponInterfaceLog.setServerIp(CommonUtil.getLocalIpAddr());
                        grouponInterfaceLog.setStartTime(startTime);

                        try {
                            JSONObject mq = JSONObject.parseObject(grouponMessageIn.getMessage());
                            grouponInterfaceLog.setOrderCode(mq.getString("orderCode"));
                            grouponInterfaceLog.setEndUserId(mq.getString("endUserId"));
                        } catch (Exception ex) {
                            logger.error(ex.getMessage(), ex);
                        }

                        if (null != caughtThrowable) {
                            logger.error("GrouponServiceAspect catched Throwable. Details: {}", caughtThrowable);
                            grouponInterfaceLog.setExceptionInfo(GrouponException.getThrowableStack(caughtThrowable));
                            GrouponInterfaceLogService grouponInterfaceLogService = (GrouponInterfaceLogService) SpringBeanProxy
                                    .getBean("grouponInterfaceLogService");
                            grouponInterfaceLogService.save(grouponInterfaceLog);
                        }
                    }
                }
            } catch (Exception e) {
                logger.error("grouponInterfaceLogService save is exception", e);
            }

        }

        return returnValue;
    }

}
