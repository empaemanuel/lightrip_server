package org.group72.server.dao;

import org.group72.server.model.LightNode;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;


// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete
public interface LightRepository extends CrudRepository<LightNode, Double> {

    @Query("SELECT l FROM LightNode l WHERE l.latitude = :latitude and l.longitude = :longitude ")
    LightNode getLightNode(@Param("latitude") double latitude, @Param("longitude") double longitude);

    @Query("SELECT l FROM LightNode l WHERE l.latitude > :lat1 AND l.latitude < :lat2 AND l.longitude > :long1 AND l.longitude < :long2")
    List<LightNode> getLightNodes(@Param("lat1") double lat1, @Param("long1") double long1, @Param("lat2") double lat2, @Param("long2") double long2);

    @Query("SELECT l FROM LightNode l WHERE l.latitude > :lat1 AND l.latitude < :lat2 AND l.longitude >= :long1 AND l.longitude <= :long2")
    LightNode getLightNodes2(@Param("lat1") double lat1, @Param("long1") double long1, @Param("lat2") double lat2, @Param("long2") double long2);


    //lat1 and long1 = bottom left corner, lat2 and long2 = top right corner
}

