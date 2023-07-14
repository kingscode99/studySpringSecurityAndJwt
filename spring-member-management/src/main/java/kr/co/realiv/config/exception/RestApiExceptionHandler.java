package kr.co.realiv.config.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class RestApiExceptionHandler {

    @ExceptionHandler(value = {CheckApiException.class})
    public ResponseEntity<Object> handleApiRequestException(CheckApiException checkApiException) {
        return ResponseEntity.status(checkApiException.getErrorCode().getHttpStatus()).body(new RestApiException(checkApiException.getErrorCode()));
    }
}
