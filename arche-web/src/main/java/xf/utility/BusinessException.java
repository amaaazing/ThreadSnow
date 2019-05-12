package xf.utility;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;

public class BusinessException extends RuntimeException {

    /** 默认-1代表系统异常 */
    public static final int CODE_ERROR = -1;

    public static final String MESSAGE_ERROR = "系统繁忙";

    /** 异常码,默认-1 */
    private int code = CODE_ERROR;

    /** 异常提示信息,默认系统繁忙 */
    private String message = MESSAGE_ERROR;

    /** 异常，可选 */
    private Throwable throwable;

    public BusinessException() {
    }

    public BusinessException(int code, String message) {
        this.code = code;
        this.message = message;
    }

    public BusinessException(int code, String message, Throwable throwable) {
        this.code = code;
        this.message = message;
        this.throwable = throwable;
    }

    public static BusinessException valueOf(String message) {
        return new BusinessException(CODE_ERROR, message);
    }

    public static BusinessException valueOf(int code, String message) {
        return new BusinessException(code, message);
    }

    public static BusinessException valueOf(String message, Throwable throwable) {
        return new BusinessException(CODE_ERROR, message, throwable);
    }

    public static BusinessException valueOf(int code, String message, Throwable throwable) {
        return new BusinessException(code, message, throwable);
    }

    /**
     * @return
     * 异常码
     */
    public int getCode() {
        return code;
    }

    /**
     */
    public void setCode(int code) {
        this.code = code;
    }

    /**
     * @return
     * 异常提示信息
     */
    public String getMessage() {
        return message;
    }

    /**
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return
     * 异常，可选
     */
    public Throwable getThrowable() {
        return throwable;
    }

    /**
     */
    public void setThrowable(Throwable throwable) {
        this.throwable = throwable;
    }

    /**
     * @title: getThrowableStack
     * @description: 获取异常字符串
     * @return
     */
    public static String getThrowableStack(Throwable t) throws IOException {
        if (t == null)
            return null;
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        try {
            t.printStackTrace(new PrintStream(baos));
        } finally {
            baos.close();
        }
        return baos.toString();

    }

    /**
     * @title: getThrowableStack
     * @description: 获取异常长度
     * @param e
     * @param length
     * @return
     * @throws IOException
     */
    public static String getThrowableStack(Throwable e, int length) throws IOException {
        String errMsg = getThrowableStack(e);
        return ", detailMessage = [\n" + errMsg.substring(0, errMsg.length() > length ? length : errMsg.length())
                + "...]";
    }

    /* 可以借鉴
    @Override
    public String getMessage() {
        String message=super.getMessage();
        String invokeClientInfo = BusystockUtil.getInvokeClientInfo();
        if (StringUtils.isNotBlank(invokeClientInfo)) {
            message = invokeClientInfo + message;
        }
        return message;
    }*/
}
