package org.group72.server.model;


import javax.persistence.*;

@Entity
@IdClass(NodeKey.class)
public class LightNode {

    @Id
    private double latitude;

    @Id
    private double longitude;
    
    public LightNode() {
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public LightNode(double latitude, double longitude){
        this.latitude=latitude;
        this.longitude=longitude;
    }
}
