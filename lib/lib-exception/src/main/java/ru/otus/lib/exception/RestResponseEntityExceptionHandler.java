package ru.otus.lib.exception;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.otus.common.error.BusinessAppException;
import ru.otus.common.error.ErrorDto;

import java.util.HashMap;
import java.util.Map;
import java.util.NoSuchElementException;

@ControllerAdvice
public class RestResponseEntityExceptionHandler
        extends ResponseEntityExceptionHandler {

    private static final String UNAUTHORIZED = "unauthorized";

    @ExceptionHandler({ AuthenticationException.class })
    public ResponseEntity<ErrorDto> handleAuthenticationDeniedException(
            Exception ex, WebRequest request) {
        var message = ex.getMessage() != null ? ex.getMessage() : UNAUTHORIZED;
        var errorDto = ErrorDto.builder()
                .code(UNAUTHORIZED)
                .message(message)
                .build();
        return new ResponseEntity<ErrorDto>(errorDto, new HttpHeaders(), HttpStatus.UNAUTHORIZED);
    }

    @ExceptionHandler({ AccessDeniedException.class })
    public ResponseEntity<ErrorDto> handleAccessDeniedException(
            Exception ex, WebRequest request) {
        var errorDto = ErrorDto.builder()
                .code(UNAUTHORIZED)
                .message("Access denied")
                .build();
        return new ResponseEntity<ErrorDto>(errorDto, new HttpHeaders(), HttpStatus.FORBIDDEN);
    }

    @ExceptionHandler({ BusinessAppException.class })
    public ResponseEntity<ErrorDto> handleBusinessAppException(
            Exception ex, WebRequest request) {
        var appException = (BusinessAppException) ex;
        var errorDto = ErrorDto.builder()
                .code(appException.getCode())
                .message(appException.getMessage())
                .build();
        var status = appException.getHttpStatus() != null ?
                HttpStatus.valueOf(appException.getHttpStatus()) :
                HttpStatus.UNPROCESSABLE_ENTITY;
        return new ResponseEntity<ErrorDto>(errorDto, new HttpHeaders(), status);
    }

    @ExceptionHandler({NoSuchElementException.class, EntityNotFoundException.class})
    public ResponseEntity<ErrorDto> handleNoSuchElementException(Exception ex, WebRequest request) {
        var errorDto = ErrorDto.builder()
                .code("not.found")
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<ErrorDto>(errorDto, new HttpHeaders(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler({EntityExistsException.class})
    public ResponseEntity<ErrorDto> handleEntityExistsException(Exception ex, WebRequest request) {
        var errorDto = ErrorDto.builder()
                .code("entity.exists")
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<ErrorDto>(errorDto, new HttpHeaders(), HttpStatus.CONFLICT);
    }

    @ExceptionHandler({IllegalStateException.class, IllegalArgumentException.class})
    public ResponseEntity<ErrorDto> handleIllegalException(Exception ex, WebRequest request) {
        var errorDto = ErrorDto.builder()
                .code("unprocessable.entity")
                .message(ex.getMessage())
                .build();
        return new ResponseEntity<ErrorDto>(errorDto, new HttpHeaders(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ErrorDto> handle(Exception ex, WebRequest request) {
        var errorDto = ErrorDto.builder()
                .code("internal.system.error")
                .message("Internal System Error")
                .build();
        return new ResponseEntity<ErrorDto>(errorDto, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(
            MethodArgumentNotValidException ex,
            HttpHeaders headers,
            HttpStatusCode status,
            WebRequest request) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getFieldErrors().forEach(error ->
                errors.put(error.getField(), error.getDefaultMessage()));

        var errorDto = ErrorDto.builder()
                .code("bad.request")
                .message("Invalid arguments")
                .details(errors)
                .build();

        return ResponseEntity.status(HttpStatus.BAD_REQUEST)
                .body(errorDto);
    }
}