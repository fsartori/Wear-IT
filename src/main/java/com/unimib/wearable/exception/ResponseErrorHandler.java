package com.unimib.wearable.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.http.HttpStatus;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.DefaultResponseErrorHandler;

import java.io.IOException;
import java.util.Optional;

import static com.unimib.wearable.constants.Constants.MESSAGE;

@Slf4j
public class ResponseErrorHandler extends DefaultResponseErrorHandler {

    @Override
    public boolean hasError(ClientHttpResponse httpResponse) throws IOException {
        return (httpResponse.getStatusCode().series() == HttpStatus.Series.CLIENT_ERROR ||
                httpResponse.getStatusCode().series() == HttpStatus.Series.SERVER_ERROR);
    }

    @Override
    public void handleError(ClientHttpResponse httpResponse) throws IOException {
        throw new RequestException(httpResponse.getRawStatusCode(), getErrorMessage(httpResponse));
    }

    private String getErrorMessage(ClientHttpResponse httpResponse) {
        try {
            JSONObject jsonObject = new JSONObject(new String(getResponseBody(httpResponse)));
            return Optional.of(jsonObject.getString(MESSAGE)).orElse("unable to parse error message, no message present");
        } catch (Exception e) {
            log.error("unable to parse error message from response: {}", e.getMessage());
            return "unable to parse error message from response";
        }
    }

}
