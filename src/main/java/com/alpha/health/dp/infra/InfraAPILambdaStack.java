package com.alpha.health.dp.infra;

import software.amazon.awscdk.Duration;
import software.amazon.awscdk.services.apigateway.LambdaIntegration;
import software.amazon.awscdk.services.apigateway.ProxyResourceOptions;
import software.amazon.awscdk.services.apigateway.RestApi;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.logs.RetentionDays;
import software.amazon.awscdk.services.s3.Bucket;
import software.constructs.Construct;


/**
 * The stack that defines api gateway and lambda.
 */
public class InfraAPILambdaStack extends Construct {
    public static final String SERVICE_NAME = "AlphaHealthDpService";
    @SuppressWarnings("serial")
    public InfraAPILambdaStack(Construct scope, String id) {
        super(scope, id);

        Bucket bucket = new Bucket(this, SERVICE_NAME + "-Stack");

        Function handler = Function.Builder.create(this, SERVICE_NAME + "Handler")
            .runtime(Runtime.JAVA_11)
            .code(Code.fromAsset("target/alpha_health_dp_service102-aws-0.1.jar"))
            // TODO
            .handler("com.alpha.health.dp.core.lambda.AlphaHealthDPServiceLambda")
            // Note that Map.of is only Java 9 or later
            .environment(java.util.Map.of(
                "BUCKET", bucket.getBucketName()))
            .logRetention(RetentionDays.ONE_WEEK)
            .timeout(Duration.minutes(1))
            .build();

        bucket.grantReadWrite(handler);

        RestApi api = RestApi.Builder.create(this, "alpha-health-dp-service-beta-pdx")
            .restApiName(SERVICE_NAME).description("This service services as data providers for web app.")
            .build();

        LambdaIntegration lambdaIntegration = LambdaIntegration.Builder.create(handler)
            .requestTemplates(java.util.Map.of(
                "application/json", "{ \"statusCode\": \"200\" }"))
            .build();

        /**
         * add lambda as proxy such that requests into Api-g will forward to lambda.
         */
        api.getRoot().addProxy(
            ProxyResourceOptions
                .builder()
                .defaultIntegration(lambdaIntegration)
                .anyMethod(true)
                .build());
    }
}
