package com.bnym.phm.bids.commons.exception.handler;

import com.bnym.phm.bids.commons.exception.BIDSApplicationException;
import com.fasterxml.jackson.databind.ObjectMapper;
import feign.Response;
import feign.codec.ErrorDecoder;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static feign.FeignException.errorStatus;

public class BIDSErrorDecoder implements ErrorDecoder {


    @Override
    public Exception decode(String methodKey,Response response) {
        HttpStatus statusCode = HttpStatus.valueOf(response.status());
        HttpHeaders responseHeaders = new HttpHeaders();
        for (Map.Entry<String, Collection<String>> entry : response.headers().entrySet()) {
            responseHeaders.put(entry.getKey(), new ArrayList<>(entry.getValue()));
        }
        String responseBodyMessage;
        try {
            ObjectMapper mapper = new ObjectMapper();
            Map<String, Object> errorObject = mapper.readValue(response.body().asReader(), HashMap.class);
            responseBodyMessage = (String) errorObject.get("message");
        } catch (IOException e) {
            throw new BIDSApplicationException(statusCode, "Failed to process response body.", e);
        }
        if (response.status() >= 400 && response.status() <= 499) {
            return new BIDSApplicationException(statusCode, responseBodyMessage);
        }
        if (response.status() >= 500 && response.status() <= 599) {
            return new BIDSApplicationException(statusCode, responseBodyMessage);
        }

        return errorStatus(methodKey, response);
    }
}
