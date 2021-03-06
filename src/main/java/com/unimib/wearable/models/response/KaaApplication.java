package com.unimib.wearable.models.response;

import com.unimib.wearable.dto.response.config.KaaEndPointConfiguration;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class KaaApplication implements Serializable {

    private String applicationName;
    private List<KaaEndPointConfiguration> endpoints;
}
