package com.arms.config.security;

import com.arms.config.handler.AuthSuccessHandler;
import com.arms.config.handler.KeycloakLogoutHandler;
import com.arms.config.handler.component.AuthSuccessAfterDuplicateUserRemove;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity.CorsSpec;
import org.springframework.security.config.web.server.ServerHttpSecurity.CsrfSpec;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.logout.DelegatingServerLogoutHandler;
import org.springframework.security.web.server.authentication.logout.WebSessionServerLogoutHandler;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;

import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true)
public class SecurityConfiguration {

    @Value("${spring.security.auth.success.redirect-url}")
    private String redirectUrl;
    private final AuthSuccessAfterDuplicateUserRemove authSuccessAfterDuplicateUserRemove;
    private final KeycloakLogoutHandler keycloakLogoutHandler;

    public SecurityConfiguration(KeycloakLogoutHandler keycloakLogoutHandler
            , AuthSuccessAfterDuplicateUserRemove authSuccessAfterDuplicateUserRemove) {
        this.keycloakLogoutHandler = keycloakLogoutHandler;
        this.authSuccessAfterDuplicateUserRemove = authSuccessAfterDuplicateUserRemove;
    }

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

        DelegatingServerLogoutHandler logoutHandler = new DelegatingServerLogoutHandler(
                new WebSessionServerLogoutHandler()
                , keycloakLogoutHandler
        );

        return http
                .cors().disable()
                .csrf().disable()
                .cors(CorsSpec::disable)
                .csrf(CsrfSpec::disable)
                .authorizeExchange(
                        authorize -> authorize
                                .pathMatchers("/login").permitAll()
                                .pathMatchers("/middle-proxy-api").permitAll()
                                .pathMatchers("/middle-proxy-api/**").permitAll()
                                .pathMatchers("/backend-core-api").permitAll()
                                .pathMatchers("/backend-core-api/**").permitAll()
                                .pathMatchers("/engine-fire-api").permitAll()
                                .pathMatchers("/engine-fire-api/**").permitAll()
                                .pathMatchers("/auth-anon/**","/dwr/**").permitAll()
                                .pathMatchers("/auth-user/**","/auth-check/**").hasAnyRole("USER", "ADMIN")
                                .pathMatchers("/auth-admin/**").hasRole("ADMIN")
                                .anyExchange().authenticated()
                )
                .exceptionHandling()
                .authenticationEntryPoint(
                    (exchange, denied)
                        -> exchange.getSession().map(session->{
                            String referer = exchange.getRequest().getHeaders().getFirst("referer");
                            session.getAttributes().put("rd-page",referer);
                            return Mono.empty();
                       }).then( Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Session is null")))
                )
                .and()
                .oauth2Login()
                .authenticationSuccessHandler(
                    new AuthSuccessHandler(authSuccessAfterDuplicateUserRemove,redirectUrl)
                )
                .and()
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutHandler(logoutHandler)
                        .logoutSuccessHandler(((webFilterExchange, authentication) -> {
                            ServerWebExchange exchange = webFilterExchange.getExchange();

                            ServerHttpResponse response = exchange.getResponse();
                            response.setStatusCode(HttpStatus.OK);
                            return response.setComplete();
                        }))
                )
                .build();
    }

}