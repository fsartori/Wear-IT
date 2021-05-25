package com.unimib.wearable.kaaService;

import com.unimib.wearable.dto.response.config.DeviceConfigurationResponseDTO;
import com.unimib.wearable.dto.response.config.KaaEndPointConfigDTO;
import com.unimib.wearable.dto.response.config.KaaEndPointConfiguration;
import com.unimib.wearable.dto.response.data.KaaEndPointDataDTO;
import com.unimib.wearable.models.request.KaaEndpointQueryParams;
import com.unimib.wearable.models.response.KaaApplication;
import com.unimib.wearable.response.DataNames;
import com.unimib.wearable.response.KaaResponse;
import com.unimib.wearable.response.Sensor;
import com.unimib.wearable.webClient.RESTClient;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static com.unimib.wearable.constants.Constants.DATA_NAME_SERVICE;

@Slf4j
public class KaaServiceImpl extends KaaServiceImplAbstract implements KaaService {

    private final RedisTemplate<String, Object> redisTemplate;
    private final RESTClient restClient;

    public KaaServiceImpl(RESTClient restClient, RedisTemplate<String, Object> redisTemplate) {
        this.restClient = restClient;
        this.redisTemplate = redisTemplate;
    }

    public ResponseEntity<KaaResponse<Sensor>> applicationNameFallback(Exception e){
        log.error("circuit breaker enable, an error has occurred: {}", e.getMessage());
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(new KaaResponse<>(HttpStatus.INTERNAL_SERVER_ERROR.value(), HttpStatus.INTERNAL_SERVER_ERROR.getReasonPhrase(),
                        new Sensor(new DataNames((List<String>) redisTemplate.opsForValue().get("applicationNames")))));
    }

    @CircuitBreaker(name = DATA_NAME_SERVICE, fallbackMethod = "applicationNameFallback")
    @CachePut(value = "applicationNames-cache", key="'applicationNames'")
    @Override
    public List<String> getApplicationDataNames() {
        return getDeviceConfigurationsName(restClient.getKaaEndpointConfigurations(baseEndpoint));
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

    @Cacheable(value = "endpoint-cache", key = "#fromDate+#toDate+#includeTime+#sort+#periodSample")
    @Override
    public List<KaaEndPointDataDTO> getKaaEndPointsData(final String fromDate, final String toDate, final String includeTime, final String sort, final String periodSample) {
        KaaApplication kaaApplication = getApplicationDataNames(new ArrayList<>());
        List<KaaEndPointConfiguration> kaaEndPointConfigurations = kaaApplication.getEndpoints();
        return kaaEndPointConfigurations
                .stream()
                .map(kaaEndpointConfiguration -> getData(kaaEndpointConfiguration, setQueryParams(fromDate, toDate, includeTime, sort, periodSample)))
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
        List<String> dataNames = getApplicationDataNames();

        return CollectionUtils.isEmpty(endpointId) ? getKaaEndPointsData(dataNames, new KaaEndpointQueryParams()) :
                getKaaEndPointsData(getKaaEndPointConfiguration(endpointId, dataNames), new KaaEndpointQueryParams());
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
