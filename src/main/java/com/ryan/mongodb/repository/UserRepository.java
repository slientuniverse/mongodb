package com.ryan.mongodb.repository;

import com.ryan.mongodb.entity.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

/**
 * @author ryan
 */
public interface UserRepository extends MongoRepository<User, String> {

    User findByName(String name);

    @Query(value = "{'_id': ?0}", fields = "{'age' : 0}")
    String findNameById(String id);

    List<User> findByNameLike(String name);
}