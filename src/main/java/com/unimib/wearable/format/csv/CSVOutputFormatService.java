package com.unimib.wearable.format.csv;

import com.unimib.wearable.dto.response.data.KaaEndPointDataDTO;
import org.springframework.core.io.InputStreamResource;

import java.util.List;
import java.util.Optional;

public interface CSVOutputFormatService {

    Optional<InputStreamResource> createCSVFile(final List<KaaEndPointDataDTO> kaaEndPointDataDTOS);
}
