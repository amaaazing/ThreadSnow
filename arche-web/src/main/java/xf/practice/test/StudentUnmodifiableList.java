package xf.practice.test;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * StudentUnmodifiableList类，仅对外提供的getCourses()方法，而没有setCourses()方法，而且
 通过getCourses()方法获得的courses是“只读的”，如果你试图向其添加一个新课程，则
 抛出java.lang.UnsupportedOperationException。你必须通过StudentUnmodifiableList.addCourse()来
 向特定的StudentUnmodifiableList对象添加一个新课程。就好像，你必须让顾客自己向购物车里放食物，
 而不能在顾客毫不知情下，偷偷向其购物车里放食物。

	真是万物皆通情理啊:)
	
	
	这样做满足了实例封闭。为了方便以后搞多线程
 * @author xinzhenqiu
 *
 */
public class StudentUnmodifiableList {

	private String name;

    private ArrayList<String> courses;
    
    public StudentUnmodifiableList(String name, ArrayList<String> courses)
    {
    	this.name = name;
    	this.courses = courses;
    }

    public String getName()
    {
        return name;
    }

    public void setName(String name)
    {
        this.name = name;
    }
    
    // 通过此方法向学生对象添加课程，不能通过ArrayList对象添加课程。
    // 实例封闭
    public void addCourse(String course)
    {
    	courses.add(course);
    }
    
    public String removeCourse(String course)
    {
    	boolean removed = courses.remove(courses);
 
    	if (removed)
    	{
    		return course;
    	}
    	else
    	{
            return null;
    	}
    }
    
    // 实例封闭
    public List<String> getCourses()
    {	// 返回的是只读的课程list
    	return Collections.unmodifiableList(courses);
    }
}
