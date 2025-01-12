package therapyapp.infrastructure;

import java.util.Collections;

import software.amazon.awscdk.BundlingOptions;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.services.lambda.Code;
import software.amazon.awscdk.services.lambda.Function;
import software.amazon.awscdk.services.s3.assets.AssetOptions;
import software.constructs.Construct;

public class LambdaStack extends Stack {

    private final Function authLambda;
    private final Function clientLambda;
    private final Function therapistLambda;
    private final Function sessionLambda;
    private final Function messageLambda;
    private final Function appointmentLambda;

    public LambdaStack(final Construct scope, final String id) {
        super(scope, id);

        authLambda = createLambdaFunction("AuthHandler", "auth.AuthHandler::handleRequest");
        clientLambda = createLambdaFunction("ClientHandler", "clients.ClientHandler::handleRequest");
        therapistLambda = createLambdaFunction("TherapistHandler", "therapists.TherapistHandler::handleRequest");
        sessionLambda = createLambdaFunction("SessionHandler", "sessions.SessionHandler::handleRequest");
        messageLambda = createLambdaFunction("MessageHandler", "messaging.MessageHandler::handleRequest");
        appointmentLambda = createLambdaFunction("AppointmentHandler", "appointments.AppointmentHandler::handleRequest");
    }

    private Function createLambdaFunction(String functionName, String handler) {
        return Function.Builder.create(this, functionName)
                .runtime(software.amazon.awscdk.services.lambda.Runtime.JAVA_11)
                .handler(handler)
                .code(Code.fromAsset("../target/therapy-app.jar", AssetOptions.builder().bundling(BundlingOptions.builder()
                                        .command(Collections.singletonList("mvn package -DskipTests"))
                                        .image(software.amazon.awscdk.services.lambda.Runtime.JAVA_11.getBundlingImage())
                                        .build()).build()))
                .build();
    }

    public Function getAuthLambda() {
        return authLambda;
    }

    public Function getClientLambda() {
        return clientLambda;
    }

    public Function getTherapistLambda() {
        return therapistLambda;
    }

    public Function getSessionLambda() {
        return sessionLambda;
    }

    public Function getMessageLambda() {
        return messageLambda;
    }

    public Function getAppointmentLambda() {
        return appointmentLambda;
    }
}
