package com.unimib.wearable.kaaService;


import com.unimib.wearable.models.response.KaaApplication;
import com.unimib.wearable.dto.KaaEndPointConfiguration;
import com.unimib.wearable.dto.KaaEndPoint;
import com.unimib.wearable.models.request.KaaEndpointQueryParams;

import java.util.List;

public interface KaaService {

    List<String> getApplicationDataNames();

    KaaApplication getApplicationDataNames(final List<String> endpointId);

    List<KaaEndPoint> getKaaEndPointsData(final List<KaaEndPointConfiguration> kaaEndpointConfigurations,  final KaaEndpointQueryParams kaaEndpointQueryParams);

    List<KaaEndPoint> getKaaEndPointsData(final String timeSeriesName, final KaaEndpointQueryParams kaaEndpointQueryParams);

    boolean checkAvailability(final List<String> endpointId, final String dataName);

}
