package org.group72.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.group72.server.model.Edge;
import org.group72.server.model.LightNode;
import org.group72.server.model.Node;
import org.junit.jupiter.api.Test;
import org.group72.server.dao.LightRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

public class EdgeTest {

    Node n1 = new Node(58.1452, 18.1895);
    Node n2 = new Node(58.1452, 18.1897);
    Node n3 = new Node(58.1453, 18.1896);
    Node n4 = new Node(58.1455, 18.1897);

    Edge e1 = new Edge(n1, n2, 1, 1);
    Edge e2 = new Edge(n3, n4, 2, 1);
    Edge e3 = new Edge(n1, n2, 1, 1);
  
    @Test
    public void testCreateEdge(){
        Node node1 = new Node(59.313670, 18.090316);
        Node node2 = new Node(59.312827, 18.090316);
        Edge edge = new Edge(node1, node2);

        assertEquals(59.313670, edge.getNode1().getLatitude());
        assertEquals(18.090316, edge.getNode2().getLongitude());
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
