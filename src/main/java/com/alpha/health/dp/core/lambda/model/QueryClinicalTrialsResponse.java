package com.alpha.health.dp.core.lambda.model;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.Collections;
import java.util.Map;

/**
 * response body
 */

@Getter
@Setter
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class QueryClinicalTrialsResponse {

    boolean isBase64Encoded = false;

    Map<String, String> headers = Collections.emptyMap();
    Map<String, String> multiValueHeaders = Collections.emptyMap();

    public int statusCode;

    public String body;
}
