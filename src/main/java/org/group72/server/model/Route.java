package org.group72.server.model;

import javax.persistence.*;
import java.io.Serializable;

/**
*
*
*
*@Author Magnus P.
* */

@Entity // This tells Hibernate to make a table out of this class
@IdClass(Route.class)
public class Route implements Serializable {


    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    @Id
    private Integer ownedBy;


    /*private JSONArray MaxLightRoute;
    private JSONArray MedLightRoute;
    private JSONArray MinLightRoute;*/


    public Route(){
    }

    public long getId() {
        return id;
    }

    public Integer getOwnedBy() {
        return ownedBy;
    }

    public void setOwnedBy(Integer ownedBy) {
        this.ownedBy = ownedBy;
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