package com.unimib.wearable.dto.response.data;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.unimib.wearable.dto.KaaValue;
import com.unimib.wearable.dto.deserializer.AppEndpointDeserializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
@JsonDeserialize(using = AppEndpointDeserializer.class)
public class KaaEndPointDataDTO implements Serializable {

    private String endpointId;
    private Map<String, List<KaaValue>> values;

    public KaaEndPointDataDTO(String endpointId, Map<String, List<KaaValue>> values) {
        this.endpointId = endpointId;
        this.values = values;
    }
}
