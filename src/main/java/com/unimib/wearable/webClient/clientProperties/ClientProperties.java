package com.unimib.wearable.webClient.clientProperties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.util.HashMap;
import java.util.Map;

@Data
@ConfigurationProperties(prefix = "webclient")
public class ClientProperties {

    private Map<String, String> authProperties;
    private HttpProperties httpProperties = new HttpProperties();
    private Map<String, String> headers = new HashMap<>() {{
        put(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
    }};

    @Data
    public static class HttpProperties {

        private String baseUrl;
        private String authUrl;

    }

}
