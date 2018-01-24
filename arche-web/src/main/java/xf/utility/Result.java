package xf.utility;

import java.io.Serializable;

/**
 * 页面通用返回对象
 */
public class Result implements Serializable {

    private static final long serialVersionUID = 7816924549748350772L;

    /** 返回码 */
    private int code;

    /** 返回提示信息 */
    private String message;

    /** 返回业务对象 */
    private Object data;

    public Result() {
    }

    public Result(int code, String message) {
        this.code = code;
        this.message = message;
    }


    public int getCode() {
        return code;
    }


    public void setCode(int code) {
        this.code = code;
    }


    public String getMessage() {
        return message;
    }


    public void setMessage(String message) {
        this.message = message;
    }

    public Object getData() {
        return data;
    }

    public void setData(Object data) {
        this.data = data;
    }
}
