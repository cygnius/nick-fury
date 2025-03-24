package com.myorg.lambda.mapping;

import com.myorg.db.DynamoDBStack;
import com.myorg.lambda.factory.LambdaFactory;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.services.dynamodb.Table;
import software.amazon.awscdk.services.lambda.Function;
import software.constructs.Construct;

public class MappingLambdaStack extends Stack {
    private final Function createMappingLambda;
    private final Function deleteMappingLambda;

    public MappingLambdaStack(final Construct scope, final String id, DynamoDBStack dynamoDBStack) {
        super(scope, id);

        // Retrieve table from DynamoDBStack
        Table mappingsTable = dynamoDBStack.getMappingsTable();
        Table clientsTable = dynamoDBStack.getClientsTable();
        Table therapistsTable = dynamoDBStack.getTherapistsTable();

        // CreateMapping Lambda function
        createMappingLambda = LambdaFactory.createLambda(this, "CreateMappingLambda", "com.app.handler.mappings.CreateMappingHandler::handleRequest");

        // DeleteMapping Lambda function
        deleteMappingLambda = LambdaFactory.createLambda(this, "DeleteMappingLambda","com.app.handler.mappings.DeleteMappingHandler::handleRequest");

        // Grant permission
        clientsTable.grantReadData(createMappingLambda);
        therapistsTable.grantReadData(createMappingLambda);
        mappingsTable.grantWriteData(createMappingLambda);
        mappingsTable.grantReadWriteData(deleteMappingLambda);
    }

    public Function getCreateMappingLambda() {
        return createMappingLambda;
    }

    public Function getDeleteMappingLambda() {
        return deleteMappingLambda;
    }
}
