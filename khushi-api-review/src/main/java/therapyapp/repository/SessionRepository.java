package therapyapp.repository;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;
import software.amazon.awssdk.services.dynamodb.model.GetItemRequest;
import software.amazon.awssdk.services.dynamodb.model.PutItemRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanRequest;
import software.amazon.awssdk.services.dynamodb.model.ScanResponse;
import therapyapp.model.Session;

@Singleton
public class SessionRepository {

    @Inject
    private DynamoDbClient dynamoDbClient;

    private static final String TABLE_NAME = "Sessions";


    public void saveSession(Session session) {
        PutItemRequest request = PutItemRequest.builder()
                .tableName(TABLE_NAME)
                .item(Map.of(
                        "sessionId", AttributeValue.builder().s(session.getSessionId()).build(),
                        "therapistId", AttributeValue.builder().s(session.getTherapistId()).build(),
                        "clientId", AttributeValue.builder().s(session.getClientId()).build(),
                        "sessionDate", AttributeValue.builder().s(session.getSessionDate().toString()).build(),
                        "privateNotes", AttributeValue.builder().s(session.getPrivateNotes()).build(),
                        "sharedNotes", AttributeValue.builder().s(session.getSharedNotes()).build()
                ))
                .build();

        dynamoDbClient.putItem(request);
    }

    
    public Session getSessionById(String sessionId) {
        GetItemRequest request = GetItemRequest.builder()
                .tableName(TABLE_NAME)
                .key(Map.of("sessionId", AttributeValue.builder().s(sessionId).build()))
                .build();

        Map<String, AttributeValue> item = dynamoDbClient.getItem(request).item();

        if (item == null || item.isEmpty()) {
            return null;
        }

        return new Session(
                item.get("sessionId").s(),
                item.get("therapistId").s(),
                item.get("clientId").s(),
                item.get("sessionDate").s(),
                item.get("privateNotes").s(),
                item.get("sharedNotes").s()
        );
    }

    
    public List<Session> listSessionsByTherapistAndDate(String therapistId, LocalDateTime afterDate) {
        ScanRequest request = ScanRequest.builder()
                .tableName(TABLE_NAME)
                .filterExpression("therapistId = :therapistId AND sessionDate >= :afterDate")
                .expressionAttributeValues(Map.of(
                        ":therapistId", AttributeValue.builder().s(therapistId).build(),
                        ":afterDate", AttributeValue.builder().s(afterDate.toString()).build()
                ))
                .build();

        List<Session> sessions = new ArrayList<>();
        ScanResponse response = dynamoDbClient.scan(request);

        for (Map<String, AttributeValue> item : response.items()) {
            sessions.add(new Session(
                    item.get("sessionId").s(),
                    item.get("therapistId").s(),
                    item.get("clientId").s(),
                    item.get("sessionDate").s(),
                    item.get("privateNotes").s(),
                    item.get("sharedNotes").s()
            ));
        }

        return sessions;
    }
}
