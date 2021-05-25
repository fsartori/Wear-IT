package com.unimib.wearable.kaaService;

import com.unimib.wearable.dto.response.config.KaaEndPointConfiguration;
import com.unimib.wearable.models.request.KaaEndpointQueryParams;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.unimib.wearable.constants.Constants.*;


public abstract class KaaServiceImplAbstract {

    @Value("${base-repo}")
    protected String baseEndpoint;

    @Value("${base-endpoint}")
    protected String baseUrl;

    @Value("${kaaAppName}")
    protected String appName;

    protected String createUrl(final String baseUrl, final KaaEndPointConfiguration kaaEndPointConfiguration, final KaaEndpointQueryParams kaaEndpointQueryParams) {
        return String.format(ENDPOINT_BASE_FORMAT, baseUrl, StringUtils.join(kaaEndPointConfiguration.getDataNames(), ","), kaaEndPointConfiguration.getEndpointId()) + setQueryParams(kaaEndpointQueryParams);
    }

    protected String createUrl(final String baseUrl, final String timeSeriesName, final KaaEndpointQueryParams kaaEndpointQueryParams) {
        return String.format(BASE_QUERY_PARAMS, baseUrl, timeSeriesName) + setQueryParams(kaaEndpointQueryParams);
    }

    private String setQueryParams(final KaaEndpointQueryParams kaaEndpointQueryParams) {
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

    protected KaaEndpointQueryParams setQueryParams(String fromDate, String toDate, String includeTime, String sort, String periodSample) {
        KaaEndpointQueryParams kaaEndpointQueryParams;

        long pS = StringUtils.isEmpty(fromDate) ? 1000L : Long.parseLong(periodSample);
        long fD = StringUtils.isEmpty(fromDate) ? 1601596800000L : Long.parseLong(fromDate);
        long tD = StringUtils.isEmpty(fromDate) ? 1601679600000L : Long.parseLong(toDate);

        try {
            Date from = new Date(fD);
            Date to = new Date(tD);

            kaaEndpointQueryParams = (pS < 1000L) ?
                    new KaaEndpointQueryParams(from, to, includeTime, sort) :
                    new KaaEndpointQueryParams(from, to, includeTime, sort, pS);

        } catch (Exception e) {
            kaaEndpointQueryParams = (1000L > pS) ?
                    new KaaEndpointQueryParams(includeTime, sort) :
                    new KaaEndpointQueryParams(includeTime, sort, pS);
        }

        return kaaEndpointQueryParams;
    }

    private Map<String, String> setQueryParam(final KaaEndpointQueryParams kaaEndpointQueryParams){
        Map<String, String> params = new HashMap<>();

        params.put(FROM_DATE, setDate(kaaEndpointQueryParams.getFromDate()));
        params.put(TO_DATE, setDate(kaaEndpointQueryParams.getToDate()));
        params.put(INCLUDE_TIME_FORMAT, kaaEndpointQueryParams.getIncludeTime());
        params.put(SORT_FORMAT, kaaEndpointQueryParams.getSort());

        return params;
    }
}
