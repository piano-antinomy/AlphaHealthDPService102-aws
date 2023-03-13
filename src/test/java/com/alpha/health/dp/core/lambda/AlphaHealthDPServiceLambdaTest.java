package com.alpha.health.dp.core.lambda;

import com.alpha.health.dp.core.lambda.model.DPServiceResponse;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;

public class AlphaHealthDPServiceLambdaTest {
    private final AlphaHealthDPServiceLambda target = new AlphaHealthDPServiceLambda();

    @Test
    protected void test_lambdaRouteRequestToTheRightProcessor() {
        // Arrange
        Map<String, Object> request = new HashMap<String, Object>() {{
            put("resource", "/query/clinicalTrials ");
            put("queryStringParameters", Map.of("location", "Seattle", "condition", "migraine","userProfileId",""));
        }};

        // Act
        DPServiceResponse response = target.handleRequest(request, null);

        // Assert
        Assertions.assertEquals(200, response.getStatusCode());
    }

    @Test
    protected void test_lambdaHandlesNullQueries() {
        // Arrange
        Map<String, Object> request = new HashMap<String, Object>() {{
            put("resource", "/query/clinicalTrials ");
        }};

        // Act
        DPServiceResponse response = target.handleRequest(request, null);

        // Assert
        Assertions.assertEquals(401, response.getStatusCode());
    }

    @Test
    protected void test_lambdaHandlesMultipleWordsInParams() {
        // Arrange
        final String trialId = "NCT04472338";
        Map<String, Object> request = new HashMap<String, Object>() {{
            put("resource", "/query/clinicalTrials ");
            put("queryStringParameters", Map.of("location", "United States", "condition", trialId, "userProfileId",""));
        }};

        // Act
        DPServiceResponse response = target.handleRequest(request, null);

        // Assert
        Assertions.assertEquals(200, response.getStatusCode());
        Assertions.assertTrue(response.getBody().contains(trialId));


    }
}
