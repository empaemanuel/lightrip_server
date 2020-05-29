package org.group72.server.model;

import javax.persistence.*;

import java.util.ArrayList;
import java.util.List;

/**
 * Class for undirected edges. Each edge has two positions A and B which should have no internal order.
 * Should be able to calculate its own distance. Is connected by JPA and linked with hibernate to
 * the database described by src/resources/application.properties.
 * @author Emil.M
 */
@Entity
public class Edge implements Comparable<Edge>{

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;

    @ManyToOne
    private Node node1;

    @ManyToOne
    private Node node2;

    private int lightWeight;
    private int distanceWeight;
    /**
     * Distance in meters.
     */



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

    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2){
        double radius = 6378.137; // Radius of earth in KM
        double dLat = lat2 * Math.PI / 180 - lat1 * Math.PI / 180;
        double dLon = lon2 * Math.PI / 180 - lon1 * Math.PI / 180;
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(lat1 * Math.PI / 180) * Math.cos(lat2 * Math.PI / 180) *
                        Math.sin(dLon/2) * Math.sin(dLon/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double d = radius * c;
        return  d * 1000; // meters
    }


    //OLD
//    public int calculateDistance() {
//        return (int) Math.round(calculateDistance(getNode1().getLatitude(), getNode1().getLongitude(), getNode2().getLatitude(), getNode2().getLongitude()));
//    }

    //OLD VERSION
//    private double calculateDistance(double lat1, double lng1, double lat2, double lng2) {
//        double earthRadius = 6371000;
//        double dLat = Math.toRadians(lat2-lat1);
//        double dLng = Math.toRadians(lng2-lng1);
//        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
//                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
//                        Math.sin(dLng/2) * Math.sin(dLng/2);
//        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
//
//        return earthRadius * c;
//    }


    /**
     * TODO: Make sure it works correctly, specially the placement of the rectangles as well as the size of the rectangle, particularly when the distance to the last point is shorter than usual.
     * Figure out how many lightnodes exist along and around this edge
     * @return amount of lightnodes found
     */
    /*public int getNumberOfLightNodes(){


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
        LightController lightController = new LightController();
        int sumOfLights = 0;
        double tempMetersInLatLong = 0.0002;
        int metersPerRectangle = 10;
        ArrayList<Node> lightList = new ArrayList<Node>();
        int distance = calculateDistance(node1.getLatitude(), node1.getLongitude(), node2.getLatitude(), node2.getLongitude()); // get total length between the 2 nodes
        int rectanglesNeeded = (int)Math.ceil(distance/metersPerRectangle)+1; //1 rectangle for every 10 meters, not sure if this is good what if the distance is not dividable by 10?
        //next figure out where the place the rectangles - we need to figure out the path between the 2 nodes and place checkpoints every 10 meters
        double latDif = (node2.getLatitude()-node1.getLatitude())/rectanglesNeeded;    //total difference in latitude between the 2 nodes divided by amount of rectangles
        double longDif = (node2.getLongitude()-node1.getLongitude())/rectanglesNeeded;     ////total difference in longitude between the 2 nodes divided by amount of rectangles

        ArrayList<Node> pointList = new ArrayList<Node>((int)Math.ceil(rectanglesNeeded)+1); //create list to hold the "points" along the route from node1 to node2
        for(int i = 0; i<rectanglesNeeded+1; i++){  //forloop to add all the points lat/long values into our list
            pointList.add(new Node(node1.getLatitude()+(i*latDif), node1.getLongitude()+(i*longDif))); //for each point add the number point it is times
        }                                                                                                               //the distance from 1 point to another  //bit confusing but makes sense, i think

        for(int j = 0; j<pointList.size(); j++){  //now go through all points and get lights inside each rectangle
            Node node1 = new Node(pointList.get(j).getLatitude()-tempMetersInLatLong, pointList.get(j).getLongitude()-tempMetersInLatLong);//bottom left corner of rectangle
            Node node2 = new Node(pointList.get(j).getLatitude()+tempMetersInLatLong, pointList.get(j).getLongitude()+tempMetersInLatLong);//top right corner of rectangle
            sumOfLights += lightController.getSpecificLights(node1, node2).size();  //send sql request for lights between corners of rectangle, check how many lights found and add to the sum of lights found
        }


        return sumOfLights; // return total number of lights found
    }*/

    public ArrayList<Edge> getBorderingEdges() {
        return null;
        //return connectedEdges;
    }


    public int getDistanceWeight() {
        return distanceWeight; }

    public Node getNode1() {
        return node1;
    }

    public Node getNode2() {
        return node2;
    }

    public Node getOtherNode(Node node){
        if(node.equals(node1)){
            return node2;
        }
        return node1;
    }

    public int getLightWeight() {
        return lightWeight;
    }
    public int getId() {
        return id;
    }


    public boolean equals(Object o){
        if(o == this){
            return true;
        }
        if(!(o instanceof Edge)){
            return false;
        }

        Edge e = (Edge) o;

        return e.getId() == getId();
    }

    @Override
    public int compareTo(Edge o){
        if(getDistanceWeight() < getDistanceWeight()){
            return -1;
        }else if(getDistanceWeight() > o.getDistanceWeight()){
            return 1;
        }else{
            return 0;
        }
    }

    @Override
    public String toString(){
        return "ID:"+getId()+", \"node1_latitude\":\""+node1.getLatitude()+"\",\"node1_longitude\":\""+node1.getLongitude()+"\",\"node2_latitude\":\""+node2.getLatitude()+"\",\"node2.longitude\":\""+node2.getLongitude()+"\"";
    }

}
