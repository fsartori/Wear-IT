package com.unimib.wearable.exception;

import lombok.Getter;

import java.io.IOException;

@Getter
public class RequestException extends IOException {

    private final int code;

    public RequestException(int code, String message){
        super(message);
        this.code = code;
    }

}
