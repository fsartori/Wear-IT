package com.unimib.wearable.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.util.Map;

@Component
@Data
@ConfigurationProperties(prefix = "kaaendpoints")
public class KaaEndpointMap {

    private Map<String, String> endpoints;
}
