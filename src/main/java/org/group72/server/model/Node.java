package org.group72.server.model;


import java.util.Objects;

import javax.persistence.*;

@Entity
@IdClass(NodeKey.class)
public class Node {

    @Id
    private double latitude;

    @Id
    private double longitude;

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

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude);
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

    @Override
    public String toString(){
        return "Latitude: "+ getLatitude() + " - Longitude: " + getLongitude();
    }
}
