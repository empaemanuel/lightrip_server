package org.group72.server.dao;

import org.group72.server.model.Edge;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

public interface EdgeRepository extends CrudRepository<Edge, Integer> {

    /**
     * Fetches an edge where the positions matches corresponding column.
     * todo implement fetch that disregards the internal order of parameters.
     * @param lat_A
     * @param lon_A
     * @param lat_B
     * @param lon_B
     * @return
     */
    @Query("SELECT e "+
            "FROM Edge e " +
            "WHERE e.node1.latitude = :lat_A " +
            "and e.node1.longitude = :lon_A " +
            "and e.node2.latitude = :lat_B " +
            "and e.node2.longitude = :lon_B " )
    Edge getEdge(@Param("lat_A") double lat_A, @Param ("lon_A") double lon_A, @Param ("lat_B") double lat_B, @Param ("lon_B") double lon_B);

    @Transactional
    @Modifying
    @Query("UPDATE Edge e SET e.lightWeight = :LightWeight WHERE e.node1.latitude = :Lat1 AND e.node1.longitude = :Long1 AND e.node2.latitude = :Lat2 AND e.node2.longitude = :Long2")
    void setEdgeLightWeight(@Param("Lat1") double latitude1, @Param("Long1") double longitude1, @Param("Lat2") double latitude2, @Param("Long2") double longitude2, @Param("LightWeight") int lightWeight);

    @Query("SELECT e " +
            "FROM Edge e " +
            "WHERE (e.node1.latitude = :latitude " +
            "and e.node1.longitude = :longitude) " +
            "or (e.node2.latitude = :latitude " +
            "and e.node2.longitude = :longitude) ")
    List<Edge> getEdgesBy(@Param("latitude") double latitude, @Param("longitude") double longitude);

}
