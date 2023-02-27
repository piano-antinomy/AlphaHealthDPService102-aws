package com.alpha.health.dp.core.lambda;

import com.alpha.health.dp.core.lambda.model.DPServiceResponse;
import com.alpha.health.dp.core.lambda.model.processor.ProcessorRequest;
import com.alpha.health.dp.core.lambda.model.processor.ProcessorResponse;
import com.alpha.health.dp.core.lambda.model.processor.QueryClinicalTrialsProcessorRequest;
import com.alpha.health.dp.core.lambda.query.processors.QueryClinicalTrialsProcessorImpls;
import com.alpha.health.dp.core.lambda.query.processors.ServiceRequestProcessor;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.joda.JodaModule;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.HashMap;
import java.util.Map;

/**
 * Lambda handler of AlphaHealth DP Service.
 */
public class AlphaHealthDPServiceLambda implements RequestHandler<Map<String, Object>, DPServiceResponse> {

    private static final String RESOURCE = "resource";
    private static final String QUERY_PARAMS = "queryStringParameters";
    private static final String DEFAULT_EMPTY = "";
    private static final String QUERY_CLINICAL_TRIALS_PATH = "/query/clinicalTrials";
    private static final Logger LOGGER = LogManager.getLogger(AlphaHealthDPServiceLambda.class);
    // Use this map instead of Guice dependency injection to save lambda init time.
    private final Map<String, ServiceRequestProcessor> processorMap = new HashMap<String, ServiceRequestProcessor>(){{
        put(QUERY_CLINICAL_TRIALS_PATH, new QueryClinicalTrialsProcessorImpls());
    }};
    private final ObjectMapper objectMapper =
        new ObjectMapper().registerModule(new JodaModule()).setDateFormat(new SimpleDateFormat("yyyy-MM-dd"));

    @Override
    public DPServiceResponse handleRequest(Map<String, Object> input, Context context) {

        LOGGER.info(input);

        try {
            final String pathName = ((String) input.get(RESOURCE)).strip();
            final ProcessorRequest request = generateProcessorRequest(input, pathName);

            // routing to processors
            return DPServiceResponse
                .builder()
                .headers(Map.of(
                    "Access-Control-Allow-Origin", "http://localhost:3000",
                    "Access-Control-Allow-Methods", "GET",
                    "Access-Control-Allow-Headers", "Content-Type, Authorization, User-Agent"))
                .body(buildJsonString(processorMap.get(pathName).process(request)))
                .statusCode(200)
                .build();
        } catch (final JsonProcessingException e) {
            LOGGER.error("Failed to produce json responses", e);
            return buildErrorResponse(500, "InternalServerError");
        } catch (final IllegalArgumentException e) {
            LOGGER.error("Failed to respond due to client error", e);
            return buildErrorResponse(401, "InvalidParameterException");
        } catch (final RuntimeException e) {
            LOGGER.error("Failed to respond", e);
            return buildErrorResponse(500, "InternalServerError");
        }
    }

    private DPServiceResponse buildErrorResponse(final int errorCode, String errorMessage) {
        return DPServiceResponse
            .builder()
            .body(errorMessage)
            .headers(Map.of("X-Amzn-ErrorType", errorMessage))
            .statusCode(errorCode)
            .build();
    }

    private String buildJsonString(ProcessorResponse response) throws JsonProcessingException {
        return objectMapper.writeValueAsString(response);
    }

    private ProcessorRequest generateProcessorRequest(Map<String, Object> input, String pathName) {
        final Map<String, String> queryParams = (Map<String, String>) input.get(QUERY_PARAMS);

        if (QUERY_CLINICAL_TRIALS_PATH.equals(pathName)) {
            LOGGER.info("parsing from query params: " + queryParams);

            return QueryClinicalTrialsProcessorRequest.builder()
                .condition(
                    queryParams != null ? queryParams.getOrDefault("condition", DEFAULT_EMPTY) : DEFAULT_EMPTY)
                .location(
                    queryParams != null ? queryParams.getOrDefault("location", DEFAULT_EMPTY) : DEFAULT_EMPTY)
                .build();
        } else {
            throw new IllegalArgumentException("unsupported path: " + pathName);
        }
    }
}
