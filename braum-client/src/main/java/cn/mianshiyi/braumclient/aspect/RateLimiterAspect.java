package cn.mianshiyi.braumclient.aspect;

import cn.mianshiyi.braumclient.annotation.EasyRateLimier;
import cn.mianshiyi.braumclient.common.Constant;
import cn.mianshiyi.braumclient.enums.LimiterHandleType;
import cn.mianshiyi.braumclient.enums.LimiterKeyType;
import cn.mianshiyi.braumclient.enums.LimiterType;
import cn.mianshiyi.braumclient.exception.RateLimitBlockException;
import cn.mianshiyi.braumclient.exception.RateLimitTimeoutBlockException;
import cn.mianshiyi.braumclient.ratelimit.EasyLocalRateLimiter;
import cn.mianshiyi.braumclient.ratelimit.EasyRateLimiter;
import cn.mianshiyi.braumclient.ratelimit.EasyRedisCalcRateLimiter;
import cn.mianshiyi.braumclient.redis.RedisCalc;
import cn.mianshiyi.braumclient.utils.EasyLimiterThreadLocal;
import com.google.common.collect.Maps;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.expression.MethodBasedEvaluationContext;
import org.springframework.core.DefaultParameterNameDiscoverer;
import org.springframework.core.ParameterNameDiscoverer;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;
import java.util.Map;

/**
 *
 */
@Aspect
@Component
public class RateLimiterAspect {

    private static final Map<String, EasyRateLimiter> RATE_LIMITER_MAP = Maps.newConcurrentMap();

    final Object LOCK = new Object();

    private final ExpressionParser parser = new SpelExpressionParser();

    private final ParameterNameDiscoverer nameDiscoverer = new DefaultParameterNameDiscoverer();

    @Autowired(required = false)
    private RedisCalc redisCalc;

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
        String sourceName = analysisSourceName(pjp, originMethod, annotation);
        EasyRateLimiter rateLimiter = getReteLimiter(annotation, sourceName);
        if (rateLimiter == null) {
            return pjp.proceed();
        }
        rateLimiterExecute(annotation, rateLimiter);
        return pjp.proceed();
    }

    private void rateLimiterExecute(EasyRateLimier annotation, EasyRateLimiter rateLimiter) {
        LimiterHandleType limiterHandleType = annotation.limiterHandleType();
        switch (limiterHandleType) {
            case WAIT:
                long timeout = annotation.timeout();
                if (!rateLimiter.acquire(timeout)) {
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

    private EasyRateLimiter getReteLimiter(EasyRateLimier annotation, String sourceName) {
        double permitsPerSecond = annotation.permitsPerSecond();
        LimiterType limiterType = annotation.limiterType();
        if (sourceName == null || sourceName.equals("")) {
            //TODO 打印日志
            return null;
        }
        return buildRateLimiter(sourceName, permitsPerSecond, limiterType);
    }


    private EasyRateLimiter buildRateLimiter(String value, double permitsPerSecond, LimiterType limiterType) {
        if (RATE_LIMITER_MAP.get(value) == null) {
            synchronized (LOCK) {
                if (RATE_LIMITER_MAP.get(value) == null) {
                    EasyRateLimiter easyRateLimiter;
                    if (LimiterType.DIST == limiterType) {
                        easyRateLimiter = new EasyRedisCalcRateLimiter().setRedisCalc(redisCalc).create(permitsPerSecond, value);
                    } else {
                        easyRateLimiter = new EasyLocalRateLimiter().create(permitsPerSecond, value);
                    }
                    RATE_LIMITER_MAP.put(value, easyRateLimiter);
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

    public String analysisSourceName(JoinPoint joinPoint, Method method, EasyRateLimier easyRateLimier) {
        StringBuilder builder = new StringBuilder(Constant.PRE_KEY);
        builder.append(easyRateLimier.value());
        LimiterKeyType limiterKeyType = easyRateLimier.keyType();
        if (limiterKeyType == null) {
            return builder.toString();
        }
        switch (limiterKeyType) {
            case PARAM:
                String[] keys = easyRateLimier.keys();
                if (keys != null && keys.length > 0) {
                    for (String key : keys) {
                        if (key != null) {
                            EvaluationContext context = new MethodBasedEvaluationContext(null, method, joinPoint.getArgs(), nameDiscoverer);
                            Object parserObj = parser.parseExpression(key).getValue(context);
                            if (parserObj != null) {
                                builder.append(Constant.SPIT);
                                builder.append(parserObj);
                            }
                        }
                    }
                }
                break;
            case DEFTL:
                String threadKey = EasyLimiterThreadLocal.get();
                if (threadKey != null && !threadKey.equals("")) {
                    builder.append(Constant.SPIT);
                    builder.append(threadKey);
                    EasyLimiterThreadLocal.remove();
                }
                break;
        }
        return builder.toString();
    }
}
