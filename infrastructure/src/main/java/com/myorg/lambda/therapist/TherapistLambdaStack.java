package com.myorg.lambda.therapist;

import com.myorg.db.DynamoDBStack;
import com.myorg.lambda.factory.LambdaFactory;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.services.dynamodb.Table;
import software.amazon.awscdk.services.lambda.Function;
import software.constructs.Construct;

public class TherapistLambdaStack extends Stack {
    private final Function createTherapistLambda;
    private final Function deleteTherapistLambda;

    public TherapistLambdaStack(final Construct scope, final String id, DynamoDBStack dynamoDBStack) {
        super(scope, id);

        // Retrieve the Therapists table from DynamoDBStack
        Table therapistsTable = dynamoDBStack.getTherapistsTable();

        // Create the CreateTherapist Lambda using factory
        createTherapistLambda = LambdaFactory.createLambda(this, "CreateTherapistLambda", "com.app.handler.therapists.CreateTherapistHandler::handleRequest");

        // Create the DeleteTherapist Lambda using factory
        deleteTherapistLambda = LambdaFactory.createLambda(this, "DeleteTherapistLambda", "com.app.handler.therapists.DeleteTherapistHandler::handleRequest");

        // Grant Lambda permissions to write to the Therapists table
        therapistsTable.grantWriteData(createTherapistLambda);
        therapistsTable.grantReadWriteData(deleteTherapistLambda);
    }

    public Function getCreateTherapistLambda() {
        return createTherapistLambda;
    }

    public Function getDeleteTherapistLambda() {
        return deleteTherapistLambda;
    }
}
