package io.sunyi.config.admin.web;

import io.sunyi.config.server.service.AppService;
import io.sunyi.config.server.service.ConfigService;
import io.sunyi.config.server.service.EnvService;
import io.sunyi.config.commons.model.Config;
import io.sunyi.config.commons.model.ConfigHistory;
import io.sunyi.config.server.dao.ConfigDao;
import io.sunyi.config.admin.AdminConstants;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

/**
 * @author sunyi
 */
@Controller
@RequestMapping("/config")
public class ConfigController extends AdminBaseController {

    @Autowired
    private AppService appService;

    @Autowired
    private EnvService envService;

    @Autowired
    private ConfigService configService;

    @Autowired
    private ConfigDao configDao;

    @RequestMapping(value = "/add", method = RequestMethod.GET)
    public String toAdd(@RequestParam(required = false) String app, @RequestParam(required = false) String env, Model model) {

        // TODO 需要性能优化，但数据量太少，暂时不重要
        boolean hasApp = appService.hasApp();
        model.addAttribute("showNoAppWarn", !hasApp);
        boolean hasEnv = envService.hasEnv();
        model.addAttribute("showNoEnvWarn", !hasEnv);
        List<String> appNames = appService.allNames();
        model.addAttribute("appNames", appNames);
        List<String> envNames = envService.allNames();
        model.addAttribute("envNames", envNames);
        if (!hasApp || !hasEnv) {
            //没有APP或者没有EVN，这种情况直接返回
            return "config/list";
        }

        model.addAttribute("app", app);
        model.addAttribute("env", env);

        return "config/add";
    }

    @RequestMapping(value = "/add", method = RequestMethod.POST)
    public String add(@ModelAttribute("config") Config config, Model model) {

        configService.add(config);

        model.addAttribute(AdminConstants.ALERT_MESSAGE_SUCC,"新增成功！");
        return "config/edit";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.GET)
    public String toEdit(@RequestParam Long id, Model model) {
        Config config = configDao.selectById(id);
        model.addAttribute("config", config);
        return "config/edit";
    }

    @RequestMapping(value = "/edit", method = RequestMethod.POST)
    public String edit(@ModelAttribute("config") Config config, Model model) {

        if (config.getContent() == null) {
            config.setContent("");
        }

        Config configResult = configService.edit(config);
        model.addAttribute("config", configResult);

        model.addAttribute(AdminConstants.ALERT_MESSAGE_SUCC,"修改成功！");
        return "config/edit";
    }

    @RequestMapping("/list")
    public String list(@RequestParam(required = false) String app, @RequestParam(required = false) String env, Model model) {

        // TODO 需要性能优化，但数据量太少，暂时不重要
        boolean hasApp = appService.hasApp();
        model.addAttribute("showNoAppWarn", !hasApp);
        boolean hasEnv = envService.hasEnv();
        model.addAttribute("showNoEnvWarn", !hasEnv);
        List<String> appNames = appService.allNames();
        model.addAttribute("appNames", appNames);
        List<String> envNames = envService.allNames();
        model.addAttribute("envNames", envNames);

        if (!hasApp || !hasEnv) {
            //没有APP或者没有EVN，这种情况直接返回
            return "config/list";
        }

        if (StringUtils.isBlank(app)) {
            model.addAttribute("showHelpInfo", true);
            return "config/list";
        }

        model.addAttribute("app", app);
        if (StringUtils.isBlank(env)) {
            env = envNames.get(0);
        }
        model.addAttribute("env", env);

        List<Config> configs = configDao.selectByParam(app, env, null);
        model.addAttribute("configs", configs);

        return "config/list";
    }

    @RequestMapping("/history")
    public String history(@RequestParam Long id, Model model) {

        Config config = configDao.selectById(id);
        model.addAttribute("config", config);

        List<ConfigHistory> histories = configService.getHistory(id);
        model.addAttribute("histories", histories);

        return "config/history";
    }

}