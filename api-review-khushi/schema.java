import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeDefinition;
import software.amazon.awssdk.services.dynamodb.model.CreateTableRequest;
import software.amazon.awssdk.services.dynamodb.model.KeySchemaElement;
import software.amazon.awssdk.services.dynamodb.model.KeyType;
import software.amazon.awssdk.services.dynamodb.model.ProvisionedThroughput;
import software.amazon.awssdk.services.dynamodb.model.ScalarAttributeType;

public class DynamoDbSchemaCreator {

    private static final DynamoDbClient dynamoDbClient = DynamoDbClient.create();

    public static void main(String[] args) {
        createUserTable();
        createJournalTable();
        createMessageTable();
        createAppointmentTable();
        createAccessRequestTable();
    }

    private static void createUserTable() {
        CreateTableRequest request = CreateTableRequest.builder()
            .tableName("Users")
            .keySchema(
                KeySchemaElement.builder().attributeName("userId").keyType(KeyType.HASH).build()
            )
            .attributeDefinitions(
                AttributeDefinition.builder().attributeName("userId").attributeType(ScalarAttributeType.S).build()
            )
            .provisionedThroughput(
                ProvisionedThroughput.builder().readCapacityUnits(5L).writeCapacityUnits(5L).build()
            )
            .build();

        dynamoDbClient.createTable(request);
        System.out.println("Users table created.");
    }

    private static void createJournalTable() {
        CreateTableRequest request = CreateTableRequest.builder()
            .tableName("Journals")
            .keySchema(
                KeySchemaElement.builder().attributeName("journalId").keyType(KeyType.HASH).build()
            )
            .attributeDefinitions(
                AttributeDefinition.builder().attributeName("journalId").attributeType(ScalarAttributeType.S).build(),
                AttributeDefinition.builder().attributeName("clientId").attributeType(ScalarAttributeType.S).build()
            )
            .provisionedThroughput(
                ProvisionedThroughput.builder().readCapacityUnits(5L).writeCapacityUnits(5L).build()
            )
            .build();

        dynamoDbClient.createTable(request);
        System.out.println("Journals table created.");
    }

    private static void createMessageTable() {
        CreateTableRequest request = CreateTableRequest.builder()
            .tableName("Messages")
            .keySchema(
                KeySchemaElement.builder().attributeName("messageId").keyType(KeyType.HASH).build()
            )
            .attributeDefinitions(
                AttributeDefinition.builder().attributeName("messageId").attributeType(ScalarAttributeType.S).build()
            )
            .provisionedThroughput(
                ProvisionedThroughput.builder().readCapacityUnits(5L).writeCapacityUnits(5L).build()
            )
            .build();

        dynamoDbClient.createTable(request);
        System.out.println("Messages table created.");
    }

    private static void createAppointmentTable() {
        CreateTableRequest request = CreateTableRequest.builder()
            .tableName("Appointments")
            .keySchema(
                KeySchemaElement.builder().attributeName("appointmentId").keyType(KeyType.HASH).build()
            )
            .attributeDefinitions(
                AttributeDefinition.builder().attributeName("appointmentId").attributeType(ScalarAttributeType.S).build()
            )
            .provisionedThroughput(
                ProvisionedThroughput.builder().readCapacityUnits(5L).writeCapacityUnits(5L).build()
            )
            .build();

        dynamoDbClient.createTable(request);
        System.out.println("Appointments table created.");
    }

    private static void createAccessRequestTable() {
        CreateTableRequest request = CreateTableRequest.builder()
            .tableName("AccessRequests")
            .keySchema(
                KeySchemaElement.builder().attributeName("requestId").keyType(KeyType.HASH).build()
            )
            .attributeDefinitions(
                AttributeDefinition.builder().attributeName("requestId").attributeType(ScalarAttributeType.S).build()
            )
            .provisionedThroughput(
                ProvisionedThroughput.builder().readCapacityUnits(5L).writeCapacityUnits(5L).build()
            )
            .build();

        dynamoDbClient.createTable(request);
        System.out.println("AccessRequests table created.");
    }
}
