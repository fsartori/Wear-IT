package com.unimib.wearable.exception;

import lombok.Getter;

@Getter
public class KaaEndPointException extends Exception{

    private final int code;

    public KaaEndPointException(int code, String message) {
        super(message);
        this.code = code;
    }
}
