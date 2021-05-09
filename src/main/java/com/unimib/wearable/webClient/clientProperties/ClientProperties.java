package com.unimib.wearable.webClient;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;


@Component
@Data
@AllArgsConstructor
@NoArgsConstructor
@ConfigurationProperties(prefix = "webclient")
public class ClientProperties {

    private String bearerToken;

    private HttpProperties httpProperties = new HttpProperties();
    private Map<String, String> headers = new HashMap<>() {{
        put(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
        put(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_FORM_URLENCODED_VALUE);
    }};

    @Data
    public static class HttpProperties {

        private AuthorizationProperties authorizationProperties = new AuthorizationProperties();

        private String baseUrl;

        private String authUrl;

        @Data
        public static class AuthorizationProperties {
            private String username, pwd;
        }

    }

}
