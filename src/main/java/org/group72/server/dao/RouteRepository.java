package org.group72.server.dao;

import org.group72.server.model.Route;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

/**
 * Repository interface to fetch saved routes belonging to a specific user.
 */

@Repository
public interface RouteRepository extends CrudRepository<Route, Integer> {

    @Query("SELECT r FROM Route r WHERE r.ownedBy = :ownedBy")
    Route getUserRoutes(@Param ("ownedBy") Integer ownedBy);
}
