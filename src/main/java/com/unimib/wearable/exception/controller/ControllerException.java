package com.unimib.wearable.exception.controller;

import com.unimib.wearable.exception.RequestException;
import com.unimib.wearable.models.response.BaseResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerException {

    @ExceptionHandler(value = RequestException.class)
    public ResponseEntity<BaseResponse> responseErrorHandler(RequestException requestException){
        return ResponseEntity
                .status(requestException.getCode())
                .body(new BaseResponse(requestException.getCode(), requestException.getMessage()));
    }

}
