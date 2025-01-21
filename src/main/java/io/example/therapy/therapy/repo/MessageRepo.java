package io.example.therapy.therapy.repo;

import org.socialsignin.spring.data.dynamodb.repository.EnableScan;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import io.example.therapy.therapy.entity.Message;

@Repository
@EnableScan
public interface MessageRepo extends CrudRepository<Message, String> {

}
