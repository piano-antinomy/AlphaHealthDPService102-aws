package com.alpha.health.dp.core.lambda;

import com.alpha.health.dp.core.lambda.model.DPServiceResponse;
import com.alpha.health.dp.core.lambda.model.processor.ProcessorRequest;
import com.alpha.health.dp.core.lambda.model.processor.QueryClinicalTrialsProcessorRequest;
import com.alpha.health.dp.core.lambda.query.processors.QueryClinicalTrialsProcessorImpls;
import com.alpha.health.dp.core.lambda.query.processors.ServiceRequestProcessor;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.HashMap;
import java.util.Map;

/**
 * Lambda handler of AlphaHealth DP Service.
 */
public class AlphaHealthDPServiceLambda implements RequestHandler<Map<String, Object>, DPServiceResponse> {

    private static final String RESOURCE = "resource";
    private static final String QUERY_PARAMS = "queryStringParameters";

    private static final String QUERY_CLINICAL_TRIALS_PATH = "/query/clinicalTrials";
    private static final Logger LOGGER = LogManager.getLogger(AlphaHealthDPServiceLambda.class);
    private final Map<String, ServiceRequestProcessor> processorMap = new HashMap<String, ServiceRequestProcessor>(){{
        put(QUERY_CLINICAL_TRIALS_PATH, new QueryClinicalTrialsProcessorImpls());
    }};
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public DPServiceResponse handleRequest(Map<String, Object> input, Context context) {

        LOGGER.info(input);

        try {
            final String pathName = ((String) input.get(RESOURCE)).strip();
            final ProcessorRequest request = generateProcessorRequest(input, pathName);

            // routing to processors
            return DPServiceResponse
                .builder()
                .body(processorMap.get(pathName).process(request).toString())
                .statusCode(200)
                .build();

        } catch (final IllegalArgumentException e) {
            LOGGER.error("Failed to respond due to client error", e);
            return DPServiceResponse
                .builder()
                .body("Invalid input!")
                .headers(Map.of("X-Amzn-ErrorType", "InvalidParameterException"))
                .statusCode(401)
                .build();
        } catch (final RuntimeException e) {
            LOGGER.error("Failed to respond", e);
            return DPServiceResponse
                .builder()
                .body("Internal Exceptions")
                .headers(Map.of("X-Amzn-ErrorType", "InternalServerError"))
                .statusCode(500)
                .build();
        }
    }

    private ProcessorRequest generateProcessorRequest(Map<String, Object> input, String pathName) {
        final Map<String, String> queryParams = (Map<String, String>) input.get(QUERY_PARAMS);

        if (QUERY_CLINICAL_TRIALS_PATH.equals(pathName)) {
            LOGGER.info("parsing from query params: " + queryParams);
            return QueryClinicalTrialsProcessorRequest.builder()
                .condition(queryParams.getOrDefault("condition", ""))
                .location(queryParams.getOrDefault("location", ""))
                .build();
        } else {
            throw new IllegalArgumentException("unsupported path: " + pathName);
        }
    }
}
