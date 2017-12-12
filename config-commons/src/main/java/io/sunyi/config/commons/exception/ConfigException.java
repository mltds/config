package io.sunyi.config.commons.exception;

/**
 * @author sunyi 2017/8/2.
 */
public class ConfigException extends  RuntimeException{

    public ConfigException() {
        super();
    }

    public ConfigException(String message) {
        super(message);
    }

    public ConfigException(String message, Throwable cause) {
        super(message, cause);
    }

    public ConfigException(Throwable cause) {
        super(cause);
    }

}
