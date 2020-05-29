package org.group72.server.model;


import javax.persistence.*;
import java.util.ArrayList;

@Entity
@IdClass(NodeKey.class)
public class Node {

    @Id
    private double latitude;

    @Id
    private double longitude;

    private int lightLevel;

    public Node() {
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public Node(double latitude, double longitude){
        this.latitude=latitude;
        this.longitude=longitude;
    }


    public ArrayList<Edge> getBorderingEdges() {
        return null;
        //return connectedEdges;
    }

    public int getLightLevel() {
        return lightLevel;
    }

    @Override
    public boolean equals(Object o) {

        if (o == this) {
            return true;
        }


        if (!(o instanceof Node)) {
            return false;
        }

        Node n = (Node) o;

        return n.getLatitude() == latitude && n.getLongitude() == longitude;
    }
}
