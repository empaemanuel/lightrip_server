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

//    @Test
//    public void testCreateEdge(){
//        Node node1 = new Node(59.313670, 18.090316);
//        Node node2 = new Node(59.312827, 18.090316);
//        Edge edge = new Edge(node1, node2);
//
//        assertEquals(59.313670, edge.getNode1().getLatitude());
//        assertEquals(18.090316, edge.getNode2().getLongitude());
//    }

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

    /*@Test
    public void testFileRead(){
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayList<Map<String, String>> inputList;
        ArrayList<LightNode> lightList= new ArrayList<>();
        try {
            inputList = objectMapper.readValue(new FileInputStream("D:/AndroidFlutter/lightrip/lighttrip_server/src/main/resources/light_node.json"), ArrayList.class);
        }catch(Exception e){
            return;
        }
        for(int i = 0; i<inputList.size(); i++) {
            System.err.println(inputList.get(i).get("latitude")+"-" +inputList.get(i).get("longitude"));

            lightList.add(new LightNode(Double.parseDouble(inputList.get(i).get("latitude")), Double.parseDouble(inputList.get(i).get("longitude"))));
        }
        assertEquals(null, inputList);
    }*/


}
