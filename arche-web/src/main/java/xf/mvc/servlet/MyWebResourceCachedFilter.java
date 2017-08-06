package xf.mvc.servlet;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;

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
* @ClassName: WebResourceCachedFilter
* @Description: Web资源缓存过滤器
* 对于页面中很少更新的数据，例如商品分类，为避免每次都要从数据库查询分类数据，
* 因此可把分类数据缓存在内存或文件中，以此来减轻数据库压力，提高系统响应速度。
*/ 
public class MyWebResourceCachedFilter implements Filter {
    /**
    * @Field: map
    *          缓存Web资源的Map容器
    */ 
    private Map<String,byte[]> map = new HashMap<String,byte[]>();
    
    @Override
    public void init(FilterConfig filterConfig) throws ServletException {

    }

    @Override
    public void doFilter(ServletRequest req, ServletResponse resp,
            FilterChain chain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) resp;
        //1.得到用户请求的uri
        String uri = request.getRequestURI();
        //2.看缓存中有没有uri对应的数据
        byte b[] = map.get(uri);
        //3.如果缓存中有，直接拿缓存的数据打给浏览器，程序返回
        if(b!=null){
            //根据字节数组和指定的字符编码构建字符串
            String webResourceHtmlStr = new String(b,response.getCharacterEncoding());
            System.out.println(webResourceHtmlStr);
            response.getOutputStream().write(b);
            return;
        }
        //4.如果缓存没有，让目标资源执行，并捕获目标资源的输出
        BufferResponse myresponse = new BufferResponse(response);
        chain.doFilter(request, myresponse);
        //获取缓冲流中的内容的字节数组
        byte out[] = myresponse.getBuffer();
        //5.把资源的数据以用户请求的uri为关键字保存到缓存中
        map.put(uri, out);
        //6.把数据打给浏览器
        response.getOutputStream().write(out);
    }

    @Override
    public void destroy() {

    }

    class BufferResponse extends HttpServletResponseWrapper{
        private ByteArrayOutputStream bout = new ByteArrayOutputStream();  //捕获输出的缓存
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
                return bout.toByteArray();
            }catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
    }

    class MyServletOutputStream extends ServletOutputStream{
        private ByteArrayOutputStream bout;
        public MyServletOutputStream(ByteArrayOutputStream bout){  //接收数据写到哪里
            this.bout = bout;
        }
        @Override
        public void write(int b) throws IOException {
            bout.write(b);
        }
    }
}
