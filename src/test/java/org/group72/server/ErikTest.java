package org.group72.server;

import org.group72.server.model.Edge;
import org.group72.server.model.Node;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class ErikTest {

    Node Node1 = new Node(59.18389, 18.05175);
    Node Node2 = new Node(59.18389, 18.05174);
    Edge Edge1 = new Edge(Node1, Node2);


    Edge Edge2 = new Edge(Node1, Node1);




    @Test
        public void testLongLat() {
        assertEquals(1, Edge1.calculateDistance());
//        assertNotEquals(2, Edge1.calculateDistanceInMeters());
        System.out.println(Edge1.calculateDistance());
//        System.out.println(Edge1.calculateDistanceInMeters());
//        System.out.println(Edge1.calculateDistanceRounded());
        System.out.println(Edge2.calculateDistance());
//        System.out.println(Edge2.calculateDistanceInMeters());
//        System.out.println(Edge2.calculateDistanceRounded());
}

}
