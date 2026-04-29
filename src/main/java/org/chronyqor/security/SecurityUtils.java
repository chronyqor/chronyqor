package org.chronyqor.security;

import org.chronyqor.domain.User;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public class SecurityUtils {

    public User getCurrentUser() {
        return (User) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
    }

    public Long getCurrentUserId() {
        return getCurrentUser().getId();
    }
}
