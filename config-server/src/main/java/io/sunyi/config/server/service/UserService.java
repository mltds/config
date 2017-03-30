package io.sunyi.config.server.service;

import org.springframework.stereotype.Service;

/**
 * 每个公司有各自的SSO实现，这里默认 system
 * @author sunyi
 */
@Service
public class UserService {

    public String getCurrentUser() {
        return "system";
    }

}
