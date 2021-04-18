package com.unimib.wearable.dto;

import lombok.Data;

import java.util.Date;
import java.util.Map;

@Data
//basta mappare la response da kaa
public class KaaValueMultiDTO {

    private String timestamp;
    private Map<String, Integer> values;
}
