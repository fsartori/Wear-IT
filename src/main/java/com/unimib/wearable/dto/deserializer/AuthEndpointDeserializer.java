package com.unimib.wearable.dto.deserializer;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.deser.std.StdDeserializer;
import com.unimib.wearable.dto.response.auth.KaaEndpointAuthDTO;

import java.io.IOException;

public class TokenEndpointDeserializer extends StdDeserializer<KaaEndpointAuthDTO> {

    public TokenEndpointDeserializer() {
        this(null);
    }

    public TokenEndpointDeserializer(Class<?> vc) {
        super(vc);
    }

    @Override
    public KaaEndpointAuthDTO deserialize(JsonParser jsonParser, DeserializationContext ctxt) throws IOException, JsonProcessingException {
        JsonNode root = jsonParser.getCodec().readTree(jsonParser);
        return new KaaEndpointAuthDTO(root.get("access_token").asText(), root.get("expires_in").asLong());
    }
}
