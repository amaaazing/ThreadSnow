package xf.log;

import java.lang.ref.Reference;
import java.lang.ref.WeakReference;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;

public class SpringUtils implements ApplicationContextAware, InitializingBean{

	private static Map<String, WeakReference<ApplicationContext>> contextMap_ = new HashMap<String, WeakReference<ApplicationContext>>();

	private String currentKey_;

	private ApplicationContext currentContext_;

	public static Object getBackendBean(String beanName){
		Object o=contextMap_.get("mainContext").get();
		if (o!=null){
			return ((ApplicationContext)o).getBean(beanName);
		}
		return null;
	}

	public static ApplicationContext getContext(String key){
		return contextMap_.get(key).get();
	}

	public static Iterator<ApplicationContext> getContextIterator(){
		return new ReferenceIteratorWrapper<ApplicationContext>(contextMap_.values().iterator());
	}

	public void setApplicationContext(ApplicationContext applicationContext) throws BeansException{
		currentContext_ = applicationContext;
	}

	public String getContextKey(){
		return currentKey_;
	}

	public void setContextKey(String contextKey){
		currentKey_ = contextKey;
	}

	public void afterPropertiesSet() throws BeanInitializationException{
		if (currentKey_ == null) {
			throw new BeanInitializationException("Property 'contextKey' is required.");
		}
		contextMap_.put(currentKey_, new WeakReference<ApplicationContext>(currentContext_));
		currentKey_ = null;
		currentContext_ = null;
	}

	private static class ReferenceIteratorWrapper<T> implements Iterator<T>{
		private Iterator<? extends Reference<T>> iterator_;

		public ReferenceIteratorWrapper(Iterator<? extends Reference<T>> iterator){
			iterator_ = iterator;
		}

		public boolean hasNext(){
			return iterator_.hasNext();
		}

		public T next(){
			return iterator_.next().get();
		}

		public T nextNotNull(){
			while (iterator_.hasNext()) {
				T t = iterator_.next().get();
				if (t != null) {
					return t;
				}
			}
			return null;
		}

		public void remove(){
			iterator_.remove();
		}

	}

}
