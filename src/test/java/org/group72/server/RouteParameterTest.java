package org.group72.server;

import org.group72.server.controller.RouteController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


@ExtendWith(SpringExtension.class)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@AutoConfigureMockMvc
public class RouteParameterTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private RouteController routeController;

    @Test
    public void testHttpCallToGetRoute1() throws Exception{
        mockMvc.perform(get("/get_route/get_route?startLat=59.3125267&startLong=18.0881813&endLat=59.3131301&endLong=18.0882606&lightLevel=9")
                .contentType("application/json"))
                .andExpect(status().isOk());

        ArgumentCaptor<Double> coordCaptor = ArgumentCaptor.forClass(Double.class);
        verify(routeController, times(1)).generateRoute(coordCaptor.capture(),coordCaptor.capture(),coordCaptor.capture(),coordCaptor.capture(), eq(9));
        assertEquals(59.3125267, coordCaptor.getAllValues().get(0));
        assertEquals(18.0881813, coordCaptor.getAllValues().get(1));
        assertEquals(59.3131301, coordCaptor.getAllValues().get(2));
        assertEquals(18.0882606, coordCaptor.getAllValues().get(3));
    }

    @Test
    public void testHttpCallToGetRoute2() throws Exception{
        mockMvc.perform(get("/get_route/get_route?startLat=59.312161&startLong=18.087463&endLat=59.311612&endLong=18.089634&lightLevel=10")
                .contentType("application/json"))
                .andExpect(status().isOk());

        ArgumentCaptor<Double> coordCaptor = ArgumentCaptor.forClass(Double.class);
        verify(routeController, times(1)).generateRoute(coordCaptor.capture(),coordCaptor.capture(),coordCaptor.capture(),coordCaptor.capture(), eq(10));
        assertEquals(59.312161, coordCaptor.getAllValues().get(0));
        assertEquals(18.087463, coordCaptor.getAllValues().get(1));
        assertEquals(59.311612, coordCaptor.getAllValues().get(2));
        assertEquals(18.089634, coordCaptor.getAllValues().get(3));
    }
}
