package io.sunyi.config.admin.web;

import io.sunyi.config.server.service.EnvService;
import io.sunyi.config.commons.model.Env;
import io.sunyi.config.server.dao.EnvDao;
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
@RequestMapping("/env")
public class EnvController extends AdminBaseController {

    @Autowired
    private EnvDao envDao;

    @Autowired
    private EnvService envService;

    @RequestMapping("/list")
    public String list(Model model) {
        List<Env> envs = envDao.selectAll();
        model.addAttribute("envs", envs);
        return "env/list";
    }

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String toAdd() {
        return "env/add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String add(@RequestParam String name, Model model) {
        envService.add(name);
        return "redirect:/env/list";
    }
}