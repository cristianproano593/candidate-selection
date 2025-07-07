package com.example.common.exception;

import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorResponse> onValidationError(MethodArgumentNotValidException ex, HttpServletRequest req) {
        String msg = ex.getBindingResult()
                       .getFieldErrors()
                       .stream()
                       .map(fe -> fe.getField() + ": " + fe.getDefaultMessage())
                       .collect(Collectors.joining(", "));
        ErrorResponse err = new ErrorResponse();
        err.setStatus(422);
        err.setError("Validation Failed");
        err.setMessage(msg);
        err.setPath(req.getRequestURI());
        return ResponseEntity.unprocessableEntity().body(err);
    }
    
    @ExceptionHandler(EntityNotFoundException.class)
    public ResponseEntity<ErrorResponse> onNotFound(EntityNotFoundException ex, HttpServletRequest req) {
        ErrorResponse err = new ErrorResponse();
        err.setStatus(404);
        err.setError("Not Found");
        err.setMessage(ex.getMessage());
        err.setPath(req.getRequestURI());
        return ResponseEntity.status(404).body(err);
    }

    @ExceptionHandler(AuthenticationException.class)
    public ResponseEntity<ErrorResponse> onAuthenticationFailure(AuthenticationException ex,
                                                                  HttpServletRequest req) {
        ErrorResponse err = new ErrorResponse();
        err.setStatus(401);
        err.setError("Unauthorized");
        err.setMessage(ex.getMessage());
        err.setPath(req.getRequestURI());
        return ResponseEntity.status(401).body(err);
    }

    @ExceptionHandler(AccessDeniedException.class)
    public ResponseEntity<ErrorResponse> onAccessDenied(AccessDeniedException ex, HttpServletRequest req) {
        ErrorResponse err = new ErrorResponse();
        err.setStatus(403);
        err.setError("Forbidden");
        err.setMessage("No tienes permisos para acceder a este recurso");
        err.setPath(req.getRequestURI());
        return ResponseEntity.status(403).body(err);
    }
    
    
    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> onGeneric(Exception ex, HttpServletRequest req) {
        ErrorResponse err = new ErrorResponse();
        err.setStatus(500);
        err.setError("Internal Server Error");
        err.setMessage("Ha ocurrido un error inesperado");
        err.setPath(req.getRequestURI());
        ex.printStackTrace();
        return ResponseEntity.status(500).body(err);
    }
    
    @ExceptionHandler(ApplicationException.class)
    public ResponseEntity<ErrorResponse> handleApplicationException(ApplicationException ex, HttpServletRequest req) {
        ErrorResponse err = new ErrorResponse(
            ex.getStatus(),
            ex.getError(),
            ex.getMessage(),
            req.getRequestURI()
        );

        return ResponseEntity.status(ex.getStatus()).body(err);
    }
}