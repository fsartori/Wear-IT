package com.unimib.wearable.mqtt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "mqtt")
public class MqttClientProperties {

    private String server;

    private String clientId;

    private int connectionRetries;

    private int connectionTimeout;
}
