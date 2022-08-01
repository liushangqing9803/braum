package cn.mianshiyi.braumadmin.controller;

import cn.mianshiyi.braumadmin.entity.APIResponse;
import cn.mianshiyi.braumadmin.entity.LimiterRegisterInfoEntity;
import cn.mianshiyi.braumadmin.entity.ListResponse;
import cn.mianshiyi.braumadmin.entity.qo.LimiterDataQo;
import cn.mianshiyi.braumadmin.entity.qo.LimiterRegisterDataQo;
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
    public ListResponse find(LimiterRegisterDataQo qo) {
        APIResponse<ListResponse<LimiterRegisterInfoEntity>> page = limiterRegisterInfoService.findPage(qo);
        ListResponse<LimiterRegisterInfoEntity> data = page.getData();
        if (data == null) {
            return new ListResponse<>(null, 0, -1);
        }
        data.setCode(page.getErrcode());
        return data;
    }
}
