package xf.utility;

import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class NumberUtils {

    /**
     * 精确加法
     * @param v1
     * @param v2
     * @return
     */
    public static double add(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.add(b2).doubleValue();
    }
    /**
     * 精确减法
     * @param v1
     * @param v2
     * @return
     */
    public static double sub(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.subtract(b2).doubleValue();
    }
    /**
     * 精确乘法
     * @param v1
     * @param v2
     * @return
     */
    public static double mul(double v1,double v2){
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.multiply(b2).doubleValue();
    }
    /**
     * 精确除法
     * @param v1
     * @param v2
     * @param scale
     * @return
     */
    public static BigDecimal div(double v1, double v2, int scale)
    {
        if (scale < 0)
        {
            throw new IllegalArgumentException("The scale must be a positive integer or zero");
        }
        BigDecimal b1 = new BigDecimal(Double.toString(v1));
        BigDecimal b2 = new BigDecimal(Double.toString(v2));
        return b1.divide(b2, scale, BigDecimal.ROUND_HALF_UP);
    }

    /**
     * 提供精确的小数位四舍五入处理。
     * @param v 需要四舍五入的数字
     * @param scale 小数点后保留几位
     * @return 四舍五入后的结果
     */
    public static double round(double v, int scale)
    {
        if (scale < 0)
        {
            throw new IllegalArgumentException(
                    "The scale must be a positive integer or zero");
        }

        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, scale, BigDecimal.ROUND_HALF_UP).doubleValue();
    }

    /**
     *
     * 非四舍五入取整处理
     * @param v
     * @return
     */
    public static  int roundDown(double v)
    {
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, 0, BigDecimal.ROUND_DOWN).intValue();
    }

    /**
     *
     * 四舍五入取整处理
     * @param v
     * @return
     */
    public static  int roundUp(double v)
    {
        BigDecimal b = new BigDecimal(Double.toString(v));
        BigDecimal one = new BigDecimal("1");
        return b.divide(one, 0, BigDecimal.ROUND_UP).intValue();
    }

    /**
     * 整型数据转换
     * @param intStr
     * @param defaultValue
     * @return
     */
    public static <T extends Number> T parseNumber(String intStr,T defaultValue){
        try {
            if(defaultValue instanceof Integer){
                return (T)Integer.valueOf(intStr);
            }else if(defaultValue instanceof Long){
                return (T)Long.valueOf(intStr);
            }else{
                return (T)new BigDecimal(intStr);
            }
        } catch (Exception e) {}

        return defaultValue;
    }


    public static Double roundDouble(double val, int precision) {
        Double ret = null;
        try {
            double factor = Math.pow(10, precision);
            ret = Math.floor(val * factor + 0.5) / factor;
        } catch (Exception e) {
           // log.error(e.getMessage(), e);
        }

        return ret;
    }

    public static BigDecimal subtract(BigDecimal base, BigDecimal ... subtrahends) {
        for (BigDecimal subtrahend : subtrahends) {
            base = base.subtract(subtrahend);
        }
        return base;
    }

    public static BigDecimal multiply(BigDecimal base, BigDecimal ... multiplicands) {
        for (BigDecimal multiplicand : multiplicands) {
            base = base.multiply(multiplicand);
        }
        return base;
    }

    public static BigDecimal add(BigDecimal base, BigDecimal ... augends) {
        for (BigDecimal augend : augends) {
            base = base.add(augend);
        }
        return base;
    }

    public static Double getPercent(double x, double total) {
        String result;
        double tempReuslt = x/total;
        NumberFormat numFormat = NumberFormat.getInstance();
        numFormat.setMinimumFractionDigits(4);
        result= numFormat.format(tempReuslt);
        return new Double(result);
    }

    public static String format(Date date) {
        return format(date, "yyyy-MM-dd HH:mm:ss");
    }

    public static String format(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }
    /**
     *
     * @Description:IP地址转换为数字。 a.b.c.d 的ip number是：a * 256的3次方 + b * 256的2次方 + c * 256的1次方 + d * 256的0次方
     * @param ipAddress
     * @return
     */
    public static long covertIpToLong(String ipAddress) {
        if (StringUtils.isBlank(ipAddress)) {
            return 0;
        }
        try {
            String[] ipAddressInArray = ipAddress.split("\\.");
            long result = 0;
            for (int i = 0; i < ipAddressInArray.length; i++) {
                int power = 3 - i;
                int ip = Integer.parseInt(ipAddressInArray[i]);
                result += ip * Math.pow(256, power);
            }

            return result;
        } catch (Exception ex) {
          //  log.error(ex.getMessage(), ex);
            return 0;
        }
    }

    /**
     *
     * @Description: 长整形还原成IP
     * @param i
     * @return
     */
    public static String converLongToIp(long i) {
        return ((i >> 24) & 0xFF) + "." + ((i >> 16) & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + (i & 0xFF);
    }
    public static void main(String[] args) {
//    	BigDecimal big1 = new BigDecimal(2.0);
//    	BigDecimal big2 = new BigDecimal(3.0);
//    	BigDecimal big3 = new BigDecimal(5.0);
//    	BigDecimal result;
//    	result = add(big1, big2, big3);
//    	System.out.println(result.doubleValue());
//    	result = subtract(big1, big2, big3);
//    	System.out.println(result.doubleValue());
//    	result = multiply(big1, big2, big3);
//    	System.out.println(result.doubleValue());
        System.out.println(getPercent(10.0, 88.0));
    }


}

