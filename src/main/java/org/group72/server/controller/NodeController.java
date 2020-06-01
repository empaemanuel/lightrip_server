package org.group72.server.controller;


import org.group72.server.dao.EdgeRepository;
import org.group72.server.dao.NodeRepository;
import org.group72.server.model.Edge;
import org.group72.server.model.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping(path="/node")
public class NodeController {

    @Autowired
    private NodeRepository nodeRepository;

    @GetMapping(path="/allNodes")
    public @ResponseBody
    Iterable<Node> getAllNodes() {
        // This returns a JSON or XML with the users
        return nodeRepository.findAll();
    }
}
