package com.finfusion.APS.service.exception;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.Date;

@ControllerAdvice
public class GlobalExceptionHandler {
    @ExceptionHandler(
            {
                    EntityNotFoundException.class,
                    ExpiredJwtException.class,
                    EmailAlreadyInUseException.class,
                    UserAuthenticationException.class,
                    AssetUpdateException.class,
                    EntitySaveException.class
            }
    )
    public ResponseEntity<ErrorDetails> handleException(Exception ex) {
        final HttpStatus exceptionStatus = determineHttpStatus(ex);
        return generateExceptionResponse(exceptionStatus, ex);
    }

    private HttpStatus determineHttpStatus(Exception ex) {
        if (ex instanceof EntityNotFoundException) {
            return HttpStatus.NOT_FOUND;
        } else if (ex instanceof ExpiredJwtException || ex instanceof UserAuthenticationException || ex instanceof TokenValidationException) {
            return HttpStatus.UNAUTHORIZED;
        } else if (ex instanceof EmailAlreadyInUseException) {
            return HttpStatus.BAD_REQUEST;
        } else if (ex instanceof AssetUpdateException) {
            return HttpStatus.FORBIDDEN;
        } else if (ex instanceof EntitySaveException) {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        } else {
            return HttpStatus.INTERNAL_SERVER_ERROR;
        }
    }

    private ResponseEntity<ErrorDetails> generateExceptionResponse(HttpStatus status, Throwable ex) {
        ErrorDetails errorDetails = new ErrorDetails(new Date(), status, ex.getMessage());
        return new ResponseEntity<>(errorDetails, status);
    }
}