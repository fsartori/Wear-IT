package com.unimib.wearable.dto.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.unimib.wearable.dto.response.data.KaaEndPointDataDTO;
import com.unimib.wearable.dto.KaaValue;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class AppEndpointDeserializer extends StdDeserializer<KaaEndPointDataDTO> {

    public AppEndpointDeserializer() {
        this(null);
    }

    public AppEndpointDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public KaaEndPointDataDTO deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
        JsonNode root = jsonParser.getCodec().readTree(jsonParser);

        Map<String, List<KaaValue>> deviceDataSample = new HashMap<>();

        StreamSupport.stream(root.spliterator(), false)
                .forEach(endpoint -> endpoint.fields().forEachRemaining(device -> putToMap(device, deviceDataSample)));

        return new KaaEndPointDataDTO(setEndpointId(root), deviceDataSample);
    }

    private String setEndpointId(JsonNode root) {
        return StreamSupport.stream(root.spliterator(), false)
                .findFirst()
                .filter(node -> !node.isEmpty())
                .map(x -> x.fieldNames().next()).orElse(StringUtils.EMPTY);
    }

    private void putToMap(Map.Entry<String, JsonNode> entry, Map<String, List<KaaValue>> deviceDataSample) {
        deviceDataSample.put(entry.getValue().fieldNames().next(), flatList(entry.getValue()));
    }

    //flat all data into a single list, each iteration is related to only one type of data
    private List<KaaValue> flatList(JsonNode jsonNode) {
        return StreamSupport.stream(jsonNode.spliterator(), false)
                .flatMap(device -> createSampleList(device).stream()).collect(Collectors.toList());
    }

    private List<KaaValue> createSampleList(JsonNode device) {
        return StreamSupport.stream(device.spliterator(), false)
                .map(this::createSample)
                .collect(Collectors.toList());
    }

    //sample data
    private KaaValue createSample(JsonNode dataSample) {
        return new KaaValue(dataSample.get("timestamp").asText(), dataSample.get("values").get("value").asText());
    }
}
