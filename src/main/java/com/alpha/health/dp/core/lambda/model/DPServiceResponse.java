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
public class DPServiceResponse {

    @Builder.Default
    private boolean isBase64Encoded = false;

    @Builder.Default
    private Map<String, String> headers = Collections.emptyMap();

    @Builder.Default
    private Map<String, String> multiValueHeaders = Collections.emptyMap();

    private int statusCode;

    private String body;
}
