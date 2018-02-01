package xf.utility;

import org.apache.commons.lang3.StringUtils;

import java.net.InetAddress;
import java.net.NetworkInterface;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Enumeration;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public final class BizUtil {
    /**
     * @title: isExistNull
     * @description: 判断是否存在null
     * @param args
     * @return
     */
    public static boolean isExistNull(Object... args) {
        for (Object arg : args) {
            if (arg == null) {
                return true;
            }
        }
        return false;
    }

    /**
     * @title: isExistEmpty
     * @description: 判断是否存在空，String类型包括""
     * @param args
     * @return
     */
    public static boolean isExistEmpty(Object... args) {
        for (Object arg : args) {
            if (arg == null || (arg.getClass().equals(String.class) && StringUtils.isEmpty((String) arg))) {
                return true;
            }
        }
        return false;
    }

    /**
     * @title: isExistBlank
     * @description: 判断是否存在空，String类型包括""、" "
     * @param args
     * @return
     */
    public static boolean isExistBlank(Object... args) {
        for (Object arg : args) {
            if (arg == null || (arg.getClass().equals(String.class) && StringUtils.isBlank((String) arg))) {
                return true;
            }
        }
        return false;
    }

    /**
     * @title: isAllNull
     * @description: 判断参数是否全部为null
     * @param args
     * @return
     */
    public static boolean isAllNull(Object... args) {
        boolean result = true;
        for (Object arg : args) {
            result = result && (null == arg);
        }
        return result;
    }

    /**
     * @title: isAllEmpty
     * @description: 判断是否全部为空，String类型包括""
     * @param args
     * @return
     */
    public static boolean isAllEmpty(Object... args) {
        boolean result = true;
        for (Object arg : args) {
            result = result && (null == arg || (arg.getClass().equals(String.class) && StringUtils.isEmpty((String) arg)));
        }
        return result;
    }

    /**
     * @title: isAllBlank
     * @description: 判断是否全部为空，String类型包括""、" "
     * @param args
     * @return
     */
    public static boolean isAllBlank(Object... args) {
        boolean result = true;
        for (Object arg : args) {
            result = result && (null == arg || (arg.getClass().equals(String.class) && StringUtils.isBlank((String) arg)));
        }
        return result;
    }

    /**
     * @title: mapToList
     * @description: Map 转 List
     * @param <K> 泛型 key
     * @param <V> 泛型 value
     * @param map
     * @return
     */
    public static <K, V> List<V> mapToList(Map<K, V> map) {
        List<V> list = new ArrayList<V>();
        Iterator<Entry<K, V>> iter = map.entrySet().iterator();
        while (iter.hasNext()) {
            Entry<K, V> entry = iter.next();
            list.add(entry.getValue());
        }
        return list;
    }

    /**
     * @title: genMsg
     * @description: 生成提示信息
     * @param msgs
     * @return
     */
    public static String genMsg(String... msgs) {
        if (msgs == null || msgs.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(" parameters = [");
        for (String msg : msgs) {
            sb.append(msg).append(", ");
        }
        sb.delete(sb.length() - 2, sb.length()).append("]");
        return sb.toString();
    }

    /**
     * @title: getThrowableStack
     * @description: 获取异常堆栈信息
     * @param e
     * @return
     */
    public static String getThrowableStack(Throwable e) {
        StringBuilder sb = new StringBuilder(e.toString()).append("\n");
        StackTraceElement[] stackTraceElements = e.getStackTrace();
        for (int index = 0; index < stackTraceElements.length; index++) {
            sb.append("at [" + stackTraceElements[index].getClassName() + ",");
            sb.append(stackTraceElements[index].getFileName() + ",");
            sb.append(stackTraceElements[index].getMethodName() + ",");
            sb.append(stackTraceElements[index].getLineNumber() + "]\n");
        }
        return sb.toString();
    }

    /**
     * @title: getThrowableStack
     * @description: 根据length获取异常堆栈信息
     * @param e
     * @param length
     * @return
     */
    public static String getThrowableStack(Throwable e, int length) {
        String errMsg = getThrowableStack(e);
        return ", detailMessage = [\n" + errMsg.substring(0, errMsg.length() > length ? length : errMsg.length()) + "...]";
    }

//    public static Date getFormatDate(Date someDate, String format) throws BaseException {
//        if (someDate == null) {
//            return null;
//        }
//        SimpleDateFormat simpledateformat = new SimpleDateFormat(format);
//        String s = simpledateformat.format(someDate).toString();
//        try {
//            return simpledateformat.parse(s);
//        } catch (ParseException e) {
//            throw new BaseException(RetDict.PRASE_DATE_CODE, RetDict.PRASE_DATE_MSG);
//        }
//    }

//    public static Date getFormatDate(Date someDate) throws BaseException {
//        return getFormatDate(someDate, "yyyy-MM-dd");
//    }

    public static String getFormatDateString(Date someDate, String format) {
        if (someDate == null) {
            return null;
        }
        if (StringUtils.isBlank(format)) {
            format = "yyyy-MM-dd";
        }
        SimpleDateFormat simpledateformat = new SimpleDateFormat(format);
        return simpledateformat.format(someDate).toString();
    }


//    public static Date parseDate(String date, String format) throws BaseException {
//        if (StringUtils.isBlank(date)) {
//            return null;
//        }
//        if (StringUtils.isBlank(format)) {
//            format = "yyyy-MM-dd";
//        }
//        SimpleDateFormat simpledateformat = new SimpleDateFormat(format);
//        try {
//            return simpledateformat.parse(date);
//        } catch (ParseException e) {
//            throw new BaseException(RetDict.PRASE_DATE_CODE, RetDict.PRASE_DATE_MSG);
//        }
//    }

    /**
     * 返回本地IP
     *
     * @return
     */
    public static String getLocalIpAddr() {
        String ipAddr = "0.0.0.0";
        try {
            InetAddress ip = null;
            // 根据网卡获取IP
            Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
            while (interfaces.hasMoreElements()) {
                NetworkInterface ni = (NetworkInterface) interfaces.nextElement();
                Enumeration<InetAddress> inetAddresses = ni.getInetAddresses();
                while (inetAddresses.hasMoreElements()) {
                    InetAddress inetAddress = inetAddresses.nextElement();
                    if (!inetAddress.isLoopbackAddress()) {
                        ip = inetAddress;
                    }
                }
            }
            if (ip == null) {
                ip = InetAddress.getLocalHost();
            }
            ipAddr = ip.getHostAddress();
        } catch (Exception err) {
        }
        return ipAddr;
    }

    private BizUtil() {
    }
}
