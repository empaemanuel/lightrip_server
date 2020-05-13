package org.group72.server.dao;

import org.group72.server.model.LightNode;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;


// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete
public interface LightRepository extends CrudRepository<LightNode, Double> {

    @Query("SELECT l FROM LightNode l WHERE l.latitude = :latitude and l.longitude = :longitude ")
    LightNode getLightNode(@Param("latitude") double latitude, @Param("longitude") double longitude);

}