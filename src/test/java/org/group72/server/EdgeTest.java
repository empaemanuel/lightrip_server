package org.group72.server;

import org.group72.server.model.Edge;
import org.group72.server.model.Node;
import org.junit.Before;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.*;

public class EdgeTest {


    Node n1 = new Node(58.1452, 18.1895);
    Node n2 = new Node(58.1452, 18.1897);
    Node n3 = new Node(58.1453, 18.1896);
    Node n4 = new Node(58.1455, 18.1897);

    Edge e1 = new Edge(n1, n2, 1, 1);
    Edge e2 = new Edge(n3, n4, 2, 1);
    Edge e3 = new Edge(n1, n2, 1, 1);


    @Test
    public void distanceBetweenEdge1NodesTrue() {
        assertEquals(12, e1.getDistance());
    }

    @Test
    public void getNode1True() {
        assertEquals(n1, e1.getNode1());
    }

    @Test
    public void getNode2True() {
        assertEquals(n2, e1.getNode2());
    }

    @Test
    public void getLightLevelTrue() {
        assertEquals(1, e1.getLightLevel());
    }

    @Test
    public void getIDTrue() {
        assertEquals(1, e1.getId());
    }

    @Test
    public void edgeEqualsEdgeFalse() {
        assertFalse(e1.equals(e2));
    }

    @Test
    public void edgeEqualsEdgeTrue() {
        assertTrue(e1.equals(e3));
    }

    @Test
    public void edgeCompareToEdgeFalse(){
        assertEquals(-1, e1.compareTo(e2));
    }

    @Test
    public void edgeCompareToEdgeZero() {
        assertEquals(0, e1.compareTo(e3));
    }

    @Test
    public void edgeCompareToEdgeTrue() {
        assertEquals(1, e2.compareTo(e1));
    }

}
