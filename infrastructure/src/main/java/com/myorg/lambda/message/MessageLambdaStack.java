package com.myorg.lambda.message;

import com.myorg.db.DynamoDBStack;
import com.myorg.lambda.factory.LambdaFactory;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.services.dynamodb.Table;
import software.amazon.awscdk.services.lambda.Function;
import software.constructs.Construct;

public class MessageLambdaStack extends Stack {
    private final Function getMessageBySenderAtTimeLambda;
    private final Function getMessagesBetweenUsersLambda;
    private final Function sendMessageLambda;

    public MessageLambdaStack(final Construct scope, final String id, DynamoDBStack dynamoDBStack) {
        super(scope, id);

        Table messagesTable = dynamoDBStack.getMessagesTable();

        sendMessageLambda = LambdaFactory.createLambda(this, "SendMessageLambda", "com.app.handler.messages.SendMessageHandler::handleRequest");

        getMessageBySenderAtTimeLambda = LambdaFactory.createLambda(this, "GetMessageBySenderAtTimeLambda", "com.app.handler.messages.GetMessageBySenderAtTimeHandler::handleRequest");

        getMessagesBetweenUsersLambda = LambdaFactory.createLambda(this, "GetMessagesBetweenUsersLambda", "com.app.handler.messages.GetMessagesBetweenUsersHandler::handleRequest");

        messagesTable.grantReadWriteData(sendMessageLambda);
        messagesTable.grantReadData(getMessagesBetweenUsersLambda);
        messagesTable.grantReadData(getMessageBySenderAtTimeLambda);
    }

    public Function getSendMessageLambda() { return sendMessageLambda; }

    public Function getMessagesBetweenUsersLambda() { return getMessagesBetweenUsersLambda; }

    public Function getMessageBySenderAtTimeLambda() { return getMessageBySenderAtTimeLambda; }
}
