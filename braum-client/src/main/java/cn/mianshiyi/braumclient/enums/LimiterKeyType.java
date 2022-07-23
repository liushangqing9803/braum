package cn.mianshiyi.braumclient.enums;

/**
 * 限流key类型
 * 参数 根据表达式解析
 * 自定义 放入 EasyLimiterThreadLocal
 *
 * @author shangqing.liu
 */
public enum LimiterKeyType {
    PARAM, DEFTL,
}
