package com.unimib.wearable.mqtt;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

@Data
@ConfigurationProperties(prefix = "kaaendpoints")
public class KaaEndpointMap {

    private Map<String, String> endpoints;
}
