package com.unimib.wearable.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

@Data
public class KaaEndPoint {

    private String endpointId;
    private Map<String, List<Object>> values;

    public KaaEndPoint(String endpointId) {
        this.endpointId = endpointId;
    }
}
