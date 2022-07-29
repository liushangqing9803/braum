package cn.mianshiyi.braumadmin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author shangqing.liu
 */
@Controller
public class GatewayController {

    @GetMapping("/viewLabel")
    public String global() {
        return "viewLabel";
    }
}
