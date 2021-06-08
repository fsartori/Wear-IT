package com.unimib.wearable.config;

import com.unimib.wearable.csv.CSVOutputFormatOutputFormatServiceImpl;
import com.unimib.wearable.csv.CSVOutputFormatService;
import com.unimib.wearable.exception.RequestException;
import com.unimib.wearable.kaaService.KaaService;
import com.unimib.wearable.kaaService.KaaServiceImpl;
import com.unimib.wearable.mqtt.KaaEndpointMap;
import com.unimib.wearable.mqtt.MqttClientProperties;
import com.unimib.wearable.mqtt.MqttService;
import com.unimib.wearable.mqtt.MqttServiceImpl;
import com.unimib.wearable.webClient.RESTClient;
import com.unimib.wearable.webClient.clientProperties.ClientProperties;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.cloud.client.circuitbreaker.CircuitBreakerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;


@Configuration
public class Config {

    @Bean
    public KaaService kaaService(RESTClient restClient,
                                 RedisTemplate<String, Object> redisTemplate,
                                 CircuitBreakerFactory circuitBreakerFactory) {
        return new KaaServiceImpl(restClient, redisTemplate, circuitBreakerFactory.create("kaaServiceCB"));
    }

    @Bean
    public RESTClient restClient(ClientProperties clientProperties) throws RequestException {
        return new RESTClient(clientProperties);
    }

    @Bean
    public ClientProperties clientProperties() {
        return new ClientProperties();
    }


    @Bean
    public CSVOutputFormatService csvOutputFormatService() {
        return new CSVOutputFormatOutputFormatServiceImpl();
    }

    @Bean
    public MqttService mqttService(MqttClientProperties mqttClientProperties, KaaEndpointMap kaaEndpoints) throws MqttException {
        return new MqttServiceImpl(mqttClientProperties,kaaEndpoints);
    }

    @Bean
    public KaaEndpointMap kaaEndpoints(){
        return new KaaEndpointMap();
    }

    @Bean
    public MqttClientProperties mqttClientProperties(){
        return new MqttClientProperties();
    }
}
