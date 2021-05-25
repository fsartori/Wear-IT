package com.unimib.wearable.csv;

import com.unimib.wearable.dto.KaaValue;

import java.io.IOException;
import java.util.List;
import java.util.Map;

public interface CSVService {

    void createCSVFile(Map<String, List<KaaValue>> value) throws IOException;
}
