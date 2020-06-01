package org.group72.server;

import org.group72.server.dao.EdgeRepository;
import org.group72.server.model.Edge;
import org.group72.server.model.Node;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class EdgeControllerTest {

    @Autowired
    private EdgeRepository edgeRepository;

    @Test
    public void testGetEdges(){
        List<Edge> edgeList1 = edgeRepository.getEdgesBy(59.3111944,18.088632);
        List<Edge> edgeList2 = edgeRepository.getEdgesBy( 59.311604, 18.09461);
        assertEquals(3, edgeList1.size());
        assertEquals(4, edgeList2.size());
    }

    @Test
    public void testNodeExists(){
        Node node = new Node(59.3127258, 18.0937475); //intersection that exists

        List<Edge> edgeList = edgeRepository.getEdgesBy(node.getLatitude(), node.getLongitude());
        assertNotEquals(0, edgeList.size());
    }
}
