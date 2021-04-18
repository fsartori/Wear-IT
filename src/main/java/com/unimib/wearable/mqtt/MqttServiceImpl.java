package com.unimib.wearable.mqtt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unimib.wearable.config.KaaEndpointMap;
import com.unimib.wearable.dto.KaaEndPoint;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

import java.util.Optional;

@Component
@Slf4j
public class MqttServiceImpl implements MqttService {


    private final String server;
    private final MqttClient mqttClient;
    private final MqttConnectOptions mqttConnectOptions;
    private final KaaEndpointMap kaaEndpoints;

    public MqttServiceImpl(MqttClientProperties mqttClientProperties, KaaEndpointMap kaaEndpoints) throws MqttException {
        mqttClient = new MqttClient(mqttClientProperties.getServer(), mqttClientProperties.getClientId(), new MemoryPersistence());
        mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setCleanSession(true);
        mqttConnectOptions.setConnectionTimeout(mqttClientProperties.getConnectionTimeout());
        this.kaaEndpoints = kaaEndpoints;
        this.server = mqttClientProperties.getServer();
        connectToServer();
    }


    @Override
    public void connectToServer() throws MqttException {
        log.info("connecting to mqttServer ({})", server);
        try {
            mqttClient.connect(mqttConnectOptions);
        } catch (MqttException e) {
            log.error("error establishing connection - ({})", e.getMessage());
            mqttClient.disconnect();
            mqttClient.close(true);
            throw new MqttException(e.getReasonCode());
        }
    }

    @Override
    public void reconnectToServer() throws MqttException {
        log.info("checking mqttServer connection ({})", server);
        int retries = 3;
        int attempts = 0;
        boolean isConnected = false;

        try {
            while (retries > 0) {
                if (!mqttClient.isConnected()) {
                    log.info("reconnecting to mqttServer ({}) - attempt {}", server, attempts++);
                    mqttClient.reconnect();
                    retries--;
                } else {
                    log.info("connection established");
                    isConnected = true;
                }
            }

            if (!isConnected) {
                log.error("connection failed, shutting down the existing connection attempt");
                mqttClient.disconnect();
                mqttClient.close(true);
                throw new MqttException(HttpStatus.REQUEST_TIMEOUT.value());
            }
        } catch (MqttException e) {
            log.error("error while reestablishing connection");
            mqttClient.disconnect();
            mqttClient.close(true);
            throw new MqttException(e.getReasonCode());
        }


    }

    @Override
    public boolean publishToServer(KaaEndPoint kaaEndPoint) throws JsonProcessingException {
        log.info("sending data to from kaaEndpoint ({}) to KaaServer", kaaEndPoint.getEndpointId());

        boolean sendToServer = false;

        ObjectMapper objectMapper = new ObjectMapper();

        byte[] payload = objectMapper.writeValueAsBytes(kaaEndPoint.getValues());

        MqttMessage mqttMessage = new MqttMessage(payload);

        try {
            final Optional<String> topic = Optional.ofNullable(kaaEndpoints.getEndpoints())
                    .filter(ke -> !CollectionUtils.isEmpty(ke) && ke.containsKey(kaaEndPoint.getEndpointId()))
                    .map(ke -> ke.get(kaaEndPoint.getEndpointId()));

            if (topic.isPresent()){
                mqttClient.publish(kaaEndpoints.getEndpoints().get(kaaEndPoint.getEndpointId()), mqttMessage);
                log.info("the data has been sent to KaaServer");
                sendToServer = true;
            }

        } catch (MqttException mqttException) {
            log.error("Error while sending data to KaaServer from kaaEndpoint ({}) caused by : {}", kaaEndPoint, mqttException.getMessage());
        }

        return sendToServer;
    }

}
