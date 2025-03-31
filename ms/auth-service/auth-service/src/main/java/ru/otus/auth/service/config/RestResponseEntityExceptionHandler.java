package ru.otus.auth.service.config;

import jakarta.persistence.EntityExistsException;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import ru.otus.auth.service.error.ErrorDto;

import java.util.NoSuchElementException;

@ControllerAdvice
public class RestResponseEntityExceptionHandler
        extends ResponseEntityExceptionHandler {

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
}