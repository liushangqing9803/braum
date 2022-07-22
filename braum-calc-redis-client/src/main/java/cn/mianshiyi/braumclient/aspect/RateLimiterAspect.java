package cn.mianshiyi.braumclient.aspect;

import cn.mianshiyi.braumclient.annotation.EasyRateLimier;
import cn.mianshiyi.braumclient.common.Constant;
import cn.mianshiyi.braumclient.enums.LimiterHandleType;
import cn.mianshiyi.braumclient.exception.RateLimitBlockException;
import cn.mianshiyi.braumclient.exception.RateLimitTimeoutBlockException;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.RateLimiter;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 *
 */
@Aspect
@Component
public class RateLimiterAspect {

    private static Map<String, RateLimiter> RATE_LIMITER_MAP = Maps.newConcurrentMap();

    final Object LOCK = new Object();

    @Pointcut("@annotation(cn.mianshiyi.braumclient.annotation.EasyRateLimier)")
    public void rateLimiterAnnotationPointcut() {
    }

    @Around("rateLimiterAnnotationPointcut()")
    public Object invokeRateLimiterAspect(ProceedingJoinPoint pjp) throws Throwable {
        Method originMethod = resolveMethod(pjp);

        EasyRateLimier annotation = originMethod.getAnnotation(EasyRateLimier.class);
        if (annotation == null) {
            throw new IllegalStateException("Wrong state for rate limiter annotation");
        }
        RateLimiter rateLimiter = getReteLimiter(annotation);
        if (rateLimiter == null) {
            return pjp.proceed();
        }
        rateLimiterExecute(annotation, rateLimiter);
        return pjp.proceed();
    }

    private void rateLimiterExecute(EasyRateLimier annotation, RateLimiter rateLimiter) {
        LimiterHandleType limiterHandleType = annotation.limiterHandleType();
        switch (limiterHandleType) {
            case WAIT:
                long timeout = annotation.timeout();
                TimeUnit timeUnit = annotation.timeoutUnit();
                if (!rateLimiter.tryAcquire(timeout, timeUnit)) {
                    throw new RateLimitTimeoutBlockException(Constant.BLOCK_TIMEOUT_EXCEPTION_MSG);
                }
                break;
            case EXCEPTION:
                if (!rateLimiter.tryAcquire()) {
                    throw new RateLimitBlockException(Constant.BLOCK_EXCEPTION_MSG);
                }
            default:
        }
    }


    private RateLimiter getReteLimiter(EasyRateLimier annotation) {
        String value = annotation.value();
        double permitsPerSecond = annotation.permitsPerSecond();
        if (value == null || value.equals("")) {
            //TODO 打印日志
            return null;
        }
        return buildRateLimiter(value, permitsPerSecond);
    }


    private RateLimiter buildRateLimiter(String value, double permitsPerSecond) {
        if (RATE_LIMITER_MAP.get(value) == null) {
            synchronized (LOCK) {
                if (RATE_LIMITER_MAP.get(value) == null) {
                    RateLimiter rateLimiter = RateLimiter.create(permitsPerSecond);
                    RATE_LIMITER_MAP.put(value, rateLimiter);
                }
            }
        }
        return RATE_LIMITER_MAP.get(value);
    }

    protected Method resolveMethod(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Class<?> targetClass = joinPoint.getTarget().getClass();

        Method method = getDeclaredMethodFor(targetClass, signature.getName(), signature.getMethod().getParameterTypes());
        if (method == null) {
            throw new IllegalStateException("Cannot resolve target method: " + signature.getMethod().getName());
        }
        return method;
    }

    private Method getDeclaredMethodFor(Class<?> clazz, String name, Class<?>... parameterTypes) {
        try {
            return clazz.getDeclaredMethod(name, parameterTypes);
        } catch (NoSuchMethodException e) {
            Class<?> superClass = clazz.getSuperclass();
            if (superClass != null) {
                return getDeclaredMethodFor(superClass, name, parameterTypes);
            }
        }
        return null;
    }
}
