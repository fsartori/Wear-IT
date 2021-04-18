package com.unimib.wearable.csv;

import com.unimib.wearable.dto.KaaValueSingleDTO;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface CSVService {

    void createCSVFile(Map<String, List<KaaValueSingleDTO>> value) throws IOException;
}
