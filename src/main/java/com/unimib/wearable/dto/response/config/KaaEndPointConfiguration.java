package com.unimib.wearable.dto.response.config;

import lombok.*;

import java.io.Serializable;
import java.util.List;

@Builder
@NoArgsConstructor
@AllArgsConstructor
@Data
public class KaaEndPointConfiguration implements Serializable {

    private String endpointId;
    private List<String> dataNames;
}
