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
    private Edge startStreet;
    private Edge endStreet;

    public Route(Edge startStreet, Edge endStreet){
        this.startStreet = startStreet;
        this.endStreet = endStreet;
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

    private Set<Edge> findRoute(Edge currentStreet, int lightLevel){
        private Set<Edge> returnedRoute;
        private PriorityQueue<Edge> pQueue = new PriorityQueue<Edge>(currentStreet.getBorderingStreets());
        pQueue.addAll()
        for(Edge e : pQueue){
            if(e.getLightLevel() >= lightLevel && !e.isChecked()) {
                if(e.equals(endStreet)){
                    returnedRoute.add(e);
                    return returnedRoute;
                }else{
                 private Set<Edge> theory = findRoute(e, lightLevel);
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