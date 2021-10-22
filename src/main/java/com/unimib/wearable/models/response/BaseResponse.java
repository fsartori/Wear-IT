package com.unimib.wearable.models.response;

import lombok.*;
import org.springframework.http.HttpStatus;

@Builder
@Data
public class BaseResponse {

    private final HttpStatus status;
    private final String message;
}
