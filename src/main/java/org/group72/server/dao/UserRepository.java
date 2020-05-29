package org.group72.server.dao;

import org.group72.server.model.User;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete
@Repository
public interface UserRepository extends CrudRepository<User, Integer> {

    @Query("SELECT user FROM User user WHERE user.id = :id")
    User getUserWithID(@Param("id") Integer id);
}