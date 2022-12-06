package com.devsuperior.dscatalog.resources.exceptions;

import com.devsuperior.dscatalog.services.exceptions.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
}
