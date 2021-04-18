package com.unimib.wearable.models.response;

import com.unimib.wearable.dto.KaaEndPointConfiguration;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class KaaApplication {

    private String applicationName; // example: btngtro547tsntf25rtg
    private List<KaaEndPointConfiguration> endpoints;
}
