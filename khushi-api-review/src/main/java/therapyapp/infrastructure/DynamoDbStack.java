package therapyapp.infrastructure;

import software.amazon.awscdk.App;
import software.amazon.awscdk.RemovalPolicy;
import software.amazon.awscdk.Stack;
import software.amazon.awscdk.StackProps;
import software.amazon.awscdk.services.dynamodb.Attribute;
import software.amazon.awscdk.services.dynamodb.AttributeType;
import software.amazon.awscdk.services.dynamodb.BillingMode;
import software.amazon.awscdk.services.dynamodb.GlobalSecondaryIndexProps;
import software.amazon.awscdk.services.dynamodb.Table;
import software.constructs.Construct;

public class DynamoDbStack extends Stack {
    
    public final Table usersTable;
    public final Table journalsTable;
    public final Table therapistsTable;
    public final Table mappingTable;
    public final Table journalAccessTable;
    public final Table sessionsTable;
    public final Table messagesTable;
    public final Table appointmentsTable;
    
    public DynamoDbStack(final Construct scope, final String id, final StackProps props) {
        super(scope, id, props);
        
        // 1. Users Table
        usersTable = Table.Builder.create(this, "UsersTable")
                .tableName("Users")
                .partitionKey(Attribute.builder()
                        .name("userId")
                        .type(AttributeType.STRING)
                        .build())
                .billingMode(BillingMode.PAY_PER_REQUEST)
                .removalPolicy(RemovalPolicy.DESTROY)
                .build();
        // Add GSI: email-index
        usersTable.addGlobalSecondaryIndex(GlobalSecondaryIndexProps.builder()
                .indexName("email-index")
                .partitionKey(Attribute.builder()
                        .name("email")
                        .type(AttributeType.STRING)
                        .build())
                .sortKey(Attribute.builder()
                        .name("userId")
                        .type(AttributeType.STRING)
                        .build())
                .build());
        
        
        journalsTable = Table.Builder.create(this, "JournalsTable")
                .tableName("Journals")
                .partitionKey(Attribute.builder()
                        .name("clientId")
                        .type(AttributeType.STRING)
                        .build())
                .sortKey(Attribute.builder()
                        .name("timestamp")
                        .type(AttributeType.STRING)
                        .build())
                .billingMode(BillingMode.PAY_PER_REQUEST)
                .removalPolicy(RemovalPolicy.DESTROY)
                .build();
        
    
        therapistsTable = Table.Builder.create(this, "TherapistsTable")
                .tableName("Therapists")
                .partitionKey(Attribute.builder()
                        .name("therapistId")
                        .type(AttributeType.STRING)
                        .build())
                .billingMode(BillingMode.PAY_PER_REQUEST)
                .removalPolicy(RemovalPolicy.DESTROY)
                .build();
        
        
        mappingTable = Table.Builder.create(this, "MappingTable")
                .tableName("TherapistClientMapping")
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
    
        mappingTable.addGlobalSecondaryIndex(GlobalSecondaryIndexProps.builder()
                .indexName("therapist-index")
                .partitionKey(Attribute.builder()
                        .name("therapistId")
                        .type(AttributeType.STRING)
                        .build())
                .sortKey(Attribute.builder()
                        .name("clientId")
                        .type(AttributeType.STRING)
                        .build())
                .build());
        
        
        journalAccessTable = Table.Builder.create(this, "JournalAccessTable")
                .tableName("JournalAccess")
                .partitionKey(Attribute.builder()
                        .name("therapistId")
                        .type(AttributeType.STRING)
                        .build())
                .sortKey(Attribute.builder()
                        .name("clientId")
                        .type(AttributeType.STRING)
                        .build())
                .billingMode(BillingMode.PAY_PER_REQUEST)
                .removalPolicy(RemovalPolicy.DESTROY)
                .build();
        
        journalAccessTable.addGlobalSecondaryIndex(GlobalSecondaryIndexProps.builder()
                .indexName("client-index")
                .partitionKey(Attribute.builder()
                        .name("clientId")
                        .type(AttributeType.STRING)
                        .build())
                .sortKey(Attribute.builder()
                        .name("therapistId")
                        .type(AttributeType.STRING)
                        .build())
                .build());
        
        
        sessionsTable = Table.Builder.create(this, "SessionsTable")
                .tableName("Sessions")
                .partitionKey(Attribute.builder()
                        .name("sessionId")
                        .type(AttributeType.STRING)
                        .build())
                .billingMode(BillingMode.PAY_PER_REQUEST)
                .removalPolicy(RemovalPolicy.DESTROY)
                .build();
        
        sessionsTable.addGlobalSecondaryIndex(GlobalSecondaryIndexProps.builder()
                .indexName("therapist-session-index")
                .partitionKey(Attribute.builder()
                        .name("therapistId")
                        .type(AttributeType.STRING)
                        .build())
                .sortKey(Attribute.builder()
                        .name("sessionDate")
                        .type(AttributeType.STRING)
                        .build())
                .build());
        
    
        messagesTable = Table.Builder.create(this, "MessagesTable")
                .tableName("Messages")
                .partitionKey(Attribute.builder()
                        .name("conversationId")
                        .type(AttributeType.STRING)
                        .build())
                .sortKey(Attribute.builder()
                        .name("timestamp")
                        .type(AttributeType.STRING)
                        .build())
                .billingMode(BillingMode.PAY_PER_REQUEST)
                .removalPolicy(RemovalPolicy.DESTROY)
                .build();
        
        messagesTable.addGlobalSecondaryIndex(GlobalSecondaryIndexProps.builder()
                .indexName("sender-index")
                .partitionKey(Attribute.builder()
                        .name("senderId")
                        .type(AttributeType.STRING)
                        .build())
                .sortKey(Attribute.builder()
                        .name("timestamp")
                        .type(AttributeType.STRING)
                        .build())
                .build());
        
        
        appointmentsTable = Table.Builder.create(this, "AppointmentsTable")
                .tableName("Appointments")
                .partitionKey(Attribute.builder()
                        .name("appointmentId")
                        .type(AttributeType.STRING)
                        .build())
                .billingMode(BillingMode.PAY_PER_REQUEST)
                .removalPolicy(RemovalPolicy.DESTROY)
                .build();
    }
}
