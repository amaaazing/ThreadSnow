package xf.mvc.struts;


import java.util.Map;

import org.apache.struts2.util.StrutsTypeConverter;

import ognl.DefaultTypeConverter;
import xf.mvc.struts.converter.Point;

/**
 * 不必与配置文件在用一个目录下
 *
 */

public class PointConverter extends DefaultTypeConverter{

	
	public Object convertValue(Map context, Object value, Class toType){
		
		if(String.class == toType){
			// 服务器向浏览器转
		}
			
		if(Point.class == toType){
			// 浏览器向服务器转
		}
		
		
		return null;
	}
	
	// 项目开发用这个
	class PointConverter1 extends StrutsTypeConverter{

		@Override // 从字符串转换为对象
		/**
		 * String[] 为什么是字符串数组，因为ftl中可以写多个name相同的<input>标签
		 */
		public Object convertFromString(Map context, String[] values,
				Class toClass) {
			Point point = new Point();
			
			String[] pValue = values[0].split(",");
			
			point.setX(Integer.parseInt(pValue[0]));
			point.setY(Integer.parseInt(pValue[1]));
			
			return point;
		}

		@Override // 转换成字符串。也即自定义类型转换
		public String convertToString(Map context, Object o) {
			// TODO Auto-generated method stub
			return null;
		}
		
	}
	
}
