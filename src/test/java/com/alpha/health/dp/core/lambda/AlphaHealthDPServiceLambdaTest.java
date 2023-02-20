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
            put("queryStringParameters", Map.of("location", "Seattle", "condition", "anything"));
        }};

        // Act
        DPServiceResponse response = target.handleRequest(request, null);

        // Assert
        Assertions.assertEquals(200, response.getStatusCode());
    }
}
