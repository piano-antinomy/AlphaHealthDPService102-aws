package com.alpha.health.dp.infra;

import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.constructs.Construct;
// import software.amazon.awscdk.Duration;
// import software.amazon.awscdk.services.sqs.Queue;

public class AlphaHealthDpService102AwsStack extends Stack {
    public AlphaHealthDpService102AwsStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public AlphaHealthDpService102AwsStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        // The code that defines your stack goes here

        // example resource
        // final Queue queue = Queue.Builder.create(this, "AlphaHealthDpService102AwsQueue")
        //         .visibilityTimeout(Duration.seconds(300))
        //         .build();
    }
}
