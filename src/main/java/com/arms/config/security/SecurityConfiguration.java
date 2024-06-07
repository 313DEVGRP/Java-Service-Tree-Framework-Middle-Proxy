package com.arms.config.security;

import java.util.Collection;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcReactiveOAuth2UserService;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.userinfo.ReactiveOAuth2UserService;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.oauth2.core.oidc.user.OidcUserAuthority;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.logout.DelegatingServerLogoutHandler;
import org.springframework.security.web.server.authentication.logout.WebSessionServerLogoutHandler;
import org.springframework.security.web.server.context.NoOpServerSecurityContextRepository;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.web.server.ServerWebExchange;

import lombok.extern.slf4j.Slf4j;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true)
@Slf4j
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
                                .pathMatchers("/engine-search-api/**").hasAnyRole("USER", "MANAGER", "ADMIN")
                                .pathMatchers("/dwr/**").permitAll()
                                .pathMatchers("/auth-anon/**","/auth-sche/**").permitAll()
                                .pathMatchers("/auth-user/**","/auth-check/**").hasAnyRole("USER", "MANAGER", "ADMIN")
                                .pathMatchers("/auth-manager/**").hasAnyRole("MANAGER", "ADMIN")
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


    @Bean
    public ReactiveOAuth2UserService<OidcUserRequest, OidcUser> oidcUserService() {
        final OidcReactiveOAuth2UserService delegate = new OidcReactiveOAuth2UserService();

        return (userRequest) -> {
            // Delegate to the default implementation for loading a user
            return delegate.loadUser(userRequest)
                .doOnError(err -> log.info("Err: ", err.getCause()))
                .doOnSuccess(user -> log.info("Auths " + user.getAuthorities()))
                .map(user -> {
                    Set<GrantedAuthority> mappedAuthorities = new HashSet<>();

                    user.getAuthorities().forEach(authority -> {
                        if (authority instanceof OidcUserAuthority) {
                            OidcUserAuthority oidcUserAuthority = (OidcUserAuthority) authority;
                            mappedAuthorities.addAll(extractAuthorityFromClaims(oidcUserAuthority.getUserInfo().getClaims()));
                        }
                    });

                    return new DefaultOidcUser(mappedAuthorities, user.getIdToken(), user.getUserInfo());
                });
        };
    }

    private List<GrantedAuthority> extractAuthorityFromClaims(Map<String, Object> claims) {
        return mapRolesToGrantedAuthorities(getRolesFromClaims(claims));
    }

    private Collection<String> getRolesFromClaims(Map<String, Object> claims) {

        LinkedHashMap<String, LinkedHashMap<String,List<String>>> realm_access = ((LinkedHashMap)claims.get("realm_access"));

        if(realm_access==null){
            return List.of("ROLE_USER");
        }

        if(realm_access.get("roles")==null){
            return List.of("ROLE_USER");
        }

        return (Collection<String>) realm_access.get("roles");

    }

    private  List<GrantedAuthority> mapRolesToGrantedAuthorities(Collection<String> roles) {
        return roles.stream().filter(role -> role.startsWith("ROLE_")).map(SimpleGrantedAuthority::new).collect(
            Collectors.toList());
    }

}
