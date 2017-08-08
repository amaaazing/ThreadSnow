package xf.utility;

import java.io.InputStream;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;
import java.util.jar.Manifest;

import javax.servlet.ServletContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;




public class CommonUtils {

	public static final Logger log = Logger.getLogger(CommonUtils.class);
	public static final String regEx = "[。,、！？；：`﹑•＂…‘’“”〝〞/~\\∕|¦‖—　()（）﹛﹜〈〉﹝﹞「」‹›〖〗\\[\\]{}《》〔〕『』«»【】﹐﹕﹔！？﹖﹏＇ˊ﹫︳_＿￣¯︴@―ˋ´﹋﹌¿¡;︰¸﹢﹦﹤­˜﹟﹩￥﹠﹪﹡﹨﹍﹎ˆˇ﹊﹉*%&$#~‐<=+︵︷︿︹︽_﹁﹃︻︼﹄﹂ˉ︾︺﹀︸︶]";
	 
	public static String getIp()    {
		StringBuffer sb=new StringBuffer();
		Enumeration<NetworkInterface> netInterfaces = null;   
		try {   
		    netInterfaces = NetworkInterface.getNetworkInterfaces();   
		    while (netInterfaces.hasMoreElements()) {   
		        NetworkInterface ni = netInterfaces.nextElement();   
		        
		        sb.append("DisplayName:" + ni.getDisplayName()+",");   
		        sb.append("Name:" + ni.getName()+",");   
		        Enumeration<InetAddress> ips = ni.getInetAddresses();   
		        while (ips.hasMoreElements()) {   
		        	sb.append("IP:"  
		            + ips.nextElement().getHostAddress());   
		        }   
		        sb.append("\r\n");
		    }   
		} catch (Exception e) {   
			log.error("::",e);
		}  
		return sb.toString();
    }
	
    /**
     * 返回本地IP
     * @return
     */
    public static String getLocalIpAddr(){
    	String ipAddr = "";
        try{
        	InetAddress ip=null;
			// 根据网卡获取IP
			Enumeration<NetworkInterface> interfaces = NetworkInterface.getNetworkInterfaces();
			while (interfaces.hasMoreElements()) {
				NetworkInterface ni = (NetworkInterface) interfaces.nextElement();
				Enumeration<InetAddress> inetAddresses = ni.getInetAddresses();
				while (inetAddresses.hasMoreElements()) {
					InetAddress inetAddress = inetAddresses.nextElement();
					if (!inetAddress.isLoopbackAddress()) {
						ip = inetAddress;
					}
				}
			}
			if(ip==null) {
				ip=InetAddress.getLocalHost();
			}
			ipAddr = ip.getHostAddress();
        }
        catch(Exception err){
        }
        return ipAddr;
    }
    
    /**
     * 从manifest.mf读取版本号
     * @param ctx
     * @return
     */
    public static String getCodeVersionFromManifest(ServletContext ctx){
        String codeversion="";
        try{
            InputStream is = ctx.getResourceAsStream("/META-INF/MANIFEST.MF");
            Manifest mf = new Manifest(is);
            codeversion = mf.getMainAttributes().getValue("Revision");
        }
        catch(Exception e){
            codeversion ="0";
        }
        return codeversion;
    }
    
    /**
     * 
     * @Description: 获取客户端ip 
     * @param request
     * @return
     */
    public static String getIpAddr(HttpServletRequest request) {
        
        String ip = request.getHeader("x-forwarded-for");
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if(ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }
        return ip;
    }
}
