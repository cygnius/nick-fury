package io.example.therapy.therapy.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import io.example.therapy.therapy.entity.Session;



@Repository
public interface SessionRepo extends CrudRepository<Session, String> {
    // Session findBySessionId(String sessionId);
    // List<Session> findByClientEmail(String clientEmail);
    // List<Session> findByTherapistEmail(String therapistEmail);
}
