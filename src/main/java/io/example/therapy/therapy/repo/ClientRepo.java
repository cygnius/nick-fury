package io.example.therapy.therapy.repo;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import io.example.therapy.therapy.entity.Client;


@Repository
public interface ClientRepo extends CrudRepository<Client, String> {
    
}
