package therapyapp;


import software.amazon.awscdk.App;
import therapyapp.infrastructure.ApiGatewayStack;
import therapyapp.infrastructure.LambdaStack;
import therapyapp.infrastructure.DynamoDbStack;

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
        new ApiGatewayStack(app, "ApiGatewayStack");

        // Synthesize the app
        app.synth();
    }
}
