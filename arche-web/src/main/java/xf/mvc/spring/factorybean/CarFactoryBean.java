package xf.mvc.spring.factorybean;

import org.springframework.beans.factory.FactoryBean;

public class CarFactoryBean implements FactoryBean<Car>{

	// 接受逗号分隔符设置属性信息
	private String carInfo;

	// 返回由FactoryBean创建的bean实例，如果isSingleton()返回true，则该实例会放到spring容器中的单实例缓存池中
	// 当配置文件中<bean>的class属性配置的实现类是FactoryBean时，通过getBean()返回的不是FactoryBean本身，而是FactoryBean#getObject()方法返回的对象
	@Override
	public Car getObject() throws Exception {
		
		Car car = new Car();
		
		String[] infos = carInfo.split(",");
		
		car.setBrand(infos[0]);
		car.setMaxSpeed(Integer.valueOf(infos[1]));
		car.setPrice(Double.valueOf(infos[2]));
		
		return car;
	}

	// 返回由FactoryBean创建的bean的类型
	@Override
	public Class<?> getObjectType() {
		return Car.class;
	}

	// 返回由FactoryBean创建的bean的作用域是singleton还是prototype
	@Override
	public boolean isSingleton() {
		return false;
	}
	
	public String getCarInfo() {
		return carInfo;
	}

	// 接受逗号分隔符设置属性信息
	public void setCarInfo(String carInfo) {
		this.carInfo = carInfo;
	}
}
