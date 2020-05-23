package org.group72.server.model;

import javax.persistence.*;
import net.minidev.json.JSONObject;
import org.group72.server.dao.LightRepository;
import org.group72.server.model.LightNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.List;


/**
 * Class for undirected edges. Each edge has two positions A and B which should have no internal order.
 * Should be able to calculate its own distance. Is connected by JPA and linked with hibernate to
 * the database described by src/resources/application.properties.
 * @author Emil.M
 */
@Entity
public class Edge {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    private Node node1;

    @ManyToOne
    private Node node2;

    @Autowired
    private LightRepository lightRepository;

    /**
     * Distance in meters.
     */
    private int distance;

    //Required by JPA.
    public Edge() {
    }


    public Edge(Node n1, Node n2){
        this.node1 = n1;
        this.node2 = n2;
    }

    /**
     * Calculates the distance between the nodes
     * as a positive value in meters.
     * @return double distance in meters
     */
    private static int calculateDistance(double lat1, double lon1, double lat2, double lon2){
        double radius = 6378.137; // Radius of earth in KM
        double dLat = lat2 * Math.PI / 180 - lat1 * Math.PI / 180;
        double dLon = lon2 * Math.PI / 180 - lon1 * Math.PI / 180;
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = radius * c;
        return (int) d * 1000; // meters
    }

    /**
     * TODO: implement this method  (Max)
     * Figure out how many lightnodes exist along this edge (between the 2 nodes)
     * @return amount of lightnodes found
     */
    public int getNumberOfLightNodes(){
        //figure out what area to search for nodes in, what coords, how far out do we look?
        //probably get light nodes with sql query (WHERE LAT<(TEMPCOORD) & LONG<(TEMPCOORD)) something like this
        //                                  ____________________________
        //                                  |                           | <- find the coords of this corner
        //                                  |           o(point1)        |
        //                                  |                           |
        //                                  |                           |
        //                                  |                           |
        //                                  |                           |
        //                                  |                           |
        //                                  |                           |
        //                                  |                           |
        //                                  |                           |
        //                                  |                           |
        //                                  |            o(point2)       |
        //                                  |                           |
        //find the coords of this corner -> -----------------------------
        //next sql query to get all lightnodes with coords between the 2 corners lat values & long values
        //return amount of lightnodes found

        //List<LightNode> nodeList = lightRepository.getLightNodes(59.312630, 18.086975, 59.313380, 18.087276);
        //return nodeList.size();
        int sumOfLights = 0;
        double tempMetersInLatLong = 0.0002;
        int metersPerRectangle = 10;
        ArrayList<Node> lightList = new ArrayList<Node>();
        int distance = calculateDistance(node1.getLatitude(), node1.getLongitude(), node2.getLatitude(), node2.getLongitude()); // get length
        int rectanglesNeeded = (int)Math.ceil(distance/metersPerRectangle)+1; //1 rectangle for every 10 meters, not sure if this is good
        //next figure out where the place the rectangles - we need to figure out the path between the 2 nodes and place checkpoints every 10 meters
        double latDif = node2.getLatitude()-node1.getLatitude();
        double longDif = node2.getLongitude()-node1.getLongitude();

        ArrayList<Node> pointList = new ArrayList<Node>((int)Math.ceil(rectanglesNeeded)+1);
        for(int i = 0; i<rectanglesNeeded+1; i++){
            pointList.add(new Node(node1.getLatitude()+(i*latDif), node1.getLongitude()+(i*longDif)));
        }

        for(int j = 0; j<pointList.size(); j++){
            sumOfLights += lightRepository.getLightNodes(pointList.get(j).getLatitude()- tempMetersInLatLong, pointList.get(j).getLongitude()- tempMetersInLatLong, pointList.get(j).getLatitude()+ tempMetersInLatLong, pointList.get(j).getLongitude()+ tempMetersInLatLong).size();
        }


        return sumOfLights;
    }

    public Node getNode1() {
        return node1;
    }

    public Node getNode2() {
        return node2;
    }
}
