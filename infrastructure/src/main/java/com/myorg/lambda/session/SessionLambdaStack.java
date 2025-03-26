package com.myorg.lambda.session;

import com.myorg.db.DynamoDBStack;
import com.myorg.lambda.factory.LambdaFactory;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.services.dynamodb.Table;
import software.amazon.awscdk.services.lambda.Function;
import software.constructs.Construct;

public class SessionLambdaStack extends Stack {
    private final Function createSessionLambda;
    private final Function deleteSessionLambda;
    private final Function getAllSessionsLambda;
    private final Function getSessionsByTherapistLambda;
    private final Function updateSessionLambda;

    public SessionLambdaStack(final Construct scope, final String id, DynamoDBStack dynamoDBStack) {
        super(scope, id);

        // Retrieve the Sessions table from DynamoDBStack
        Table sessionsTable = dynamoDBStack.getSessionsTable();

        // Create the CreateSession Lambda using factory
        createSessionLambda = LambdaFactory.createLambda(this, "CreateSessionLambda", "com.app.handler.sessions.CreateSessionHandler::handleRequest");

        // Create the DeleteSession Lambda using factory
        deleteSessionLambda = LambdaFactory.createLambda(this, "DeleteSessionLambda", "com.app.handler.sessions.DeleteSessionHandler::handleRequest");

        // Create the GetAllSessions Lambda using factory
        getAllSessionsLambda = LambdaFactory.createLambda(this, "GetAllSessionsLambda", "com.app.handler.sessions.GetAllSessionsHandler::handleRequest");

        // Create the GetSessionsByTherapist Lambda using factory
        getSessionsByTherapistLambda = LambdaFactory.createLambda(this, "GetSessionsByTherapistLambda", "com.app.handler.sessions.GetSessionsByTherapistHandler::handleRequest");

        // Create the UpdateSession Lambda using factory
        updateSessionLambda = LambdaFactory.createLambda(this, "UpdateSessionLambda", "com.app.handler.sessions.UpdateSessionHandler::handleRequest");

        // Grant the necessary permissions to the Lambdas
        sessionsTable.grantReadWriteData(createSessionLambda);
        sessionsTable.grantReadWriteData(deleteSessionLambda);
        sessionsTable.grantReadWriteData(updateSessionLambda);
        sessionsTable.grantReadData(getAllSessionsLambda);
        sessionsTable.grantReadData(getSessionsByTherapistLambda);
    }

    public Function getCreateSessionLambda() {
        return createSessionLambda;
    }

    public Function getDeleteSessionLambda() {
        return deleteSessionLambda;
    }

    public Function getSessionsByTherapistLambda() {
        return getSessionsByTherapistLambda;
    }

    public Function getUpdateSessionLambda() {
        return updateSessionLambda;
    }

    public Function getAllSessionsLambda() {
        return getAllSessionsLambda;
    }
}
