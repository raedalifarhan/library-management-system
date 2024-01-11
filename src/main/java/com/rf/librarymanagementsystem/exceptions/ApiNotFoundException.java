package com.rf.librarymanagementsystem.exceptions;

public class ApiNotFoundException extends RuntimeException {

    public ApiNotFoundException(String message) { super(message); }

    public ApiNotFoundException(Throwable cause) { super(cause); }

    public ApiNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }

}
