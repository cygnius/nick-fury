package com.myorg.db;

import software.amazon.awscdk.RemovalPolicy;
import software.constructs.Construct;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.dynamodb.Attribute;
import software.amazon.awscdk.services.dynamodb.AttributeType;
import software.amazon.awscdk.services.dynamodb.BillingMode;
import software.amazon.awscdk.services.dynamodb.Table;
import software.amazon.awscdk.services.dynamodb.GlobalSecondaryIndexProps;

public class DynamoDBStack extends Stack {
    public DynamoDBStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public DynamoDBStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        // Define the Clients Table
        Table clientsTable = Table.Builder.create(this, "ClientsTable")
                .tableName("Clients")
                .partitionKey(Attribute.builder()
                        .name("clientId")
                        .type(AttributeType.STRING)
                        .build())
                .billingMode(BillingMode.PAY_PER_REQUEST) // No need to specify read/write capacity
                .removalPolicy(RemovalPolicy.DESTROY) // Change to RETAIN for production
                .build();

        // Define the Therapists Table
        Table therapistsTable = Table.Builder.create(this, "TherapistsTable")
                .tableName("Therapists")
                .partitionKey(Attribute.builder()
                        .name("therapistId")
                        .type(AttributeType.STRING)
                        .build())
                .billingMode(BillingMode.PAY_PER_REQUEST) // No need to specify read/write capacity
                .removalPolicy(RemovalPolicy.DESTROY) // Change to RETAIN for production
                .build();

        // Global Secondary Index 1 (GSI1) - Query therapists by specialization
        therapistsTable.addGlobalSecondaryIndex(GlobalSecondaryIndexProps.builder()
                .indexName("GSI1")
                .partitionKey(Attribute.builder()
                        .name("specialization")
                        .type(AttributeType.STRING)
                        .build())
                .build());

        // Define the Messages Table
        Table messagesTable = Table.Builder.create(this, "MessagesTable")
                .tableName("Messages")
                .partitionKey(Attribute.builder()
                        .name("messageId")
                        .type(AttributeType.STRING)
                        .build())
                .sortKey(Attribute.builder()
                        .name("timestamp")
                        .type(AttributeType.STRING)
                        .build())
                .billingMode(BillingMode.PAY_PER_REQUEST) // No need to specify read/write capacity
                .removalPolicy(RemovalPolicy.DESTROY) // Change to RETAIN for production
                .build();

        // Global Secondary Index 1 (GSI1) - Query messages by sender
        messagesTable.addGlobalSecondaryIndex(GlobalSecondaryIndexProps.builder()
                .indexName("GSI1")
                .partitionKey(Attribute.builder()
                        .name("sender")
                        .type(AttributeType.STRING)
                        .build())
                .sortKey(Attribute.builder()
                        .name("receiver")
                        .type(AttributeType.STRING)
                        .build())
                .build());

        // Define the Journals Table
        Table journalsTable = Table.Builder.create(this, "JournalsTable")
                .tableName("Journals")
                .partitionKey(Attribute.builder()
                        .name("clientId")
                        .type(AttributeType.STRING)
                        .build())
                .sortKey(Attribute.builder()
                        .name("timestamp")
                        .type(AttributeType.STRING)
                        .build())
                .billingMode(BillingMode.PAY_PER_REQUEST) // No need to specify read/write capacity
                .removalPolicy(RemovalPolicy.DESTROY) // Change to RETAIN for production
                .build();

        // Global Secondary Index (GSI1) - Query journals by feeling
        journalsTable.addGlobalSecondaryIndex(GlobalSecondaryIndexProps.builder()
                .indexName("GSI1")
                .partitionKey(Attribute.builder()
                        .name("feeling")
                        .type(AttributeType.STRING)
                        .build())
                .sortKey(Attribute.builder()
                        .name("timestamp")
                        .type(AttributeType.STRING)
                        .build())
                .build());

        // Define the Sessions Table
        Table sessionsTable = Table.Builder.create(this, "SessionsTable")
                .tableName("Sessions")
                .partitionKey(Attribute.builder()
                        .name("sessionId")
                        .type(AttributeType.STRING)
                        .build())
                .sortKey(Attribute.builder()
                        .name("sessionDate")
                        .type(AttributeType.STRING)
                        .build())
                .billingMode(BillingMode.PAY_PER_REQUEST) // No need to specify read/write capacity
                .removalPolicy(RemovalPolicy.DESTROY) // Change to RETAIN for production
                .build();

        // Global Secondary Index (GSI1) - Query sessions by therapistId
        sessionsTable.addGlobalSecondaryIndex(GlobalSecondaryIndexProps.builder()
                .indexName("GSI1")
                .partitionKey(Attribute.builder()
                        .name("therapistId")
                        .type(AttributeType.STRING)
                        .build())
                .sortKey(Attribute.builder()
                        .name("sessionDate")
                        .type(AttributeType.STRING)
                        .build())
                .build());

        // Define the ClientTherapistMappings Table
        Table clientTherapistMappingsTable = Table.Builder.create(this, "ClientTherapistMappingsTable")
                .tableName("ClientTherapistMappings")
                .partitionKey(Attribute.builder()
                        .name("clientId")
                        .type(AttributeType.STRING)
                        .build())
                .sortKey(Attribute.builder()
                        .name("therapistId")
                        .type(AttributeType.STRING)
                        .build())
                .billingMode(BillingMode.PAY_PER_REQUEST)
                .removalPolicy(RemovalPolicy.DESTROY)
                .build();

        // Global Secondary Index 1 (GSI1) - Query clienttherapistmappings by therapistId
        clientTherapistMappingsTable.addGlobalSecondaryIndex(GlobalSecondaryIndexProps.builder()
                .indexName("GSI1")
                .partitionKey(Attribute.builder()
                        .name("therapistId")
                        .type(AttributeType.STRING)
                        .build())
                .sortKey(Attribute.builder()
                        .name("clientId")
                        .type(AttributeType.STRING)
                        .build())
                .build());

        // Define the Appointments Table
        Table appointmentsTable = Table.Builder.create(this, "AppointmentsTable")
                .tableName("Appointments")
                .partitionKey(Attribute.builder()
                        .name("appointmentId")
                        .type(AttributeType.STRING)
                        .build())
                .sortKey(Attribute.builder()
                        .name("timestamp")
                        .type(AttributeType.STRING)
                        .build())
                .billingMode(BillingMode.PAY_PER_REQUEST)
                .removalPolicy(RemovalPolicy.DESTROY)
                .build();

        appointmentsTable.addGlobalSecondaryIndex(GlobalSecondaryIndexProps.builder()
                .indexName("GSI1")
                .partitionKey(Attribute.builder()
                        .name("therapistId")
                        .type(AttributeType.STRING)
                        .build())
                .sortKey(Attribute.builder()
                        .name("timestamp")
                        .type(AttributeType.STRING)
                        .build())
                .build());
    }
}