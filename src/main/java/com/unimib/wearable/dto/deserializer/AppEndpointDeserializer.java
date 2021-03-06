package com.unimib.wearable.dto.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.unimib.wearable.dto.response.data.KaaValue;
import com.unimib.wearable.dto.response.data.KaaMultiValue;
import com.unimib.wearable.dto.response.data.KaaSingleValue;
import com.unimib.wearable.dto.response.data.KaaEndPointDataDTO;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class AppEndpointDeserializer extends StdDeserializer<KaaEndPointDataDTO> {

    private static final String TIME_STAMP = "timestamp";
    private static final String VALUE = "value";
    private static final String VALUES = "values";

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

    private String setEndpointId(final JsonNode root) {
        return StreamSupport.stream(root.spliterator(), false)
                .findFirst()
                .filter(node -> !node.isEmpty())
                .map(x -> x.fieldNames().next()).orElse(StringUtils.EMPTY);
    }

    private void putToMap(final Map.Entry<String, JsonNode> entry, final Map<String, List<KaaValue>> deviceDataSample) {
        deviceDataSample.put(entry.getValue().fieldNames().next(), flatList(entry.getValue()));
    }

    //flat all data into a single list, each iteration is related to only one type of data
    private List<KaaValue> flatList(final JsonNode jsonNode) {
        return StreamSupport.stream(jsonNode.spliterator(), false)
                .flatMap(device -> createSampleList(device).stream()).collect(Collectors.toList());
    }

    private List<KaaValue> createSampleList(final JsonNode device) {
        return StreamSupport.stream(device.spliterator(), false)
                .map(this::createSample)
                .collect(Collectors.toList());
    }

    private KaaValue createSample(final JsonNode dataSample) {
        return isMultiValue(dataSample) ? createMultiValue(dataSample) : createSingleValue(dataSample);
    }


    private boolean isMultiValue(final JsonNode jsonNode) {
        return jsonNode.get(VALUES).size() > 1;
    }

    private KaaValue createSingleValue(final JsonNode dataSample) {
        return new KaaSingleValue(dataSample.get(TIME_STAMP).asText(), dataSample.get(VALUES).get(VALUE).asText());
    }

    private KaaValue createMultiValue(final JsonNode dataSample) {
        Map<String, String> values = new HashMap<>();
        dataSample.fields().forEachRemaining(x -> values.put(x.getKey(), x.getValue().asText()));
        return new KaaMultiValue(dataSample.get(TIME_STAMP).asText(), values);
    }
}
