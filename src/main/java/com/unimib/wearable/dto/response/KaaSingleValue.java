package com.unimib.wearable.dto.response;

import com.unimib.wearable.dto.KaaValue;
import lombok.Getter;

import java.io.Serializable;

@Getter
public class KaaSingleValue extends KaaValue implements Serializable {

    private final String value;

    public KaaSingleValue(String timestamp, String value){
        super(timestamp);
        this.value = value;
    }
}
