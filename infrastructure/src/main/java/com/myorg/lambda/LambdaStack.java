package com.myorg.lambda;

import com.myorg.db.DynamoDBStack;
import com.myorg.lambda.client.ClientLambdaStack;
import com.myorg.lambda.mapping.MappingLambdaStack;
import com.myorg.lambda.therapist.TherapistLambdaStack;
import software.amazon.awscdk.Stack;
import software.constructs.Construct;

public class LambdaStack extends Stack {

    private final ClientLambdaStack clientLambdaStack;
    private final TherapistLambdaStack therapistLambdaStack;
    private final MappingLambdaStack mappingLambdaStack;

    public LambdaStack(final Construct scope, final String id, DynamoDBStack dynamoDBStack) {
        super(scope, id);

        // Initialize ClientLambdaStack
        clientLambdaStack = new ClientLambdaStack(this, "ClientLambdaStack", dynamoDBStack);

        // Initialize TherapistLambdaStack
        therapistLambdaStack = new TherapistLambdaStack(this, "TherapistLambdaStack", dynamoDBStack);

        // Initialize MappingLambdaStack
        mappingLambdaStack = new MappingLambdaStack(this, "MappingLambdaStack", dynamoDBStack);
    }

    public ClientLambdaStack getClientLambdaStack() {
        return clientLambdaStack;
    }

    public TherapistLambdaStack getTherapistLambdaStack() {
        return therapistLambdaStack;
    }

    public MappingLambdaStack getMappingLambdaStack() {
        return mappingLambdaStack;
    }
}
