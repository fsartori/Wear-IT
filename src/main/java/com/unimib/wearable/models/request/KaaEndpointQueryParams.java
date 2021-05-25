package com.unimib.wearable.models.request;

import lombok.Data;
import org.apache.commons.lang3.StringUtils;

import java.util.Date;

@Data
public class KaaEndpointQueryParams {

    private Date fromDate;
    private Date toDate;
    private String includeTime;
    private String sort;
    private Long samplePeriod;

    public KaaEndpointQueryParams() {
        fromDate = new Date(1601596800000L);
        toDate = new Date(1601679600000L);
        includeTime = "both";
        sort = "ASC";
        samplePeriod = 1000L;
    }

    public KaaEndpointQueryParams(String includeTime, String sort) {
        fromDate = new Date(1601596800000L);
        toDate = new Date(1601679600000L);
        setIncludeTime(includeTime);
        setSort(sort);
        samplePeriod = 1000L;
    }

    public KaaEndpointQueryParams(String includeTime, String sort, Long samplePeriod) {
        fromDate = new Date(1601596800000L);
        toDate = new Date(1601679600000L);
        setIncludeTime(includeTime);
        setSort(sort);
        setSamplePeriod(samplePeriod);
    }

    public KaaEndpointQueryParams(Date fromDate, Date toDate, String includeTime, String sort) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        setIncludeTime(includeTime);
        setSort(sort);
        samplePeriod = 1000L;
    }

    public KaaEndpointQueryParams(Date fromDate, Date toDate, String includeTime, String sort, long samplePeriod) {
        this.fromDate = fromDate;
        this.toDate = toDate;
        setIncludeTime(includeTime);
        setSort(sort);
        setSamplePeriod(samplePeriod);
    }

    private void setIncludeTime(String includeTime){
        this.includeTime = StringUtils.isEmpty(includeTime) ? "both" : includeTime;
    }

    private void setSort(String sort){
        this.sort = StringUtils.isEmpty(sort) ? "ASC" : sort;
    }

    private void setSamplePeriod(Long samplePeriod){
        this.samplePeriod = (samplePeriod != null) ? samplePeriod : 1000L;
    }

}
