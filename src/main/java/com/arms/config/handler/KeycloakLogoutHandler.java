package com.arms.config.handler;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.logout.ServerLogoutHandler;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Mono;


@Component
@Slf4j
public class KeycloakLogoutHandler implements ServerLogoutHandler {

  private final WebClient webClient;
  private final String serverUrl;

  public KeycloakLogoutHandler(WebClient webClient, @Value("${spring.security.oauth2.client.registration.middle-proxy.server-url}")String serverUrl) {
    this.webClient = webClient;
    this.serverUrl = serverUrl;
  }

  @Override
  public Mono<Void> logout(WebFilterExchange exchange, Authentication authentication) {
    OidcUser principal = (OidcUser)authentication.getPrincipal();
    return logoutFromKeycloak(principal.getIdToken().getTokenValue());
  }

  public Mono<Void> logoutFromKeycloak(String idToken) {
    String endSessionEndpoint =  serverUrl+"/realms/master/protocol/openid-connect/logout";//추후에 yml 컨피그로 이동 시켜야함
    UriComponentsBuilder builder = UriComponentsBuilder
            .fromUriString(endSessionEndpoint)
            .queryParam("id_token_hint",idToken);

    return webClient.get()
            .uri(builder.toUriString())
            .exchangeToMono(response -> {
              if (response.statusCode().is2xxSuccessful()) {
                log.info("Successfully logged out from Keycloak");
                return Mono.empty();
              } else {
                log.error("Could not propagate logout to Keycloak");
                return Mono.error(new RuntimeException("Could not propagate logout to Keycloak"));
              }
            });
  }

}
