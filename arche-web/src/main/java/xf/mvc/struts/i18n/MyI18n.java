package xf.mvc.struts.i18n;

import java.text.MessageFormat;
import java.util.Locale;
import java.util.ResourceBundle;

/**
 * 
 * 拦截器：i18nInterceptor 
 *
 */
public class MyI18n {

	
	// 如何获得本地的语言支持
	
	// struts封装了下面的底层实现：简约而不简单
	public static void main(String[] args) {
		
//		Locale[] locales = Locale.getAvailableLocales();
//		
//		for(Locale locale : locales){
//			System.out.println(locale.getDisplayCountry() + ":" + locale.getLanguage());
//		}
		
		
		
		Locale locale = Locale.getDefault();
		
		//getBundle(String baseName, Locale locale)
		ResourceBundle bundle = ResourceBundle.getBundle("myFile", locale);
		
		// 属性文件中是 key = value的形式
		
		 String value = bundle.getString("hello");
		 
		 // 格式化myFile_en_US.properties中的消息
		 String result = MessageFormat.format(value, new Object[]{"武汉"});
		 
		 System.out.println(result);
		 /**
		  * 参看：ActionSupport 中的getText()
		  * 
		  * 
		  */
	}
	
	/**
	 * 
	 * 属性文件：baseName_language_country.properties
	 * 如: myFile_en_US.properties
	 * 	   myFile_zh_CN.properties
	 * 
	 * struts2的页面标签也能进行国际化。使用key属性
	 * 
	 */
}
