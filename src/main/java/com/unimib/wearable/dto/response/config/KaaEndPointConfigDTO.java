package com.unimib.wearable.dto;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.unimib.wearable.dto.deserializer.ConfigEndpointDeserializer;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonDeserialize(using = ConfigEndpointDeserializer.class)
public class KaaEndPointConfigDTO {

    private List<String> dataNames;

    private List<KEP> kaaEP;

    //might be the final solution
    private String endpointId;

    private Map<String,List<KaaValue>> sensor;

    public KaaEndPointConfigDTO(String endpointId, Map<String, List<KaaValue>> sensor) {
        this.endpointId = endpointId;
        this.sensor = sensor;
    }

    public KaaEndPointConfigDTO(List<KEP> kaaEP) {
        this.kaaEP = kaaEP;
    }
}
