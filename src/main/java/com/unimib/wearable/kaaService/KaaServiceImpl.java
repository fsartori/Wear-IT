package com.unimib.wearable.kaaService;

import com.unimib.wearable.dto.KEP;
import com.unimib.wearable.models.response.KaaApplication;
import com.unimib.wearable.dto.KaaEndPoint;
import com.unimib.wearable.dto.KaaEndPointConfiguration;
import com.unimib.wearable.dto.KaaEndPointResponse;
import com.unimib.wearable.models.request.KaaEndpointQueryParams;
import com.unimib.wearable.webClient.RESTClient;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
public class KaaServiceImpl extends KaaServiceImplAbstract implements KaaService {

    private final RESTClient restClient;

    @Value("${baseRepo}")
    private String baseEndpoint;

    public KaaServiceImpl(RESTClient restClient) {
        this.restClient = restClient;
    }

    @Override
    public List<String> getApplicationDataNames() {
        log.info("getApplicationDataNames - {}", baseEndpoint);

        ResponseEntity<KaaEndPointResponse> kaaEndPointResponse = restClient.execute(baseEndpoint, HttpMethod.GET);

        return Optional.ofNullable(kaaEndPointResponse.getBody())
                .filter(response -> !CollectionUtils.isEmpty(response.getKaaEP()))
                .map(k -> k.getKaaEP().stream().map(KEP::getName).collect(Collectors.toList()))
                .orElse(new ArrayList<>());

    }

    @Override
    public KaaApplication getApplicationDataNames(List<String> endpointId) {
        log.info("getApplicationDataNames from endpoints ({})", endpointId);

        List<KaaEndPoint> kaaEndPoints = getKaaEndpoint(endpointId);

        List<KaaEndPointConfiguration> config = kaaEndPoints.stream()
                .map(kaaEndPoint -> new KaaEndPointConfiguration(kaaEndPoint.getEndpointId(), new ArrayList<>()))
                .collect(Collectors.toList());


        return new KaaApplication("", config);
    }

    private List<KaaEndPoint> getKaaEndpoint(List<String> endpointId) {
        List<String> dataNames = getApplicationDataNames();

        return (CollectionUtils.isEmpty(endpointId)) ? getKaaEndPointsData(StringUtils.join(dataNames, ","), new KaaEndpointQueryParams()) :
                getKaaEndPointsData(getKaaEndPointConfiguration(endpointId, dataNames), new KaaEndpointQueryParams());
    }

    private List<KaaEndPointConfiguration> getKaaEndPointConfiguration(List<String> endpointId, List<String> dataNames) {
        return endpointId.stream()
                .map(kaaEndPointConfiguration -> new KaaEndPointConfiguration(kaaEndPointConfiguration, dataNames))
                .collect(Collectors.toList());
    }

    @Override
    public List<KaaEndPoint> getKaaEndPointsData(List<KaaEndPointConfiguration> kaaEndpointConfigurations,
                                                 KaaEndpointQueryParams kaaEndpointQueryParams) {

        return kaaEndpointConfigurations.stream()
                .flatMap(s -> setKaaEndPoint(s, kaaEndpointQueryParams).stream())
                .collect(Collectors.toList());

    }

    @Override
    public List<KaaEndPoint> getKaaEndPointsData(String timeSeriesName,
                                                 KaaEndpointQueryParams kaaEndpointQueryParams) {

        return setKaaEndPoint(timeSeriesName, kaaEndpointQueryParams);

    }

    @Override
    public boolean checkAvailability(List<String> endpointId, String dataName) {

        KaaApplication kaaApplication = getApplicationDataNames(endpointId.subList(0, 1));
        Optional<KaaEndPointConfiguration> kaaEndPointConfiguration = kaaApplication.getEndpoints().stream().findFirst();

        return kaaEndPointConfiguration.isPresent() && kaaEndPointConfiguration.get().getDataNames().contains(dataName);

    }


    private <T> List<KaaEndPoint> setKaaEndPoint(T kaaEndPointConfiguration,
                                                 KaaEndpointQueryParams kaaEndpointQueryParams) {

        ResponseEntity<KaaEndPointResponse> kaaResponse;

        if (kaaEndPointConfiguration instanceof KaaEndPointConfiguration) {
            kaaResponse = restClient.execute(createUrl((KaaEndPointConfiguration) kaaEndPointConfiguration, kaaEndpointQueryParams), HttpMethod.GET);
        } else {
            kaaResponse = restClient.execute(createUrl((String) kaaEndPointConfiguration, kaaEndpointQueryParams), HttpMethod.GET);

        }

        return Optional.ofNullable(kaaResponse.getBody())
                .filter(re -> Objects.nonNull(kaaResponse.getBody().getKaaEndPoint()))
                .map(KaaEndPointResponse::getKaaEndPoint).orElseGet(ArrayList::new);
    }

}
