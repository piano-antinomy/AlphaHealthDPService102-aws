package com.alpha.health.dp.core.lambda;


import com.alpha.health.dp.core.lambda.model.QueryClinicalTrialsResponse;
import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.util.Map;

/**
 * Lambda handler of AlphaHealth DP Service.
 */
public class AlphaHealthDPServiceLambda implements RequestHandler<Map<String, Object>, QueryClinicalTrialsResponse> {

    private static final Logger LOGGER = LogManager.getLogger(AlphaHealthDPServiceLambda.class);

    @Override
    public QueryClinicalTrialsResponse handleRequest(Map<String, Object> input, Context context) {

        LOGGER.info(input);

        return QueryClinicalTrialsResponse
            .builder()
            .body("Successful")
            .statusCode(200).build();
    }
}
