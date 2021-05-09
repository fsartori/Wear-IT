package com.unimib.wearable.dto.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.unimib.wearable.dto.KEP;
import com.unimib.wearable.dto.KaaEndPointResponse;
import io.swagger.v3.core.util.Json;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class EndpointDeserializer extends StdDeserializer<KaaEndPointResponse> {

    public EndpointDeserializer() {
        this(null);
    }
    public EndpointDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public KaaEndPointResponse deserialize(JsonParser p, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode node = p.getCodec().readTree(p);

        List<String> appsName = new ArrayList<>();

        node.fieldNames().forEachRemaining(appsName::add);

        return appsName.stream()
                .map(appName -> createKEP(node.get(appName))).collect(Collectors.toList()).get(0);
    }

    private KaaEndPointResponse createKEP(JsonNode jsonNode){
        List<KEP> kep = new ArrayList<>();
        jsonNode.iterator().forEachRemaining(
                x -> kep.add(new KEP(x.get("name").asText(), setValues(x.get("values")))));

        return new KaaEndPointResponse(kep);
    }

    private List<String> setValues(JsonNode jsonNode){
        List<String> values = new ArrayList<>();
        jsonNode.iterator().forEachRemaining(x -> values.add(x.asText()));
        return values;
    }
}
