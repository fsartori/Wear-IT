package com.unimib.wearable.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RequestException extends Exception{


    public RequestException(int code, String message){
        super(message);
    }

}
