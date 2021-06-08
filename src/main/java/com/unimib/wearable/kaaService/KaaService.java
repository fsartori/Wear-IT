package com.unimib.wearable.kaaService;


import com.unimib.wearable.dto.response.data.KaaEndPointDataDTO;
import com.unimib.wearable.models.response.KaaApplication;

import java.util.List;

public interface KaaService {

    List<String> getApplicationDataNames() throws Exception;

    KaaApplication getApplicationDataNames(final List<String> endpointId) throws Exception;

    List<KaaEndPointDataDTO> getKaaEndPointsData(String fromDate, String toDate, String includeTime, String sort, String periodSample) throws Exception;

    boolean checkAvailability(final List<String> endpointId, final String dataName) throws Exception;

}
