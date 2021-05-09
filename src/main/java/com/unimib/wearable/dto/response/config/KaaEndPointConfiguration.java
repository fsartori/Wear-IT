package com.unimib.wearable.dto.response.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@Getter
@Setter
public class KaaEndPointConfiguration {

    private String endpointId;
    private List<String> dataNames;
}
