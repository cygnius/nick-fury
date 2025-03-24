package com.app.repository;

import com.app.model.Session;
import com.app.util.DynamoDBUtil;
import software.amazon.awssdk.enhanced.dynamodb.*;

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
        sessionTable.scan().items().forEach(sessions::add);
        return sessions;
    }

    public void updateSession(Session session) {
        sessionTable.putItem(session);
    }

    public void deleteSession(String sessionId, String sessionDate) {
        Key key = Key.builder().partitionValue(sessionId).sortValue(sessionDate).build();
        sessionTable.deleteItem(key);
    }
}
