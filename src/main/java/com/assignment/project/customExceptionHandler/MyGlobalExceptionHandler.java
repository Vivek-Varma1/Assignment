package com.assignment.project.customExceptionHandler;

import com.assignment.project.response.ErrorResponse;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class MyGlobalExceptionHandler {

    /*
        Validation Exception
     */
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<Map<String, String>>
    handleValidationException(
            MethodArgumentNotValidException e
    ) {

        Map<String, String> response = new HashMap<>();

        e.getBindingResult()
                .getAllErrors()
                .forEach(err -> {

                    String fieldName =
                            ((FieldError) err).getField();

                    String message =
                            err.getDefaultMessage();

                    response.put(fieldName, message);
                });

        return new ResponseEntity<>(
                response,
                HttpStatus.BAD_REQUEST
        );
    }

    /*
        Resource Not Found
     */
    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ErrorResponse>
    handleResourceNotFoundException(
            ResourceNotFoundException e
    ) {

        ErrorResponse response =
                new ErrorResponse(
                        false,
                        e.getMessage(),
                        HttpStatus.NOT_FOUND.value(),
                        LocalDateTime.now()
                );

        return new ResponseEntity<>(
                response,
                HttpStatus.NOT_FOUND
        );
    }

    /*
        API Exception
     */
    @ExceptionHandler(ApiException.class)
    public ResponseEntity<ErrorResponse>
    handleApiException(ApiException e) {

        ErrorResponse response =
                new ErrorResponse(
                        false,
                        e.getMessage(),
                        HttpStatus.BAD_REQUEST.value(),
                        LocalDateTime.now()
                );

        return new ResponseEntity<>(
                response,
                HttpStatus.BAD_REQUEST
        );
    }

    /*
        Rate Limit Exception
     */
    @ExceptionHandler(RateLimitException.class)
    public ResponseEntity<ErrorResponse>
    handleRateLimitException(
            RateLimitException e
    ) {

        ErrorResponse response =
                new ErrorResponse(
                        false,
                        e.getMessage(),
                        HttpStatus.TOO_MANY_REQUESTS.value(),
                        LocalDateTime.now()
                );

        return new ResponseEntity<>(
                response,
                HttpStatus.TOO_MANY_REQUESTS
        );
    }

    /*
        Generic Exception
     */
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse>
    handleGenericException(Exception e) {

        ErrorResponse response =
                new ErrorResponse(
                        false,
                        e.getMessage(),
                        HttpStatus.INTERNAL_SERVER_ERROR.value(),
                        LocalDateTime.now()
                );

        return new ResponseEntity<>(
                response,
                HttpStatus.INTERNAL_SERVER_ERROR
        );
    }
}