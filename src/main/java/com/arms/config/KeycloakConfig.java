package com.arms.config;

import org.keycloak.OAuth2Constants;
import org.keycloak.admin.client.Keycloak;
import org.keycloak.admin.client.KeycloakBuilder;
import org.keycloak.admin.client.resource.RealmResource;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class KeycloakConfig {
    @Value("${spring.security.oauth2.client.registration.middle-proxy.realm}")
    private String realm;
    @Value("${spring.security.oauth2.client.registration.middle-proxy.server-url}")
    private String serverUrl;
    @Value("${spring.security.oauth2.client.registration.middle-proxy.client-id}")
    private String clientId;
    @Value("${spring.security.oauth2.client.registration.middle-proxy.client-secret}")
    private String clientSecret;

    @Bean
    public Keycloak keycloak(){
        return KeycloakBuilder.builder()
            .serverUrl(serverUrl)
            .realm(realm)
            .grantType(OAuth2Constants.CLIENT_CREDENTIALS)
            .clientId(clientId)
            .clientSecret(clientSecret)
            .build();
    }


    @Bean
    public RealmResource realmResource(Keycloak keycloak){
        return keycloak.realm(realm);
    }


}
