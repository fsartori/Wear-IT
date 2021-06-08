package com.unimib.wearable.models.response;

import lombok.Getter;

@Getter
public class KaaResponse<T> extends BaseResponse{

    private final T data;

    public KaaResponse(int status, String message, T data) {
        super(status, message);
        this.data = data;
    }

}
