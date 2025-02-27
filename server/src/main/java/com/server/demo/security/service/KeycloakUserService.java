package com.server.demo.security.service;

import java.util.List;
import java.util.Map;

import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.resource.UserResource;
import org.keycloak.representations.idm.UserRepresentation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.server.demo.exception.BusinessException;

import lombok.extern.slf4j.Slf4j;
@Service
@Slf4j
public class KeycloakUserService {

    private final Keycloak keycloak;

    @Value("${keycloak.realm}")
    private String realm;

    public KeycloakUserService(Keycloak keycloak) {
        this.keycloak = keycloak;
    }

    public void updateUserAttributes(String userId, Map<String, List<String>> attributes) {
        try {
            UserResource userResource = keycloak.realm(realm).users().get(userId);
            UserRepresentation user = userResource.toRepresentation();

            log.debug("Atualizando atributos para usuário: {}", userId);

            user.setAttributes(attributes);

            userResource.update(user);

        } catch (Exception e) {
            log.error("Erro ao atualizar atributos do usuário: {}", e.getMessage(), e);
            throw new BusinessException(String.format("Erro ao atualizar informações do usuário %s", e));
        }
    }

    public UserRepresentation getUserInfo(String userId) {
        try {
            UserResource userResource = keycloak.realm(realm).users().get(userId);
            log.debug("Atualizando atributos para usuário: {}", userId);

            return userResource.toRepresentation();
        } catch (Exception e) {
            log.error("Erro ao buscar usuário: {}", e.getMessage(), e);
            throw new BusinessException(String.format("Erro ao buscar informações do usuário %s", e));
        }
    }
}