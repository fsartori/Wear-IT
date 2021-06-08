package com.unimib.wearable.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.unimib.wearable.config.OpenApiResponse;
import com.unimib.wearable.dto.response.data.KaaEndPointDataDTO;
import com.unimib.wearable.models.request.KaaEndpointDataRequest;
import com.unimib.wearable.exception.RequestException;
import com.unimib.wearable.kaaService.KaaService;
import com.unimib.wearable.models.response.*;
import com.unimib.wearable.mqtt.KaaEndpointMap;
import com.unimib.wearable.mqtt.MqttService;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.unimib.wearable.constants.Constants.*;

@RestController
@AllArgsConstructor
@EnableCaching
@RequestMapping(KAA_API)
@Slf4j
public class KaaController extends AbstractController {


    private final KaaService kaaService;
    private final MqttService mqttService;
    private final KaaEndpointMap kaaEndpointMap;

    @ApiResponse
    @GetMapping(value = SCAN, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<KaaResponse<Sensor>> getAllApplicationNames() throws Exception {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new KaaResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                        new Sensor(new DataNames(kaaService.getApplicationDataNames()))));
    }

    @ApiResponse
    @GetMapping(value = SENSOR, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<KaaResponse<EndpointAvailability>> getSensor(@RequestParam(value = ENDPOINT_ID, required = false) final List<String> endpointList,
                                                                       @RequestParam(value = DATA_NAME, required = false) final String dataName) throws Exception {

        validateParams(endpointList, dataName);

        return ResponseEntity.status(HttpStatus.OK)
                .body(new KaaResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                        new EndpointAvailability(kaaService.checkAvailability(endpointList, dataName))));
    }

    @ApiResponse
    @GetMapping(value = SENSORS, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<KaaResponse<KaaApplication>> getSensors(@RequestParam(value = ENDPOINT_ID) final List<String> endpointId) throws Exception {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new KaaResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(),
                        kaaService.getApplicationDataNames(endpointId)));
    }

    @ApiResponse
    @GetMapping(value = PLAY, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, MediaType.MULTIPART_FORM_DATA_VALUE})
    public ResponseEntity<?> getDataSamples(@RequestParam(value = FROM, required = false) final String fromDate,
                                            @RequestParam(value = TO, required = false) final String toDate,
                                            @RequestParam(value = INCLUDE_TIME, required = false) final String includeTime,
                                            @RequestParam(value = SORT, required = false) final String sort,
                                            @RequestParam(value = PERIOD_SAMPLE, required = false) final String periodSample,
                                            @RequestParam(value = OUTPUT_FORMAT, required = false, defaultValue = "json") final String format) throws Exception {

        validateOutputFormat(format);

        List<KaaEndPointDataDTO> kaaEndPointDataDTOS = kaaService.getKaaEndPointsData(fromDate, toDate, includeTime, sort, periodSample);
        return createResponse(kaaEndPointDataDTOS, format);
    }

    @OpenApiResponse
    @PostMapping(value = STORE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<BaseResponse> store(@RequestBody KaaEndpointDataRequest kaaEndPointDataDTO) throws MqttException, JsonProcessingException, RequestException {
        validateParams(kaaEndPointDataDTO);

        if (!CollectionUtils.isEmpty(kaaEndpointMap.getEndpoints()) && !kaaEndpointMap.getEndpoints().containsKey(kaaEndPointDataDTO.getEndpointId()))
            throw new RequestException(HttpStatus.BAD_REQUEST.value(), "The endpoint is not present");

        return createResponse(mqttService.publishToServer(kaaEndPointDataDTO));
    }

}
