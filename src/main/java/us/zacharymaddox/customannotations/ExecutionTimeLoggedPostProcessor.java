package us.zacharymaddox.customannotations;

import java.lang.reflect.Method;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.aop.framework.AopProxyUtils;
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.cglib.proxy.Enhancer;
import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.core.MethodIntrospector;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;

@Component
public class ExecutionTimeLoggedPostProcessor implements BeanPostProcessor {
	
	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
		Class<?> targetClass = AopProxyUtils.ultimateTargetClass(bean);
		Map<Method, ExecutionTimeLogged> annotatedMethods = MethodIntrospector.selectMethods(targetClass, 
				(MethodIntrospector.MetadataLookup<ExecutionTimeLogged>) method -> {
					return AnnotatedElementUtils.getMergedAnnotation(method, ExecutionTimeLogged.class);
				});
		
		if (annotatedMethods.isEmpty()) {
			return bean;
		} else {
			Enhancer enhancer = new Enhancer();
			enhancer.setSuperclass(targetClass);
			enhancer.setCallback((MethodInterceptor) (obj, method, args, proxy) -> {
				if (method.isAnnotationPresent(ExecutionTimeLogged.class)) {
					Logger logger = LoggerFactory.getLogger(targetClass);
					StopWatch stopWatch = new StopWatch();
					stopWatch.start();
					Object result = proxy.invokeSuper(obj, args);
					stopWatch.stop();
					logger.info("Executed for {} mills", stopWatch.getTotalTimeMillis());
					return result;
				} else {
					return proxy.invokeSuper(obj, args);
				}
				
			});
			return enhancer.create();
		}
	}

}
