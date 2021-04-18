package com.unimib.wearable.mqtt;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.unimib.wearable.dto.KaaEndPoint;
import org.eclipse.paho.client.mqttv3.MqttException;

public interface MqttService {

    void connectToServer() throws MqttException;

    void reconnectToServer() throws MqttException, Exception;

    boolean publishToServer(final KaaEndPoint kaaEndPoint) throws JsonProcessingException, MqttException;


}
