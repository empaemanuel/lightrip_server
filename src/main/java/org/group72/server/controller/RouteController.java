package org.group72.server.controller;

import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.group72.server.dao.EdgeRepository;
import org.group72.server.dao.NodeRepository;
import org.group72.server.model.Edge;
import org.group72.server.model.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * This class handles incoming requests regarding generation, viewing and storing routes
 * but should not handle any logic. Currently it handles a simple demo request so that
 * the dev team can discuss the structure of the data.
 * @author Emil M.
 */
@Controller
@RequestMapping(path="/route")
public class RouteController {
    @Autowired
    private EdgeRepository edgeRepository;
    @Autowired
    private NodeRepository nodeRepository;

    /**
     * A demo route manually created.
     * @return An array of edges as JSON.
     */
    @GetMapping(path = "/demo")
    public @ResponseBody JSONObject demo(){

        JSONObject response = new JSONObject();
        response.appendField("user" , "The ID token can be sent here");

        JSONArray route = new JSONArray();
        //59.3121417  18.0911303 -> 59.3123095 18.0912094
        Node n = nodeRepository.getNode(59.3121417,18.0911303);
        route.appendElement(n);

        n = nodeRepository.getNode(59.3123095, 18.0912094);
        route.appendElement(n);

        n = nodeRepository.getNode(59.3126612, 18.0913751);
        route.appendElement(n);

        n = nodeRepository.getNode(59.3126381, 18.0915173);
        route.appendElement(n);

        n = nodeRepository.getNode(59.312634, 18.0916134);
        route.appendElement(n);

        n = nodeRepository.getNode(59.3126498, 18.0916942);
        route.appendElement(n);

        response.appendField("route", route);
        response.appendField("distance", 130);

        return response;
    }

    @GetMapping(path="/connectEdges")
    public @ResponseBody String connectEdges() {
        // This returns a JSON or XML with the users
    	populateDatabase();
        return "arrays";
    }

    /**
     * SHOULD BE REMOVED OR HIDDEN BEFORE RELEASE.
     * Script used for populating the database with edges.
     * @return simple message to indicate when the script is done.
     */
//    @GetMapping(path="/populate")
//    public @ResponseBody String populate() {
//        populateDatabase();
//        return "done";
//    }

    private void populateDatabase(){
        String filePath = "/Users/idaso/documents/vitaberglinjer.list";

        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(filePath));
            String line = reader.readLine();
            while(line != null){
                extractPositionsFromJsonListVersion(line);
                line = reader.readLine(); //moves to next line
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Extracts a list of geo positions from single JSON structure.
     */


    private void extractPositionsFromJson(String json){
        JSONArray points = JsonPath.read(json, "$.geometry.coordinates");

        Node prev = null;
        for(int i=0; i < points.size(); i++) {
            JSONArray point = (JSONArray) points.get(i);
            double longitude = (double) point.get(0);
            double latitude = (double) point.get(1);

            Node n = new Node(latitude,longitude);
            nodeRepository.save(n);
            if(prev!=null){
                Edge e = new Edge(prev, n);
                
                /*
                 * Om vi ska ta in en lista på alla connected edges borde vi ev ha en avsmalning först så den metoden inte
                 * behöver gå genom ALLA edges. 
                 * exists a findAllById - should change Edges ID to coordinates
                 */
                for (Edge oldEdge : edgeRepository.findAll()) {
                	if (oldEdge.getNode1() == e.getNode1() && oldEdge.getNode2() != e.getNode2()) {
                		System.out.println(e + "  1");
                		e.connectEdges(oldEdge.getNode1(), oldEdge.getNode2());
                	}else if(oldEdge.getNode2() == e.getNode2() && oldEdge.getNode1() != e.getNode1()) {
                		System.out.println(e + "  2");
                    	e.connectEdges(oldEdge.getNode1(), oldEdge.getNode2());
                		}
                	}
        			System.out.println(e + "  3");
                	edgeRepository.save(e);
                }
            	prev = n;
            }
        }
    
    private void extractPositionsFromJsonListVersion(String json){
        JSONArray points = JsonPath.read(json, "$.geometry.coordinates");

        Node prev = null;
        for(int i=0; i < points.size(); i++) {
            JSONArray point = (JSONArray) points.get(i);
            double longitude = (double) point.get(0);
            double latitude = (double) point.get(1);

            Node n = new Node(latitude,longitude);
            if(prev!=null){
                Edge e = new Edge(prev, n);
                
                /*
                 * Om vi ska ta in en lista på alla connected edges borde vi ev ha en avsmalning först så den metoden inte
                 * behöver gå genom ALLA edges. 
                 * exists a findAllById - should change Edges ID to coordinates
                 */
                for (Edge oldEdge : edgeRepository.findAll()) {
                	if (oldEdge.getNode1().equals(e.getNode1()) && !(oldEdge.getNode2().equals(e.getNode2()))) {
                		System.out.println(e.getConnections()  + "  1");
                		e.connectEdges(oldEdge.getNode1(), oldEdge.getNode2());
                	}else if(oldEdge.getNode2().equals(e.getNode2()) && !(oldEdge.getNode1().equals(e.getNode1()))) {
                		System.out.println(e.getConnections()  + "  2");
                    	e.connectEdges(oldEdge.getNode1(), oldEdge.getNode2());
                		}
                	}
        			System.out.println(e.getConnections() + "  3");
                }
            	prev = n;
            }
        }
    }
