package cn.mianshiyi.braumadmin.controller;

import cn.mianshiyi.braumadmin.entity.APIResponse;
import cn.mianshiyi.braumadmin.entity.LimiterDataEntity;
import cn.mianshiyi.braumadmin.entity.qo.LimiterDataQo;
import cn.mianshiyi.braumadmin.entity.vo.LimiterDataView;
import cn.mianshiyi.braumadmin.service.LimiterDataService;
import cn.mianshiyi.braumadmin.utils.DateUtil;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.text.ParseException;
import java.util.Date;
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
    @RequestMapping(value = "/query", method = RequestMethod.POST)
    @ResponseBody
    public APIResponse find(LimiterDataQo limiterDataQo) throws ParseException {
        if (limiterDataQo == null || StringUtils.isEmpty(limiterDataQo.getLimiterName())) {
            return APIResponse.returnFail("限流名称不能为空");
        }
        if (StringUtils.isEmpty(limiterDataQo.getStartTime()) || StringUtils.isEmpty(limiterDataQo.getEndTime())) {
            limiterDataQo.setStart(System.currentTimeMillis() / 1000 - 15 * 60);
            limiterDataQo.setEnd(System.currentTimeMillis() / 1000);
        } else {
            limiterDataQo.setStart(DateUtil.parse(limiterDataQo.getStartTime(), DateUtil.YYYY_MM_dd_HHMMSS_NORMAL).getTime() / 1000);
            limiterDataQo.setEnd(DateUtil.parse(limiterDataQo.getEndTime(), DateUtil.YYYY_MM_dd_HHMMSS_NORMAL).getTime() / 1000);
        }

        LimiterDataView byQo = limiterDataService.findByQo(limiterDataQo);
        return APIResponse.returnSuccess(byQo);
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




