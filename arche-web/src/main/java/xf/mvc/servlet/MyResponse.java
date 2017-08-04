package xf.mvc.servlet;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * 在开发过程中，如果希望服务器输出什么浏览器就能看到什么，那么在服务器端都要以字符串的形式进行输出。
 * 
 * 
 * @author xinzhenqiu
 *
 */
public class MyResponse extends HttpServlet{

	// HttpServletResponse常见应用——设置响应头控制浏览器的行为
	// response.setHeader("refresh", "5");//设置refresh响应头控制浏览器每隔5秒钟刷新一次
	// 设置http响应头控制浏览器禁止缓存当前文档内容 
	//1 response.setDateHeader("expries", -1);
	//2 response.setHeader("Cache-Control", "no-cache");
	//3 response.setHeader("Pragma", "no-cache");
	
	// 通过response实现请求重定向:
	/**
	 * 
	 * 　　请求重定向指：一个web资源收到客户端请求后，通知客户端去访问另外一个web资源，这称之为请求重定向。

　　应用场景：用户登陆，用户首先访问登录页面，登录成功后，就会跳转到某个页面，这个过程就是一个请求重定向的过程

　　实现方式：response.sendRedirect(String location)，即调用response对象的sendRedirect方法实现请求重定向
　　sendRedirect内部的实现原理：使用response设置302状态码和设置location响应头实现重定向

          * 1.调用sendRedirect方法实现请求重定向,
          * sendRedirect方法内部调用了
          * response.setHeader("Location", "/JavaWeb_HttpServletResponse_Study_20140615/index.jsp");
          * response.setStatus(HttpServletResponse.SC_FOUND);//设置302状态码，等同于response.setStatus(302);
          
         response.sendRedirect("/JavaWeb_HttpServletResponse_Study_20140615/index.jsp");         
         //2.使用response设置302状态码和设置location响应头实现重定向实现请求重定向
         //response.setHeader("Location", "/JavaWeb_HttpServletResponse_Study_20140615/index.jsp");
         //response.setStatus(HttpServletResponse.SC_FOUND);//设置302状态码，等同于response.setStatus(302);
	 * 
	 * 
	 * 
	 */
	
	
	// HttpServletResponse对象常见应用
	
	// 使用OutputStream流向客户端浏览器输出中文数据
	
	// 使用OutputStream流输出中文注意问题：
/*
	在服务器端，数据是以哪个码表输出的，那么就要控制客户端浏览器以相应的码表打开，比如：
	outputStream.write("中国".getBytes("UTF-8"));使用OutputStream流向客户端浏览器输出中文，以UTF-8的编码进行输出，
	此时就要控制客户端浏览器以UTF-8的编码打开，否则显示的时候就会出现中文乱码，那
	么在服务器端如何控制客户端浏览器以以UTF-8的编码显示数据呢？可以通过设置响应头控制浏览器的行为，
	例如：response.setHeader("content- type", "text/html;charset=UTF-8");通过设置响应头控制浏览器以UTF-8的编码显示数据。
*/
	
	public void outputChineseByOutputStream(HttpServletResponse response) throws IOException{
		String data = "中国";
		
		OutputStream outputStream = response.getOutputStream();//获取OutputStream输出流
		response.setHeader("content-type", "text/html;charset=UTF-8");
		
		byte[] dataByteArray = data.getBytes("UTF-8"); //将字符转换成字节数组，指定以UTF-8编码进行转换
		
		outputStream.write(dataByteArray);
		
	}
	
	
	// 使用PrintWriter流向客户端浏览器输出中文数据

	public void outputChineseByPrintWriter(HttpServletResponse response) throws IOException{
		String data = "中国";
		         
		//通过设置响应头控制浏览器以UTF-8的编码显示数据，如果不加这句话，那么浏览器显示的将是乱码
		//response.setHeader("content-type", "text/html;charset=UTF-8");
		         
		response.setCharacterEncoding("UTF-8");//设置将字符以"UTF-8"编码输出到客户端浏览器
		         /**
		          * PrintWriter out = response.getWriter();这句代码必须放在response.setCharacterEncoding("UTF-8");之后
		          * 否则response.setCharacterEncoding("UTF-8")这行代码的设置将无效，浏览器显示的时候还是乱码
		          */
		PrintWriter out = response.getWriter();//获取PrintWriter输出流
		         /**
		          * 多学一招：使用HTML语言里面的<meta>标签来控制浏览器行为，模拟通过设置响应头控制浏览器行为
		          * out.write("<meta http-equiv='content-type' content='text/html;charset=UTF-8'/>");
		          * 等同于response.setHeader("content-type", "text/html;charset=UTF-8");
		          */
		// response.setHeader("content-type", "text/html;charset=UTF-8");
		out.write("<meta http-equiv='content-type' content='text/html;charset=UTF-8'/>");
		out.write(data);//使用PrintWriter流向客户端输出字符
	}	

	
	 // 使用HttpServletResponse对象就可以实现文件的下载
	
	// 文件下载注意事项：编写文件下载功能时推荐使用OutputStream流，避免使用PrintWriter流，
	// 因为OutputStream流是字节流，可以处理任意类型的数据，而PrintWriter流是字符流，只能处理字符数据，
	// 如果用字符流处理字节数据，会导致数据丢失。

/*	文件下载功能的实现思路：

	　　1.获取要下载的文件的绝对路径

	　　2.获取要下载的文件名

	　　3.设置content-disposition响应头控制浏览器以下载的形式打开文件

	　　4.获取要下载的文件输入流

	　　5.创建数据缓冲区

	　　6.通过response对象获取OutputStream流

	　　7.将FileInputStream流写入到buffer缓冲区

	　　8.使用OutputStream将缓冲区的数据输出到客户端浏览器
*/	
	
	private void downloadFileByOutputStream(HttpServletResponse response)
	             throws FileNotFoundException, IOException {
		//1.获取要下载的文件的绝对路径
		String realPath = getServletContext().getRealPath("/download/1.JPG");
		//2.获取要下载的文件名
		String fileName = realPath.substring(realPath.lastIndexOf("\\") + 1);
		//3.设置content-disposition响应头控制浏览器以下载的形式打开文件
		response.setHeader("content-disposition", "attachment;filename="+fileName);
		//4.获取要下载的文件输入流
		InputStream in = new FileInputStream(realPath);
		
		int len = 0;
		//5.创建数据缓冲区
		byte[] buffer = new byte[1024];
		//6.通过response对象获取OutputStream流
		OutputStream out = response.getOutputStream();
		//7.将FileInputStream流写入到buffer缓冲区
		while((len = in.read(buffer)) > 0){
			//8.使用OutputStream将缓冲区的数据输出到客户端浏览器
			out.write(buffer,0,len);
		}
		
		in.close();
	}
	
	// 使用Response实现中文文件下载
	// 中文文件名要使用URLEncoder.encode方法进行编码(URLEncoder.encode(fileName, "字符编码"))，否则会出现文件名乱码。
	// response.setHeader("content-disposition", "attachment;filename="+URLEncoder.encode(fileName, "UTF-8"));
	
	// 生成验证码
	// 生成随机图片用作验证码，主要用到了一个BufferedImage类
    public void doGet(HttpServletRequest request, HttpServletResponse response)
             throws ServletException, IOException {
         
         response.setHeader("refresh", "5");//设置refresh响应头控制浏览器每隔5秒钟刷新一次
         //1.在内存中创建一张图片
         BufferedImage image = new BufferedImage(80, 20, BufferedImage.TYPE_INT_RGB);
         //2.得到图片
         //Graphics g = image.getGraphics();
         Graphics2D g = (Graphics2D)image.getGraphics();
         g.setColor(Color.WHITE);//设置图片的背景色
         g.fillRect(0, 0, 80, 20);//填充背景色
         //3.向图片上写数据
         g.setColor(Color.BLUE);//设置图片上字体的颜色
         g.setFont(new Font(null, Font.BOLD, 20));
         g.drawString(makeNum(), 0, 20);
         //4.设置响应头控制浏览器浏览器以图片的方式打开
         response.setContentType("image/jpeg");//等同于response.setHeader("Content-Type", "image/jpeg");
         //5.设置响应头控制浏览器不缓存图片数据
         response.setDateHeader("expries", -1);
        response.setHeader("Cache-Control", "no-cache");
         response.setHeader("Pragma", "no-cache");
         //6.将图片写给浏览器
         ImageIO.write(image, "jpg", response.getOutputStream());
     }
 
     /**
      * 生成随机数字
      * @return
      */
     private String makeNum(){
         Random random = new Random();
         String num = random.nextInt(9999999)+"";
         StringBuffer sb = new StringBuffer();
         for (int i = 0; i < 7-num.length(); i++) {
             sb.append("0");
         }
         num = sb.toString()+num;
         return num;
     }
	
}
