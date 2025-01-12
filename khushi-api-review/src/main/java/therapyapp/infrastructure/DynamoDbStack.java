package therapyapp.infrastructure;

import software.amazon.awscdk.App;
import software.amazon.awscdk.Stack;
import software.constructs.Construct;
import software.amazon.awscdk.services.dynamodb.*;
import software.amazon.awscdk.RemovalPolicy;

public class DynamoDbStack extends Stack {

    public final Table userTable;
    public final Table journalTable;
    public final Table sessionTable;
    public final Table messageTable;
    public final Table appointmentTable;

    public DynamoDbStack(final Construct scope, final String id) {
        super(scope, id);

        userTable = createTable("UserTable", "userId");
        journalTable = createTable("JournalTable", "journalId");
        sessionTable = createTable("SessionTable", "sessionId");
        messageTable = createTable("MessageTable", "messageId");
        appointmentTable = createTable("AppointmentTable", "appointmentId");
    }

    private Table createTable(String tableName, String partitionKeyName) {
        return Table.Builder.create(this, tableName)
                .tableName(tableName)
                .partitionKey(Attribute.builder()
                        .name(partitionKeyName)
                        .type(AttributeType.STRING)
                        .build())
                .removalPolicy(RemovalPolicy.DESTROY)  // Only for development; use RETAIN for production
                .build();
    }
}
