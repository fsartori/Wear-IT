package com.unimib.wearable.kaaService;

import com.unimib.wearable.dto.response.config.KaaEndPointConfiguration;
import com.unimib.wearable.models.request.KaaEndpointQueryParams;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Objects;

import static com.unimib.wearable.constants.Constants.*;

@Slf4j
public abstract class KaaServiceImplAbstract {

    @Value("${base-repo}")
    protected String baseEndpoint;

    @Value("${base-endpoint}")
    protected String baseUrl;

    @Value("${kaaAppName}")
    protected String appName;

    protected String createUrl(final String baseUrl, final KaaEndPointConfiguration kaaEndPointConfiguration, final KaaEndpointQueryParams kaaEndpointQueryParams) {
        return String.format(ENDPOINT_BASE_FORMAT, baseUrl, StringUtils.join(kaaEndPointConfiguration.getDataNames(), ","), kaaEndPointConfiguration.getEndpointId()) + setUrlQueryParams(kaaEndpointQueryParams);
    }

    protected String createUrl(final String baseUrl, final String timeSeriesName, final KaaEndpointQueryParams kaaEndpointQueryParams) {
        return String.format(BASE_QUERY_PARAMS, baseUrl, timeSeriesName) + setUrlQueryParams(kaaEndpointQueryParams);
    }

    private String setUrlQueryParams(final KaaEndpointQueryParams kaaEndpointQueryParams) {
        String queryParams = String.format(FROM_DATE, Objects.nonNull(kaaEndpointQueryParams.getFromDate()) ?
                setDate(kaaEndpointQueryParams.getFromDate()) : setDate(new Date(Long.parseLong("OL"))));

        queryParams += String.format(TO_DATE, Objects.nonNull(kaaEndpointQueryParams.getToDate()) ?
                setDate(kaaEndpointQueryParams.getToDate()) : setDate(new Date(new Date(System.currentTimeMillis()).getTime())));

        if (Objects.nonNull(kaaEndpointQueryParams.getIncludeTime()))
            queryParams += String.format(INCLUDE_TIME_FORMAT, kaaEndpointQueryParams.getIncludeTime());


        if (Objects.nonNull(kaaEndpointQueryParams.getIncludeTime()))
            queryParams += String.format(SORT_FORMAT, kaaEndpointQueryParams.getSort());


        return queryParams;
    }

    protected String setDate(final Date date) {
        return new SimpleDateFormat(DATE_FORMAT).format(date);
    }

    protected KaaEndpointQueryParams setQueryParamsFromRequest(String fromDate, String toDate, String includeTime, String sort, String samplePeriod) {
        KaaEndpointQueryParams kaaEndpointQueryParams;

        try {
            kaaEndpointQueryParams = new KaaEndpointQueryParams(new Date(Long.parseLong(fromDate)), new Date(Long.parseLong(toDate)), includeTime, sort, Long.parseLong(samplePeriod));
        } catch (Exception e) {
            log.error("An error occurred while parsing data from request: {}", e.getMessage());
            kaaEndpointQueryParams = new KaaEndpointQueryParams(includeTime, sort);
        }

        return kaaEndpointQueryParams;
    }

}
