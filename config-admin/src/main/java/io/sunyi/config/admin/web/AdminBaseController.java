package io.sunyi.config.admin.web;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author sunyi
 */
public abstract class AdminBaseController {

    public Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler
    public String errorHandler(Exception e) {
        logger.error(AdminBaseController.class.toString() + " Catch Error", e);
        return "error";
    }

}