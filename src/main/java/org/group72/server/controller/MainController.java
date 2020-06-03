package org.group72.server.controller;

import org.group72.server.model.Greeting;
import org.group72.server.model.LightNode;
import org.group72.server.script.EdgeScript;
import org.group72.server.script.LightScript;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicLong;

@Controller
public class MainController {

    private static final String template = "Greetings, %s!";
    private static final AtomicLong counter = new AtomicLong();

    @GetMapping(path="/hello")
    public @ResponseBody Greeting greeting(@RequestParam(value = "name", defaultValue = "World") String name) {
        return new Greeting(counter.incrementAndGet(), String.format(template, name));
    }

    @GetMapping(path="/")
    public @ResponseBody String homePage() {
        return "Greetings from lightrip server!";
    }


    @Autowired
    EdgeScript es;


    /**
     * Load all edges from file into database
     *
     */
    @RequestMapping(path="/loadEdges")
    public @ResponseBody String loadEdges(){
        //Se till att filformatet är .list och lägg till hela filvägen nedan.
        String filePath = "D:/AndroidFlutter/lightrip/lighttrip_server/src/main/resources/rutter.list";
        es.loadEdgesAndNodes(filePath);
        return "done!";
    }

    /**
     * Load all lightNodes from file into database
     *
     */
    @Autowired
    LightScript ls;
    @RequestMapping(path="/loadLights")
    public @ResponseBody String loadLights(){
        //Se till att filformatet är .list och lägg till hela filvägen nedan.
        String filePath = "D:/AndroidFlutter/lightrip/lighttrip_server/src/main/resources/belysning.list";
        ls.load(filePath);
        return "done!";
    }
}