package com.unimib.wearable.exception;

import com.unimib.wearable.response.KaaResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class ControllerException {

    @ExceptionHandler(value = KaaEndPointException.class)
    public ResponseEntity<KaaResponse<?>> responseErrorHandler(KaaEndPointException requestException){
        return ResponseEntity
                .status(requestException.getCode())
                .body(new KaaResponse<>(requestException.getCode(), requestException.getMessage()));
    }
}
