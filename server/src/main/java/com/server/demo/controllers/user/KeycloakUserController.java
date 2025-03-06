package com.server.demo.controllers.user;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.server.demo.security.service.KeycloakUserService;
import com.server.demo.security.service.SecurityService;

import org.keycloak.representations.idm.UserRepresentation;

import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class KeycloakUserController {

    private final SecurityService securityService;
    private final KeycloakUserService keycloakUserService;

    @GetMapping("/info")
    public ResponseEntity<Map<String, Object>> getUserInfo(@AuthenticationPrincipal Jwt jwt) {
        try {
            var userInfo = new HashMap<String, Object>();

            userInfo.put("username", jwt.getClaimAsString("preferred_username"));
            userInfo.put("email", jwt.getClaimAsString("email"));
            userInfo.put("firstName", jwt.getClaimAsString("given_name"));
            userInfo.put("lastName", jwt.getClaimAsString("family_name"));
            userInfo.put("roles", securityService.getCurrentUserRoles());

            String userId = jwt.getSubject();
            UserRepresentation user = keycloakUserService.getUserInfo(userId);

            Map<String, List<String>> attributes = user.getAttributes();
            if (attributes != null) {
                userInfo.put("nomeDaMae", getFirstAttributeValue(attributes, "nomeDaMae"));
                userInfo.put("cidade", getFirstAttributeValue(attributes, "cidade"));
                userInfo.put("estado", getFirstAttributeValue(attributes, "estado"));
                userInfo.put("celular", getFirstAttributeValue(attributes, "celular"));
            }

            return ResponseEntity.ok(userInfo);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().build();
        }
    }

    private String getFirstAttributeValue(Map<String, List<String>> attributes, String key) {
        List<String> values = attributes.get(key);
        return values != null && !values.isEmpty() ? values.get(0) : null;
    }

    @GetMapping("/roles")
    public ResponseEntity<Collection<String>> getUserRoles() {
        return ResponseEntity.ok(securityService.getCurrentUserRoles());
    }
}