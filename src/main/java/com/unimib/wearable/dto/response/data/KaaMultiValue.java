package com.unimib.wearable.dto.response;

import com.unimib.wearable.dto.KaaValue;
import lombok.Getter;

import java.io.Serializable;
import java.util.Map;

@Getter
public class KaaMultiValue extends KaaValue implements Serializable {

    private final Map<String, String> values;

    public KaaMultiValue(String timestamp,Map<String, String> values){
        super(timestamp);
        this.values = values;
    }
}
