package com.unimib.wearable.dto.response.data;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;
import java.util.Map;

@SuperBuilder
@Getter
public class KaaMultiValue extends KaaValue implements Serializable {

    private final Map<String, String> values;

}
