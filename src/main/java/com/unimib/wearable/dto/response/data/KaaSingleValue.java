package com.unimib.wearable.dto.response.data;

import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Getter
@SuperBuilder
public class KaaSingleValue extends KaaValue implements Serializable {

    private final String value;

}
