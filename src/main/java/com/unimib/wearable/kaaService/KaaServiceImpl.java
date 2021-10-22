package com.unimib.wearable.kaaService;

import com.unimib.wearable.dto.response.config.DeviceConfigurationResponseDTO;
import com.unimib.wearable.dto.response.config.KaaEndPointConfigDTO;
import com.unimib.wearable.dto.response.config.KaaEndPointConfiguration;
import com.unimib.wearable.dto.response.data.KaaEndPointDataDTO;
import com.unimib.wearable.exception.RequestException;
import com.unimib.wearable.models.request.KaaEndpointQueryParams;
import com.unimib.wearable.models.response.KaaApplication;
import com.unimib.wearable.webClient.RESTClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@AllArgsConstructor
@Slf4j
public class KaaServiceImpl extends KaaServiceImplAbstract implements KaaService {

    private final RESTClient restClient;
    private final RedisTemplate<String, Object> redisTemplate;
    private final CircuitBreaker circuitBreaker;

    private List<String> applicationNameFallback(String error) {
        log.error("circuit breaker enable, an error has occurred: {}", error);
        return Optional.ofNullable((List<String>) redisTemplate.opsForValue().get("applicationNames-cache::appNames"))
                .orElse(new ArrayList<>());
    }

    @CachePut(value = "applicationNames-cache", key = "'appNames'", condition = "#result != null")
    @Override
    public List<String> getApplicationDataNames() {
        return circuitBreaker.run(() -> getDeviceConfigurationsName(restClient.getKaaEndpointConfigurations(baseEndpoint)),
                throwable -> applicationNameFallback(throwable.getMessage()));
    }

    private List<String> getDeviceConfigurationsName(final ResponseEntity<KaaEndPointConfigDTO> kaaEndPointResponse) {
        return Optional.ofNullable(kaaEndPointResponse.getBody())
                .filter(response -> !CollectionUtils.isEmpty(response.getDeviceConfigurationResponse()))
                .map(k -> k.getDeviceConfigurationResponse().stream().map(DeviceConfigurationResponseDTO::getName).collect(Collectors.toList()))
                .orElse(new ArrayList<>());
    }

    @Cacheable(value = "sensors-cache", key = "#endpointId")
    @Override
    public KaaApplication getApplicationDataNames(final List<String> endpointId) {
        log.info("getApplicationDataNames from endpoints {}", endpointId);
        return KaaApplication.builder()
                .applicationName(appName)
                .endpoints(getConfigurations(getKaaEndpoint(endpointId)))
                .build();
    }

    @Cacheable(value = "endpoint-cache", key = "#fromDate+'-'+#toDate+'-'+#includeTime+'-'+#sort+'-'+#samplePeriod")
    @Override
    public List<KaaEndPointDataDTO> getKaaEndPointsData(final String fromDate, final String toDate, final String includeTime, final String sort, final String samplePeriod) throws RequestException {
        KaaEndpointQueryParams queryParams = setQueryParamsFromRequest(fromDate, toDate, includeTime, sort, samplePeriod);
        KaaApplication kaaApplication = getApplicationDataNames(Collections.emptyList());
        List<KaaEndPointConfiguration> kaaEndPointConfigurations = kaaApplication.getEndpoints();
        return kaaEndPointConfigurations
                .stream()
                .map(kaaEndpointConfiguration -> getData(kaaEndpointConfiguration, queryParams))
                .collect(Collectors.toList());
    }


    @Override
    public boolean checkAvailability(final List<String> endpointId, final String dataName) {
        KaaApplication kaaApplication = getApplicationDataNames(endpointId.subList(0, 1));
        Optional<KaaEndPointConfiguration> kaaEndPointConfiguration = kaaApplication.getEndpoints().stream().findFirst();

        return kaaEndPointConfiguration.isPresent() && kaaEndPointConfiguration.get().getDataNames().contains(dataName);
    }

    private List<KaaEndPointConfiguration> getConfigurations(final List<KaaEndPointDataDTO> kaaEndPointDataDTO) {
        return kaaEndPointDataDTO
                .stream()
                .filter(kaaEndPoint -> !CollectionUtils.isEmpty(kaaEndPoint.getValues()))
                .map(kaaEndPoint -> KaaEndPointConfiguration.builder()
                        .endpointId(kaaEndPoint.getEndpointId())
                        .dataNames(new ArrayList<>(kaaEndPoint.getValues().keySet()))
                        .build())
                .collect(Collectors.toList());
    }

    private List<KaaEndPointDataDTO> getKaaEndpoint(final List<String> endpointId) {
        return CollectionUtils.isEmpty(endpointId) ? getKaaEndPointsData(getApplicationDataNames(), new KaaEndpointQueryParams()) :
                getKaaEndPointsData(getKaaEndPointConfiguration(endpointId, getApplicationDataNames()), new KaaEndpointQueryParams());
    }

    private List<KaaEndPointConfiguration> getKaaEndPointConfiguration(final List<String> endpointId, final List<String> dataNames) {
        return endpointId
                .stream()
                .map(kaaEndPointConfiguration -> KaaEndPointConfiguration.builder()
                        .endpointId(kaaEndPointConfiguration)
                        .dataNames(dataNames)
                        .build())
                .collect(Collectors.toList());
    }

    private List<KaaEndPointDataDTO> getKaaEndPointsData(final List<?> kaaEndpointConfigurations, final KaaEndpointQueryParams kaaEndpointQueryParams) {
        return kaaEndpointConfigurations
                .stream()
                .map(kaaEndpointConfiguration -> getData(kaaEndpointConfiguration, kaaEndpointQueryParams))
                .collect(Collectors.toList());
    }

    private <T> KaaEndPointDataDTO getData(final T kaaEndPointConfiguration, final KaaEndpointQueryParams kaaEndpointQueryParams) {
        return getKaaEndpoint(kaaEndPointConfiguration, kaaEndpointQueryParams).getBody();
    }

    private <T> ResponseEntity<KaaEndPointDataDTO> getKaaEndpoint(final T kaaEndPointConfiguration, final KaaEndpointQueryParams kaaEndpointQueryParams) {
        return kaaEndPointConfiguration instanceof KaaEndPointConfiguration ?
                restClient.getKaaEndpointData(createUrl(baseUrl, (KaaEndPointConfiguration) kaaEndPointConfiguration, kaaEndpointQueryParams)) :
                restClient.getKaaEndpointData(createUrl(baseUrl, (String) kaaEndPointConfiguration, kaaEndpointQueryParams));
    }


}
