package xf.utility;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Map;

public class LoginCookieUtil {
    /**
     * 在客户端浏览器中种植登录所需的cookie
     * 1.种植ut、ac、yih_uid、seus到cookie中
     * 2.清除cookie中的ucocode
     *
     * @param request  客户端请求对象
     * @param response 服务端响应对象
     * @param map                 待种植的cookie集合
     */
    public static void createLoginCookie(HttpServletRequest request, HttpServletResponse response, Map<String, Object> map) {
        int utDefaultDeadLine = -1;                       //ut默认过期时间，浏览器关闭即过期
        int uidDefaultDeadLine = 3 * 30 * 24 * 60 * 60;   //uid默认过期时间
        int acDefaultDeadLine = 3 * 30 * 24 * 60 * 60;    //ac默认过期时间
        int seusDefaultDeadLine = -1;                     //seus默认过期时间，浏览器关闭即过期

        String uid = ((Long)map.get("uid")).toString();  //用户ID
        String ut = (String) map.get("ut");    //登录令牌
        String ac = (String) map.get("ac");    //登录名
        String seus = (String) map.get("seus");//标记ut存在的值

        Integer uidDeadLine = (Integer) map.get("uidDeadLine");   //uid过期时间
        Integer utDeadLine = (Integer) map.get("utDeadLine");     //ut过期时间
        Integer acDeadLine = (Integer) map.get("acDeadLine");     //ac过期时间
        Integer seusDeadLine = (Integer) map.get("seusDeadLine"); //seus过期时间

        uidDeadLine = uidDeadLine == null ? uidDefaultDeadLine : uidDeadLine;
        utDeadLine = utDeadLine == null ? utDefaultDeadLine : utDeadLine;
        acDeadLine = acDeadLine == null ? acDefaultDeadLine : acDeadLine;
        seusDeadLine = seusDeadLine == null ? seusDefaultDeadLine : seusDeadLine;

        String cookieDomain = (String) map.get("cookieDomain");
        Boolean isHttpOnly = (Boolean) map.get("isHttpOnly");  //ut是否设置为httponly。true：是；false：否

        //种植ut、ac、yihaodian_uid、seus到cookie中
        if(isHttpOnly) {
            response.addHeader("Set-Cookie", "ut="+ut+";path=/;domain=.xf.com;httpOnly");
        }else {
            setCookie("ut", ut, cookieDomain, utDeadLine, response);
        }
        setCookie("yih_uid", uid, cookieDomain, uidDefaultDeadLine, response);
        setCookie("ac", ac, cookieDomain, acDefaultDeadLine, response);
        setCookie("seus", seus, cookieDomain, seusDefaultDeadLine, response);

        //清除cookie中的ucocode
        String ucocode = getCookieValue("ucocode", request);
        if(ucocode != null) {
            clearCookie("ucocode", cookieDomain, response);
        }
    }

    /**
     * 获取浏览器里的cookie值
     *
     * @param name
     * @param request
     * @return
     */
    public static String getCookieValue(String name, HttpServletRequest request) {
        Cookie cookies[] = request.getCookies();
        if (cookies == null)
            return null;
        Cookie cookie = null;
        for (int i = 0; i < cookies.length; i++) {
            cookie = cookies[i];
            if (cookie.getName().equalsIgnoreCase(name))
                return cookie.getValue();
        }

        return null;
    }

    /**
     * 向浏览器中种植cookie
     * @param name
     * @param value
     * @param domain
     * @param expire
     * @param response
     */
    public static void setCookie(String name, String value, String domain, int expire, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, value);
        cookie.setDomain(domain);
        cookie.setPath("/");
        if (expire >= 0)
            cookie.setMaxAge(expire);
        response.addCookie(cookie);
    }

    public static void clearCookie(String name, String domain, HttpServletResponse response) {
        setCookie(name, "", domain, 0, response);
    }
}
