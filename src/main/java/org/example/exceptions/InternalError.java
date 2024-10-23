package org.example.exceptions;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(code = HttpStatus.INTERNAL_SERVER_ERROR)
public class InternalError extends RuntimeException{
    private final String message;

    public InternalError(String message) {
        this.message = message;
    }
}
