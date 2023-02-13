package com.alpha.health.dp.core.lambda;


import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.LambdaLogger;
import com.amazonaws.services.lambda.runtime.RequestHandler;

import java.util.Map;

/**
 * Lambda handler of AlphaHealth DP Service.
 */
public class AlphaHealthDPServiceLambda implements RequestHandler<Map<String, String>, String> {
    @Override
    public String handleRequest(Map<String, String> input, Context context) {
        LambdaLogger logger = context.getLogger();

        input.forEach((k, v) -> logger.log(k + ":" + v));

        return new String("hello world! 200");
    }
}
