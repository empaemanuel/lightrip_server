package org.group72.server;

import org.group72.server.controller.LightController;
import org.group72.server.dao.LightRepository;
import org.group72.server.model.LightNode;
import org.group72.server.model.Node;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@ExtendWith(SpringExtension.class)
@DataJpaTest
public class LightTest {
//    @Autowired
//    private TestEntityManager entityManager;

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private LightRepository lightRepository;

    @Test
    void getLightNodeTest(){
        LightNode savedLN = entityManager.persist(new LightNode(11.1111, 22.2222));
        LightNode getFromDB = lightRepository.getLightNode(savedLN.getLatitude(), savedLN.getLongitude());
        assertEquals(savedLN, getFromDB);
    }

    @Test
    void getLightNodesBetweenTwoNodesTest() {
        LightNode savedLN = entityManager.persist(new LightNode(59.313000, 18.090316));
        LightNode getFromDB = lightRepository.getLightNodes2(
                59.312827, 18.090316,
            59.313670, 18.090316
        );
        assertEquals(savedLN, getFromDB);
    }


}
