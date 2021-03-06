package xf.mvc.servlet;

/**
 * 会话管理
 * 
 * 一、会话的概念
 * 会话可简单理解为：用户开一个浏览器，点击多个超链接，访问服务器多个web资源，然后关闭浏览器，整个过程称之为一个会话。
 * 有状态会话：一个同学来过教室，下次再来教室，我们会知道这个同学曾经来过，这称之为有状态会话。
 * 
 * 二、会话过程中要解决的一些问题
 * 每个用户在使用浏览器与服务器进行会话的过程中，不可避免各自会产生一些数据，程序要想办法为每个用户保存这些数据。
 * 
 * 三、保存会话数据的两种技术
 * 1、Cookie
 * Cookie是客户端技术，程序把每个用户的数据以cookie的形式写给用户各自的浏览器。当用户使用浏览器再去访问服务器中的web资源时，
 * 就会带着各自的数据去。这样，web资源处理的就是用户各自的数据了。
 * 2、Session
 * Session是服务器端技术，利用这个技术，服务器在运行时可以为每一个用户的浏览器创建一个其独享的session对象，
 * 由于session为用户浏览器独享，所以用户在访问服务器的web资源时，可以把各自的数据放在各自的session中，
 * 当用户再去访问服务器中的其它web资源时，其它web资源再从用户各自的session中取出数据为用户服务。
 * 
 *
 */
public class MyCookie {

	// 操作Cookie的API
	
/*	public void setMaxAge(int expiry)
 * 
 *  设置Cookie的最大保存时间，即cookie的有效期，当服务器给浏览器回送一个cookie时，
	如果在服务器端没有调用setMaxAge方法设置cookie的有效期，那么cookie的有效期只在一次会话过程中有效，
	用户开一个浏览器，点击多个超链接，访问服务器多个web资源，然后关闭浏览器，整个过程称之为一次会话，
	当用户关闭浏览器，会话就结束了，此时cookie就会失效，如果在服务器端使用setMaxAge方法设置了cookie的有效期，
	比如设置了30分钟，那么当服务器把cookie发送给浏览器时，此时cookie就会在客户端的硬盘上存储30分钟，
	在30分钟内，即使浏览器关了，cookie依然存在，在30分钟内，打开浏览器访问服务器时，浏览器都会把cookie一起带上，
	这样就可以在服务器端获取到客户端浏览器传递过来的cookie里面的信息了，这就是cookie设置maxAge和不设置maxAge的区别，
	不设置maxAge，那么cookie就只在一次会话中有效，一旦用户关闭了浏览器，那么cookie就没有了，那么浏览器是怎么做到这一点的呢，
	我们启动一个浏览器，就相当于启动一个应用程序，而服务器回送的cookie首先是存在浏览器的缓存中的，当浏览器关闭时，
	浏览器的缓存自然就没有了，所以存储在缓存中的cookie自然就被清掉了，而如果设置了cookie的有效期，那么浏览器在关闭时，
	就会把缓存中的cookie写到硬盘上存储起来，这样cookie就能够一直存在了。
*/

	// cookie写到硬盘上的存储路径 C:\Users\Default\AppData\Local\Microsoft\Windows
	
	/**
	 * public void setPath(String uri)
	 * 
	 * 设置cookie的有效路径，比如把cookie的有效路径设置为"/xdp"，那么浏览器访问"xdp"目录下的web资源时，都会带上cookie，
	 * 再比如把cookie的有效路径设置为"/xdp/gacl"，那么浏览器只有在访问"xdp"目录下的"gacl"这个目录里面的web资源时才会带上cookie一起访问，
	 * 而当访问"xdp"目录下的web资源时，浏览器是不带cookie的
	 */
	
//	response接口也中定义了一个addCookie方法，它用于在其响应头中增加一个相应的Set-Cookie头字段。 同样，request接口中也定义了一个getCookies方法，它用于获取客户端提交的Cookie。
	
	// 使用cookie边界值条件注意事项：如果用户是第一次访问，那么得到的cookies将是null，此处应该判断
	
	/**
	 * 注意：删除cookie时，path必须一致，否则不会删除
	 */
	
	// cookie中存取中文 
	/**
	 * 在cookie中存储中文，必须使用URLEncoder类里面的encode(String s, String enc)方法进行中文转码
	 * Cookie cookie = new Cookie("userName", URLEncoder.encode("白小凤", "UTF-8"));
	 * response.addCookie(cookie);
	 * 
	 * 在获取cookie中的中文数据时，再使用URLDecoder类里面的decode(String s, String enc)进行解码，
	 * URLDecoder.decode(cookies[i].getValue(), "UTF-8")
	 *  
	 */
	
	
}
