package xf.mvc.spring.factorybean;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class TestCar {

	public static void main(String[] args) {
		
		ApplicationContext context = new ClassPathXmlApplicationContext("spring/spring-factorybean.xml");
		
		Car car = (Car) context.getBean("car");
		
		System.out.println(car.getBrand()+" " + car.getMaxSpeed()+" " +car.getPrice());
	}
}
