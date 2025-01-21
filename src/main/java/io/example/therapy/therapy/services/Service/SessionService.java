package io.example.therapy.therapy.services.Service;

import java.nio.file.AccessDeniedException;

import io.example.therapy.therapy.entity.Session;

public interface SessionService {
    Session createSession(Session session) throws AccessDeniedException;
    // Session findSession(String sessionKey);
    // List<Session> findSessionByCLient(String clientId);
    // List<Session> findSessionByTherapist(String therapistId);
}
