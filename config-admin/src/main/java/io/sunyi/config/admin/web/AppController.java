package io.sunyi.config.admin.web;

import io.sunyi.config.commons.model.App;
import io.sunyi.config.server.dao.AppDao;
import io.sunyi.config.server.service.AppService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author sunyi
 */
@Controller
@RequestMapping("/app")
public class AppController extends AdminBaseController {

    @Autowired
    private AppDao appDao;

    @Autowired
    private AppService appService;

    @RequestMapping("/list")
    public String list(Model model) {
        List<App> apps = appDao.selectAll();
        model.addAttribute("apps", apps);
        return "app/list";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String toAdd() {
        return "app/add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String add(@RequestParam String name, Model model) {
        appService.add(name);
        return "redirect:app/list";
    }

}