package com.rf.librarymanagementsystem.exceptions;

public class ApiBadRequestException extends RuntimeException {

    public ApiBadRequestException(String message) { super(message); }

    public ApiBadRequestException(Throwable cause) { super(cause); }

    public ApiBadRequestException(String message, Throwable cause) {
        super(message, cause);
    }

}
