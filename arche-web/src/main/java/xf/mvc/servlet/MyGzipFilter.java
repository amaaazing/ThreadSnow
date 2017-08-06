package xf.mvc.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.zip.GZIPOutputStream;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletOutputStream;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

/**
* @ClassName: GzipFilter
* @Description: 压缩过滤器，将web应用中的文本都经过压缩后再输出到浏览器
* response增强案例——压缩响应正文内容
*/ 
public class MyGzipFilter implements Filter {

	/**
	 * 应用HttpServletResponseWrapper对象，压缩响应正文内容。
	 * 
　　具体思路:通过filter向目标页面传递一个自定义的response对象。在自定义的response对象中，重写getOutputStream方法和getWriter方法，
	使目标资源调用此方法输出页面内容时，获得的是我们自定义的ServletOutputStream对象。在我们自定义的ServletOuputStream对象中，
	重写write方法，使写出的数据写出到一个buffer中。当页面完成输出后，在filter中就可得到页面写出的数据，从而我们可以调用GzipOuputStream
	对数据进行压缩后再写出给浏览器，以此完成响应正文件压缩功能。
	 */
    public void doFilter(ServletRequest req, ServletResponse resp,
            FilterChain chain) throws IOException, ServletException {
        
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        
        BufferResponse myresponse = new BufferResponse(response);
        
        chain.doFilter(request, myresponse); 
// ***************************使用Decorator设计模式增强response对象***************************************************
        //拿出缓存中的数据，压缩后再打给浏览器
        byte out[] = myresponse.getBuffer();
        System.out.println("原始大小:" + out.length);
        
        ByteArrayOutputStream bout = new ByteArrayOutputStream();
        //压缩输出流中的数据
        GZIPOutputStream gout = new GZIPOutputStream(bout);
        gout.write(out);
        gout.close();
        
        byte gzip[] = bout.toByteArray();
        System.out.println("压缩后的大小:" + gzip.length);
        
        response.setHeader("content-encoding", "gzip");
        response.setContentLength(gzip.length);
        response.getOutputStream().write(gzip);
    }
    
    public void destroy() {
        
    }

    public void init(FilterConfig filterConfig) throws ServletException {
        
    }
}

class BufferResponse extends HttpServletResponseWrapper{

    private ByteArrayOutputStream bout = new ByteArrayOutputStream();
    private PrintWriter pw;
    private HttpServletResponse response;
    public BufferResponse(HttpServletResponse response) {
        super(response);
        this.response = response;
    }
    @Override
    public ServletOutputStream getOutputStream() throws IOException {
        return new MyServletOutputStream(bout);
    }
    @Override
    public PrintWriter getWriter() throws IOException {
        pw = new PrintWriter(new OutputStreamWriter(bout,this.response.getCharacterEncoding()));
        return pw;
    }
    
    public byte[] getBuffer(){
        try{
            if(pw!=null){
                pw.close();
            }
            if(bout!=null){
                bout.flush();
                return bout.toByteArray();
            }
            
            
            return null;
        }catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

class MyServletOutputStream extends ServletOutputStream{

    private ByteArrayOutputStream bout;
    public MyServletOutputStream(ByteArrayOutputStream bout){
        this.bout = bout;
    }
    
    @Override
    public void write(int b) throws IOException {
        this.bout.write(b);
    }
    
/*   
		<filter>
		    <description>配置压缩过滤器</description>
		    <filter-name>GzipFilter</filter-name>
		    <filter-class>me.gacl.web.filter.GzipFilter</filter-class>
		</filter>
		
		<!--jsp文件的输出的内容都经过压缩过滤器压缩后才输出 -->
		<filter-mapping>
		    <filter-name>GzipFilter</filter-name>
		    <url-pattern>*.jsp</url-pattern>
		    <!-- 配置过滤器的拦截方式-->
		    <!-- 对于在Servlet中通过
		        request.getRequestDispatcher("jsp页面路径").forward(request, response) 
		    方式访问的Jsp页面的要进行拦截 -->
		    <dispatcher>FORWARD</dispatcher>
		    <!--对于直接以URL方式访问的jsp页面进行拦截，过滤器的拦截方式默认就是 REQUEST-->
		    <dispatcher>REQUEST</dispatcher>
		</filter-mapping>
		<!--js文件的输出的内容都经过压缩过滤器压缩后才输出 -->
		<filter-mapping>
		    <filter-name>GzipFilter</filter-name>
		    <url-pattern>*.js</url-pattern>
		</filter-mapping>
		<!--css文件的输出的内容都经过压缩过滤器压缩后才输出 -->
		<filter-mapping>
		    <filter-name>GzipFilter</filter-name>
		    <url-pattern>*.css</url-pattern>
		</filter-mapping>
		<!--html文件的输出的内容都经过压缩过滤器压缩后才输出 -->
		<filter-mapping>
		    <filter-name>GzipFilter</filter-name>
		    <url-pattern>*.html</url-pattern>
		</filter-mapping>
*/
}
