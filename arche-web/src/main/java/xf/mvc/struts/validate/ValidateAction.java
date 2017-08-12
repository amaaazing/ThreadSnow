package xf.mvc.struts.validate;

import xf.mvc.struts.converter.Point;

import com.opensymphony.xwork2.ActionSupport;

/**
 * 类型转换与输入校验的流程
 * 	1.首先struts对客户端传来的数据进行类型转换
 * 	2.类型转换完毕后再进行输入校验
 * 	3.如果类型转换和输入校验都没有错误，那么进入execute方法
 * 
 * 	注意：如果类型转换不成功，也同样要进行输入校验
 *	
 *	校验顺序：1. xml 2. validateHello() 3. validate()
 *
 *	actionError()的错误信息放在ArrayList中的
 */
public class ValidateAction extends ActionSupport{

	Point point;
	
	// 如果类型转换时发生错误，并将错误信息放到addFieldError中，struts默认会跳转到input的结果视图
	// 怎么替换struts默认的类型转换错误信息？
	// 在struts.xml中<constant name="struts.custom.i18n.resources" value="message"></constant>
	// 在message.properties文件中定义错误信息
	/**
	 * 输入校验的前提：类型转换没有错误
	 * 
	 * 输入校验的时机：struts完成类型转换后，再进行校验
	 * 
	 * struts的标签库内置了默认校验,类型转换错误时，会在标签上面提示错误
	 */
	
	@Override 
    public void validate() { // 重写父类的validate方法
		// 此时可以调用action里面的属性进行参数校验了
		if(point == null){
			// 页面上用struts的<s:fielderror></s:fielderror>将提示信息输出
			 // 参数的第一个point对应页面中input的name
			// 关注一下addFieldError的数据结构，将错误信息加入一个集合（LinkedHashMap）中，为什么是map咧？为什么可以加入多个错误信息？(支持多重校验)
			// 第一，类型转换错误，action中的属性point就为null，第二，如果再判断null，就会在页面提示2个错误信息
			this.addFieldError("point", "point is null");
		}
    }
	
	/**
	 * hasError() == hasActionError()|| hasFieldError(),
	 * 
	 * hasError 为true , 页面跳转到定义的错误页面
	 * 
	 * 使用<s:actionerror>标签显示错误信息
	 * 
	 */
	
	/**
	 * <action name="hello" class = "" methode = "hello">
	 * 
	 * 则校验方法为：validateHello
	 *
	 */
	public void validateHello(){
		// 先执行validateHello(),后执行validate()（if any）
		
		// 如果action中有validate(),一定会执行的
		
		// 可以定义：validateExecute()
	}
	
	/**
	 *  还有xml格式的输入校验，支持正则表达式
	 */
}
