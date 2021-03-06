package com.unimib.wearable.models.request;

import com.unimib.wearable.dto.response.data.KaaValue;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;
import java.util.Map;

@Data
@NoArgsConstructor
public class KaaEndpointDataRequest {

    private String endpointId;
    private Map<String, List<KaaValue>> values;

}
