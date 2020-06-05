package org.group72.server.controller;

import org.group72.server.dao.LightRepository;
import org.group72.server.model.LightNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


/**
 * This class defines the API for handling CRUD requests
 * of Lights.
 */

@Controller
@RequestMapping(path="/light")
public class LightController {
    @Autowired
    private LightRepository lightRepository;

    /**
     * Get all lights from our database
     *
     * @return all lightNodes
     */
    @GetMapping(path="/allLights")
    public @ResponseBody Iterable<LightNode> getAllLights() {
        // This returns a JSON or XML with the users
        return lightRepository.findAll();
    }
}