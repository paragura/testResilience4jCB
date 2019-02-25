package info.paragura;

import io.github.resilience4j.circuitbreaker.CircuitBreaker;
import io.github.resilience4j.circuitbreaker.CircuitBreakerRegistry;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Aspect
public class MyCircuitBreakerAspect {

    @Autowired
    CircuitBreakerRegistry registry;

    @Autowired
    private ApplicationContext appContext;

    @Around("bean(MyCircuitBreaker*)")
    public Object circuitBreakAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String beanName = getBeanName(joinPoint);
        CircuitBreaker breaker = registry.circuitBreaker(beanName);
        return CircuitBreaker.decorateCheckedSupplier(breaker, joinPoint::proceed).apply();
    }

    private String getBeanName(ProceedingJoinPoint joinPoint) {
        Object targetObject = joinPoint.getTarget();
        Object proxiedObject = joinPoint.getThis();
        Map<String, ? extends Object> beans = appContext.getBeansOfType(targetObject.getClass());
        for (Map.Entry<String, ? extends Object> entry : beans.entrySet()) {
            if (entry.getValue() == proxiedObject) {
                return entry.getKey();
            }
        }
        throw new IllegalArgumentException("can't find bean name. obj:" + targetObject.getClass().getSimpleName() + ",beans:" + String.join(",", beans.keySet()));
    }
}

