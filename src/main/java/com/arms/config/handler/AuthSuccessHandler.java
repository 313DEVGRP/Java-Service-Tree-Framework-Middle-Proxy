package com.arms.config.handler;

import java.net.URI;

import com.arms.config.handler.component.AuthSuccessAfterDuplicateUserRemove;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;

import reactor.core.publisher.Mono;

public class AuthSuccessHandler extends RedirectServerAuthenticationSuccessHandler {

    private final AuthSuccessAfterDuplicateUserRemove authSuccessAfterDuplicateUserRemove;

    private final String redirectUrl;


    public AuthSuccessHandler(AuthSuccessAfterDuplicateUserRemove authSuccessAfterDuplicateUserRemove, String redirectUrl) {
        this.redirectUrl = redirectUrl;
        this.authSuccessAfterDuplicateUserRemove = authSuccessAfterDuplicateUserRemove;
    }


    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {

        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
        String preferredUsername = oidcUser.getPreferredUsername();

        webFilterExchange.getExchange().getSession()
            .map(session -> (String)session.getAttributes().get("rd-page"))
            .subscribe(rdPage->{
                if(rdPage!=null){
                    super.setLocation(URI.create(rdPage));
                }else{
                    super.setLocation(URI.create(redirectUrl));
                };
            });

        return webFilterExchange.getExchange().getSession()
                //중복 로그인 제거
                //.flatMap(session -> authSuccessAfterDuplicateUserRemove.removeSession(session.getId(), preferredUsername))
                .then(super.onAuthenticationSuccess(webFilterExchange,authentication));

    }


}
