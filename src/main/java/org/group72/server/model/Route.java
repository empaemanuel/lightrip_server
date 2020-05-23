package org.group72.server.model;

import javax.persistence.*;
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
    //Klagar på JSONarray. Tror det generellt inte är super bra att spara hela arrayer i en cell i en kolumn
    //men om vi vill göra det så behöver vi komma på någon annan lösning, kanske kovertera JSON arrayerna
    // till en strängar?? Emil
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

    //Krävs av JPA för att kunna kompilera
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