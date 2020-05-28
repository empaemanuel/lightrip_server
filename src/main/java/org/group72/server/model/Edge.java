package org.group72.server.model;

import java.util.ArrayList;
import java.util.Objects;

import javax.persistence.*;

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
    
    private ArrayList<Integer> connectedEdges;

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
     * Used to bring arraylist of edges from edge repo
     * @author Ida
     */
    
    public void connectEdges(int ID) {
    	connectedEdges.add(ID);
        
    }
    /**
     * Calculates the distance between the nodes
     * as a positive value in meters.
     * @return double distance in meters
     */
    /*
    private static int calculateDistace(double lat1, double lon1, double lat2, double lon2){
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
    */

    public int calculateDistance() {
        return (int) Math.round(calculateDistance(getNode1().getLatitude(), getNode1().getLongitude(), getNode2().getLatitude(), getNode2().getLongitude()));
    }
    
    private double calculateDistance(double lat1, double lng1, double lat2, double lng2) {
        double earthRadius = 6371000;
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));

        return earthRadius * c;
    }
   
    public Node getNode1() {
        return node1;
    }

    public Node getNode2() {
        return node2;
    }

    public ArrayList<Integer> getConnections() {
    	return connectedEdges;
    }
    
    public void setConnections() {
    	ArrayList<Integer> connectedEdges = new ArrayList<Integer>();
    	this.connectedEdges = connectedEdges;
    }

	public double getDistance() {
		return distance;
	}
	
	public int getID() {
		return id;
	}
	
	 @Override
	    public boolean equals(Object o) {
	        if (this == o) return true;
	        if (o == null || getClass() != o.getClass()) return false;
	        Edge e = (Edge) o;
	        return Double.compare(e.getNode1().getLatitude(), getNode1().getLatitude()) == 0 &&
	                Double.compare(e.getNode1().getLongitude(), getNode1().getLongitude()) == 0 &&
	                Double.compare(e.getNode2().getLatitude(), getNode2().getLatitude()) == 0 &&
	                Double.compare(e.getNode2().getLongitude(), getNode2().getLongitude()) == 0;
	    }

	    @Override
	    public int hashCode() {
	        return Objects.hash(getNode1().getLatitude(), getNode1().getLongitude(), getNode2().getLatitude(), getNode2().getLongitude());
	    }

}
