package com.myorg.lambda.factory;

import software.amazon.awscdk.Duration;
import software.amazon.awscdk.services.lambda.*;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.services.lambda.Runtime;

public class LambdaFactory {

    public static Function createLambda(Stack stack, String functionName, String handler) {
        return Function.Builder.create(stack, functionName)
                .functionName(functionName)
                .runtime(Runtime.JAVA_21)
                .handler(handler)
                .code(Code.fromAsset("../api-handlers/target/api-handlers.jar")) // Ensure Lambda JAR is built
                .memorySize(512)
                .timeout(Duration.seconds(10))
                .build();
    }
}
