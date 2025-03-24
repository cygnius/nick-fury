package com.myorg;

import com.myorg.apigw.ApiStack;
import com.myorg.db.DynamoDBStack;
import com.myorg.lambda.LambdaStack;
import software.constructs.Construct;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
// import software.amazon.awscdk.Duration;
// import software.amazon.awscdk.services.sqs.Queue;

public class InfrastructureStack extends Stack {
    public InfrastructureStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public InfrastructureStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        // The code that defines your stack goes here

        // example resource
        // final Queue queue = Queue.Builder.create(this, "InfrastructureQueue")
        //         .visibilityTimeout(Duration.seconds(300))
        //         .build();

        // Creating the DynamoDB stack inside InfrastructureStack
        DynamoDBStack dynamoDBStack = new DynamoDBStack(this, "DynamoDBStack");

        // Create the Lambda Stack and pass the DynamoDBStack
        LambdaStack lambdaStack = new LambdaStack(this, "LambdaStack", dynamoDBStack);

        // Create the API Gateway Stack and pass the LambdaStack (so it can integrate with Lambdas)
        new ApiStack(this, "ApiStack", lambdaStack);
    }
}
