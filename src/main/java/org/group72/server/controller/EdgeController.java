package org.group72.server.controller;

import org.group72.server.dao.EdgeRepository;
import org.group72.server.model.Edge;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path="/edge")
public class EdgeController {

    @Autowired
    private EdgeRepository edgeRepository;

    @GetMapping(path="/allEdges")
    public @ResponseBody Iterable<Edge> getAllEdges() {
        // This returns a JSON or XML with the users
        return edgeRepository.findAll();
    }
}
