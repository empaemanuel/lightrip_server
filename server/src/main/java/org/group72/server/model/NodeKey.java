package org.group72.server.model;


import java.io.Serializable;
import java.util.Objects;

/**
 * This class is required by JPA for the use of a composed key in the Node table.
 * @author Emil M.
 */
public class NodeKey implements Serializable {
    private double latitude;
    private double longitude;

    //The default constructor is required by JPA and should not be removed
    public NodeKey() {
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        NodeKey nodeKey = (NodeKey) o;
        return Double.compare(nodeKey.latitude, latitude) == 0 &&
                Double.compare(nodeKey.longitude, longitude) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(latitude, longitude);
    }
}
