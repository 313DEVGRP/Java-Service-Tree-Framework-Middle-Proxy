package com.arms.config.handler.component;

import com.arms.config.handler.KeycloakLogoutHandler;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.ReactiveRedisOperations;
import org.springframework.security.core.context.SecurityContextImpl;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.session.data.redis.ReactiveRedisSessionRepository;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class AuthSuccessAfterDuplicateUserRemove  {
    private final ReactiveRedisSessionRepository reactiveRedisSessionRepository;
    private final KeycloakLogoutHandler keycloakLogoutHandler;

    private static final String SESSION_KEY_PATTERN = "spring:session:sessions:";
    private static final String USER_INFO_KEY = "sessionAttr:SPRING_SECURITY_CONTEXT";

    public AuthSuccessAfterDuplicateUserRemove( ReactiveRedisSessionRepository reactiveRedisSessionRepository
                                                ,KeycloakLogoutHandler keycloakLogoutHandler
        ) {
        this.reactiveRedisSessionRepository = reactiveRedisSessionRepository;
        this.keycloakLogoutHandler = keycloakLogoutHandler;

    }

    public Mono<Void> removeSession(String currentSessionId, String preferredUsername){
        ReactiveRedisOperations<String, Object> sessionRedisOperations = reactiveRedisSessionRepository.getSessionRedisOperations();
        return sessionRedisOperations.keys(SESSION_KEY_PATTERN+"*")
            .flatMap(sessionKey -> sessionRedisOperations.opsForHash()
                .entries(sessionKey)
                .filter(attribute->USER_INFO_KEY.equals(attribute.getKey()))
                .flatMap(attribute->{
                    SecurityContextImpl securityContext = (SecurityContextImpl)attribute.getValue();
                    OidcUser oidcUser = (OidcUser)securityContext.getAuthentication().getPrincipal();
                    if(preferredUsername.equals(oidcUser.getPreferredUsername())){
                        String sessionId = sessionKey.replace(SESSION_KEY_PATTERN, "");
                        if(!currentSessionId.equals(sessionId)){
                            //TO-DO
                            //dwrClient.sendMessage(preferredUsername + "님 과 동일한 계정이 로그인 되었습니다.");
                            return keycloakLogoutHandler.logoutFromKeycloak(oidcUser.getIdToken().getTokenValue())
                                    .then(reactiveRedisSessionRepository.deleteById(sessionId));
                        }
                    }
                    return Mono.empty();
                })).then();
    }

}
