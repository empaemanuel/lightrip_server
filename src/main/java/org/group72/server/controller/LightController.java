package org.group72.server.controller;

import net.minidev.json.JSONObject;
import org.group72.server.dao.LightRepository;
import org.group72.server.model.LightNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * This class defines the API for handling CRUD requests
 * of Lights.
 */

@Controller
@RequestMapping(path="/light")
public class LightController {
    @Autowired
    private LightRepository lightRepository;

    @GetMapping(path="/showSingle")
    public @ResponseBody JSONObject getLightNode(){
        JSONObject jo = new JSONObject();
        jo.appendField("light" , lightRepository.getLightNode(59.3080706,18.0896543));
        return jo;
    }

    @GetMapping(path="/allLights")
    public @ResponseBody Iterable<LightNode> getAllLights() {
        // This returns a JSON or XML with the users
        return lightRepository.findAll();
    }

    @GetMapping(path="/someLights")
    public @ResponseBody JSONObject getSomeLights(){
        List<LightNode> lightList = lightRepository.getLightNodes(59.311684, 18.090808, 59.312823, 18.091580);
        JSONObject jo = new JSONObject();
        jo.appendField("lights: ", lightList);
        return jo;
    }



}