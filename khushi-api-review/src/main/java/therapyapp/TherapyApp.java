package therapyapp;


import software.amazon.awscdk.App;
import therapyapp.infrastructure.ApiGatewayStack;
import therapyapp.infrastructure.DynamoDbStack;
import therapyapp.infrastructure.LambdaStack;

/**
 * Main application class for initializing AWS CDK stacks.
 */
public class TherapyApp {
    public static void main(final String[] args) {
        App app = new App();

        // Initialize the DynamoDB stack
        DynamoDbStack dynamoDbStack = new DynamoDbStack(app, "DynamoDbStack");

        // Initialize the Lambda stack with the relevant handler configurations
        LambdaStack lambdaStack = new LambdaStack(app, "LambdaStack");

        // Initialize the API Gateway stack connecting to the Lambda functions
        ApiGatewayStack apiGatewayStack = new ApiGatewayStack(app, "ApiGatewayStack",
                lambdaStack.getAuthLambda(), lambdaStack.getClientLambda(), lambdaStack.getTherapistLambda(),
                lambdaStack.getSessionLambda(), lambdaStack.getMessageLambda(), lambdaStack.getAppointmentLambda());

        // Synthesize the app
        app.synth();
    }
}
