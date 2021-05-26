package com.unimib.wearable.rest;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.unimib.wearable.config.OpenApiResponse;
import com.unimib.wearable.dto.response.data.KaaEndPointDataDTO;
import com.unimib.wearable.dto.response.data.KaaEndpointDataRequest;
import com.unimib.wearable.exception.RequestException;
import com.unimib.wearable.format.OutputFormatService;
import com.unimib.wearable.format.csv.CSVOutputFormatService;
import com.unimib.wearable.format.xml.KaaEndPointDataXML;
import com.unimib.wearable.kaaService.KaaService;
import com.unimib.wearable.models.response.KaaApplication;
import com.unimib.wearable.mqtt.KaaEndpointMap;
import com.unimib.wearable.mqtt.MqttService;
import com.unimib.wearable.response.DataNames;
import com.unimib.wearable.response.EndpointAvailability;
import com.unimib.wearable.response.KaaResponse;
import com.unimib.wearable.response.Sensor;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

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
    private final OutputFormatService outputFormatService;

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
    @GetMapping(value = PLAY, produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE, "text/csv"})
    public ResponseEntity<?> getDataByDate(@RequestParam(value = FROM, required = false) final String fromDate,
                                                        @RequestParam(value = TO, required = false) final String toDate,
                                                        @RequestParam(value = INCLUDE_TIME, required = false) final String includeTime,
                                                        @RequestParam(value = SORT, required = false) final String sort,
                                                        @RequestParam(value = PERIOD_SAMPLE, required = false) final String periodSample,
                                                        @RequestParam(value = OUTPUT_FORMAT, required = false) final String format) {

        List<KaaEndPointDataDTO> kaaEndPointDataDTOS = kaaService.getKaaEndPointsData(fromDate, toDate, includeTime, sort, periodSample);

        if ("csv".equals(format)) {
            Optional<InputStreamResource> fileInputStream = outputFormatService.getCsvOutputFormatService()
                    .createCSVFile(kaaEndPointDataDTOS);
            HttpHeaders headers = new HttpHeaders();
            headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + "test.csv");
            headers.set(HttpHeaders.CONTENT_TYPE, "text/csv");
            return ResponseEntity.status(HttpStatus.OK)
                    .headers(headers)
                    .body(fileInputStream.isEmpty() ? null : fileInputStream.get());
        } else if ("xml".equals(format)) {
            KaaEndPointDataXML kaaEndPointDataXML = new KaaEndPointDataXML();
           // kaaEndPointDataXML.setKaaEndPointDataDTOS(kaaEndPointDataDTOS);
            return ResponseEntity.status(HttpStatus.OK)
                    .contentType(MediaType.APPLICATION_XML)
                    .body(new KaaResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), kaaEndPointDataXML));
        }

        return ResponseEntity.status(HttpStatus.OK)
                .body(new KaaResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), kaaEndPointDataDTOS));
    }

    @OpenApiResponse
    @PostMapping(value = STORE, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<KaaResponse<?>> store(@RequestBody KaaEndpointDataRequest kaaEndPointDataDTO) throws MqttException, JsonProcessingException, RequestException {
        validateParams(kaaEndPointDataDTO);

        if (!CollectionUtils.isEmpty(kaaEndpointMap.getEndpoints()) && !kaaEndpointMap.getEndpoints().containsKey(kaaEndPointDataDTO.getEndpointId()))
            throw new RequestException(HttpStatus.BAD_REQUEST.value(), "The endpoint is not present");

        boolean sent = mqttService.publishToServer(kaaEndPointDataDTO);

        return ResponseEntity.status(sent ? HttpStatus.OK : HttpStatus.NOT_FOUND)
                .body(new KaaResponse<>(1, ""));
    }

}
