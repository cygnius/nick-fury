package com.myorg.apigw;

import com.myorg.lambda.LambdaStack;
import software.amazon.awscdk.services.apigateway.RestApi;
import software.amazon.awscdk.services.apigateway.Resource;
import software.amazon.awscdk.services.apigateway.LambdaIntegration;
import software.amazon.awscdk.Stack;
import software.constructs.Construct;

public class ApiStack extends Stack {
    public ApiStack(final Construct scope, final String id, LambdaStack lambdaStack) {
        super(scope, id);

        // Create REST API Gateway
        RestApi api = RestApi.Builder.create(this, "ClientTherapistAPI")
                .restApiName("Client-Therapist API")
                .description("API for client-therapist interactions")
                .build();

        // Create /clients resource
        Resource clientsResource = api.getRoot().addResource("clients");

        // Create /clients/{clientId} resource
        Resource clientByIdResource = clientsResource.addResource("{clientId}");

        // Create /therapists resource
        Resource therapistsResource = api.getRoot().addResource("therapists");

        // Create /therapists/{therapistId} resource
        Resource therapistByIdResource = therapistsResource.addResource("{therapistId}");

        // Create /mappings/client/{clientId}/therapist/{therapistId} resource
        Resource mappingsResource = api.getRoot().addResource("mappings")
                .addResource("client")
                .addResource("{clientId}")
                .addResource("therapist")
                .addResource("{therapistId}");

        // Create /sessions resource
        Resource sessionsResource = api.getRoot().addResource("sessions");

        // Create /sessions/by-id/{sessionId}/{sessionDate} resource
        Resource sessionsByIdResource = sessionsResource.addResource("by-id")
                .addResource("{sessionId}")
                .addResource("{sessionDate}");

        // Create /sessions/by-therapist/{therapistId}/{sessionDate} resource
        Resource sessionsByTherapistResource = sessionsResource.addResource("by-therapist")
                .addResource("{therapistId}")
                .addResource("{sessionDate}");

        // Create /messages resource
        Resource messagesResource = api.getRoot().addResource("messages");

        // Create /conversations/{senderId}/{receiverId} resource
        Resource conversationsResource = api.getRoot().addResource("conversations")
                .addResource("{senderId}")
                        .addResource("{receiverId}");

        // Create /messages/{senderId}/{timestamp} resource
        Resource messagesAtTimestampResource = messagesResource.addResource("{senderId}")
                .addResource("{timestamp}");





        // Add POST method to invoke CreateClientLambda
        clientsResource.addMethod("POST", new LambdaIntegration(lambdaStack.getClientLambdaStack().getCreateClientLambda()));

        // Add DELETE method to invoke DeleteClientLambda
        clientByIdResource.addMethod("DELETE", new LambdaIntegration(lambdaStack.getClientLambdaStack().getDeleteClientLambda()));

        // Add POST method to invoke CreateTherapistLambda
        therapistsResource.addMethod("POST", new LambdaIntegration(lambdaStack.getTherapistLambdaStack().getCreateTherapistLambda()));

        // Add DELETE method to invoke DeleteTherapistLambda
        therapistByIdResource.addMethod("DELETE", new LambdaIntegration(lambdaStack.getTherapistLambdaStack().getDeleteTherapistLambda()));

        // Add POST method to invoke CreateMappingLambda
        mappingsResource.addMethod("POST", new LambdaIntegration(lambdaStack.getMappingLambdaStack().getCreateMappingLambda()));

        // Add DELETE method to invoke DeleteMappingLambda
        mappingsResource.addMethod("DELETE", new LambdaIntegration(lambdaStack.getMappingLambdaStack().getDeleteMappingLambda()));

        // Add POST method to invoke CreateSessionLambda
        sessionsResource.addMethod("POST", new LambdaIntegration(lambdaStack.getSessionLambdaStack().getCreateSessionLambda()));

        // Add DELETE method to invoke DeleteSessionLambda
        sessionsByIdResource.addMethod("DELETE", new LambdaIntegration(lambdaStack.getSessionLambdaStack().getDeleteSessionLambda()));

        // Add PUT method to invoke UpdateSessionLambda
        sessionsResource.addMethod("PUT", new LambdaIntegration(lambdaStack.getSessionLambdaStack().getUpdateSessionLambda()));

        // Add GET method to invoke GetAllSessionsLambda
        sessionsResource.addMethod("GET", new LambdaIntegration(lambdaStack.getSessionLambdaStack().getAllSessionsLambda()));

        // Add GET method to invoke GetSessionsByTherapistLambda
        sessionsByTherapistResource.addMethod("GET", new LambdaIntegration(lambdaStack.getSessionLambdaStack().getSessionsByTherapistLambda()));

        // Add POST method to invoke SendMessageLambda
        messagesResource.addMethod("POST", new LambdaIntegration(lambdaStack.getMessageLambdaStack().getSendMessageLambda()));

        // Add GET method to invoke GetMessagesBetweenUsersLambda
        conversationsResource.addMethod("GET", new LambdaIntegration(lambdaStack.getMessageLambdaStack().getMessagesBetweenUsersLambda()));

        // Add GET method to invoke GetMessageBySenderAtTimeLambda
        messagesAtTimestampResource.addMethod("GET", new LambdaIntegration(lambdaStack.getMessageLambdaStack().getMessageBySenderAtTimeLambda()));
    }
}
