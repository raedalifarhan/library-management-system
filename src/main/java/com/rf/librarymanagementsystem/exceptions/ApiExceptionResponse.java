package com.rf.librarymanagementsystem.exceptions;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.ZonedDateTime;

@Data
@Builder
@AllArgsConstructor
public class ApiExceptionResponse {

    private final String message;
    private final HttpStatus status;
    private final ZonedDateTime timeStamp;
}
