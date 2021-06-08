package com.unimib.wearable.rest;

import com.unimib.wearable.dto.response.data.KaaEndPointDataDTO;
import com.unimib.wearable.models.request.KaaEndpointDataRequest;
import com.unimib.wearable.exception.RequestException;
import com.unimib.wearable.csv.CSVOutputFormatService;
import com.unimib.wearable.models.response.BaseResponse;
import com.unimib.wearable.models.response.KaaResponse;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.InputStreamResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.CollectionUtils;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

public abstract class AbstractController {

    @Autowired
    private CSVOutputFormatService csvOutputFormatService;

    protected void validateOutputFormat(final String outputFormat) throws RequestException {
        if(!Arrays.asList("csv","xml","json").contains(outputFormat))
            throw new RequestException(HttpStatus.BAD_REQUEST.value(), "Invalid output format");

    }

    protected void validateParams(final KaaEndpointDataRequest data) throws RequestException {
        if (Objects.isNull(data.getEndpointId()) && Objects.isNull(data.getValues()))
            throw new RequestException(HttpStatus.BAD_REQUEST.value(), "No data to send");
    }

    protected void validateParams(final List<String> endpointList, final String dataName) throws RequestException {
        if (StringUtils.isEmpty(dataName) && CollectionUtils.isEmpty(endpointList))
            throw new RequestException(HttpStatus.BAD_REQUEST.value(), "Invalid params, endpointId and dataName must be not empty");
    }

    protected ResponseEntity<?> createResponse(final List<KaaEndPointDataDTO> kaaEndPointDataDTOS, final String format){
        return "csv".equals(format) ? createCSVResponse(kaaEndPointDataDTOS) :
                "xml".equals(format) ? createXMLResponse(kaaEndPointDataDTOS) :
                    createJsonResponse(kaaEndPointDataDTOS);
    }

    protected ResponseEntity<BaseResponse> createResponse(final boolean sent){
        return sent ? ResponseEntity.status(HttpStatus.ACCEPTED).body(new BaseResponse(HttpStatus.ACCEPTED.value(),"the data has been sent succesfully")):
                ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(new BaseResponse(HttpStatus.ACCEPTED.value(),"unexpected error while sending data"));
    }

    private ResponseEntity<?> createCSVResponse(final List<KaaEndPointDataDTO> kaaEndPointDataDTOS){
        Optional<InputStreamResource> fileInputStream = csvOutputFormatService.createCSVFile(kaaEndPointDataDTOS);
        HttpHeaders headers = new HttpHeaders();
        headers.set(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=" + "dataSample.csv");
        headers.set(HttpHeaders.CONTENT_TYPE, "text/csv");
        return ResponseEntity
                .status(HttpStatus.OK)
                .headers(headers)
                .body(fileInputStream.orElse(null));
    }

    private ResponseEntity<?> createXMLResponse(final List<KaaEndPointDataDTO> kaaEndPointDataDTOS){
        return ResponseEntity
                .status(HttpStatus.OK)
                .contentType(MediaType.APPLICATION_XML)
                .body(new KaaResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), kaaEndPointDataDTOS));
    }

    private ResponseEntity<?> createJsonResponse(final List<KaaEndPointDataDTO> kaaEndPointDataDTOS){
        return ResponseEntity
                .status(HttpStatus.OK)
                .body(new KaaResponse<>(HttpStatus.OK.value(), HttpStatus.OK.getReasonPhrase(), kaaEndPointDataDTOS));
    }

}
