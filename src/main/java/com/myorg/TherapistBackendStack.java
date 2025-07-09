package com.myorg;

import software.constructs.Construct;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
// import software.amazon.awscdk.Duration;
// import software.amazon.awscdk.services.sqs.Queue;

import software.amazon.awscdk.*;
import software.amazon.awscdk.services.apigateway.*;
import software.amazon.awscdk.services.dynamodb.*;
import software.amazon.awscdk.services.lambda.*;
// import software.amazon.awscdk.services.lambda.integrations.LambdaIntegration;
import software.amazon.awscdk.services.apigateway.LambdaIntegration;
import java.util.Map;
import software.amazon.awscdk.services.apigateway.Resource;


public class TherapistBackendStack extends Stack {
    public TherapistBackendStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public TherapistBackendStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);


        Table messagesTable = Table.Builder.create(this, "MessagesTable")
            .tableName("messages")
            .partitionKey(Attribute.builder().name("id").type(AttributeType.STRING).build())
            .billingMode(BillingMode.PAY_PER_REQUEST)
            .removalPolicy(RemovalPolicy.DESTROY)
            .build();

        // 2️. Add GSIs
        messagesTable.addGlobalSecondaryIndex(GlobalSecondaryIndexProps.builder()
            .indexName("sender_receiver_index")
            .partitionKey(Attribute.builder().name("sender_id").type(AttributeType.STRING).build())
            .sortKey(Attribute.builder().name("sender_receiver_sent_at").type(AttributeType.STRING).build())
            .build());

        messagesTable.addGlobalSecondaryIndex(GlobalSecondaryIndexProps.builder()
            .indexName("receiver_index")
            .partitionKey(Attribute.builder().name("receiver_id").type(AttributeType.STRING).build())
            .sortKey(Attribute.builder().name("receiver_sender_sent_at").type(AttributeType.STRING).build())
            .build());

        // 3️. Lambdas for message APIs
        Function sendMessageLambda = Function.Builder.create(this, "SendMessageHandler")
            .runtime(software.amazon.awscdk.services.lambda.Runtime.JAVA_17)
            .handler("com.myorg.handlers.SendMessageHandler::handleRequest")
            .code(Code.fromAsset("target/therapist-backend-0.1.jar"))
            .environment(Map.of("MESSAGE_TABLE", messagesTable.getTableName()))
                .timeout(Duration.seconds(30)) // Increased timeout
            .build();

        Function getMessagesLambda = Function.Builder.create(this, "GetMessagesHandler")
            .runtime(software.amazon.awscdk.services.lambda.Runtime.JAVA_17)
            .handler("com.myorg.handlers.GetMessagesHandler::handleRequest")
            .code(Code.fromAsset("target/therapist-backend-0.1.jar"))
            .environment(Map.of("MESSAGE_TABLE", messagesTable.getTableName()))
                .timeout(Duration.seconds(30)) // Increased timeout
            .build();


        messagesTable.grantReadWriteData(sendMessageLambda);
        messagesTable.grantReadWriteData(getMessagesLambda);

        // 4️. Message Routes in API Gateway
        RestApi api = RestApi.Builder.create(this, "TherapistAPI")
            .restApiName("Therapist Service")
            .build();

        Resource messageResource = api.getRoot().addResource("message");
        Resource chatRoute = messageResource
            .addResource("{senderId}")
            .addResource("{recipientId}");

        chatRoute.addMethod("GET", new LambdaIntegration(getMessagesLambda));
        chatRoute.addMethod("POST", new LambdaIntegration(sendMessageLambda));


        /* ######################  Mapping Logic #########################################*/

            Table clientTherapistMappingsTable = Table.Builder.create(this, "ClientTherapistMappingsTable")
                .tableName("client_therapist_mappings")
                .partitionKey(Attribute.builder().name("id").type(AttributeType.STRING).build())
                .billingMode(BillingMode.PAY_PER_REQUEST)
                .removalPolicy(RemovalPolicy.DESTROY)
                .build();

        clientTherapistMappingsTable.addGlobalSecondaryIndex(GlobalSecondaryIndexProps.builder()
                .indexName("client-therapist-index")
                .partitionKey(Attribute.builder().name("client_id").type(AttributeType.STRING).build())
                .sortKey(Attribute.builder().name("therapist_id").type(AttributeType.STRING).build())
                .build());

        //  Create Lambda functions
        Function createMappingLambda = Function.Builder.create(this, "CreateMappingHandler")
                .runtime(software.amazon.awscdk.services.lambda.Runtime.JAVA_17)
                .handler("com.myorg.handlers.CreateMappingHandler::handleRequest")
                .code(Code.fromAsset("target/therapist-backend-0.1.jar"))
                .environment(Map.of("MAPPING_TABLE", clientTherapistMappingsTable.getTableName()))
                .timeout(Duration.seconds(30))
                .build();

        Function getMappingLambda = Function.Builder.create(this, "GetMappingHandler")
                .runtime(software.amazon.awscdk.services.lambda.Runtime.JAVA_17)
                .handler("com.myorg.handlers.GetMappingHandler::handleRequest")
                .code(Code.fromAsset("target/therapist-backend-0.1.jar"))
                .environment(Map.of("MAPPING_TABLE", clientTherapistMappingsTable.getTableName()))
                .timeout(Duration.seconds(30))
                .build();

        Function updateMappingLambda = Function.Builder.create(this, "UpdateMappingHandler")
                .runtime(software.amazon.awscdk.services.lambda.Runtime.JAVA_17)
                .handler("com.myorg.handlers.UpdateMappingHandler::handleRequest")
                .code(Code.fromAsset("target/therapist-backend-0.1.jar"))
                .environment(Map.of("MAPPING_TABLE", clientTherapistMappingsTable.getTableName()))
                .timeout(Duration.seconds(30))
                .build();

        Function deleteMappingLambda = Function.Builder.create(this, "DeleteMappingHandler")
                .runtime(software.amazon.awscdk.services.lambda.Runtime.JAVA_17)
                .handler("com.myorg.handlers.DeleteMappingHandler::handleRequest")
                .code(Code.fromAsset("target/therapist-backend-0.1.jar"))
                .environment(Map.of("MAPPING_TABLE", clientTherapistMappingsTable.getTableName()))
                .timeout(Duration.seconds(30))
                .build();

        //  Grant permissions
        clientTherapistMappingsTable.grantReadWriteData(createMappingLambda);
        clientTherapistMappingsTable.grantReadWriteData(getMappingLambda);
        clientTherapistMappingsTable.grantReadWriteData(updateMappingLambda);
        clientTherapistMappingsTable.grantReadWriteData(deleteMappingLambda);

        //  API Gateway Mapping Route
        Resource mappingsResource = api.getRoot().addResource("mappings");
        Resource mappingRoute = mappingsResource
                .addResource("{client_id}")
                .addResource("{therapist_id}");

        mappingRoute.addMethod("POST", new LambdaIntegration(createMappingLambda));
        mappingRoute.addMethod("GET", new LambdaIntegration(getMappingLambda));
        mappingRoute.addMethod("PUT", new LambdaIntegration(updateMappingLambda));
        mappingRoute.addMethod("DELETE", new LambdaIntegration(deleteMappingLambda));



        /* ######################  Session Logic  ######################################## */

        // -------------------- Sessions Table --------------------
        Table sessionsTable = Table.Builder.create(this, "SessionsTable")
                .tableName("sessions")
                .partitionKey(Attribute.builder().name("id").type(AttributeType.STRING).build())
                .billingMode(BillingMode.PAY_PER_REQUEST)
                .removalPolicy(RemovalPolicy.DESTROY)
                .build();

        sessionsTable.addGlobalSecondaryIndex(GlobalSecondaryIndexProps.builder()
                .indexName("therapist_schedule_index")
                .partitionKey(Attribute.builder().name("therapist_id").type(AttributeType.STRING).build())
                .sortKey(Attribute.builder().name("scheduled_at").type(AttributeType.STRING).build())
                .build());

        sessionsTable.addGlobalSecondaryIndex(GlobalSecondaryIndexProps.builder()
                .indexName("client_status_index")
                .partitionKey(Attribute.builder().name("client_id").type(AttributeType.STRING).build())
                .sortKey(Attribute.builder().name("status-scheduled_at").type(AttributeType.STRING).build())
                .build());

        // -------------------- Session Lambda Handlers --------------------
        Function createSessionLambda = Function.Builder.create(this, "CreateSessionHandler")
                .runtime(software.amazon.awscdk.services.lambda.Runtime.JAVA_17)
                .handler("com.myorg.handlers.CreateSessionHandler::handleRequest")
                .code(Code.fromAsset("target/therapist-backend-0.1.jar"))
                .environment(Map.of("SESSION_TABLE", sessionsTable.getTableName()))
                .timeout(Duration.seconds(30))
                .build();

        Function getSessionLambda = Function.Builder.create(this, "GetSessionHandler")
                .runtime(software.amazon.awscdk.services.lambda.Runtime.JAVA_17)
                .handler("com.myorg.handlers.GetSessionHandler::handleRequest")
                .code(Code.fromAsset("target/therapist-backend-0.1.jar"))
                .environment(Map.of("SESSION_TABLE", sessionsTable.getTableName()))
                .timeout(Duration.seconds(30))
                .build();

        Function listSessionsLambda = Function.Builder.create(this, "ListSessionHandler")
                .runtime(software.amazon.awscdk.services.lambda.Runtime.JAVA_17)
                .handler("com.myorg.handlers.ListSessionsHandler::handleRequest")
                .code(Code.fromAsset("target/therapist-backend-0.1.jar"))
                .environment(Map.of("SESSION_TABLE", sessionsTable.getTableName()))
                .timeout(Duration.seconds(30))
                .build();

        Function updateSessionLambda = Function.Builder.create(this, "UpdateSessionHandler")
                .runtime(software.amazon.awscdk.services.lambda.Runtime.JAVA_17)
                .handler("com.myorg.handlers.UpdateSessionHandler::handleRequest")
                .code(Code.fromAsset("target/therapist-backend-0.1.jar"))
                .environment(Map.of("SESSION_TABLE", sessionsTable.getTableName()))
                .timeout(Duration.seconds(30))
                .build();

        Function deleteSessionLambda = Function.Builder.create(this, "DeleteSessionHandler")
                .runtime(software.amazon.awscdk.services.lambda.Runtime.JAVA_17)
                .handler("com.myorg.handlers.DeleteSessionHandler::handleRequest")
                .code(Code.fromAsset("target/therapist-backend-0.1.jar"))
                .environment(Map.of("SESSION_TABLE", sessionsTable.getTableName()))
                .timeout(Duration.seconds(30))
                .build();

        // -------------------- Permissions --------------------
        sessionsTable.grantReadWriteData(createSessionLambda);
        sessionsTable.grantReadWriteData(getSessionLambda);
        sessionsTable.grantReadWriteData(listSessionsLambda);
        sessionsTable.grantReadWriteData(updateSessionLambda);
        sessionsTable.grantReadWriteData(deleteSessionLambda);

        // -------------------- API Gateway: /sessions --------------------
        Resource sessionsResource = api.getRoot().addResource("sessions");

        Resource therapistIdRoute = sessionsResource.addResource("by-therapist").addResource("{therapist_id}");
        Resource sessionIdRoute = sessionsResource.addResource("{session_id}");

        // POST /sessions/{therapist_id}/create
        therapistIdRoute.addResource("create")
                .addMethod("POST", new LambdaIntegration(createSessionLambda));

        // GET /sessions/{therapist_id}/view
        therapistIdRoute.addResource("view")
                .addMethod("GET", new LambdaIntegration(listSessionsLambda));

        // GET /sessions/{session_id}
        sessionIdRoute.addMethod("GET", new LambdaIntegration(getSessionLambda));

        // PUT /sessions/{session_id}/update
        sessionIdRoute.addResource("update")
                .addMethod("PUT", new LambdaIntegration(updateSessionLambda));

        // DELETE /sessions/{session_id}/delete
        sessionIdRoute.addResource("delete")
                .addMethod("DELETE", new LambdaIntegration(deleteSessionLambda));


    }
}
