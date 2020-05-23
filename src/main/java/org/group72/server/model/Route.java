package org.group72.server.model;

import net.minidev.json.JSONArray;
import org.springframework.data.annotation.Id;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.ManyToOne;
import java.util.UUID;
/**
*
*
*
*@Author Magnus P.
* */

@Entity
        // This tells Hibernate to make a table out of this class
public class Route{

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private int id;

    @ManyToOne
    private User user;
//    private JSONArray MaxLightRoute;
//    private JSONArray MedLightRoute;
//    private JSONArray MinLightRoute;
    @ManyToOne
    private Edge startEdge;
    @ManyToOne
    private Edge endEdge;

    public Route(Edge startEdge, Edge endEdge){
        this.startEdge = startEdge;
        this.endEdge = endEdge;
        calculateRoute();
    }

    //Require by JPA
    public Route(){ }

    public long getId() {
        return id   ;
    }

//    public JSONArray getMaxLightRoute() {
//        return MaxLightRoute;
//    }
//
//    public JSONArray getMedLightRoute() {
//        return MedLightRoute;
//    }
//
//    public JSONArray getMinLightRoute() {
//        return MinLightRoute;
//    }

    private void calculateRoute(){

    }
}