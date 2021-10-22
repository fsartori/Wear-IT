package com.unimib.wearable.dto.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.unimib.wearable.dto.response.auth.KaaEndpointAuthDTO;

import java.io.IOException;

public class AuthEndpointDeserializer extends StdDeserializer<KaaEndpointAuthDTO> {

    public AuthEndpointDeserializer() {
        this(null);
    }

    public AuthEndpointDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public KaaEndpointAuthDTO deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException {
        JsonNode root = jsonParser.getCodec().readTree(jsonParser);
        return KaaEndpointAuthDTO.builder()
                .token(root.get("access_token").asText())
                .expires(root.get("expires_in").asLong())
                .build();
    }
}
