package com.unimib.wearable.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.unimib.wearable.config.KaaEndpointMap;
import com.unimib.wearable.dto.KaaEndPoint;
import com.unimib.wearable.dto.KaaEndPointConfiguration;
import com.unimib.wearable.exception.RequestException;
import com.unimib.wearable.kaaService.KaaService;
import com.unimib.wearable.models.response.KaaApplication;
import com.unimib.wearable.mqtt.MqttService;
import com.unimib.wearable.response.KaaResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/api/kaa/v1")
@Slf4j
public class KaaController extends AbstractController {

    private final KaaService kaaService;
    private final MqttService mqttService;
    private final KaaEndpointMap kaaEndpointMap;

    @ApiResponse
    @GetMapping(value = "/scan", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<KaaResponse<?>> getAllApplicationNames() {
        log.info("get all applicationNames");
        return ResponseEntity.status(HttpStatus.OK)
                .body(new KaaResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                        kaaService.getApplicationDataNames()));
    }

    @ApiResponse
    @GetMapping(value = "/sensor", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<KaaResponse<?>> getSensor(@RequestParam(value = "endpointId", required = false) final List<String> endpointList,
                                                    @RequestParam(value = "dataName", required = false) final String dataName) throws RequestException {

        validateParams(endpointList, dataName);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new KaaResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                        kaaService.checkAvailability(endpointList, dataName)));
    }

    @ApiResponse
    @GetMapping(value = "/sensors", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<KaaResponse<?>> getSensors(@RequestParam(value = "endpointId") final List<String> endpointList) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new KaaResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                        kaaService.getApplicationDataNames(endpointList)));
    }

    @ApiResponse
    @GetMapping(value = "/play", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<KaaResponse<?>> getDataByDate(@RequestParam(value = "kaaEndpointConfigurations") final String kaaEndpointConfigurations,
                                                        @RequestParam(value = "fromDate", required = false) final long fromDate,
                                                        @RequestParam(value = "toDate", required = false) final long toDate,
                                                        @RequestParam(value = "includeTime", required = false) final String includeTime,
                                                        @RequestParam(value = "sort", required = false) final String sort,
                                                        @RequestParam(value = "periodSample", required = false) final long periodSample) {

        KaaApplication kaaApplication = kaaService.getApplicationDataNames(new ArrayList<>());
        List<KaaEndPointConfiguration> kaaEndPointConfigurations = kaaApplication.getEndpoints();

        return ResponseEntity.status(HttpStatus.OK)
                .body(new KaaResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                        kaaService.getKaaEndPointsData(kaaEndPointConfigurations, setQueryParams(fromDate, toDate, includeTime, sort, periodSample))));
    }

    @ApiResponse
    @PostMapping(value = "store", consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<KaaResponse<?>> store(String data) throws MqttException, JsonProcessingException, RequestException {

        validateParams(data);

        KaaEndPoint kaaEndPoint = new KaaEndPoint(data);

        if (!CollectionUtils.isEmpty(kaaEndpointMap.getEndpoints()) && !kaaEndpointMap.getEndpoints().containsKey(kaaEndPoint.getEndpointId()))
            throw new RequestException(HttpStatus.BAD_REQUEST.value(), "The endpoint is not present");

        mqttService.publishToServer(new KaaEndPoint(data));
        return null;
    }

}
