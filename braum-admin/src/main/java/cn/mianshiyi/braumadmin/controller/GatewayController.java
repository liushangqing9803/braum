package cn.mianshiyi.braumadmin.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author shangqing.liu
 */
@Controller
public class GatewayController {

    @GetMapping("/index")
    public String index() {
        return "index";
    }

    @GetMapping("/viewLabel")
    public String global(ModelMap map) {
        return "viewLabel";
    }
}
