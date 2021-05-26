package com.unimib.wearable.format.csv;

import com.unimib.wearable.dto.KaaValue;
import com.unimib.wearable.dto.response.KaaMultiValue;
import com.unimib.wearable.dto.response.KaaSingleValue;
import com.unimib.wearable.dto.response.data.KaaEndPointDataDTO;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVPrinter;
import org.springframework.core.io.InputStreamResource;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
public class CSVOutputFormatOutputFormatServiceImpl implements CSVOutputFormatService {

    private static final String[] HEADERS = {"endpointId", "dateName", "timeStamp", "value"};

    @Override
    public Optional<InputStreamResource> createCSVFile(final List<KaaEndPointDataDTO> kaaEndPointDataDTOS) {
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        return createCSVPrinter(out)
                .map(csvPrinter -> write(kaaEndPointDataDTOS, csvPrinter, out))
                .filter(Optional::isPresent)
                .map(output -> new InputStreamResource(output.get()));
    }

    private Optional<CSVPrinter> createCSVPrinter(final ByteArrayOutputStream out) {
        try {
            return Optional.of(new CSVPrinter(new PrintWriter(out), CSVFormat.DEFAULT.withHeader(HEADERS)));
        } catch (IOException e) {
            log.error("an error has occurred while creating csv printer: {}", e.getMessage());
            return Optional.empty();
        }
    }

    private Optional<ByteArrayInputStream> write(final List<KaaEndPointDataDTO> kaaEndPointDataDTOS, final CSVPrinter csvPrinter, final ByteArrayOutputStream out) {
        try {
            kaaEndPointDataDTOS
                    .forEach(kaaEndPointDataDTO -> printCSV(kaaEndPointDataDTO.getValues(), csvPrinter, kaaEndPointDataDTO.getEndpointId()));
            csvPrinter.flush();
            return Optional.of(new ByteArrayInputStream(out.toByteArray()));
        } catch (IOException e) {
            return Optional.empty();
        }
    }

    private void printCSV(final Map<String, List<KaaValue>> value, final CSVPrinter csvPrinter, final String endpointId) {
        value.forEach((key, kaaValueList) -> kaaValueList.forEach(kaaValue -> printCSVRow(csvPrinter, endpointId, key, kaaValue)));
    }

    private void printCSVRow(final CSVPrinter printer, final String endpointId, final String sensor, final KaaValue kaaValue) {
        try {
            printer.printRecord(endpointId, sensor, kaaValue.getTimestamp(), getValue(kaaValue));
        } catch (IOException e) {
            log.error("an error has occurred while creating file: {}", e.getMessage());
        }
    }

    private String getValue(KaaValue kaaValue) {
        return kaaValue instanceof KaaSingleValue ? ((KaaSingleValue) kaaValue).getValue() : getMultiValue(((KaaMultiValue) kaaValue).getValues());
    }

    private String getMultiValue(Map<String, String> multiValue) {
        return multiValue.entrySet().stream()
                .map(entry -> entry.getKey() + ":" + entry.getValue())
                .collect(Collectors.joining(","));
    }

}
