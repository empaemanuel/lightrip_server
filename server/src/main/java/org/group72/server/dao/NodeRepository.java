/**
 *  Decided to not use a node table at for the moment because making
 *  foreign keys with multiple values was a bit problematic in JPA,
 *  but we should look into it for the future.
 * @author Emil M.
 */




package org.group72.server.dao;

import org.group72.server.model.Node;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

public interface NodeRepository extends CrudRepository<Node, Double> {

    @Query("SELECT n FROM Node n WHERE n.latitude = :latitude and n.longitude = :longitude ")
    Node getNode(@Param("latitude") double latitude, @Param("longitude") double longitude);

}