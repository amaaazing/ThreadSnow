package xf.mvc.servlet;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import javax.servlet.http.HttpServletResponse;

public class MyEscapeHtmlFilter implements Filter{ // html转义过滤器

	    public void doFilter(ServletRequest req, ServletResponse resp,
	            FilterChain chain) throws IOException, ServletException {
	        
	        HttpServletRequest request = (HttpServletRequest) req;
	        HttpServletResponse response = (HttpServletResponse) resp;

	        MyHtmlRequest myrequest = new MyHtmlRequest(request);
	        // 注意这里：使用的是增强后的myrequest
	        chain.doFilter(myrequest, response);
	        
	    }

	    
	    public void destroy() {
	        
	    }

	    
	    public void init(FilterConfig filterConfig) throws ServletException {
	        
	    }

	/**
	* @ClassName: MyHtmlRequest
	* @Description: 使用Decorator模式包装request对象，实现html标签转义功能
	*
	*/ 
	class MyHtmlRequest extends HttpServletRequestWrapper {

	    private HttpServletRequest request;

	    public MyHtmlRequest(HttpServletRequest request) {
	        super(request);
	        this.request = request;
	    }

	    /* 覆盖需要增强的getParameter方法
	     * @see javax.servlet.ServletRequestWrapper#getParameter(java.lang.String)
	     * 
	     * 覆盖后的getParameter会自动被调用
	     */
	    @Override
	    public String getParameter(String name) {
	        String value = this.request.getParameter(name);
	        if (value == null) {
	            return null;
	        }
	        //调用filter转义value中的html标签
	        return filter(value);
	    }

	    /**
	    * @Method: filter
	    * @Description: 过滤内容中的html标签
	    */ 
	    public String filter(String message) {
	        if (message == null){
	            return null;
	        }
	        char content[] = new char[message.length()];
	        message.getChars(0, message.length(), content, 0);
	        StringBuffer result = new StringBuffer(content.length + 50);
	        for (int i = 0; i < content.length; i++) {
	            switch (content[i]) {
	            case '<':
	                result.append("&lt;");
	                break;
	            case '>':
	                result.append("&gt;");
	                break;
	            case '&':
	                result.append("&amp;");
	                break;
	            case '"':
	                result.append("&quot;");
	                break;
	            default:
	                result.append(content[i]);
	            }
	        }
	        return result.toString();
	    }
	}	
}
