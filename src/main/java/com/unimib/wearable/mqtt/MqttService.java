package com.unimib.wearable.mqtt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.unimib.wearable.dto.response.data.KaaEndpointDataRequest;
import org.eclipse.paho.client.mqttv3.MqttException;

public interface MqttService {

    void connectToServer() throws MqttException;

    void reconnectToServer() throws Exception;

    boolean publishToServer(final KaaEndpointDataRequest kaaEndPointDataDTO) throws JsonProcessingException, MqttException;


}
