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

    private final Table clientsTable;
    private final Table therapistsTable;
    private final Table mappingsTable;
    private final Table sessionsTable;
    private final Table messagesTable;

    public DynamoDBStack(final Construct scope, final String id) {
        this(scope, id, null);
    }

    public DynamoDBStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);

        // Define the Clients Table
        clientsTable = Table.Builder.create(this, "ClientsTable")
                .tableName("Clients")
                .partitionKey(Attribute.builder()
                        .name("clientId")
                        .type(AttributeType.STRING)
                        .build())
                .billingMode(BillingMode.PAY_PER_REQUEST) // No need to specify read/write capacity
                .removalPolicy(RemovalPolicy.DESTROY) // Change to RETAIN for production
                .build();

        // Define the Therapists Table
        therapistsTable = Table.Builder.create(this, "TherapistsTable")
                .tableName("Therapists")
                .partitionKey(Attribute.builder()
                        .name("therapistId")
                        .type(AttributeType.STRING)
                        .build())
                .billingMode(BillingMode.PAY_PER_REQUEST) // No need to specify read/write capacity
                .removalPolicy(RemovalPolicy.DESTROY) // Change to RETAIN for production
                .build();

        // Global Secondary Index 1 (SpecializationIndex) - Query therapists by specialization
//        therapistsTable.addGlobalSecondaryIndex(GlobalSecondaryIndexProps.builder()
//                .indexName("SpecializationIndex")
//                .partitionKey(Attribute.builder()
//                        .name("specialization")
//                        .type(AttributeType.STRING)
//                        .build())
//                .sortKey(Attribute.builder()
//                        .name("therapistId")
//                        .type(AttributeType.STRING)
//                        .build())
//                .build());

        // Define the Messages Table
        messagesTable = Table.Builder.create(this, "MessagesTable")
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

        // Global Secondary Index 1 (ConversationIndex) - Query messages by conversationId
        messagesTable.addGlobalSecondaryIndex(GlobalSecondaryIndexProps.builder()
                .indexName("ConversationIndex")
                .partitionKey(Attribute.builder()
                        .name("conversationId")
                        .type(AttributeType.STRING)
                        .build())
                .sortKey(Attribute.builder()
                        .name("timestamp")
                        .type(AttributeType.STRING)
                        .build())
                .build());

        // Global Secondary Index 2 (SenderIndex) - Query messages by sender and timestamp
        messagesTable.addGlobalSecondaryIndex(GlobalSecondaryIndexProps.builder()
                .indexName("SenderIndex")
                .partitionKey(Attribute.builder()
                        .name("senderId")
                        .type(AttributeType.STRING)
                        .build())
                .sortKey(Attribute.builder()
                        .name("timestamp")
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

        // Global Secondary Index 1 (FeelingIndex) - Query journals by feeling
//        journalsTable.addGlobalSecondaryIndex(GlobalSecondaryIndexProps.builder()
//                .indexName("FeelingIndex")
//                .partitionKey(Attribute.builder()
//                        .name("feeling")
//                        .type(AttributeType.STRING)
//                        .build())
//                .sortKey(Attribute.builder()
//                        .name("timestamp")
//                        .type(AttributeType.STRING)
//                        .build())
//                .build());

        // Define the Sessions Table
        sessionsTable = Table.Builder.create(this, "SessionsTable")
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

        // Global Secondary Index 1 (SessionDateIndex) - Query sessions by therapistId and sessionDate
        sessionsTable.addGlobalSecondaryIndex(GlobalSecondaryIndexProps.builder()
                .indexName("SessionDateIndex")
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
        mappingsTable = Table.Builder.create(this, "MappingsTable")
                .tableName("Mappings")
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

        // Global Secondary Index 1 (MappingsIndex) - Query clienttherapistmappings by therapistId
//        mappingsTable.addGlobalSecondaryIndex(GlobalSecondaryIndexProps.builder()
//                .indexName("MappingsIndex")
//                .partitionKey(Attribute.builder()
//                        .name("therapistId")
//                        .type(AttributeType.STRING)
//                        .build())
//                .sortKey(Attribute.builder()
//                        .name("clientId")
//                        .type(AttributeType.STRING)
//                        .build())
//                .build());

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

        // Global Secondary Index 1 (AppointmentsIndex) - Query appointments by therapistId
//        appointmentsTable.addGlobalSecondaryIndex(GlobalSecondaryIndexProps.builder()
//                .indexName("AppointmentsIndex")
//                .partitionKey(Attribute.builder()
//                        .name("therapistId")
//                        .type(AttributeType.STRING)
//                        .build())
//                .sortKey(Attribute.builder()
//                        .name("timestamp")
//                        .type(AttributeType.STRING)
//                        .build())
//                .build());
    }

    public Table getClientsTable() {
        return clientsTable;
    }

    public Table getTherapistsTable() { return therapistsTable; }

    public Table getMappingsTable() { return mappingsTable; }

    public Table getSessionsTable() { return sessionsTable; }

    public Table getMessagesTable() { return messagesTable; }
}