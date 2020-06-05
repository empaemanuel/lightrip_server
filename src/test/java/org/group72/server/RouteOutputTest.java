package org.group72.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.group72.server.controller.RouteController;
import org.group72.server.dao.EdgeRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class RouteOutputTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private EdgeRepository edgeRepository;

    @InjectMocks
    private RouteController routeController;

    @Test
    public void testOutputSerialization1() throws Exception{
         mockMvc.perform(get("/get_route/get_route?startLat=59.3125267&startLong=18.0881813&endLat=59.3131301&endLong=18.0882606&lightLevel=9"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"latitude\":59.3125267,\"longitude\":18.0881813")))
                .andExpect(content().string(containsString("\"latitude\":59.3126081,\"longitude\":18.0881293")))
                 .andExpect(content().string(containsString("\"latitude\":59.3126381,\"longitude\":18.0880875")));
    }

    @Test
    public void testOutputSerialization2() throws Exception{
        mockMvc.perform(get("/get_route/get_route?startLat=59.312161&startLong=18.087463&endLat=59.311612&endLong=18.089634&lightLevel=10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("\"latitude\":59.3121651,\"longitude\":18.0876393")))
                .andExpect(content().string(containsString("\"latitude\":59.3120329,\"longitude\":18.0877661")))
                .andExpect(content().string(containsString("\"latitude\":59.3119836,\"longitude\":18.0878746")));
    }

}