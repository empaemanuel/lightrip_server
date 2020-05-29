package org.group72.server;

import org.group72.server.dao.EdgeRepository;
import org.group72.server.dao.NodeRepository;
import org.group72.server.model.Edge;
import org.group72.server.model.LightNode;
import org.group72.server.model.Node;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class EdgeTest {
    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private EdgeRepository edgeRepository;

    @Autowired
    private NodeRepository nodeRepository;

    @Test
    public void testCreateEdge(){
        Node node1 = new Node(59.313670, 18.090316);
        Node node2 = new Node(59.312827, 18.090316);
        Edge edge = new Edge(node1, node2);

        assertEquals(59.313670, edge.getNode1().getLatitude());
        assertEquals(18.090316, edge.getNode2().getLongitude());
    }

    @Test
    void getEdgeTest(){
        Node savedNodeA = entityManager.persist(new Node (11.1111, 22.2222));
        Node savedNodeB = entityManager.persist(new Node(33.3333, 44.4444));
        Node savedNodeC = entityManager.persist(new Node(55.5555, 66.6666));
        Edge savedEdgeA = entityManager.persist(new Edge(savedNodeA, savedNodeB));
        Edge savedEdgeB = entityManager.persist(new Edge(savedNodeB, savedNodeC));
        Edge savedEdgeC = entityManager.persist(new Edge(savedNodeC, savedNodeA));
        List<Edge> getFromDB = edgeRepository.getEdgesBy(savedNodeA.getLatitude(), savedNodeA.getLongitude());
        assertEquals(2, getFromDB.size());
        assertEquals(savedEdgeA, getFromDB.get(0));
        assertEquals(savedEdgeC, getFromDB.get(1));
    }

    @Test
    void calculateDistanceTest(){
        Node node1 = new Node(59.3119224, 18.0898548);
        Node node2 = new Node(59.3119493, 18.0886795);
        Edge e1 = new Edge(node1, node2);
        Node node3 = new Node(59.3125934, 18.0933891);
        Node node4 = new Node(59.3123099, 18.0934757);
        Edge e2 = new Edge(node3, node4);

        assertEquals(67, Math.ceil(e1.calculateDistance(e1.getNode1().getLatitude(), e1.getNode1().getLongitude(), e1.getNode2().getLatitude(), e1.getNode2().getLongitude())));
        assertEquals(32, Math.ceil(e2.calculateDistance(e2.getNode1().getLatitude(), e2.getNode1().getLongitude(), e2.getNode2().getLatitude(), e2.getNode2().getLongitude())));
    }




}
