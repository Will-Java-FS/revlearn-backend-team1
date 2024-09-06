package com.revlearn.team1.service.securityContext;

import com.revlearn.team1.enums.Roles;
import com.revlearn.team1.model.User;
import org.springframework.security.access.AuthorizationServiceException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

public interface SecurityContextService {
    static Long getUserId() {
//        System.out.println("authorities: " + SecurityContextHolder.getContext().getAuthentication().getAuthorities());

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();

            if (principal instanceof User user) {
                return (long) user.getId();
            } else
                throw new AuthorizationServiceException("User is authenticated with object other than User.  (Probably did not send a JWT in request).");
        } else throw new AuthorizationServiceException("Failed to acquire user authentication information.");
    }

    static Roles getUserRole() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof User user) {
                return user.getRole();
            } else
                throw new AuthorizationServiceException("User is authenticated with object other than User.  (Probably did not send a JWT in request).");
        } else throw new AuthorizationServiceException("Failed to acquire user authentication information.");
    }
}
