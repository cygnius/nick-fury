package io.example.therapy.therapy.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import io.example.therapy.therapy.entity.Therapist;


@Repository
public interface TherapistRepo extends  CrudRepository<Therapist, String>{
}
