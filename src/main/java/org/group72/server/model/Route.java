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

    @Autowired
    private EdgeRepository edgeRepository;


    @Id
    @GeneratedValue(strategy=GenerationType.AUTO)
    private long id;
    /*private JSONArray MaxLightRoute;
    private JSONArray MedLightRoute;
    private JSONArray MinLightRoute;*/
    private static Set<Node> checkedStreets;

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

    public Set<Node> findRoute(Node currentStreet, Node endStreet, int lightLevel){
        Set<Node> returnedRoute = new HashSet<>();
        PriorityQueue<Node> pQueue = new PriorityQueue<Node>();
        edgeRepository.getEdgesBy(currentStreet.getLatitude(), currentStreet.getLongitude()).forEach(edge -> {
            pQueue.add(edge.getOtherNode(currentStreet));
        });
        for(Node e : pQueue){
            if(e.equals(endStreet)){
                returnedRoute.add(e);
                return returnedRoute;
            }else{
            if(e.getLightLevel() >= lightLevel && !checkedStreets.contains(e)) {
                 Set<Node> theory = findRoute(e, endStreet, lightLevel);
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