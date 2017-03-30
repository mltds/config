package io.sunyi.config.server.web.mng;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.ExceptionHandler;

/**
 * @author sunyi
 */
public abstract class MngBaseController {

    public Logger logger = LoggerFactory.getLogger(this.getClass());

    @ExceptionHandler
    public String errorHandler(Exception e) {
        logger.error(MngBaseController.class.toString() + " Catch Error", e);
        return "mng/error";
    }

}