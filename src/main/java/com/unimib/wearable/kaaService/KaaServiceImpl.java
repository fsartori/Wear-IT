package com.unimib.wearable.kaaService;

import com.unimib.wearable.dto.response.config.DeviceConfigurationResponseDTO;
import com.unimib.wearable.dto.response.config.KaaEndPointConfigDTO;
import com.unimib.wearable.dto.response.config.KaaEndPointConfiguration;
import com.unimib.wearable.dto.response.data.KaaEndPointDataDTO;
import com.unimib.wearable.models.request.KaaEndpointQueryParams;
import com.unimib.wearable.models.response.KaaApplication;
import com.unimib.wearable.webClient.RESTClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cloud.client.circuitbreaker.CircuitBreaker;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class KaaServiceImpl extends KaaServiceImplAbstract implements KaaService {


    protected final RedisTemplate<String, Object> redisTemplate;
    private final RESTClient restClient;
    private final CircuitBreaker circuitBreaker;

    public KaaServiceImpl(RESTClient restClient, RedisTemplate<String, Object> redisTemplate, CircuitBreaker circuitBreaker) {
        this.restClient = restClient;
        this.redisTemplate = redisTemplate;
        this.circuitBreaker = circuitBreaker;
    }

    public List<String> applicationNameFallback(String error) {
        log.error("circuit breaker enable, an error has occurred: {}", error);
        return Optional.ofNullable((List<String>) redisTemplate.opsForValue().get("applicationNames-cache::appNames")).orElse(new ArrayList<>());
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
        return new KaaApplication(appName, getConfigurations(getKaaEndpoint(endpointId)));
    }

    @Cacheable(value = "endpoint-cache", key = "#fromDate+'-'+#toDate+'-'+#includeTime+'-'+#sort+'-'+#samplePeriod")
    @Override
    public List<KaaEndPointDataDTO> getKaaEndPointsData(final String fromDate, final String toDate, final String includeTime, final String sort, final String samplePeriod) {
        KaaApplication kaaApplication = getApplicationDataNames(new ArrayList<>());
        List<KaaEndPointConfiguration> kaaEndPointConfigurations = kaaApplication.getEndpoints();
        return kaaEndPointConfigurations
                .stream()
                .map(kaaEndpointConfiguration -> getData(kaaEndpointConfiguration, setQueryParamsFromRequest(fromDate, toDate, includeTime, sort, samplePeriod)))
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
                .map(kaaEndPoint -> new KaaEndPointConfiguration(kaaEndPoint.getEndpointId(), new ArrayList<>(kaaEndPoint.getValues().keySet())))
                .collect(Collectors.toList());
    }

    private List<KaaEndPointDataDTO> getKaaEndpoint(final List<String> endpointId) {
        return CollectionUtils.isEmpty(endpointId) ? getKaaEndPointsData(getApplicationDataNames(), new KaaEndpointQueryParams()) :
                getKaaEndPointsData(getKaaEndPointConfiguration(endpointId, getApplicationDataNames()), new KaaEndpointQueryParams());
    }

    private List<KaaEndPointConfiguration> getKaaEndPointConfiguration(final List<String> endpointId, final List<String> dataNames) {
        return endpointId
                .stream()
                .map(kaaEndPointConfiguration -> new KaaEndPointConfiguration(kaaEndPointConfiguration, dataNames))
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
