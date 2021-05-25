package com.unimib.wearable.webClient;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.unimib.wearable.dto.response.auth.KaaEndpointAuthDTO;
import com.unimib.wearable.dto.response.config.KaaEndPointConfigDTO;
import com.unimib.wearable.dto.response.data.KaaEndPointDataDTO;
import com.unimib.wearable.exception.KaaEndPointException;
import com.unimib.wearable.webClient.clientProperties.ClientProperties;
import com.unimib.wearable.webClient.httpMessageConverter.ObjectToUrlEncodedConverter;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

import java.util.Optional;

@Slf4j
@Data
public class RESTClient {

    private final RestTemplate restTemplate;
    private HttpHeaders httpHeaders;
    private HttpEntity<?> httpEntity;

    public RESTClient(ClientProperties clientProperties) throws KaaEndPointException {
        restTemplate = new RestTemplate();
        setMessageConverters();
        setDefaultHttpHeaders(clientProperties);
        setAuthorizationHeaders(clientProperties);
        setRequestEntity();
        setHost(clientProperties);
    }

    private void setMessageConverters() {
        restTemplate.getMessageConverters().add(new ObjectToUrlEncodedConverter(new ObjectMapper()));
    }

    private void setDefaultHttpHeaders(ClientProperties clientProperties) {
        httpHeaders = new HttpHeaders();
        clientProperties.getHeaders().forEach((k, v) -> httpHeaders.set(k, v));
    }

    private void setAuthorizationHeaders(ClientProperties clientProperties) throws KaaEndPointException {
        httpHeaders.set(HttpHeaders.AUTHORIZATION, "Bearer " + getKaaToken(clientProperties));
    }

    private void setRequestEntity() {
        httpEntity = new HttpEntity<>(null, httpHeaders);
    }

    private void setHost(ClientProperties clientProperties) {
        restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(clientProperties.getHttpProperties().getBaseUrl()));
    }

    private String getKaaToken(ClientProperties clientProperties) throws KaaEndPointException {
        ResponseEntity<KaaEndpointAuthDTO> authResponse = getAuthorization(clientProperties.getHttpProperties().getAuthUrl(),
                new HttpEntity<>(clientProperties.getAuthProperties(), httpHeaders));

        return Optional.ofNullable(authResponse.getBody())
                .filter(ar -> StringUtils.isNotEmpty(ar.getToken()))
                .map(KaaEndpointAuthDTO::getToken)
                .orElseThrow(() -> {
                    log.error("Empty token from auth endpoint");
                    return new KaaEndPointException(HttpStatus.INTERNAL_SERVER_ERROR.value(), "Unable to retrieve token from auth endpoint");
                });
    }

    private ResponseEntity<KaaEndpointAuthDTO> getAuthorization(String url, HttpEntity<?> httpEntity) {
        return restTemplate.exchange(url, HttpMethod.POST, httpEntity, KaaEndpointAuthDTO.class);
    }

    public ResponseEntity<KaaEndPointConfigDTO> getKaaEndpointConfigurations(String url) {
        return restTemplate.exchange(url, HttpMethod.GET, httpEntity, KaaEndPointConfigDTO.class);
    }

    public ResponseEntity<KaaEndPointDataDTO> getKaaEndpointData(String url) {
        return restTemplate.exchange(url, HttpMethod.GET, httpEntity, KaaEndPointDataDTO.class);
    }

    private void isValidResponse(ResponseEntity<?> responseEntity) throws KaaEndPointException {
        if (responseEntity.getStatusCode() != HttpStatus.OK)
            throw new KaaEndPointException(responseEntity.getStatusCodeValue(), responseEntity.getStatusCode().getReasonPhrase());

    }

}
