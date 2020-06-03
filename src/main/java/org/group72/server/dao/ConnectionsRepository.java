package org.group72.server.dao;

import java.util.List;

import org.group72.server.model.Connections;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ConnectionsRepository extends CrudRepository<Connections, Integer> {

    @Query("SELECT edge FROM Connections c WHERE c.edgeId = :edgeId " )
    List<Integer> getConnections(@Param("edgeId") int edgeId);
	
}
