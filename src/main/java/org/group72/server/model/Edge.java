package org.group72.server.model;

import java.util.Objects;

import javax.persistence.*;


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

    public int getDistanceWeight() {
        return distanceWeight; }

    public Node getNode1() {
        return node1;
    }

    public Node getNode2() {
        return node2;
    }

    /**
     * Takes one of the nodes of the edge as a parameter and returns the corresponding node on the other end of the edge.
     * @param node
     * @return
     */

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

    @Override
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

    @Override
    public int hashCode() {
        return Objects.hash(getNode1().getLatitude(), getNode1().getLongitude(), getNode2().getLatitude(), getNode2().getLongitude());
    }
}
