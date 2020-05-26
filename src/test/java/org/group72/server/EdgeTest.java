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


}
