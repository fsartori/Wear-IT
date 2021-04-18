package com.unimib.wearable.webClient;

import com.unimib.wearable.exception.KaaEndPointException;
import com.unimib.wearable.dto.KaaEndPointResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

@Component
@Slf4j
public class RESTClient {

    private final RestTemplate restTemplate;
    private HttpHeaders httpHeaders;
    private HttpEntity<?> httpEntity;

    public RESTClient(ClientProperties clientProperties) {
        restTemplate = new RestTemplate();
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(clientProperties.getHttpProperties().getBaseUrl()));
        setHttpHeaders(clientProperties);
        setHttpEntity();
    }

    private void setHttpHeaders(ClientProperties clientProperties) {
        httpHeaders = new HttpHeaders();
        clientProperties.getHeaders().forEach((k, v) -> httpHeaders.set(k, v));
        httpHeaders.set(HttpHeaders.AUTHORIZATION, clientProperties.getBearerToken());
    }

    private void setHttpEntity() {
        httpEntity = new HttpEntity<>(null, httpHeaders);
    }

    public ResponseEntity<KaaEndPointResponse> execute(final String url, HttpMethod httpMethod){
        log.info("retrieving data from KaaPlatform - {}", url);
        ResponseEntity<KaaEndPointResponse> kaaResponse;

        try {
            kaaResponse = restTemplate.exchange(url, httpMethod, httpEntity, KaaEndPointResponse.class);

            if (kaaResponse.getStatusCode() != HttpStatus.OK) {
                throw new KaaEndPointException(kaaResponse.getStatusCodeValue(), kaaResponse.getStatusCode().getReasonPhrase());
            }

            return kaaResponse;

        } catch (Exception e) {
            log.error("an error occurred while retrieving information from KaaPlatform- ({})", e.getMessage());
            throw new RestClientException(e.getMessage());
        }
    }
}
