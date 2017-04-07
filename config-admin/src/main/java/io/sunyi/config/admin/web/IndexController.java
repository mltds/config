package io.sunyi.config.admin.web;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author sunyi
 */
@Controller
public class IndexController extends AdminBaseController {

    @RequestMapping("/")
    public String index(){
        return "redirect:/config/list";
    }

}