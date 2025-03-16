package therapyapp.service;

import java.time.LocalDateTime;
import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import therapyapp.model.Session;
import therapyapp.repository.SessionRepository;

@Singleton
public class SessionService {

    @Inject
    private SessionRepository sessionRepository;

    
    public Session createSession(Session session) {
        sessionRepository.saveSession(session);
        return session;
    }


    public Session getSession(String sessionId) {
        return sessionRepository.getSessionById(sessionId);
    }


    public List<Session> listSessionsByTherapist(String therapistId, LocalDateTime afterDate) {
        return sessionRepository.listSessionsByTherapistAndDate(therapistId, afterDate);
    }
}
