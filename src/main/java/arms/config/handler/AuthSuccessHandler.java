package arms.config.handler;

import arms.config.handler.component.AuthSuccessAfterDuplicateUserRemove;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;
import org.springframework.security.web.server.WebFilterExchange;
import org.springframework.security.web.server.authentication.RedirectServerAuthenticationSuccessHandler;
import reactor.core.publisher.Mono;

public class AuthSuccessHandler extends RedirectServerAuthenticationSuccessHandler {

    private final AuthSuccessAfterDuplicateUserRemove authSuccessAfterDuplicateUserRemove;

    public AuthSuccessHandler(AuthSuccessAfterDuplicateUserRemove authSuccessAfterDuplicateUserRemove, String redirectUrl) {
        super(redirectUrl);
        this.authSuccessAfterDuplicateUserRemove = authSuccessAfterDuplicateUserRemove;
    }

    @Override
    public Mono<Void> onAuthenticationSuccess(WebFilterExchange webFilterExchange, Authentication authentication) {

        OidcUser oidcUser = (OidcUser) authentication.getPrincipal();
        String preferredUsername = oidcUser.getPreferredUsername();

        return webFilterExchange.getExchange().getSession()
                .flatMap(session -> authSuccessAfterDuplicateUserRemove.removeSession(session.getId(), preferredUsername))
                .then(super.onAuthenticationSuccess(webFilterExchange,authentication));

    }


}