package com.unimib.wearable.dto.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.unimib.wearable.dto.response.config.DeviceConfigurationResponseDTO;
import com.unimib.wearable.dto.response.config.KaaEndPointConfigDTO;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.unimib.wearable.constants.Constants.PARALLEL_STREAM;

public class ConfigEndpointDeserializer extends StdDeserializer<KaaEndPointConfigDTO> {

    public ConfigEndpointDeserializer() {
        this(null);
    }

    public ConfigEndpointDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public KaaEndPointConfigDTO deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
        JsonNode root = jsonParser.getCodec().readTree(jsonParser);

        List<String> applicationName = new ArrayList<>();
        root.fieldNames().forEachRemaining(applicationName::add);

        return applicationName.stream()
                .map(appName -> createKEP(root.get(appName)))
                .collect(Collectors.toList()).stream()
                .findFirst()
                .orElseGet(KaaEndPointConfigDTO::new);
    }

    private KaaEndPointConfigDTO createKEP(JsonNode jsonNode) {
        List<DeviceConfigurationResponseDTO> deviceConfigurationResponseDTO =
                StreamSupport.stream(jsonNode.spliterator(), PARALLEL_STREAM)
                        .map(x -> DeviceConfigurationResponseDTO.builder()
                                .name(x.get("name").asText())
                                .values(setValues(x.get("values")))
                                .build())
                        .collect(Collectors.toList());

        return KaaEndPointConfigDTO.builder()
                .deviceConfigurationResponse(deviceConfigurationResponseDTO)
                .build();
    }

    private List<String> setValues(JsonNode jsonNode) {
        return StreamSupport.stream(jsonNode.spliterator(), PARALLEL_STREAM)
                .map(JsonNode::asText)
                .collect(Collectors.toList());
    }
}
