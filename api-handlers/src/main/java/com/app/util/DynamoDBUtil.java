package com.app.util;

import software.amazon.awssdk.enhanced.dynamodb.DynamoDbEnhancedClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;

public class DynamoDBUtil {
    private static final DynamoDbClient dynamoDbClient = DynamoDbClient.builder()
            .region(Region.of("ap-south-1"))
            .build();

    private static final DynamoDbEnhancedClient enhancedClient = DynamoDbEnhancedClient.builder()
            .dynamoDbClient(dynamoDbClient)
            .build();

    public static DynamoDbEnhancedClient getDynamoDbEnhancedClient() {
        return enhancedClient;
    }
}
