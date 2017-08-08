package xf.log;

import java.lang.reflect.Method;
import java.text.MessageFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;
import org.apache.log4j.Logger;
import org.apache.struts2.ServletActionContext;

import xf.utility.CommonUtils;




public class LogAspect implements MethodInterceptor{

    private final static Logger log = Logger.getLogger(LogAspect.class);

    public Object invoke(MethodInvocation invocation) throws Throwable {

        if (!invocation.getMethod().isAnnotationPresent(LogAnnotation.class)) {
            return invocation.proceed();
        }

        Object result = null;
        Exception e1 = null;
        String uuid = null;
        
//        String uuid = ThreadLocalUtil.generateUUID();
//        
//        ThreadLocalUtil.setUuid(uuid);

        try {
            result = invocation.proceed();

        } catch (Exception caughtThrowable) {
            e1 = caughtThrowable;
            throw caughtThrowable;
        } finally {
            generatorLog(invocation, e1, uuid);
        }

        return result;
    }

    /**
     * 记录action的操作日志
     */
    private void generatorLog(MethodInvocation invocation, Exception e1, String uuid) {
        try {
            Method method = invocation.getMethod();
            HttpServletRequest request = ServletActionContext.getRequest();
            Long userId = null;
            StringBuffer sbData = new StringBuffer();
            // 代表是接口，需要获取操作人跟操作记录
            if (request == null) {
                //request = HessianContext.getRequest();
                Object obj[] = invocation.getArguments();
//                for (Object object : obj) {
//                    sbData.append(ReflectUtil.getFieldValues(object)).append(";");
//                }
                userId = 1L;

            } else {
                //userId = CtxSessionBag.getSessionBag().getBackOperator().getId();
            }
            if (request == null) {
                return;
            }
            sbData.append(request.getRequestURL()).append("?");
            Map map = request.getParameterMap();
            Set keSet = map.entrySet();
            Integer secBussType = 0; 
            Integer businessType = 1;
           
            for (Iterator itr = keSet.iterator(); itr.hasNext();) {
                Map.Entry me = (Map.Entry) itr.next();
                String key = (String) me.getKey();
                if ("secBussType".equals(key)) {
                    secBussType = Integer.valueOf(request.getParameter(key));
                }
                if("businessType".equals(key)){
                    businessType = Integer.valueOf(request.getParameter(key));
               }
                key = key.replace("grouponBrand.", "").replace("grouponActivity.", "");
                String value = request.getParameter(key);
                sbData.append(key).append(":").append(value).append(",");
            }
            LogAnnotation alog = method.getAnnotation(LogAnnotation.class);
            BackendLog grouponLog = new BackendLog();
            if (alog == null) {
                grouponLog.setOperateType(0);
                grouponLog.setComment("没有注解记录," + method.getName());
                grouponLog.setBussType(0);
            } else {
                try{
                	grouponLog.setComment(MessageFormat.format(alog.comment(), "团"));                   
                }catch (Exception e) {
                    grouponLog.setComment(alog.comment());
                }
                grouponLog.setOperateType(alog.operateType());
                grouponLog.setBussType(alog.bussType());
            }
            grouponLog.setCreateTime(new Date());
            grouponLog.setOperateTime(new Date());
            grouponLog.setMethodName(method.getName());
            grouponLog.setOperateData(sbData.toString());
            grouponLog.setUserId(userId);
            grouponLog.setClientIp(CommonUtils.getIpAddr(request));
            grouponLog.setServerIp(CommonUtils.getLocalIpAddr());
            grouponLog.setUuid(uuid);
            grouponLog.setSecBussType(secBussType);
            LogUtils.getInstance().insertActionData(grouponLog);
            
            // 结束后清掉uuid
            // ThreadLocalUtil.removeUuid();
        } catch (Exception e) {
            log.error("",e);
        }

    }
}
