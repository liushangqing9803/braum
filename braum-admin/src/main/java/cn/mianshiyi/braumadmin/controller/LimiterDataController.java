package cn.mianshiyi.braumadmin.controller;

import cn.mianshiyi.braumadmin.entity.APIResponse;
import cn.mianshiyi.braumadmin.entity.LimiterDataEntity;
import cn.mianshiyi.braumadmin.entity.qo.LimiterDataQo;
import cn.mianshiyi.braumadmin.entity.vo.LimiterDataVo;
import cn.mianshiyi.braumadmin.service.LimiterDataService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.Random;

/**
 * ${clasComment} Controller 接口
 *
 * @author: java
 * @since 2022-07-27 10:29:55
 */
@Controller
@RequestMapping("/limiterDataController")
public class LimiterDataController {

    @Resource
    private LimiterDataService limiterDataService;

    /**
     * @param
     * @return
     */
    @PostMapping("/query")
    @ResponseBody
    public APIResponse find(LimiterDataQo qo) {
        LimiterDataQo limiterDataQo = new LimiterDataQo();
        limiterDataQo.setName("EASY_LIMITER_distException");
        limiterDataQo.setStart(System.currentTimeMillis() / 1000 - 60);
        limiterDataQo.setEnd(System.currentTimeMillis() / 1000 + 60);

        LimiterDataVo vo = limiterDataService.findByQo(limiterDataQo);
        return APIResponse.returnSuccess(vo);
    }

    @GetMapping("/test11")
    @ResponseBody
    public String global() {
        long l = System.currentTimeMillis() / 1000;
        for (int i = 0; i < 100; i++) {
            LimiterDataEntity limiterDataEntity = new LimiterDataEntity();
            limiterDataEntity.setName("test");
            limiterDataEntity.setOccSecond(l);
            limiterDataEntity.setExecCount((long) new Random().nextInt(100));
            limiterDataEntity.setBlockCount((long) new Random().nextInt(100));

            limiterDataService.insert(limiterDataEntity);
            l++;
        }

        return "viewLabel";
    }
}




