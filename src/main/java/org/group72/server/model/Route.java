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
public class Route{

    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    /*private JSONArray MaxLightRoute;
    private JSONArray MedLightRoute;
    private JSONArray MinLightRoute;*/
    private static Set<Edge> checkedStreets;

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

    private Set<Edge> findRoute(Edge currentStreet, Edge endStreet, int lightLevel){
        Set<Edge> returnedRoute = new HashSet<>();
        PriorityQueue<Edge> pQueue = new PriorityQueue<>(currentStreet.getBorderingEdges());
        for(Edge e : pQueue){
            if(e.equals(endStreet)){
                returnedRoute.add(e);
                return returnedRoute;
            }else{
            if(e.getLightLevel() >= lightLevel && !checkedStreets.contains(e)) {
                 Set<Edge> theory = findRoute(e, endStreet, lightLevel);
                 if(theory == null)
                     return null;
                 if(theory.contains(endStreet) && (theory.size() < returnedRoute.size() || returnedRoute.isEmpty())) {
                     returnedRoute.add(e);
                     returnedRoute.addAll(theory);
                     return returnedRoute;
                 }
                }
            }
        }
        return null;
    }
}