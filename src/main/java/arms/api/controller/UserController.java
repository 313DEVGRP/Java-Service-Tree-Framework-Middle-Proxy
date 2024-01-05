package arms.api.controller;

import com.nimbusds.jwt.JWT;
import com.nimbusds.jwt.JWTParser;
import java.net.URI;
import java.text.ParseException;
import java.util.Map;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.server.reactive.ServerHttpResponse;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClient;
import org.springframework.security.oauth2.client.annotation.RegisteredOAuth2AuthorizedClient;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ServerWebExchange;
import org.springframework.web.server.WebSession;
import reactor.core.publisher.Mono;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@Slf4j
public class UserController {

  @GetMapping("/auth-user/logout")
  public Mono<Void> logout(ServerWebExchange exchange){
    Mono<WebSession> sessionMono = exchange.getSession();

    return sessionMono.flatMap(webSession -> webSession.invalidate()
        .then(Mono.fromRunnable(() -> {
          ServerHttpResponse response = exchange.getResponse();
          response.setStatusCode(HttpStatus.SEE_OTHER);
          response.getHeaders().setLocation(
              URI.create("http://www.313.co.kr/auth/realms/master/protocol/openid-connect/logout"));
        }))
        .then(exchange.getResponse().setComplete()));

  }

  @GetMapping("/auth-user/session-id")
  public Mono<String> sessionId(ServerWebExchange exchange){
    Mono<WebSession> sessionMono = exchange.getSession();
    return sessionMono.map(WebSession::getId);
  }

  @CrossOrigin(origins = "http://www.313.co.kr:9999/")
  @GetMapping("/auth-user/me")
  public Mono<Map<String,Object>> getUser(
      @ApiIgnore @RegisteredOAuth2AuthorizedClient OAuth2AuthorizedClient auth2AuthorizedClient)
      throws ParseException {

    JWT parse = JWTParser.parse(auth2AuthorizedClient.getAccessToken().getTokenValue());

    return Mono.just(parse.getJWTClaimsSet().getClaims());
  }



}
