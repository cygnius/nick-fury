package com.myorg.apigw;

import com.myorg.lambda.LambdaStack;
import software.amazon.awscdk.services.apigateway.RestApi;
import software.amazon.awscdk.services.apigateway.Resource;
import software.amazon.awscdk.services.apigateway.LambdaIntegration;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
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
        Resource clientIdResource = clientsResource.addResource("{clientId}");

        // Create /therapists resource
        Resource therapistsResource = api.getRoot().addResource("therapists");

        // Create /therapists/{therapistId} resource
        Resource therapistIdResource = therapistsResource.addResource("{therapistId}");

        // Create /mapping/client/{clientId}/therapist/{therapistId} resource
        Resource mappingResource = api.getRoot().addResource("mapping")
                .addResource("client")
                .addResource("{clientId}")
                .addResource("therapist")
                .addResource("{therapistId}");




        // Add POST method to invoke CreateClientLambda
        clientsResource.addMethod("POST", new LambdaIntegration(lambdaStack.getClientLambdaStack().getCreateClientLambda()));

        // Add DELETE method to invoke DeleteClientLambda
        clientIdResource.addMethod("DELETE", new LambdaIntegration(lambdaStack.getClientLambdaStack().getDeleteClientLambda()));

        // Add POST method to invoke CreateTherapistLambda
        therapistsResource.addMethod("POST", new LambdaIntegration(lambdaStack.getTherapistLambdaStack().getCreateTherapistLambda()));

        // Add DELETE method to invoke DeleteTherapistLambda
        therapistIdResource.addMethod("DELETE", new LambdaIntegration(lambdaStack.getTherapistLambdaStack().getDeleteTherapistLambda()));

        // Add POST method to invoke CreateMappingLambda
        mappingResource.addMethod("POST", new LambdaIntegration(lambdaStack.getMappingLambdaStack().getCreateMappingLambda()));

        // Add DELETE method to invoke DeleteMappingLambda
        mappingResource.addMethod("DELETE", new LambdaIntegration(lambdaStack.getMappingLambdaStack().getDeleteMappingLambda()));
    }
}
