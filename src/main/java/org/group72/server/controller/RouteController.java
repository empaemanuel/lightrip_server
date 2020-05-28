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
import org.springframework.web.bind.annotation.*;

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
    
//    @GetMapping(path = "/routeGenerator")
//    public @ResponseBody JSONObject routeGenerator(@RequestParam Node node1, @RequestParam Node node2) {
//    	JSONObject response = new JSONObject();
//    	
//    	findRoute(node1, node2);
//    	
//    	return response;
//    }
//    
//    private void findRoute(Node n1, Node n2) {
//    	Edge e = findEdge(n1, n2);
//
//    }
    
//    private Edge findEdge(Node n1, Node n2) {
////    	if (edgeRepository.getEdge(n1.getLatitude(), n1.getLongitude(), n2.getLatitude(), n2.getLongitude()) != null) {
////    		return edgeRepository.getEdge(n1.getLatitude(), n1.getLongitude(), n2.getLatitude(), n2.getLongitude());
////    	} else {
//    		Edge edge = edgeRepository.getEdgeFromNode1(n1.getLatitude(), n1.getLongitude());
//    		for (Edge e : edge.getConnections()) {
//    			if (e.getNode2().equals(n2)) {
//    				return e;
//    			}
//    			else {
//    				/* Den bör kolla genom alla edges som kommer från repot
//    				 * sätta in den med lägst skillnad mot n2 i en array (? pga tre rutter)
//    				 * och sedan rekursivt fortsätta med detta tills den kommer till n2
//    				 * @author Ida
//    				 */
//    				int[] topThree = new int[3];
//    				if (topThree[0] == 0) { //TODO behöver gå genom alla tre
//    					if (e.calculateDistance(e.getNode2().getLatitude(), e.getNode2().getLongitude(), n2.getLatitude(), n2.getLongitude()) //
//    						< currentBest.calculateDistance()) { 
//    					
//    					//TODO: remake calculateDistance to take in two nodes so check can be done
//    					// to control e.getNode2s distance to n2. 
//    					}
//    				}
//    				topThree[0] = e.hashCode(); //TODO supposed to be ID, check this 
//    			}
//    		}
//    	}
// //   }
    

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
                		e.connectEdges(oldEdge.getID());
                	}else if(oldEdge.getNode2() == e.getNode2() && oldEdge.getNode1() != e.getNode1()) {
                		System.out.println(e + "  2");
                    	e.connectEdges(oldEdge.getID());
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
                Edge e = edgeRepository.getEdge(n.getLatitude(), n.getLongitude(), prev.getLatitude(), prev.getLongitude());
                e.setConnections();
                /*
                 * Om vi ska ta in en lista på alla connected edges borde vi ev ha en avsmalning först så den metoden inte
                 * behöver gå genom ALLA edges. 
                 * exists a findAllById - should change Edges ID to coordinates
                 */
                for (Edge oldEdge : edgeRepository.findAll()) {
                	if (oldEdge.getNode1().equals(e.getNode1()) && !(oldEdge.getNode2().equals(e.getNode2()))) {
                		e.connectEdges(oldEdge.getID());
                    	System.out.println(e.getID() + "     getconnects" + e.getConnections() + "    ID: " + oldEdge.getID());
                		edgeRepository.setEdgeConnectedEdges(e.getNode1().getLatitude(), e.getNode1().getLongitude(), e.getNode2().getLatitude(), e.getNode2().getLongitude(), e.getConnections());
                		System.out.println(e.getID() + "  " + e.getConnections()  + "  1");
                	}else if(oldEdge.getNode2().equals(e.getNode2()) && !(oldEdge.getNode1().equals(e.getNode1()))) {
                    	e.connectEdges(oldEdge.getID());
                    	System.out.println(e.getID() + "     getconnects" + e.getConnections() + "    ID: " + oldEdge.getID());
                    	edgeRepository.setEdgeConnectedEdges(e.getNode1().getLatitude(), e.getNode1().getLongitude(), e.getNode2().getLatitude(), e.getNode2().getLongitude(), e.getConnections());
                		System.out.println(e.getID() + "  " + e.getConnections()  + "  2");
                		}
                	}
                }
            	prev = n;
            }
        }
    }
