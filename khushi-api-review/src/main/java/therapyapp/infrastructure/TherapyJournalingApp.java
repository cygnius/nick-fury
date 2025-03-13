package therapyapp.infrastructure;

import software.amazon.awscdk.App;
import software.amazon.awscdk.StackProps;


public class TherapyJournalingApp {
    public static void main(final String[] args) {
        App app = new App();

        LambdaStack lambdaStack = new LambdaStack(app, "LambdaStack");
        StackProps stackProps = StackProps.builder().build();
        DynamoDbStack dynamoDbStack = new DynamoDbStack(app, "DynamoDbStack", stackProps);
        ApiGatewayStack apiGatewayStack = new ApiGatewayStack(app, "ApiGatewayStack",
                lambdaStack.getAuthLambda(), lambdaStack.getClientLambda(), lambdaStack.getTherapistLambda(),
                lambdaStack.getSessionLambda(), lambdaStack.getMessageLambda(), lambdaStack.getAppointmentLambda());

        app.synth();
    }
}
