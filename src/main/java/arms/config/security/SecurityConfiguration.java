package arms.config.security;

import java.net.URI;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.reactive.EnableWebFluxSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity;
import org.springframework.security.config.web.server.ServerHttpSecurity.CorsSpec;
import org.springframework.security.config.web.server.ServerHttpSecurity.CsrfSpec;
import org.springframework.security.web.server.DefaultServerRedirectStrategy;
import org.springframework.security.web.server.SecurityWebFilterChain;
import org.springframework.security.web.server.authentication.logout.DelegatingServerLogoutHandler;
import org.springframework.security.web.server.authentication.logout.SecurityContextServerLogoutHandler;
import org.springframework.security.web.server.authentication.logout.ServerLogoutSuccessHandler;
import org.springframework.security.web.server.authentication.logout.WebSessionServerLogoutHandler;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@Configuration
@EnableWebFluxSecurity
@EnableGlobalMethodSecurity(jsr250Enabled = true)
public class SecurityConfiguration {

    @Bean
    public SecurityWebFilterChain securityWebFilterChain(ServerHttpSecurity http) {

        DelegatingServerLogoutHandler logoutHandler = new DelegatingServerLogoutHandler(
                new WebSessionServerLogoutHandler(),
                new SecurityContextServerLogoutHandler()
        );

        ServerLogoutSuccessHandler successHandler = (exchange, authentication)
                -> new DefaultServerRedirectStrategy().sendRedirect(
                exchange.getExchange()
                , URI.create("http://www.313.co.kr/auth/realms/master/protocol/openid-connect/logout")
        );

        return http
                .cors(CorsSpec::disable)
                .csrf(CsrfSpec::disable)
                .authorizeExchange(
                        authorize -> authorize
                                .pathMatchers("/swagger-ui/**").permitAll()
                                .pathMatchers("/swagger-resources/**").permitAll()
                                .pathMatchers("/v2/api-docs/**").permitAll()
                                .pathMatchers("/dwr/**").permitAll()
                                .pathMatchers("/auth-anon/**").permitAll()
                                .pathMatchers("/auth-user/**").hasAnyRole("USER", "ADMIN")
                                .pathMatchers("/auth-admin/**").hasRole("ADMIN")
                                .pathMatchers("/auth-check/**").hasAnyRole("USER", "ADMIN")
                                .anyExchange().authenticated()
                )
                .exceptionHandling()
                .authenticationEntryPoint(
                        (exchange, denied)
                                -> Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Session is null"))
                )
                .and()
                .oauth2Login()
                .and()
                .formLogin()
                .loginPage("")
                .and()
                .logout(logout -> logout
                        .logoutHandler(logoutHandler)
                        .logoutSuccessHandler(successHandler)
                )
                .build();
    }


}
