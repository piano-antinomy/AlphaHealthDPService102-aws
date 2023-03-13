package com.alpha.health.dp.core.lambda;

import com.alpha.health.dp.core.lambda.model.DPServiceResponse;
import com.alpha.health.dp.core.lambda.model.processor.ProcessorRequest;
import com.alpha.health.dp.core.lambda.model.processor.ProcessorResponse;
import com.alpha.health.dp.core.lambda.model.processor.QueryClinicalTrialsProcessorRequest;
import com.alpha.health.dp.core.lambda.model.processor.QueryUserProfilesProcessorRequest;
import com.alpha.health.dp.core.lambda.query.processors.QueryClinicalTrialsProcessorImpls;
import com.alpha.health.dp.core.lambda.query.processors.QueryUserProfilesProcessorImpl;
import com.alpha.health.dp.core.lambda.query.processors.ServiceRequestProcessor;
import com.alpha.health.dp.core.lambda.util.SingletonInstanceFactory;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.fasterxml.jackson.core.JsonProcessingException;
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
    private static final String DEFAULT_EMPTY = "";
    private static final String QUERY_CLINICAL_TRIALS_PATH = "/query/clinicalTrials";
    private static final String QUERY_USER_PROFILE_PATH =  "/query/userProfiles";
    private static final Logger LOGGER = LogManager.getLogger(AlphaHealthDPServiceLambda.class);
    // Use this map instead of Guice dependency injection to save lambda init time.
    private final Map<String, ServiceRequestProcessor> processorMap = new HashMap<String, ServiceRequestProcessor>(){{
        put(QUERY_CLINICAL_TRIALS_PATH, new QueryClinicalTrialsProcessorImpls());
        put(QUERY_USER_PROFILE_PATH, new QueryUserProfilesProcessorImpl());
    }};
    private final ObjectMapper objectMapper = SingletonInstanceFactory.getObjectMapperInstance();

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
            return buildErrorResponse(500, "InternalServerError " + e.getMessage());
        } catch (final IllegalArgumentException e) {
            LOGGER.error("Failed to respond due to client error", e);
            return buildErrorResponse(401, "InvalidParameterException " + e.getMessage());
        } catch (final RuntimeException e) {
            LOGGER.error("Failed to respond", e);
            return buildErrorResponse(500, "InternalServerError " + e.getMessage());
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

        if (queryParams == null) {
            throw new IllegalArgumentException(pathName + " shall have userProfileId populated in parameter");
        }

        LOGGER.info("parsing from query params: " + queryParams);

        final String userProfileId = queryParams.get("userProfileId").strip();

        if (userProfileId == null) {
            throw new IllegalArgumentException("userProfileId is a required field");
        }

        if (QUERY_CLINICAL_TRIALS_PATH.equals(pathName)) {

            return QueryClinicalTrialsProcessorRequest.builder()
                .userProfileId(userProfileId)
                .condition(
                    queryParams.getOrDefault("condition", DEFAULT_EMPTY))
                .location(
                    queryParams.getOrDefault("location", DEFAULT_EMPTY))
                .build();
        } if (QUERY_USER_PROFILE_PATH.equals(pathName)) {

            return QueryUserProfilesProcessorRequest.builder()
                .userProfileId(userProfileId)
                .build();
        } else {
            throw new IllegalArgumentException("unsupported path: " + pathName);
        }
    }
}
