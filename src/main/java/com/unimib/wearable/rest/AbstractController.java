package com.unimib.wearable.rest;

import com.unimib.wearable.exception.RequestException;
import com.unimib.wearable.models.request.KaaEndpointQueryParams;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;

import java.util.Date;
import java.util.List;

public abstract class AbstractController {

    protected void validateParams(String data) throws RequestException {
        if (StringUtils.isEmpty(data))
            throw new RequestException(HttpStatus.BAD_REQUEST.value(), "No endpoint provided, please provide a valid endpoint.");
    }

    protected void validateParams(List<String> endpointList, String dataName) throws RequestException {
        if (StringUtils.isEmpty(dataName) && endpointList.isEmpty())
            throw new RequestException(HttpStatus.BAD_REQUEST.value(), "Invalid params, endpointId and dataName must be not empty");
    }

    protected KaaEndpointQueryParams setQueryParams(final long fromDate, final long toDate, final String includeTime, final String sort, final long periodSample) {
        KaaEndpointQueryParams kaaEndpointQueryParams;

        try {
            Date from = new Date(fromDate);
            Date to = new Date(toDate);

            kaaEndpointQueryParams = (periodSample < 1000L) ?
                    new KaaEndpointQueryParams(from, to, includeTime, sort) :
                    new KaaEndpointQueryParams(from, to, includeTime, sort, periodSample);

        } catch (Exception e) {
            kaaEndpointQueryParams = (1000L > periodSample) ?
                    new KaaEndpointQueryParams(includeTime, sort) :
                    new KaaEndpointQueryParams(includeTime, sort, periodSample);
        }

        return kaaEndpointQueryParams;
    }
}
