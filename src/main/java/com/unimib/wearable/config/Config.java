package com.unimib.wearable.config;

import com.unimib.wearable.exception.KaaEndPointException;
import com.unimib.wearable.format.OutputFormatService;
import com.unimib.wearable.format.csv.CSVOutputFormatOutputFormatServiceImpl;
import com.unimib.wearable.format.csv.CSVOutputFormatService;
import com.unimib.wearable.format.xml.XMLOutputFormatService;
import com.unimib.wearable.format.xml.XMLOutputFormatServiceImpl;
import com.unimib.wearable.kaaService.KaaService;
import com.unimib.wearable.kaaService.KaaServiceImpl;
import com.unimib.wearable.webClient.RESTClient;
import com.unimib.wearable.webClient.clientProperties.ClientProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.RedisTemplate;


@Configuration
public class Config {

    @Bean
    public KaaService kaaService(RESTClient restClient, RedisTemplate<String, Object> redisTemplate) {
        return new KaaServiceImpl(restClient, redisTemplate);
    }

    @Bean
    public RESTClient restClient(ClientProperties clientProperties) throws KaaEndPointException {
        return new RESTClient(clientProperties);
    }

    @Bean
    public ClientProperties clientProperties() {
        return new ClientProperties();
    }

    @Bean
    public OutputFormatService outputFormatService(CSVOutputFormatService csvOutputFormatService, XMLOutputFormatService xmlOutputFormatService) {
        return new OutputFormatService(csvOutputFormatService, xmlOutputFormatService);
    }

    @Bean
    public CSVOutputFormatService csvOutputFormatService() {
        return new CSVOutputFormatOutputFormatServiceImpl();
    }

    @Bean
    public XMLOutputFormatService xmlOutputFormatService() {
        return new XMLOutputFormatServiceImpl();
    }
}
