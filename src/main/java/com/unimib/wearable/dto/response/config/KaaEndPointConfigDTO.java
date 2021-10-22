package com.unimib.wearable.dto.response.config;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.unimib.wearable.dto.response.data.KaaValue;
import com.unimib.wearable.dto.deserializer.ConfigEndpointDeserializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.*;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonDeserialize(using = ConfigEndpointDeserializer.class)
public class KaaEndPointConfigDTO {

    private String endpointId;

    private List<String> dataNames;

    private List<DeviceConfigurationResponseDTO> deviceConfigurationResponse;

    private Map<String,List<KaaValue>> sensor;

}
