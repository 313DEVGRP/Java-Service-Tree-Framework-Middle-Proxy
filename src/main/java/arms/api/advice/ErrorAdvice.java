package arms.api.advice;


import org.springframework.http.HttpStatus;
import org.springframework.security.oauth2.client.ClientAuthorizationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;
import reactor.core.publisher.Mono;
import arms.api.response.CommonResponse.ApiResult;
import arms.api.response.ErrorCode;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.support.WebExchangeBindException;

import static arms.api.response.CommonResponse.error;

@ControllerAdvice
public class ErrorAdvice {

  @ExceptionHandler(value = ClientAuthorizationException.class)
  public Mono<?> onException() {
    return Mono.error(new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Session is null"));
  }

  private  <E> ResponseEntity<ApiResult<E>> newResponse(String message, ErrorCode errorCode, HttpStatus status) {
    HttpHeaders headers = getHttpHeaders();
    return new ResponseEntity<>(error(message, errorCode, status), headers, status);
  }

  private  <E> ResponseEntity<ApiResult<E>> newResponse(ErrorCode errorCode, HttpStatus status) {
    HttpHeaders headers = getHttpHeaders();
    return new ResponseEntity<>(error(errorCode, status), headers, status);
  }

  private  HttpHeaders getHttpHeaders() {
    HttpHeaders headers = new HttpHeaders();
    headers.add("Content-Type", "application/json");
    return headers;
  }

  @ExceptionHandler(WebExchangeBindException.class)
  public <E> ResponseEntity<ApiResult<E>> handleException(WebExchangeBindException e) {
    BindingResult bindingResult = e.getBindingResult();
    FieldError fe = bindingResult.getFieldError();
    if (fe != null) {
      String message = fe.getDefaultMessage();
      return newResponse(message, ErrorCode.COMMON_INVALID_PARAMETER, HttpStatus.BAD_REQUEST);
    } else {
      return newResponse(ErrorCode.COMMON_INVALID_PARAMETER,HttpStatus.BAD_REQUEST);
    }
  }

  @ExceptionHandler(IllegalArgumentException.class)
  public <E> ResponseEntity<ApiResult<E>> handleArgumentException(IllegalArgumentException e) {
    return newResponse(e.getMessage(),ErrorCode.COMMON_INVALID_PARAMETER,HttpStatus.BAD_REQUEST);
  }

}
