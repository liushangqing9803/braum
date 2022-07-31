package cn.mianshiyi.braumadmin.controller;

import cn.mianshiyi.braumadmin.entity.APIResponse;
import cn.mianshiyi.braumadmin.entity.LimiterRegisterInfoEntity;
import cn.mianshiyi.braumadmin.entity.qo.LimiterDataQo;
import cn.mianshiyi.braumadmin.entity.vo.LimiterDataView;
import cn.mianshiyi.braumadmin.service.LimiterRegisterInfoService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author shangqing.liu
 */
@Controller
@RequestMapping("/limiterRegisterInfoController")
public class LimiterRegisterInfoController {

    @Resource
    private LimiterRegisterInfoService limiterRegisterInfoService;

    @PostMapping("/queryPage")
    @ResponseBody
    public APIResponse find( LimiterDataQo qo) {
        List<LimiterRegisterInfoEntity> all = limiterRegisterInfoService.findAll();
        return APIResponse.returnSuccess(all);
    }
}
