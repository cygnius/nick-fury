package com.app.repository;

import com.app.model.Session;
import com.app.util.DynamoDBUtil;
import software.amazon.awssdk.enhanced.dynamodb.*;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryConditional;
import software.amazon.awssdk.enhanced.dynamodb.model.QueryEnhancedRequest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class SessionRepository {

    private final DynamoDbTable<Session> sessionTable;
    private final DynamoDbIndex<Session> sessionDateIndex;

    public SessionRepository() {
        DynamoDbEnhancedClient enhancedClient = DynamoDBUtil.getDynamoDbEnhancedClient();
        this.sessionTable = enhancedClient.table("Sessions", TableSchema.fromBean(Session.class));
        this.sessionDateIndex = sessionTable.index("SessionDateIndex");
    }

    public void saveSession(Session session) {
        sessionTable.putItem(session);
    }

    public Optional<Session> getSessionById(String sessionId, String sessionDate) {
        Key key = Key.builder().partitionValue(sessionId).sortValue(sessionDate).build();

        return Optional.ofNullable(sessionTable.getItem(key));
    }

    public List<Session> getAllSessions() {
        List<Session> sessions = new ArrayList<>();
        for (Session session : sessionTable.scan().items()) {
            sessions.add(session);
        }
        return sessions;
    }

    public void updateSession(Session session) {
        sessionTable.putItem(session);
    }

//    public void deleteSession(String sessionId, String sessionDate) {
//        Key key = Key.builder().partitionValue(sessionId).sortValue(sessionDate).build();
//        sessionTable.deleteItem(key);
//    }

    public boolean deleteSession(String sessionId, String sessionDate) {
        Session session = sessionTable.getItem(Key.builder().partitionValue(sessionId).sortValue(sessionDate).build());
        if (session == null) {
            return false;
        }

        sessionTable.deleteItem(session);
        return true;
    }

    public List<Session> getSessionsByTherapistAndDate(String therapistId, String sessionDate) {
        QueryConditional queryConditional = QueryConditional.keyEqualTo(b ->
                b.partitionValue(therapistId).sortValue(sessionDate)
        );

        QueryEnhancedRequest queryRequest = QueryEnhancedRequest.builder()
                .queryConditional(queryConditional)
                .build();

        List<Session> sessions = new ArrayList<>();
        sessionDateIndex.query(queryRequest).forEach(page ->
                sessions.addAll(page.items())
        );
        return sessions;
    }
}
