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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Node node = (Node) o;
        return Double.compare(node.latitude, latitude) == 0 &&
                Double.compare(node.longitude, longitude) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude);
    }
}
