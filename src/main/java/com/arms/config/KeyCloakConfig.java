package com.arms.config;

import lombok.extern.slf4j.Slf4j;
import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.stereotype.Component;

@Slf4j
@Component
public class KeyCloakConfig {

    public static String serverUrl;
    public static String realm;
    public static String clientId;
    public static String clientSecret;
    public static Keycloak keycloak;

    @Value("${spring.security.oauth2.client.registration.middle-proxy.server-url}")
    public void setServerUrl(String serverUrl) {
        KeyCloakConfig.serverUrl = serverUrl;
    }

    @Value("${spring.security.oauth2.client.registration.middle-proxy.realm}")
    public void setRealm(String realm) {
        KeyCloakConfig.realm = realm;
    }

    @Value("${spring.security.oauth2.client.registration.middle-proxy.client-id}")
    public void setClientId(String clientId) {
        KeyCloakConfig.clientId = clientId;
    }

    @Value("${spring.security.oauth2.client.registration.middle-proxy.client-secret}")
    public void setClientSecret(String clientSecret) {
        KeyCloakConfig.clientSecret = clientSecret;
    }

    @Bean
    public static Keycloak getKeycloakInstance() {

        if(keycloak == null){
            keycloak = KeycloakBuilder.builder()
                    .serverUrl(serverUrl)
                    .realm(realm)
                    .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
                    .clientId(clientId)
                    .clientSecret(clientSecret)
                    .build();
        }

        return keycloak;
    }

}