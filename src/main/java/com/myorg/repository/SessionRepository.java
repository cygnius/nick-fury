package com.myorg.repository;

import com.myorg.models.Session;
import software.amazon.awssdk.services.dynamodb.DynamoDbClient;
import software.amazon.awssdk.services.dynamodb.model.*;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.dynamodb.model.AttributeValue;

import java.util.*;

public class SessionRepository {

    private final DynamoDbClient db = DynamoDbClient.builder()
            .region(Region.of(System.getenv("AWS_REGION")))
            .build();

    private final String tableName = System.getenv("SESSION_TABLE");
    private final String GSI_THERAPIST_SCHEDULE = "therapist_schedule_index";

    // Create a new session
    public Session createSession(Session session) {
        String id = UUID.randomUUID().toString();
        session.setId(id);

        Map<String, AttributeValue> item = new HashMap<>();

        item.put("id", AttributeValue.fromS(session.getId()));
        item.put("therapist_id", AttributeValue.fromS(session.getTherapistId()));
        item.put("title", AttributeValue.fromS(session.getTitle()));
        item.put("scheduled_at", AttributeValue.fromS(session.getScheduledAt()));
        item.put("duration_min", AttributeValue.fromN(String.valueOf(session.getDurationMin())));
        item.put("status", AttributeValue.fromS(session.getStatus()));
        item.put("is_shared", AttributeValue.fromBool(session.isShared()));
        if (session.getClientId() != null)
            item.put("client_id", AttributeValue.fromS(session.getClientId()));
        if (session.getContent() != null)
            item.put("content", AttributeValue.fromS(session.getContent()));

        db.putItem(PutItemRequest.builder()
                .tableName(tableName)
                .item(item)
                .build());

        return session;
    }

    // Get session by ID
    public Session getSessionById(String sessionId) {
        Map<String, AttributeValue> key = Map.of("id", AttributeValue.fromS(sessionId));

        GetItemResponse response = db.getItem(GetItemRequest.builder()
                .tableName(tableName)
                .key(key)
                .build());

        if (!response.hasItem()) return null;

        return mapToModel(response.item());
    }

    // List all sessions by therapist ID (using GSI)
    public List<Session> listSessionsForTherapist(String therapistId) {
        Map<String, AttributeValue> values = Map.of(":tid", AttributeValue.fromS(therapistId));

        QueryRequest request = QueryRequest.builder()
                .tableName(tableName)
                .indexName(GSI_THERAPIST_SCHEDULE)
                .keyConditionExpression("therapist_id = :tid")
                .expressionAttributeValues(values)
                .build();

        QueryResponse response = db.query(request);
        List<Session> sessions = new ArrayList<>();
        for (Map<String, AttributeValue> item : response.items()) {
            sessions.add(mapToModel(item));
        }
        return sessions;
    }

    // Update session details
    public boolean updateSession(String sessionId, Session session) {
        Map<String, AttributeValue> key = Map.of("id", AttributeValue.fromS(sessionId));


        Map<String, AttributeValueUpdate> updates = new HashMap<>();

//        updates.put("title", AttributeValueUpdate.builder().value(AttributeValue.fromS(session.getTitle())).action(AttributeAction.PUT).build());
//        updates.put("scheduled_at", AttributeValueUpdate.builder().value(AttributeValue.fromS(session.getScheduledAt())).action(AttributeAction.PUT).build());
//        updates.put("duration_min", AttributeValueUpdate.builder().value(AttributeValue.fromN(String.valueOf(session.getDurationMin()))).action(AttributeAction.PUT).build());
//        updates.put("status", AttributeValueUpdate.builder().value(AttributeValue.fromS(session.getStatus())).action(AttributeAction.PUT).build());
//
//
//        updates.put("is_shared", AttributeValueUpdate.builder().value(AttributeValue.fromBool(session.isShared())).action(AttributeAction.PUT).build());
//
//        if (session.getClientId() != null)
//            updates.put("client_id", AttributeValueUpdate.builder().value(AttributeValue.fromS(session.getClientId())).action(AttributeAction.PUT).build());
//
//        if (session.getContent() != null)
//            updates.put("content", AttributeValueUpdate.builder().value(AttributeValue.fromS(session.getContent())).action(AttributeAction.PUT).build());


        if (session.getTitle() != null) {
            updates.put("title", AttributeValueUpdate.builder()
                    .value(AttributeValue.fromS(session.getTitle()))
                    .action(AttributeAction.PUT)
                    .build());
        }

        if (session.getScheduledAt() != null) {
            updates.put("scheduled_at", AttributeValueUpdate.builder()
                    .value(AttributeValue.fromS(session.getScheduledAt()))
                    .action(AttributeAction.PUT)
                    .build());
        }

        if (session.getDurationMin() > 0) {
            updates.put("duration_min", AttributeValueUpdate.builder()
                    .value(AttributeValue.fromN(String.valueOf(session.getDurationMin())))
                    .action(AttributeAction.PUT)
                    .build());
        }

        if (session.getStatus() != null) {
            updates.put("status", AttributeValueUpdate.builder()
                    .value(AttributeValue.fromS(session.getStatus()))
                    .action(AttributeAction.PUT)
                    .build());
        }

        // Note: boolean primitive can't be null, so assume default false — or wrap it in Boolean
        updates.put("is_shared", AttributeValueUpdate.builder()
                .value(AttributeValue.fromBool(session.isShared()))
                .action(AttributeAction.PUT)
                .build());

        if (session.getClientId() != null) {
            updates.put("client_id", AttributeValueUpdate.builder()
                    .value(AttributeValue.fromS(session.getClientId()))
                    .action(AttributeAction.PUT)
                    .build());
        }

        if (session.getContent() != null) {
            updates.put("content", AttributeValueUpdate.builder()
                    .value(AttributeValue.fromS(session.getContent()))
                    .action(AttributeAction.PUT)
                    .build());
        }

        if (updates.isEmpty()) return false; // Nothing to update

        UpdateItemRequest request = UpdateItemRequest.builder()
                .tableName(tableName)
                .key(key)
                .attributeUpdates(updates)
                .build();

        db.updateItem(request);
        return true;
    }

    // Delete session
    public boolean deleteSession(String sessionId) {
        Map<String, AttributeValue> key = Map.of("id", AttributeValue.fromS(sessionId));

//        db.deleteItem(DeleteItemRequest.builder()
//                .tableName(tableName)
//                .key(key)
//                .build());
//        return true;

        DeleteItemRequest request = DeleteItemRequest.builder()
                .tableName(tableName)
                .key(key)
                .returnValues(ReturnValue.ALL_OLD) // Return the item that was deleted
                .build();

        DeleteItemResponse response = db.deleteItem(request);

        // If nothing was returned, item didn't exist
        return response.attributes() != null && !response.attributes().isEmpty();
    }

    // Mapper from DynamoDB item to model
    private Session mapToModel(Map<String, AttributeValue> item) {
        Session session = new Session();
        session.setId(item.get("id").s());
        session.setTherapistId(item.get("therapist_id").s());
        session.setTitle(item.get("title").s());
        session.setScheduledAt(item.get("scheduled_at").s());
        session.setDurationMin(Integer.parseInt(item.get("duration_min").n()));
        session.setStatus(item.get("status").s());
        session.setShared(item.get("is_shared").bool());

        if (item.containsKey("client_id"))
            session.setClientId(item.get("client_id").s());

        if (item.containsKey("content"))
            session.setContent(item.get("content").s());

        return session;
    }
}
