package ru.skypro.lessons.springboot.security_web.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;

public class EmployeeExceptionHandler {
    @ExceptionHandler({EmployeeNotFoundException.class, ReportNotFoundException.class})
    public ResponseEntity<?> notFound() {
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler(IllegalJsonFileException.class)
    public ResponseEntity<?> badRequest() {
        return ResponseEntity.badRequest().build();
    }
    @ExceptionHandler(InternalServerError.class)
    public ResponseEntity<?> internalServerError() {
        return ResponseEntity.internalServerError().build();
    }
}