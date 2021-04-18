package com.unimib.wearable.csv;

import com.unimib.wearable.dto.KaaValueSingleDTO;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;

public class CSVServiceImpl implements CSVService{

    String[] HEADERS = { "endpointId", "dateName", "timeStamp", "value"};

    @Override
    public void createCSVFile(Map<String, List<KaaValueSingleDTO>> value) throws IOException {
        FileWriter out = new FileWriter("book_new.csv");
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
