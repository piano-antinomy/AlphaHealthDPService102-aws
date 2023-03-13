package com.alpha.health.dp.infra;

import com.fasterxml.jackson.databind.ObjectMapper;
import software.amazon.awscdk.Duration;
import software.amazon.awscdk.services.apigateway.AuthorizationType;
import software.amazon.awscdk.services.apigateway.Deployment;
import software.amazon.awscdk.services.apigateway.LambdaIntegration;
import software.amazon.awscdk.services.apigateway.MethodLoggingLevel;
import software.amazon.awscdk.services.apigateway.MethodOptions;
import software.amazon.awscdk.services.apigateway.Resource;
import software.amazon.awscdk.services.apigateway.RestApi;
import software.amazon.awscdk.services.apigateway.Stage;
import software.amazon.awscdk.services.iam.PolicyDocument;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.lambda.Runtime;
import software.amazon.awscdk.services.logs.RetentionDays;
import software.amazon.awscdk.services.s3.Bucket;
import software.constructs.Construct;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;


/**
 * The stack that defines api gateway and lambda.
 */
public class InfraAPILambdaStack extends Construct {
    public static final String SERVICE_NAME = "AlphaHealthDpService";
    private final ObjectMapper objectMapper = new ObjectMapper();

    @SuppressWarnings("serial")
    public InfraAPILambdaStack(Construct scope, String id) {
        super(scope, id);

        Bucket bucket = new Bucket(this, SERVICE_NAME + "-Stack");

        Function handler = Function.Builder.create(this, SERVICE_NAME + "Handler")
            .runtime(Runtime.JAVA_11)
            .code(Code.fromAsset("target/alpha_health_dp_service102-aws-0.1.jar"))
            .handler("com.alpha.health.dp.core.lambda.AlphaHealthDPServiceLambda")
            // Note that Map.of is only Java 9 or later
            .environment(java.util.Map.of(
                "BUCKET", bucket.getBucketName()))
            .logRetention(RetentionDays.ONE_WEEK)
            .timeout(Duration.minutes(1))
            .memorySize(512)
            .build();

        bucket.grantReadWrite(handler);

        LambdaIntegration lambdaIntegration = LambdaIntegration.Builder.create(handler)
            .requestTemplates(java.util.Map.of(
                "application/json", "{ \"statusCode\": \"200\" }"))
            .build();
        try {
            RestApi api= RestApi.Builder.create(this, SERVICE_NAME + "APIG")
                .restApiName(SERVICE_NAME)
                .description("This service services as data providers for web app.")
                .defaultIntegration(lambdaIntegration)
                .defaultMethodOptions(MethodOptions.builder().authorizationType(AuthorizationType.IAM).build())
                .policy(PolicyDocument.fromJson(
                    objectMapper.readValue(
                        new File("target/classes/com/alpha/health/dp/infra/apig_resource_policy.json"), HashMap.class)))
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
            Resource queryPersonalPredictionAPI = queryAPI.addResource("personalPrediction");
            Resource queryPersonalTreatmentAPI = queryAPI.addResource("personalTreatment");
            Resource queryUserProfiles = queryAPI.addResource("userProfiles");

            queryClinicalTrialAPI.addMethod("GET", lambdaIntegration);
            queryPersonalPredictionAPI.addMethod("GET", lambdaIntegration);
            queryPersonalTreatmentAPI.addMethod("GET", lambdaIntegration);
            queryUserProfiles.addMethod("GET", lambdaIntegration);

            api.setDeploymentStage(betaStage);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }


    }
}
