package org.group72.server.model;

import org.group72.server.dao.EdgeRepository;
import org.springframework.beans.factory.annotation.Autowired;

import javax.persistence.*;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;
/**
*
*
*
*@Author Magnus P.
* */

@Entity // This tells Hibernate to make a table out of this class
public class Route{


    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    /*private JSONArray MaxLightRoute;
    private JSONArray MedLightRoute;
    private JSONArray MinLightRoute;*/

    public Route(){
    }

    public long getId() {
        return id   ;
    }

    /*public JSONArray getMaxLightRoute() {
        return MaxLightRoute;
    }

    public JSONArray getMedLightRoute() {
        return MedLightRoute;
    }

    public JSONArray getMinLightRoute() {
        return MinLightRoute;
    }*/


}