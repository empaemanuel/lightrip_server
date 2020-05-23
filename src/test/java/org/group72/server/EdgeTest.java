package org.group72.server;

import org.group72.server.model.Edge;
import org.group72.server.model.LightNode;
import org.group72.server.model.Node;
import org.junit.jupiter.api.Test;
import org.group72.server.dao.LightRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class EdgeTest {

    @Test
    public void testCreateEdge(){
        Node node1 = new Node(59.313670, 18.090316);
        Node node2 = new Node(59.312827, 18.090316);
        Edge edge = new Edge(node1, node2);

        assertEquals(59.313670, edge.getNode1().getLatitude());
        assertEquals(18.090316, edge.getNode2().getLongitude());
    }

    @Test
    public void testGetNumberOfLightNodes(){
        Node node1 = new Node(59.313670, 18.090316);
        Node node2 = new Node(59.312827, 18.090316);
        Edge edge = new Edge(node1, node2);
        int numberOfLights = edge.getNumberOfLightNodes();
        assertEquals(50, numberOfLights);
    }


}
