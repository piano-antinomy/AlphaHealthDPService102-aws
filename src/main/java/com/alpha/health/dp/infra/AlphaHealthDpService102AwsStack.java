package com.alpha.health.dp.infra;

import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.constructs.Construct;

/**
 * The cdk Stack for AlphaHealthDP Service. NOT the lambda logic.
 */
public class AlphaHealthDpService102AwsStack extends Stack {
    public AlphaHealthDpService102AwsStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public AlphaHealthDpService102AwsStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        // The code that defines the stack
        new InfraAPILambdaStack(this, InfraAPILambdaStack.SERVICE_NAME);
    }
}
