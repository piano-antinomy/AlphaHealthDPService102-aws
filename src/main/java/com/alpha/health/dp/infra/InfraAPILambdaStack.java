package com.alpha.health.dp.infra;

import software.amazon.awscdk.Duration;
import software.amazon.awscdk.services.apigateway.Deployment;
import software.amazon.awscdk.services.apigateway.LambdaIntegration;
import software.amazon.awscdk.services.apigateway.MethodLoggingLevel;
import software.amazon.awscdk.services.apigateway.Resource;
import software.amazon.awscdk.services.apigateway.RestApi;
import software.amazon.awscdk.services.apigateway.Stage;
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

        LambdaIntegration lambdaIntegration = LambdaIntegration.Builder.create(handler)
            .requestTemplates(java.util.Map.of(
                "application/json", "{ \"statusCode\": \"200\" }"))
            .build();

        RestApi api = RestApi.Builder.create(this, SERVICE_NAME + "APIG")
            .restApiName(SERVICE_NAME)
            .description("This service services as data providers for web app.")
            .defaultIntegration(lambdaIntegration)
            .cloudWatchRole(true)
            .build();

        /**
         * configure only beta stage for now.
         */
        Stage betaStage = Stage.Builder.create(this, SERVICE_NAME + "beta-stage")
            .stageName("beta")
            .loggingLevel(MethodLoggingLevel.INFO)
            .deployment(Deployment.Builder.create(this, SERVICE_NAME + "beta-stage-deploy")
                .description("beta deployment configure")
                .retainDeployments(true)
                .api(api)
                .build())
            .build();

        Resource queryAPI = api.getRoot().addResource("query");
        Resource queryClinicalTrialAPI = queryAPI.addResource("clinicalTrials");
        queryClinicalTrialAPI.addMethod("GET", lambdaIntegration);

    }
}
