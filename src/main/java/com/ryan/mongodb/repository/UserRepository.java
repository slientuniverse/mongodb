package com.ryan.mongodb.repository;

import com.ryan.mongodb.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;

/**
 * @author hyt
 */
public interface UserRepository extends MongoRepository<User, String> {
}
