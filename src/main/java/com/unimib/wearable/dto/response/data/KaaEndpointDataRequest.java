package com.unimib.wearable.dto.response.data;

import com.unimib.wearable.dto.KaaValue;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class KaaEndpointDataRequest {

    private String endpointId;
    private Map<String, List<KaaValue>> values;

    public KaaEndpointDataRequest(String endpointId, Map<String, List<KaaValue>> values) {
        this.endpointId = endpointId;
        this.values = values;
    }
}
