package com.unimib.wearable.constants;

public class Constants {
    public static final String KAA_EPTS_API_BASE_URL = "https://cloud.kaaiot.com/epts/api/v1/";
    public static final String KAA_MQTT_SERVER = "tcp://mqtt.cloud.kaaiot.com:1883";
    public static final String KAA_EPTS_API_BEARER_TOKEN = "Bearer eyJhbGciOiJSUzI1NiIsInR5cCIgOiAiSldUIiwia2lkIiA6ICJtQ2NXZFYtX005V2daaFRXVjBMS2twUm1OT1FkVnh6amExcUxaTnZGdGhZIn0.eyJqdGkiOiI1OGM5ZjIzMy1kYmIwLTQxYzAtYjgxZS1lNjQyOGI2NjNlMzEiLCJleHAiOjE2MDgwMjIyMjUsIm5iZiI6MCwiaWF0IjoxNjA1NDMwMjI2LCJpc3MiOiJodHRwczovL2F1dGhoLmNsb3VkLmthYWlvdC5jb20vYXV0aC9yZWFsbXMvNzg4ZTk1Y2YtNDUyMC00MzEwLWFjOWQtZmVhZTIyYzRiMjc5Iiwic3ViIjoiODg5NTAxODAtNjI5ZS00ZTUzLTgxNDItZjQxY2MxNjQwYzAzIiwidHlwIjoiQmVhcmVyIiwiYXpwIjoiYTM4MzVlNTktYTIyNS00ZTIxLThlMmEtZGIzYjYxMzRkZDRjIiwiYXV0aF90aW1lIjoxNjA1NDMwMjI1LCJzZXNzaW9uX3N0YXRlIjoiNGZmMjhjM2ItNDcyYi00ZWU2LTk1MmEtYjFhZTQwODAxNGNiIiwiYWNyIjoiMSIsImFsbG93ZWQtb3JpZ2lucyI6W10sInJlYWxtX2FjY2VzcyI6eyJyb2xlcyI6WyJhbmFseXRpY3MtYWRtaW4iLCJ0ZW5hbnQtYWRtaW4iLCJvZmZsaW5lX2FjY2VzcyIsImFuYWx5dGljcy1yZWFkZXIiLCJ1bWFfYXV0aG9yaXphdGlvbiIsInRlbmFudC1yZWFkZXIiXX0sInNjb3BlIjoiZW1haWwgcHJvZmlsZSIsInRlbmFudF9pZCI6Ijc4OGU5NWNmLTQ1MjAtNDMxMC1hYzlkLWZlYWUyMmM0YjI3OSIsImVtYWlsX3ZlcmlmaWVkIjpmYWxzZSwicm9sZXMiOlsiYW5hbHl0aWNzLWFkbWluIiwidGVuYW50LWFkbWluIiwib2ZmbGluZV9hY2Nlc3MiLCJhbmFseXRpY3MtcmVhZGVyIiwidW1hX2F1dGhvcml6YXRpb24iLCJ0ZW5hbnQtcmVhZGVyIl0sIm5hbWUiOiJEYXZpZGUgR3VhbGFuZHJpcyIsInByZWZlcnJlZF91c2VybmFtZSI6ImRhdmlkZS5ndWFsYW5kcmlzQHR3aWdvc3RvcmUuaXQiLCJnaXZlbl9uYW1lIjoiRGF2aWRlIiwiZmFtaWx5X25hbWUiOiJHdWFsYW5kcmlzIiwiZW1haWwiOiJkYXZpZGUuZ3VhbGFuZHJpc0B0d2lnb3N0b3JlLml0In0.S3ueUGezjmugyrWVkzo3Fi5NOSD62zJZ_6sFUiSTfgM3RDMujh5Iwp6xyj3E_iV_I-gE5FIyf3Fc8M0Ud5C8LRCNOAEH4R8TGjIZ8pFWmOVHpCLdE19V3y_j65JJwSkfHJwKIGv2bGZqLO9lCFWjOZ_au3QKPXze1ctM7vs18QxFTcVW__PqH1l-ForrO2qhhWlNW9B1ka-N4Wl3Fs3FaMOrx_568LqCz1oO75kjMKXlekRnNxwa4zApVsHmMjTgfLfUL9ao-cX0VMmmbMoLsuA0AEsRA5uHwBBXFqm0MvmQBn2cJCwB276WW-XN1ZYsfYcjDBcxCt_yPFslhCzZCw";
    public static final String KAA_APPLICATION_NAME = "btngtro547tsntf25rtg";
    public static final String KAA_APPLICATION_VERSION = KAA_APPLICATION_NAME + "-v1";
    public static final String KAA_MOBILE_ENDPOINT_ID = "d291e666-f622-4ade-9a14-acbb6419ffd0";
    public static final String KAA_WATCH_ENDPOINT_ID = "12b063b9-d51d-4d4f-a715-b7fbd244ad06";
    public static final String KAA_MOBILE_TOKEN = "mobile_1";
    public static final String KAA_WATCH_TOKEN = "watch_1";
    public static final String KAA_MOBILE_TOPIC = "kp1/" + KAA_APPLICATION_VERSION + "/dcx/" + KAA_MOBILE_TOKEN + "/json";
	public static final String KAA_WATCH_TOPIC = "kp1/" + KAA_APPLICATION_VERSION + "/dcx/" + KAA_WATCH_TOKEN + "/json";
    public static final String ENDPOINT_REPOSITORY_URL = "applications/"+KAA_APPLICATION_NAME+"/time-series/data";
    public static final String APPLICATION_REPOSITORY_URL = "time-series/config";
    
    public static final String DEFAULT_SORT = "ASC";
    public static final String DEFAULT_INCLUDE_TIME = "both";
    public static final String DEFAULT_OUTPUT_FORMAT = "JSON";
    public static final long DEFAULT_SAMPLE_PERIOD = 1000;

    public static final String ENDPOINT_FORMAT = "%s?timeSeriesName=%s&endpointId=%s&fromDate=%s&toDate=s%";
}