package com.yvolabs.book.handler;

import com.yvolabs.book.exception.ActivationTokenException;
import jakarta.mail.MessagingException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.LockedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static com.yvolabs.book.handler.BusinessErrorCodes.*;
import static org.springframework.http.HttpStatus.BAD_REQUEST;
import static org.springframework.http.HttpStatus.UNAUTHORIZED;

/**
 * @author Yvonne N
 * @version 1.0
 * @since 29/07/2024
 */

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ExceptionResponse> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
        Set<String> errors = new HashSet<>();
        e.getBindingResult().getAllErrors()
                .forEach(error -> {
                    var errorMessage = error.getDefaultMessage();
                    errors.add(errorMessage);
                });

        return ResponseEntity
                .status(BAD_REQUEST)
                .body(ExceptionResponse.builder()
                        .businessErrorCode(BAD_REQUEST.value())
                        .businessErrorDescription("validation error")
                        .validationErrors(errors)
                        .build()
                );
    }

    @ExceptionHandler(ActivationTokenException.class)
    public ResponseEntity<ExceptionResponse> handleActivationTokenException(ActivationTokenException e) {
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(ExceptionResponse.builder()
                        .businessErrorCode(INVALID_ACTIVATION_TOKEN.getCode())
                        .businessErrorDescription(INVALID_ACTIVATION_TOKEN.getMessage())
                        .errors(Map.of("message", e.getMessage()))
                        .build()
                );
    }

    @ExceptionHandler(BadCredentialsException.class)
    public ResponseEntity<ExceptionResponse> handleBadCredentialsException(BadCredentialsException e) {
        return ResponseEntity
                .status(UNAUTHORIZED)
                .body(ExceptionResponse.builder()
                        .businessErrorCode(BAD_CREDENTIALS.getCode())
                        .businessErrorDescription(BAD_CREDENTIALS.getMessage())
                        .errors(Map.of("message", e.getMessage()))
                        .build()
                );
    }

    @ExceptionHandler(LockedException.class)
    public ResponseEntity<ExceptionResponse> handleAccountLockedException(LockedException e) {
        return ResponseEntity
                .status(UNAUTHORIZED)
                .body(ExceptionResponse.builder()
                        .businessErrorCode(ACCOUNT_LOCKED.getCode())
                        .businessErrorDescription(ACCOUNT_LOCKED.getMessage())
                        .errors(Map.of("message", e.getMessage()))
                        .build()
                );
    }

    @ExceptionHandler(DisabledException.class)
    public ResponseEntity<ExceptionResponse> handleAccountDisabledException(DisabledException e) {
        return ResponseEntity
                .status(UNAUTHORIZED)
                .body(ExceptionResponse.builder()
                        .businessErrorCode(ACCOUNT_DISABLED.getCode())
                        .businessErrorDescription(ACCOUNT_DISABLED.getMessage())
                        .errors(Map.of("message", e.getMessage()))
                        .build()
                );
    }

    @ExceptionHandler({MessagingException.class})
    public ResponseEntity<ExceptionResponse> handleMessagingException(MessagingException e) {
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(ExceptionResponse.builder()
                        .businessErrorCode(INTERNAL_SERVER_ERROR.getCode())
                        .businessErrorDescription(e.getMessage())
                        .errors(Map.of("message", e.getMessage()))
                        .build()
                );
    }


    @ExceptionHandler(Exception.class)
    public ResponseEntity<ExceptionResponse> handleAllUnHandledExceptions(Exception e) {
        e.printStackTrace();
        return ResponseEntity
                .status(BAD_REQUEST)
                .body(ExceptionResponse.builder()
                        .businessErrorCode(INTERNAL_SERVER_ERROR.getCode())
                        .businessErrorDescription(INTERNAL_SERVER_ERROR.getMessage())
                        .errors(Map.of("message", e.getMessage()))
                        .build()
                );

    }
}
