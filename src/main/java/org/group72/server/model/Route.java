package org.group72.server.model;

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
public class Route {


    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private Integer id;

    private Integer ownedBy;


    /*private JSONArray MaxLightRoute;
    private JSONArray MedLightRoute;
    private JSONArray MinLightRoute;*/


    public Route(){
    }

    public Integer getId() {
        return id;
    }

    public Integer getOwnedBy() {
        return ownedBy;
    }

    public void setOwnedBy(Integer ownedBy) {
        this.ownedBy = ownedBy;
    }

    @Override
    public String toString(){
        return "ID:"+getId() + ","+"OwnedBy:"+ getOwnedBy();
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