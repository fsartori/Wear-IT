package com.unimib.wearable.dto.response.config;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeviceConfigurationResponseDTO {

    public String name;
    public List<String> values;
}
