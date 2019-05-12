package xf.utility;

import org.apache.commons.lang3.StringUtils;

import java.io.UnsupportedEncodingException;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    public static boolean checkEmail(String email){
        boolean flag;
        String check = "^([a-z0-9A-Z]+[-|\\.]?)+[a-z0-9A-Z]@([a-z0-9A-Z]+(-[a-z0-9A-Z]+)?\\.)+[a-zA-Z]{2,}$";
        Pattern regex = Pattern.compile(check);
        Matcher matcher = regex.matcher(email);
        flag = matcher.matches();
        return flag;
    }


    /**
     * 统计字符串长度：区别汉字、英文和数字（长度6个汉字，2个英文或数字算1个长度）(以英文为单位，中文做2个英文处理)
     * @author
     * @param str
     * @return
     */
    public static int computeStrNum(String str) {
        if(str == null || "".equals(str)) {
            return 0;
        }
        int zhNum = 0;  //中文字符个数
        int enNum = 0;  //英文字符个数
        int digitNum = 0;  //数字字符个数
        int otherNum = 0;

        for (int i = 0; i < str.length(); i++) {
            char c = str.charAt(i);
            if (c >= '0' && c <= '9') {
                digitNum++;
            }else if((c >= 'a' && c<='z') || (c >= 'A' && c<='Z')){
                enNum++;
            }else if(String.valueOf(c).matches("[\u4e00-\u9fa5]")) {  //中文字符统计
                zhNum += 2;  //以英文为单位，中文做2个英文处理
            }else {
                otherNum++;
            }
        }
        int sum = zhNum + enNum + digitNum + otherNum;
        return sum;
    }

    /**
     * 自定义截取字符串:区别汉字、英文和数字（长度6个汉字，2个英文或数字算1个长度）(以英文为单位，中文做2个英文处理)
     * @author
     * @param str
     * @param num 截取的长度:区别汉字、英文和数字（长度6个汉字，2个英文或数字算1个长度）
     * @return
     */
    public static String subStr(String str, int num) {
        if(str == null || "".equals(str)) {
            return "";
        }
        if(num <= 0) {
            return "";
        }
        StringBuilder result = new StringBuilder();
        int count = 0;
        for (int i = 0; i < str.length(); i++) {
            if(count == num) {
                break;
            }
            char c = str.charAt(i);
            if (c >= '0' && c <= '9') {
                count++;
            }else if((c >= 'a' && c<='z') || (c >= 'A' && c<='Z')){
                count++;
            }else if(String.valueOf(c).matches("[\u4e00-\u9fa5]")) {
                count += 2;
            }else {
                count++;
            }
            result.append(c);
        }
        return result.toString();
    }

    /**
     * 根据提供的字符个数截取字符串，如果字符串字符数小于截取的个数则返回全部，如果大于截取个数则超过部分由more代替
     * @author
     * @param str 截取的字符串
     * @param toCount 截取的字符个数
     * @param more 超过部分的替代字符串
     * @return 截取后的字符串，如果字符串字符数小于截取的个数则返回全部，如果大于截取个数则超过部分由more代替
     */
    public static String substring(String str, int toCount,String more)
    {
        int reInt = 0;
        StringBuilder reStr = new StringBuilder();
        if (str == null)
            return "";
        char[] tempChar = str.toCharArray();
        int length =  tempChar.length;
        for (int kk = 0; (kk < length && toCount > reInt); kk++) {
            String s1 = str.valueOf(tempChar[kk]);
            byte[] b;
            try {
                b = s1.getBytes("GBK");
                reInt += b.length;
                reStr.append(tempChar[kk]);
            } catch (UnsupportedEncodingException e) {
              //  log.error(e.getMessage(), e);
            }
        }
        if (toCount == reInt || (toCount == reInt - 1)&&more!=null)
            reStr.append(more);
        return reStr.toString();
    }

    /**
     *
     * @Description: 订单号第7第8位用*隐藏
     * @param code
     * @return
     */
    public static String maskOrderCode(String code) {
        if (StringUtils.isBlank(code)) {
            return code;
        }

        StringBuffer maskedCode = new StringBuffer(code.length());
        return maskedCode.append(StringUtils.substring(code, 0, 6)).append("**").append(StringUtils.substring(code, 8))
                .toString();
    }
    /**
     *
     * @Description: 用户名4位后用* 代替
     * @param
     * @return
     */
    public static String maskUserName(String userName) {
        if (StringUtils.isBlank(userName)) {
            return userName;
        }
        if(userName.length()<4){
            return userName;
        }

        StringBuffer maskedName = new StringBuffer(userName.length());
        return maskedName.append(StringUtils.substring(userName, 0, 4)).append("**")
                .toString();
    }
    private BizUtil() {
    }
}
