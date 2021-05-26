package com.unimib.wearable.format;

import com.unimib.wearable.format.csv.CSVOutputFormatService;
import com.unimib.wearable.format.xml.XMLOutputFormatService;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class OutputFormatService {

    private final CSVOutputFormatService csvOutputFormatService;
    private final XMLOutputFormatService xmlOutputFormatService;
}
