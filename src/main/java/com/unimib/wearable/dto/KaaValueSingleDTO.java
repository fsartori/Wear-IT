package com.unimib.wearable.dto;

import lombok.Data;

import java.util.List;

@Data
public class KaaValueSingleDTO {

    private String timestamp;
    private List<String> values;
}
