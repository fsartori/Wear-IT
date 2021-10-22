package com.unimib.wearable.dto.response.auth;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.unimib.wearable.dto.deserializer.AuthEndpointDeserializer;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@JsonDeserialize(using = AuthEndpointDeserializer.class)
public class KaaEndpointAuthDTO {

    private String token;
    private long expires;
}
