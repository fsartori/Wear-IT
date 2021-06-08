package com.unimib.wearable.models.request;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

import static com.unimib.wearable.constants.Constants.*;

@Data
public class KaaEndpointQueryParams {

    private Date fromDate;
    private Date toDate;
    private String includeTime;
    private String sort;
    private Long samplePeriod;

    public KaaEndpointQueryParams() {
        init(new Date(DEFAULT_START_DATE), new Date(System.currentTimeMillis()), DEFAULT_INCLUDE_TIME, DEFAULT_DATA_SORT, DEFAULT_SAMPLE_PERIOD);
    }

    public KaaEndpointQueryParams(String includeTime, String sort) {
        init(new Date(DEFAULT_START_DATE), new Date(System.currentTimeMillis()), includeTime, sort, DEFAULT_SAMPLE_PERIOD);

    }

    public KaaEndpointQueryParams(Date fromDate, Date toDate, String includeTime, String sort, long samplePeriod) {
      init(fromDate, toDate, includeTime, sort, samplePeriod);
    }

    private void init(final Date fromDate, final Date toDate, final String includeTime, final String sort, final long samplePeriod){
        this.fromDate = fromDate;
        this.toDate = toDate;
        setIncludeTime(includeTime);
        setSort(sort);
        this.samplePeriod = samplePeriod;
    }

    private void setIncludeTime(final String includeTime){
        this.includeTime = StringUtils.isEmpty(includeTime) ? DEFAULT_INCLUDE_TIME : includeTime;
    }

    private void setSort(final String sort){
        this.sort = StringUtils.isEmpty(sort) ? DEFAULT_DATA_SORT : sort;
    }

}
