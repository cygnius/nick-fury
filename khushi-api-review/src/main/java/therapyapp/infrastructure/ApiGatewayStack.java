package therapyapp.infrastructure;

import software.amazon.awscdk.services.apigateway.LambdaIntegration;
import software.amazon.awscdk.services.apigateway.Resource;
import software.amazon.awscdk.services.apigateway.RestApi;
import software.amazon.awscdk.services.lambda.IFunction;
import software.constructs.Construct;

public class ApiGatewayStack extends Construct {

    public ApiGatewayStack(final Construct scope, final String id,
                           IFunction authLambda,
                           IFunction clientLambda,
                           IFunction therapistLambda,
                           IFunction sessionLambda,
                           IFunction messageLambda,
                           IFunction appointmentLambda) {
        super(scope, id);

        RestApi api = RestApi.Builder.create(this, "TherapyJournalingApi")
                .restApiName("Therapy Journaling API")
                .description("API for managing therapy sessions, journaling, and communication.")
                .build();

        createLambdaIntegration(api, "auth", authLambda);
        createLambdaIntegration(api, "clients", clientLambda);
        createLambdaIntegration(api, "therapists", therapistLambda);
        createLambdaIntegration(api, "sessions", sessionLambda);
        createLambdaIntegration(api, "messages", messageLambda);
        createLambdaIntegration(api, "appointments", appointmentLambda);
    }

    private void createLambdaIntegration(RestApi api, String path, IFunction lambdaFunction) {
        Resource resource = api.getRoot().addResource(path);
        resource.addMethod("POST", new LambdaIntegration(lambdaFunction));
        resource.addMethod("GET", new LambdaIntegration(lambdaFunction));
        resource.addMethod("PUT", new LambdaIntegration(lambdaFunction));
        resource.addMethod("DELETE", new LambdaIntegration(lambdaFunction));
    }
}
