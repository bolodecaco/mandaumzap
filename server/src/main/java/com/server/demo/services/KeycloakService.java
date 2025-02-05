package com.server.demo.services;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import com.server.demo.exception.BusinessException;

@Service
public class KeycloakService {

    public String getCurrentUserId() {
        return SecurityContextHolder.getContext()
            .getAuthentication()
            .getName();
    }

    public void validateUserAccess(String userId) {
        String currentUserId = getCurrentUserId();
        boolean isAdmin = SecurityContextHolder.getContext()
            .getAuthentication()
            .getAuthorities()
            .stream()
            .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));

        if (!currentUserId.equals(userId) && !isAdmin) {
            throw new BusinessException("User not authorized to perform this action");
        }
    }

    public boolean hasRole(String role) {
        return SecurityContextHolder.getContext()
            .getAuthentication()
            .getAuthorities()
            .stream()
            .anyMatch(a -> a.getAuthority().equals(role));
    }
}
