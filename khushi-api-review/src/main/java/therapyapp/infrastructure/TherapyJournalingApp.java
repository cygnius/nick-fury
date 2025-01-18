package therapyapp.infrastructure;

import software.amazon.awscdk.App;


public class TherapyJournalingApp {
    public static void main(final String[] args) {
        App app = new App();

        LambdaStack lambdaStack = new LambdaStack(app, "LambdaStack");
        DynamoDbStack dynamoDbStack = new DynamoDbStack(app, "DynamoDbStack");
        ApiGatewayStack apiGatewayStack = new ApiGatewayStack(app, "ApiGatewayStack",
                lambdaStack.getAuthLambda(), lambdaStack.getClientLambda(), lambdaStack.getTherapistLambda(),
                lambdaStack.getSessionLambda(), lambdaStack.getMessageLambda(), lambdaStack.getAppointmentLambda());

        app.synth();
    }
}
