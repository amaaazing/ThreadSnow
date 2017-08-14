package xf.mvc.spring.start;

// UpperAction将其message属性与输入字符串相连接，并返回其大写形式
public class UpperAction implements Action {

	private String message;
	
	@Override
	public String execute(String str) {
		
		return (getMessage() + str).toUpperCase();
	}

	public String getMessage() {
		return message;
	}

	// UpperAction依赖的message，是通过类的setter方法完成依赖关系的设置
	public void setMessage(String message) {
		this.message = message;
	}

/**
但构造函数注入也有优势：
1． 避免了繁琐的setter方法的编写，所有依赖关系均在构造函数中设定，依赖关系集中呈现，更加易读。
2． 由于没有setter方法，依赖关系在构造时由容器一次性设定，因此组件在被创建之后即处于
相对“不变”的稳定状态，无需担心上层代码在调用过程中执行setter方法对组件依赖关系产生破坏，
特别是对于Singleton模式的组件而言，这可能对整个系统产生重大的影响。
3． 同样，由于关联关系仅在构造函数中表达，只有组件创建者需要关心组件内部的依赖关系。
对调用者而言，组件中的依赖关系处于黑盒之中。对上层屏蔽不必要的信息，也为系统的
层次清晰性提供了保证。
4． 通过构造函数注入，意味着我们可以在构造函数中决定依赖关系的注入顺序，对于一个大量
依赖外部服务的组件而言，依赖关系的获得顺序可能非常重要，比如某个依赖关系注入的
先决条件是组件的DataSource及相关资源已经被设定。
*/
	
	
	
	
	
}
