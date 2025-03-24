package com.myorg.lambda.client;

import com.myorg.db.DynamoDBStack;
import com.myorg.lambda.factory.LambdaFactory;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.services.dynamodb.Table;
import software.amazon.awscdk.services.lambda.Function;
import software.constructs.Construct;

public class ClientLambdaStack extends Stack {
    private final Function createClientLambda;
    private final Function deleteClientLambda;

    public ClientLambdaStack(final Construct scope, final String id, DynamoDBStack dynamoDBStack) {
        super(scope, id);

        // Retrieve the Clients table from DynamoDBStack
        Table clientsTable = dynamoDBStack.getClientsTable();

        // Create the CreateClient Lambda using factory
        createClientLambda = LambdaFactory.createLambda(this, "CreateClientLambda", "com.app.handler.clients.CreateClientHandler::handleRequest");

        // Create the DeleteClient Lambda using factory
        deleteClientLambda = LambdaFactory.createLambda(this, "DeleteClientLambda", "com.app.handler.clients.DeleteClientHandler::handleRequest");

        // Grant Lambda permissions to write to the Clients table
        clientsTable.grantWriteData(createClientLambda);
        clientsTable.grantReadWriteData(deleteClientLambda);
    }

    public Function getCreateClientLambda() {
        return createClientLambda;
    }

    public Function getDeleteClientLambda() {
        return deleteClientLambda;
    }
}
