package xf.mvc.struts.converter;

import java.util.List;

import com.opensymphony.xwork2.ActionSupport;

public class PointAction extends ActionSupport{

	// point的X，Y放在一个input中一起输入
	/**
	 * point的X，Y分开在2个input中输入，此时不需要类型转换，
	 * 但是要重写toString()方法，同时input的name=point.x ,name= point.y,要提供一个不带参数的构造方法
	 */	

	Point point;	
	
	Point point1;
	
//	// 集合的类型转换，放在循环中一个一个地add到list中，前端页面需要使用相同的name
//	private List<Point> pointList;
	
	// 在执行execute方法之前会执行属性的getter和setter方法，就会调用类型转换器
    public String execute() throws Exception {
        return SUCCESS;
    }

	public Point getPoint() {
		return point;
	}

	public void setPoint(Point point) {
		this.point = point;
	}

	public Point getPoint1() {
		return point1;
	}

	public void setPoint1(Point point1) {
		this.point1 = point1;
	}

//	public List<Point> getPointList() {
//		return pointList;
//	}
//
//	public void setPointList(List<Point> pointList) {
//		this.pointList = pointList;
//	}
    
	
    
}
