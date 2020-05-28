package org.group72.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.group72.server.controller.RouteController;
import org.group72.server.dao.EdgeRepository;
import org.group72.server.model.Edge;
import org.group72.server.model.Node;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RouteTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EdgeRepository edgeRepository;

    @MockBean
    private RouteController routeController;

    @Test
    public void testHttpCallToGetRoute() throws Exception{
        mockMvc.perform(get("/get_route/get_route?startLat=59.3132931&startLong=18.0869669&endLat=59.3131301&endLong=18.0882606&lightLevel=5")
                .contentType("application/json"))
                .andExpect(status().isOk());

        ArgumentCaptor<Double> coordCaptor = ArgumentCaptor.forClass(Double.class);
        verify(routeController, times(1)).generateRoute(coordCaptor.capture(),coordCaptor.capture(),coordCaptor.capture(),coordCaptor.capture(), eq(5));
        assertEquals(59.3132931, coordCaptor.getAllValues().get(0));
        assertEquals(18.0869669, coordCaptor.getAllValues().get(1));
        assertEquals(59.3131301, coordCaptor.getAllValues().get(2));
        assertEquals(18.0882606, coordCaptor.getAllValues().get(3));
    }

    @Test
    public void testOutputSerialization() throws Exception{    //IF GET ROUTE STARTS WORKING BUT IT STARTS COMPLAINING HERE IT IS BECAUSE THIS TEST IS WRITTEN TO EXPECT NO EDGE TO BE FOUND, ADD EDGES EXPECTED TO BE FOUND!
        MvcResult mvcResult = mockMvc.perform(get("/get_route/get_route?startLat=59.3132931&startLong=18.0869669&endLat=59.3131301&endLong=18.0882606&lightLevel=5")
                .contentType("application/json"))
                .andExpect(status().isOk())
                .andReturn();

        String expectedResponseBody = ""; //manually add edges expected to be found
        String actualResponseBody = mvcResult.getResponse().getContentAsString();

        assertEquals(expectedResponseBody, actualResponseBody.replaceAll("\\s+","")); //objectMapper.writeValueAsString(expected)
    }

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
