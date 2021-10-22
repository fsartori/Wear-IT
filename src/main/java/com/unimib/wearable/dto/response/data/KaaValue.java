package com.unimib.wearable.dto.response.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.experimental.SuperBuilder;

import java.io.Serializable;

@Getter
@SuperBuilder
@AllArgsConstructor
@Data
public class KaaValue implements Serializable {

    private String timestamp;

}
