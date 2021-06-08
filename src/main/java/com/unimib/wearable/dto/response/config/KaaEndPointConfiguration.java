package com.unimib.wearable.dto.response.config;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class KaaEndPointConfiguration implements Serializable {

    private String endpointId;
    private List<String> dataNames;
}
