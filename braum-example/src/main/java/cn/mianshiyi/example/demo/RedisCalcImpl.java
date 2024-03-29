package cn.mianshiyi.example.demo;

import cn.mianshiyi.braumclient.redis.RedisCalc;
import org.springframework.stereotype.Service;
import redis.clients.jedis.Jedis;

/**
 * @author shangqing.liu
 */
@Service
public class RedisCalcImpl implements RedisCalc {

    @Override
    public Object eval(String luaValue, String key1, String value1, String key2, String value2) {
        Jedis jedis = new Jedis("127.0.0.1", 6379);
        jedis.auth("xxxx");
        return jedis.eval(luaValue, 2, key1, key2, value1,value2);
    }

//    @Override
//    public Object eval(String luaValue, String key1, String value1, String key2, String value2) {
//        Config config = new Config();
//        //string编码重要
//        SingleServerConfig serverConfig = config.setCodec(new StringCodec()).useSingleServer()
//                .setAddress("redis://127.0.0.1:6379")
//                .setTimeout(3000)
//                .setConnectionPoolSize(100)
//                .setConnectionMinimumIdleSize(30);
//        serverConfig.setPassword("xxxx");
//        RedissonClient redissonClient = Redisson.create(config);
//        RScript script = redissonClient.getScript();
//        List<Object> strings = new LinkedList<>();
//        strings.add(key1);
//        strings.add(key2);
//        return script.eval(RScript.Mode.READ_WRITE, luaValue, RScript.ReturnType.STATUS, strings, value1, value2);
//    }
}
