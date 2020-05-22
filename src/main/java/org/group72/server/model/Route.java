package org.group72.server.model;

import java.util.UUID;
/**
*
*
*
*@Author Magnus P.
* */

@Entity // This tells Hibernate to make a table out of this class
class Route{

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    private User user;
    private JSONArray MaxLightRoute;
    private JSONArray MedLightRoute;
    private JSONArray MinLightRoute;
    private Edge startEdge;
    private Edge endEdge;

    public Route(Edge startEdge, Edge endEdge){
        this.startEdge = startEdge;
        this.endEdge = endEdge;
        calculateRoute();
    }

    public long getId() {
        return id   ;
    }

    public JSONArray getMaxLightRoute() {
        return MaxLightRoute;
    }

    public JSONArray getMedLightRoute() {
        return MedLightRoute;
    }

    public JSONArray getMinLightRoute() {
        return MinLightRoute;
    }

    private void calculateRoute(){

    }
}