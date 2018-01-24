package xf.utility;

import com.opensymphony.xwork2.ActionContext;
import net.sf.json.JSONObject;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts2.ServletActionContext;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public class JsonUtil {
    public static final Log log = LogFactory.getLog(JsonUtil.class);
    /**
     * @title: writeJson
     * @description: 以JSON形式输出内容
     */
    public static void writeJson(Object out) {
        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        response.setCharacterEncoding("UTF-8");
        try {
            response.getWriter().print(JSONObject.fromObject(out));
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }

    public static void writeOut(Object obj) {
        HttpServletResponse response = (HttpServletResponse) ActionContext.getContext().get(ServletActionContext.HTTP_RESPONSE);
        response.setCharacterEncoding("UTF-8");
        try {
            response.getWriter().print(obj);
        } catch (IOException e) {
            log.error(e.getMessage());
        }
    }
}
