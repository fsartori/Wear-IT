package com.unimib.wearable.kaaService;

import com.unimib.wearable.dto.KaaEndPointConfiguration;
import com.unimib.wearable.models.request.KaaEndpointQueryParams;
import org.apache.commons.lang3.StringUtils;

import java.text.SimpleDateFormat;
import java.util.Date;


public abstract class KaaServiceImplAbstract {

    private static final String BASE_QUERY_PARAMS = "%s?timeSeriesName=%s";
    private static final String FROM_DATE = "&fromDate=%s";
    private static final String TO_DATE = "&toDate=%s";
    private static final String INCLUDE_TIME_FORMAT = "&includeTime=%s";
    private static final String SORT_FORMAT = "&sort=%s";
    private static final String ENDPOINT_BASE_FORMAT = BASE_QUERY_PARAMS + "&endpointId=%s";

    protected String createUrl(final KaaEndPointConfiguration kaaEndPointConfiguration, final KaaEndpointQueryParams kaaEndpointQueryParams) {
       return String.format(ENDPOINT_BASE_FORMAT, "uri", StringUtils.join(kaaEndPointConfiguration.getDataNames(), ","), kaaEndPointConfiguration.getEndpointId()) + setQueryParams(kaaEndpointQueryParams);
    }

    protected String createUrl(final String timeSeriesName, final KaaEndpointQueryParams kaaEndpointQueryParams) {
        return String.format(BASE_QUERY_PARAMS, "uri", timeSeriesName) + setQueryParams(kaaEndpointQueryParams);
    }

    private String setQueryParams(final KaaEndpointQueryParams kaaEndpointQueryParams) {
        String queryParams = String.format(FROM_DATE, kaaEndpointQueryParams.getFromDate() != null ?
                setDate(kaaEndpointQueryParams.getFromDate()) : setDate(new Date(Long.parseLong("OL"))));

        queryParams += String.format(TO_DATE, kaaEndpointQueryParams.getToDate() != null ?
                setDate(kaaEndpointQueryParams.getToDate()) : setDate(new Date(new Date(System.currentTimeMillis()).getTime())));

        if (kaaEndpointQueryParams.getIncludeTime() != null) {
            queryParams += String.format(INCLUDE_TIME_FORMAT, kaaEndpointQueryParams.getIncludeTime());
        }

        if (kaaEndpointQueryParams.getIncludeTime() != null) {
            queryParams += String.format(SORT_FORMAT, kaaEndpointQueryParams.getSort());
        }

        return queryParams;
    }

    protected String setDate(final Date date) {
        String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";
        return new SimpleDateFormat(DATE_FORMAT).format(date);
    }
}
