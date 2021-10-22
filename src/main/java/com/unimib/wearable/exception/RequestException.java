package com.unimib.wearable.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.io.IOException;

@Getter
public class RequestException extends IOException {

    private final HttpStatus statusCode;

    public RequestException(HttpStatus statusCode, String message){
        super(message);
        this.statusCode = statusCode;
    }

}
