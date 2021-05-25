package com.unimib.wearable.rest;

import com.unimib.wearable.dto.response.data.KaaEndpointDataRequest;
import com.unimib.wearable.exception.RequestException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.util.CollectionUtils;

import java.util.List;
import java.util.Objects;

public abstract class AbstractController {

    protected void validateParams(KaaEndpointDataRequest data) throws RequestException {
        if (Objects.isNull(data.getEndpointId()) && Objects.isNull(data.getValues()))
            throw new RequestException(HttpStatus.BAD_REQUEST.value(), "No data to send");
    }

    protected void validateParams(List<String> endpointList, String dataName) throws RequestException {
        if (StringUtils.isEmpty(dataName) && CollectionUtils.isEmpty(endpointList))
            throw new RequestException(HttpStatus.BAD_REQUEST.value(), "Invalid params, endpointId and dataName must be not empty");
    }

}
