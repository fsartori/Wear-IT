package com.unimib.wearable.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class KaaResponse<T> {

    private final int status;
    private final String message;
    private T data;

    public KaaResponse(int status, String message) {
        this.status = status;
        this.message = message;
    }

}
