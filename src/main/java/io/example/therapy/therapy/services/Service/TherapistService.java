package io.example.therapy.therapy.services.Service;

import java.nio.file.AccessDeniedException;

public interface TherapistService {
    //Therapist saveTherapist(Therapist therapist);
    //Therapist getTherapistByEmail(String email);
    void deleteTherapistByEmail(String email) throws AccessDeniedException;
    //Iterable<Therapist> getAllTherapists();
}
