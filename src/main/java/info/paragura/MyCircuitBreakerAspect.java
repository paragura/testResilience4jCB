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

    public static final String MY_CIRCUIT_BREAKER_NAME_PREFIX = "MyCircuitBreaker-";

    @Autowired
    CircuitBreakerRegistry registry;

    @Autowired
    private ApplicationContext appContext;

    @Around("bean(" + MY_CIRCUIT_BREAKER_NAME_PREFIX + "*)")
    public Object circuitBreakAround(ProceedingJoinPoint joinPoint) throws Throwable {
        String beanName = getBeanName(joinPoint).replace(MY_CIRCUIT_BREAKER_NAME_PREFIX, "");
        CircuitBreaker breaker = registry.circuitBreaker(beanName);
        return CircuitBreaker.decorateCheckedSupplier(breaker, joinPoint::proceed).apply();
    }

    private String getBeanName(ProceedingJoinPoint joinPoint) {
        Object targetObject = joinPoint.getTarget();
        Object proxiedObject = joinPoint.getThis();
        Map<String, ?> beans = appContext.getBeansOfType(targetObject.getClass());
        for (Map.Entry<String, ?> entry : beans.entrySet()) {
            if (entry.getValue() == proxiedObject) {
                return entry.getKey();
            }
        }
        throw new IllegalArgumentException("can't find bean name. obj:" + targetObject.getClass().getSimpleName() + ",beans:" + String.join(",", beans.keySet()));
    }
}

