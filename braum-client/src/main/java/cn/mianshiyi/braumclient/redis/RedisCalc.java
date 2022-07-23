package cn.mianshiyi.braumclient.redis;

/**
 * redis 调用lua脚本接口
 *
 * @author shangqing.liu
 */
public interface RedisCalc {
    /**
     * 因考虑redis客户端较多，用户需自行实现该接口，并交由spring管理
     *
     * @param luaValue lua脚本
     * @param key1     限流key
     * @param value1   生产时间
     * @param key2     限流key
     * @param value2   最大token数量
     * @return 执行结果
     */
    Object eval(String luaValue, String key1, String value1, String key2, String value2);
}
