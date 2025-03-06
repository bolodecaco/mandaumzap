package com.server.demo.services;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.instancio.Instancio;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;

import com.server.demo.exception.BusinessException;

@ExtendWith(MockitoExtension.class)
class KeycloakServiceTest {

    @InjectMocks
    private KeycloakService keycloakService;

    @Mock
    private SecurityContext securityContext;

    @Mock
    private Authentication authentication;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.setContext(securityContext);
        when(securityContext.getAuthentication()).thenReturn(authentication);
    }

    @Test
    void getCurrentUserId_ShouldReturnUserId() {
        String expectedUserId = "testUser";
        when(authentication.getName()).thenReturn(expectedUserId);

        String actualUserId = keycloakService.getCurrentUserId();
        assertEquals(expectedUserId, actualUserId);
    }

    @Test
    void validateUserAccess_ShouldNotThrowException_WhenUserIsAdmin() {
        String currentUserId = "adminUser";
        when(authentication.getName()).thenReturn(currentUserId);
        when(authentication.getAuthorities()).thenAnswer(invocation -> List.of((GrantedAuthority) () -> "ROLE_ADMIN"));

        assertDoesNotThrow(() -> keycloakService.validateUserAccess("anotherUser"));
    }

    @Test
    void validateUserAccess_ShouldThrowException_WhenUserIsNotAdminAndNotSameUser() {
        String currentUserId = "user1";
        when(authentication.getName()).thenReturn(currentUserId);
        when(authentication.getAuthorities()).thenAnswer(invocation -> List.of((GrantedAuthority) () -> "ROLE_USER"));

        BusinessException exception = assertThrows(BusinessException.class, () -> 
            keycloakService.validateUserAccess("user2"));
        
        assertEquals("User not authorized to perform this action", exception.getMessage());
    }

    @Test
    void hasRole_ShouldReturnTrue_WhenUserHasRole() {
        when(authentication.getAuthorities()).thenAnswer(invocation -> List.of((GrantedAuthority) () -> "ROLE_USER"));

        assertTrue(keycloakService.hasRole("ROLE_USER"));
    }

    @Test
    void hasRole_ShouldReturnFalse_WhenUserDoesNotHaveRole() {
        when(authentication.getAuthorities()).thenAnswer(invocation -> List.of((GrantedAuthority) () -> "ROLE_USER"));

        assertFalse(keycloakService.hasRole("ROLE_ADMIN"));
    }
}
