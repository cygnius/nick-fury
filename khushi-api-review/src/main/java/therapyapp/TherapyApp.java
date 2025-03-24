package therapyapp;


import software.amazon.awscdk.App;
import therapyapp.infrastructure.ApiGatewayStack;
import therapyapp.infrastructure.DynamoDbStack;
import therapyapp.infrastructure.LambdaStack;

public class TherapyApp {
    public static void main(final String[] args) {
        App app = new App();

    
        DynamoDbStack dynamoDbStack = new DynamoDbStack(app, "DynamoDbStack", null);

        LambdaStack lambdaStack = new LambdaStack(app, "LambdaStack");

        ApiGatewayStack apiGatewayStack = new ApiGatewayStack(app, "ApiGatewayStack",
                lambdaStack.getAuthLambda(), lambdaStack.getClientLambda(), lambdaStack.getTherapistLambda(),
                lambdaStack.getSessionLambda(), lambdaStack.getMessageLambda(), lambdaStack.getAppointmentLambda());

        // Synthesize the app
        app.synth();
    }
}
