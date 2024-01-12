package com.rf.librarymanagementsystem.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.ZoneId;
import java.time.ZonedDateTime;

@RestControllerAdvice
public class ApiExceptionHandler {

    @ExceptionHandler
    public ResponseEntity<ApiExceptionResponse> handleApiRequestException (ApiNotFoundException ex) {

        HttpStatus badRequest = HttpStatus.NOT_FOUND;

        ApiExceptionResponse error = ApiExceptionResponse.builder()
                .message(ex.getMessage())
                .status(HttpStatus.BAD_REQUEST)
                .timeStamp(ZonedDateTime.now(ZoneId.of("Z")))
                .build();

        return new ResponseEntity<>(error, badRequest);
    }

    @ExceptionHandler
    public ResponseEntity<ApiExceptionResponse> handleDuplicateBookException(ApiBadRequestException ex) {
        ApiExceptionResponse error = ApiExceptionResponse.builder()
                .message(ex.getMessage())
                .status(HttpStatus.BAD_REQUEST)
                .timeStamp(ZonedDateTime.now(ZoneId.of("Z")))
                .build();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler
    public ResponseEntity<ApiExceptionResponse> handleBadRequestException(Exception ex) {
        ApiExceptionResponse error = ApiExceptionResponse.builder()
                .message(ex.getMessage())
                .status(HttpStatus.BAD_REQUEST)
                .timeStamp(ZonedDateTime.now(ZoneId.of("Z")))
                .build();

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }
}
