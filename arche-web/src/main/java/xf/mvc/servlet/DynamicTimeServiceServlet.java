package xf.mvc.servlet;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 动态返回服务器时间
 */
public class DynamicTimeServiceServlet extends HttpServlet {

    private static final long serialVersionUID = 8761940403655154350L;

    public DynamicTimeServiceServlet() {
        super();
        // TODO Auto-generated constructor stub
    }

    protected void doGet(HttpServletRequest request,
                         HttpServletResponse response) throws ServletException, IOException {
        response.setCharacterEncoding("UTF-8");
        response.setDateHeader("Expires", 0);
        response.setHeader("Cache-Control", "no-cache");
        response.setHeader("pragma","no-cache");
        OutputStream fos = response.getOutputStream();

        try {
            SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss");
            String nowTime = ft.format(new Date());
            nowTime = "var nowTime=\"" + nowTime + "\"";
            fos.write(nowTime.getBytes());
            fos.close();
        } catch (Throwable e) {
            throw new RuntimeException("error ", e);
        } finally {
            try {
                if (fos != null) {
                    fos.close();
                }
            } catch (IOException e) {
                throw new RuntimeException("close failure", e);
            }

        }


    }

    protected void doPost(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    protected void doDelete(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        super.doDelete(req, resp);
    }

    @Override
    protected void doPut(HttpServletRequest req, HttpServletResponse resp)
            throws ServletException, IOException {
        super.doPut(req, resp);
    }

    @Override
    public void init() throws ServletException {
        super.init();
    }

}

