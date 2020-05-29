package org.group72.server.model;

import javax.persistence.*;

/*
 * Table to keep track of connecting edges
 * edgeId is the edge that you want to find the connected edges to
 * edge is one of the edges that edgeID is connected to
 * Collection in table connections. 
 */

@Entity
public class Connections {
	@Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private int id;
	
	private int edge;
	
	private int edgeId;
	
	public Connections() {
		
	}
	
    public Connections(int id, int e){
        this.edgeId = id;
        this.edge = e;
    }
    
	public int getID() {
		return id;
	}
	
	public int getEdgeId() {
		return edgeId;
	}

}
