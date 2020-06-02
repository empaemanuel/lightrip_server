package org.group72.server.script;

import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.group72.server.dao.EdgeRepository;
import org.group72.server.dao.NodeRepository;
import org.group72.server.model.Edge;
import org.group72.server.model.LightNode;
import org.group72.server.model.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.aspectj.EnableSpringConfigured;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Array;
import java.util.ArrayList;

@Component
public class EdgeScript {

    @Autowired
    NodeRepository nodeRepository;

    @Autowired
    EdgeRepository edgeRepository;

    public ArrayList<LightNode> loadEdgesAndNodes(String filePath){
        ArrayList<LightNode> lightNodes = new ArrayList<>();
        try {
            BufferedReader reader = new BufferedReader(new FileReader(filePath));
            String line = reader.readLine();
            while(line != null){
                readEdgesAndNodesFromLine(line);
                line = reader.readLine(); //moves to next line
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return lightNodes;
    }

    /**
     * Extracts a list of geo positions from single JSON structure.
     */


    private void readEdgesAndNodesFromLine(String json) {
        JSONArray nodes = JsonPath.read(json, "$.geometry.coordinates");
        ArrayList<ArrayList<Double>> arr = (ArrayList) nodes.get(0);
        Node prev = null;

        System.out.println("NEW LINE==========================");
        for (int i = 0; i < arr.size(); i++) {
            ArrayList<Double> location = arr.get(i);

            double lng = location.get(0);
            double lat = location.get(1);
            Node node = new Node(lat, lng);

            nodeRepository.save(node);
            node = nodeRepository.getNode(node.getLatitude(), node.getLongitude());
            System.out.println("saving node: " + node.getLatitude() + ", " + node.getLongitude());
            System.out.println("prev = " + prev);
            if (prev != null) {
                System.out.println("here!");
                Edge edge = new Edge(prev, node);
                System.out.println("saving edge: " + edge);
                edgeRepository.save(edge);
            }
            prev = node;
            System.out.println("setting prev to: " + prev.getLatitude() + ", " + prev.getLongitude());
        }
    }
}
