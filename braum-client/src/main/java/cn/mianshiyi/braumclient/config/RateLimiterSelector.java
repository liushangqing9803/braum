package cn.mianshiyi.braumclient.config;

import org.springframework.context.annotation.ImportSelector;
import org.springframework.core.type.AnnotationMetadata;

/**
 * @author shangqing.liu
 */
public class RateLimiterSelector implements ImportSelector {
    @Override
    public String[] selectImports(AnnotationMetadata importingClassMetadata) {
        return new String[]{"cn.mianshiyi.braumclient.aspect.RateLimiterAspect","cn.mianshiyi.braumclient.monitor.MonitorStart"};
    }
}
