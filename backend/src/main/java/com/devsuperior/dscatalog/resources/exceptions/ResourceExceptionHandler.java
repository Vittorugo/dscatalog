package com.devsuperior.dscatalog.resources.exceptions;

import com.devsuperior.dscatalog.services.exceptions.DataBaseException;
import com.devsuperior.dscatalog.services.exceptions.EntityNotFoundException;
import com.devsuperior.dscatalog.services.exceptions.PropertyNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import javax.servlet.http.HttpServletRequest;
import java.time.Instant;

@ControllerAdvice
public class ResourceExceptionHandler {

    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<MessageError> entityNotFoundHandler(EntityNotFoundException exception, HttpServletRequest servletRequest) {
        MessageError error = MessageError.builder()
                .message(exception.getMessage())
                .status(HttpStatus.NOT_FOUND.value())
                .path(servletRequest.getRequestURI())
                .time(Instant.now())
                .error("Entity not Found")
                .build();
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(error);
    }

    @ExceptionHandler(PropertyNotFoundException.class)
    public ResponseEntity<MessageError> propertyNotFoundHandler(PropertyNotFoundException exception, HttpServletRequest servletRequest) {
        MessageError error = MessageError.builder()
                .time(Instant.now())
                .error("Property Not Found")
                .message(exception.getMessage())
                .path(servletRequest.getRequestURI())
                .status(HttpStatus.BAD_REQUEST.value())
                .build();
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(error);
    }

    @ExceptionHandler(DataBaseException.class)
    public ResponseEntity<MessageError> dataBasehandler(DataBaseException exception, HttpServletRequest servletRequest) {
        HttpStatus status = HttpStatus.BAD_REQUEST;
        MessageError error = MessageError.builder()
                .time(Instant.now())
                .message(exception.getMessage())
                .error("Integrity violation")
                .path(servletRequest.getRequestURI())
                .status(status.value())
                .build();
        return ResponseEntity.status(status).body(error);
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ValidationError> methodArgumentNotValidExceptionHandler(MethodArgumentNotValidException exception, HttpServletRequest servletRequest) {
        HttpStatus status = HttpStatus.UNPROCESSABLE_ENTITY;
        ValidationError error = new ValidationError();
        error.setTime(Instant.now());
        error.setMessage(exception.getMessage());
        error.setError("Validation Exception");
        error.setPath(servletRequest.getRequestURI());
        error.setStatus(status.value());

        exception.getBindingResult().getFieldErrors().forEach(fieldError -> {
            error.addError(fieldError.getField(), fieldError.getDefaultMessage());
        });

        return ResponseEntity.status(status).body(error);
    }
}
