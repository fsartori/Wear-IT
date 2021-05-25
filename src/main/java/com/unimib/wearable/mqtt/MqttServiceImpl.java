package com.unimib.wearable.mqtt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.unimib.wearable.dto.response.data.KaaEndpointDataRequest;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
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
    private final int connectionRetries;

    public MqttServiceImpl(MqttClientProperties mqttClientProperties, KaaEndpointMap kaaEndpoints) throws MqttException {
        mqttClient = new MqttClient(mqttClientProperties.getServer(), mqttClientProperties.getClientId(), new MemoryPersistence());
        mqttConnectOptions = new MqttConnectOptions();
        mqttConnectOptions.setCleanSession(true);
        mqttConnectOptions.setConnectionTimeout(mqttClientProperties.getConnectionTimeout());
        this.kaaEndpoints = kaaEndpoints;
        this.server = mqttClientProperties.getServer();
        connectionRetries = mqttClientProperties.getConnectionRetries();
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
        int retries = connectionRetries;
        int attempts = 0;
        boolean isConnected = false;

        try {
            while (retries > 0 && !isConnected) {
                if (!mqttClient.isConnected()) {
                    log.info("reconnecting to mqttServer ({}) - attempt {}", server, attempts++);
                    reconnectToServer();
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
            log.error("error while reestablishing connection - {}", e.getMessage());
            mqttClient.disconnect();
            mqttClient.close(true);
            throw new MqttException(e.getReasonCode());
        }
    }

    @Override
    public boolean publishToServer(KaaEndpointDataRequest kaaEndPointDataDTO) throws JsonProcessingException, MqttException {
        log.info("sending data to from kaaEndpoint ({}) to KaaServer", kaaEndPointDataDTO.getEndpointId());
        checkConnection();
        return publishData(kaaEndPointDataDTO, setMessage(kaaEndPointDataDTO));
    }

    private void checkConnection() throws MqttException {
        if (!mqttClient.isConnected()) {
            log.warn("mqttClient is not connected!Trying to reconnect.");
            reconnectToServer();
        }
    }

    private MqttMessage setMessage(KaaEndpointDataRequest kaaEndPointDataDTO) throws JsonProcessingException {
        return new MqttMessage(new ObjectMapper().writeValueAsBytes(kaaEndPointDataDTO.getValues()));
    }

    private boolean publishData(KaaEndpointDataRequest kaaEndPointDataDTO, MqttMessage mqttMessage) throws MqttException {
        return sendData(getTopic(kaaEndPointDataDTO), kaaEndPointDataDTO, mqttMessage);
    }

    private boolean sendData(Optional<String> topic, KaaEndpointDataRequest kaaEndPointDataDTO, MqttMessage mqttMessage) throws MqttException {
        return topic.isPresent() && publish(kaaEndPointDataDTO, mqttMessage, topic.get());
    }

    private boolean publish(KaaEndpointDataRequest kaaEndPointDataDTO, MqttMessage mqttMessage, String topic) throws MqttException {
        mqttClient.publish(kaaEndpoints.getEndpoints().get(kaaEndPointDataDTO.getEndpointId()), mqttMessage);
        log.info("the data has been sent to KaaServer - topic:{}", topic);
        return true;
    }

    private Optional<String> getTopic(KaaEndpointDataRequest kaaEndPointDataDTO) {
        return Optional.ofNullable(kaaEndpoints.getEndpoints())
                .filter(kaaEndpoint -> !CollectionUtils.isEmpty(kaaEndpoint) && kaaEndpoint.containsKey(kaaEndPointDataDTO.getEndpointId()))
                .map(kaaEndpoint -> kaaEndpoint.get(kaaEndPointDataDTO.getEndpointId()));
    }

}
