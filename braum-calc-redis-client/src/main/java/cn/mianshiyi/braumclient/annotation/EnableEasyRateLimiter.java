package cn.mianshiyi.braumclient.annotation;

import cn.mianshiyi.braumclient.config.RateLimiterSelector;
import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @author shangqing.liu
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
@Documented
@Import({RateLimiterSelector.class})
public @interface EnableEasyRateLimiter {
}
