package com.unimib.wearable.constants;

public class Constants {

    public static final String KAA_API = "/api/kaa/v1";
    public static final String SCAN = "/scan";
    public static final String SENSOR = "/sensor";
    public static final String SENSORS = "/sensors";
    public static final String PLAY = "/play";
    public static final String STORE = "/store";

    public static final String ENDPOINT_ID = "endpointId";
    public static final String DATA_NAME = "dataName";
    public static final String FROM = "fromDate";
    public static final String TO = "toDate";
    public static final String INCLUDE_TIME = "includeTime";
    public static final String SORT = "sort";
    public static final String PERIOD_SAMPLE = "periodSample";
    public static final String OUTPUT_FORMAT = "format";


    public static final String BASE_QUERY_PARAMS = "%s?timeSeriesName=%s";
    public static final String FROM_DATE = "&fromDate=%s";
    public static final String TO_DATE = "&toDate=%s";
    public static final String INCLUDE_TIME_FORMAT = "&includeTime=%s";
    public static final String SORT_FORMAT = "&sort=%s";
    public static final String ENDPOINT_BASE_FORMAT = BASE_QUERY_PARAMS + "&endpointId=%s";
    public static final String DATE_FORMAT = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'";

    public static final String DATA_NAME_SERVICE = "dataNameService";
}
