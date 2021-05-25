package com.unimib.wearable.csv;

import com.unimib.wearable.dto.KaaValue;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

@Service
public class CSVServiceImpl implements CSVService{

    String[] HEADERS = { "endpointId", "dateName", "timeStamp", "value"};

    @Override
    public void createCSVFile(Map<String, List<KaaValue>> value) throws IOException {
        FileWriter out = new FileWriter("samples.csv");
        try (CSVPrinter printer = new CSVPrinter(out, CSVFormat.DEFAULT.withHeader(HEADERS))) {
            value.forEach((author, title) -> {
                try {
                    printer.printRecord(author, title);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        }
    }
}
