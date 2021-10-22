package com.unimib.wearable.models.response;

import com.unimib.wearable.webClient.clientProperties.ClientProperties;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class KaaResponse<T> extends BaseResponse{

    private final T data;

    public KaaResponse(HttpStatus status, String message, T data) {
        super(status, message);
        this.data = data;
    }

}
