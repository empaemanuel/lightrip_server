package org.group72.server;

import org.group72.server.controller.RouteController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class RouteTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RouteController routeController;

    @Test
    public void testLowWeightRoute() throws Exception{
        mockMvc.perform(get("/get_route/get_route?startLat=59.3118827&startLong=18.0933015&endLat=59.3122317&endLong=18.0940654&lightLevel=5")
                .contentType("application/json"))
                .andExpect(status().isOk());
    }

}
