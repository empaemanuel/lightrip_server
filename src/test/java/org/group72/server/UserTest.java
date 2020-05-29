package org.group72.server;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.group72.server.controller.RouteController;
import org.group72.server.controller.UserController;
import org.group72.server.dao.EdgeRepository;
import org.group72.server.dao.UserRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(SpringExtension.class)
@SpringBootTest
@AutoConfigureMockMvc
public class UserTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private UserRepository userRepository;

    @MockBean
    private UserController userController;

    @Test
    public void testHttpCallToGetUser() throws Exception{
        mockMvc.perform(get("/user/specified?id=1")
                .contentType("application/json"))
                .andExpect(status().isOk());

        ArgumentCaptor<Integer> userCaptor = ArgumentCaptor.forClass(Integer.class);
        verify(userController, times(1)).getUser(userCaptor.capture());
        assertEquals(1, userCaptor.getAllValues().get(0));
    }



}
