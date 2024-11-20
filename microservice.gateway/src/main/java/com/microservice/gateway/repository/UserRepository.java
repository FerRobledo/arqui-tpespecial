package com.microservice.gateway.repository;

import com.microservice.gateway.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, String> {

    Optional<User> findOneByUsernameIgnoreCase(String username);
}
