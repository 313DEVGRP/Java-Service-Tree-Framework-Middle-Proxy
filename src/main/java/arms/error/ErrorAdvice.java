package arms.error;


import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.client.ClientAuthorizationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;

@ControllerAdvice
public class ErrorAdvice {

  @ExceptionHandler(value = ClientAuthorizationException.class)
  public Mono<?> onException() {
    System.out.println("@@@@@@@@@@@@@@@@@@@");
    return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Session is null"));
  }

}
