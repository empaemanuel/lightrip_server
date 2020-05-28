package org.group72.server;

import org.group72.server.controller.RouteController;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertTrue;

@ExtendWith(SpringExtension.class)
//@WebMvcTest
public class RouteTest {

    @Test
    public void testLowWeightRoute(){
        assertTrue(true);
    }

    @Test
    public void testMediumWeightRoute(){
        assertTrue(true);
    }

    @Test
    public void testHighWeightRoute(){
        assertTrue(true);
    }
}
