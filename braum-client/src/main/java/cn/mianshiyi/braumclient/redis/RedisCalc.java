package cn.mianshiyi.braumclient.redis;

import java.util.Map;


/**
 * @author shangqing.liu
 */
public abstract class RedisCalc {

    public abstract String eval(Map<String, String> limitParam, String evalValue);

}
