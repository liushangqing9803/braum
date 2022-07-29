package cn.mianshiyi.example.controller;

import cn.mianshiyi.braumclient.annotation.EasyRateLimier;
import cn.mianshiyi.braumclient.enums.LimiterHandleType;
import cn.mianshiyi.braumclient.enums.LimiterKeyType;
import cn.mianshiyi.braumclient.enums.LimiterType;
import cn.mianshiyi.example.model.User;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author shangqing.liu
 */
@Controller
public class TestController {

    @RequestMapping("/localException")
    @ResponseBody
    @EasyRateLimier(value = "localException", permitsPerSecond = 20,blockMessage = "出错了",limiterType = LimiterType.LOCAL, limiterHandleType = LimiterHandleType.EXCEPTION)
    @EasyRateLimier(value = "localException222", permitsPerSecond = 0.1,blockMessage = "出错了111",limiterType = LimiterType.LOCAL, limiterHandleType = LimiterHandleType.EXCEPTION)
    public String localException(@RequestBody User user) {
        return "sss";
    }

    @RequestMapping("/localWait")
    @ResponseBody
    @EasyRateLimier(value = "localWait", permitsPerSecond = 1, limiterType = LimiterType.LOCAL, limiterHandleType = LimiterHandleType.WAIT, timeout = 1000)
    public String localWait(@RequestBody User user) {
        return "sss";
    }

    @RequestMapping("/distException")
    @ResponseBody
    @EasyRateLimier(value = "distException", permitsPerSecond = 10, limiterType = LimiterType.DIST, limiterHandleType = LimiterHandleType.EXCEPTION)
    public String distException(@RequestBody User user) {
        return "sss";
    }

    @RequestMapping("/distWait")
    @ResponseBody
    @EasyRateLimier(value = "distWait", permitsPerSecond = 1, limiterType = LimiterType.DIST, limiterHandleType = LimiterHandleType.WAIT, timeout = 1000)
    public String distWait(@RequestBody User user) {
        return "sss";
    }

    @RequestMapping("/distExceptionKeys")
    @ResponseBody
    @EasyRateLimier(value = "distExceptionKeys", keys = {"#user.idNo"}, permitsPerSecond = 5, limiterType = LimiterType.DIST, limiterHandleType = LimiterHandleType.EXCEPTION)
    public String distExceptionKeys(@RequestBody User user) {
        System.out.println(user);
        return "sss";
    }

    @RequestMapping("/distWaitKeys")
    @ResponseBody
    @EasyRateLimier(value = "distWaitKeys", keys = {"#user.idNo", "#user.name",}, permitsPerSecond = 0.1, limiterType = LimiterType.DIST, limiterHandleType = LimiterHandleType.WAIT, timeout = 1000)
    public String distWaitKeys(@RequestBody User user) {
        System.out.println(user);
        return "sss";
    }


    @RequestMapping("/distExceptionTLNo")
    @ResponseBody
    @EasyRateLimier(value = "distExceptionTLNo", keys = {"#user.idNo", "#user.name",}, keyType = LimiterKeyType.DEFTL, permitsPerSecond = 0.1, limiterType = LimiterType.DIST, limiterHandleType = LimiterHandleType.WAIT, timeout = 1000)
    public String distExceptionTLNo(@RequestBody User user) {
        System.out.println(user);
        return "sss";
    }
    @RequestMapping("/distExceptionTL")
    @ResponseBody
    @EasyRateLimier(value = "distExceptionTL", keys = {"#user.idNo", "#user.name",}, keyType = LimiterKeyType.DEFTL, permitsPerSecond = 0.1, limiterType = LimiterType.DIST, limiterHandleType = LimiterHandleType.WAIT, timeout = 1000)
    public String distExceptionTL(@RequestBody User user) {
        System.out.println(user);
        //方法前置拦截放入
        //EasyLimiterThreadLocal.set("ssssdfsdf");
        return "sss";
    }
}
