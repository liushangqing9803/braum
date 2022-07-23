package cn.mianshiyi.braumclient.utils;

/**
 * @author shangqing.liu
 */
public class EasyLimiterThreadLocal {

    private static final InheritableThreadLocal<String> EASY_LIMITER_THREAD_LOCAL = new InheritableThreadLocal<>();

    public static void set(String identifier) {
        EASY_LIMITER_THREAD_LOCAL.set(identifier);
    }

    public static String get() {
        return EASY_LIMITER_THREAD_LOCAL.get();
    }

    public static void remove() {
        EASY_LIMITER_THREAD_LOCAL.remove();
    }
}
