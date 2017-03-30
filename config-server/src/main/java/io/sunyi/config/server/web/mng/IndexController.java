package io.sunyi.config.server.web.mng;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

/**
 * @author sunyi
 */
@Controller
public class IndexController extends MngBaseController {

    @RequestMapping("/")
    public String index(){
        return "redirect:/mng/config/list";
    }

}