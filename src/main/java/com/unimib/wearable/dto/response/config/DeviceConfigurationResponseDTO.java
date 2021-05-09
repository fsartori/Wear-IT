package com.unimib.wearable.dto.response.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfigDeviceResponseDTO {

    public String name;
    public List<String> values;
}
