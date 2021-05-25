package com.unimib.wearable.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Map;

@Data
@NoArgsConstructor
public class KaaValue implements Serializable {

    private String timestamp;
    private String value;
    private Map<String, String> values;

    public KaaValue(String timestamp, String value) {
        this.timestamp = timestamp;
        this.value = value;
    }

    public KaaValue(String timestamp, Map<String, String> values) {
        this.timestamp = timestamp;
        this.values = values;
    }
}
