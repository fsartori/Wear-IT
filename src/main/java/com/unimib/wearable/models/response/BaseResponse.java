package com.unimib.wearable.models.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
public class BaseResponse {

    private final int status;
    private final String message;
}
