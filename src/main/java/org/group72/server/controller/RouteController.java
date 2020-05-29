package org.group72.server.controller;

import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import net.minidev.json.JSONObject;
import org.group72.server.dao.ConnectionsRepository;
import org.group72.server.dao.EdgeRepository;
import org.group72.server.dao.NodeRepository;
import org.group72.server.model.Connections;
import org.group72.server.model.Edge;
import org.group72.server.model.Node;
import org.group72.server.model.Route;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashSet;
import java.util.PriorityQueue;
import java.util.Set;

/**
 * This class handles incoming requests regarding generation, viewing and storing routes
 * but should not handle any logic. Currently it handles a simple demo request so that
 * the dev team can discuss the structure of the data.
 * @author Emil M.
 */
@Controller
@RequestMapping(path="/get_route")
public class RouteController {

    @Autowired
    private EdgeRepository edgeRepository;
    @Autowired
    private NodeRepository nodeRepository;
    @Autowired
    private ConnectionsRepository connectionsRepository;


    private Set<Edge> checkedStreets = new HashSet<>();

    /**
     * A demo route manually created.
     * @return An array of edges as JSON.
     */
    @GetMapping(path = "/get_route")
    public @ResponseBody JSONObject generateRoute(@RequestParam double startLat, @RequestParam double startLong, @RequestParam double endLat, @RequestParam double endLong, @RequestParam Integer lightLevel){
        Node startStreet = new Node(startLat, startLong);
        Node endStreet = new Node(endLat, endLong);
        System.err.println(startLat +" - "+ startLong +" - "+ endLat +" - "+ endLong +" - "+ lightLevel);
        JSONObject response = new JSONObject();
        JSONArray routeArray = new JSONArray();
        Set<Edge> temPSet = findRoute(startStreet, endStreet, lightLevel);
        routeArray.addAll(temPSet);
        response.appendField("route", routeArray);
        checkedStreets.clear();
        return response;
    }


    public Set<Edge> findRoute(Node currentStreet, Node endStreet, int lightLevel){
        Set<Edge> returnedRoute = new HashSet<>();
        Set<Edge> calculatedRoute = new HashSet<>();
        PriorityQueue<Edge> pQueue = new PriorityQueue<>();
        pQueue.addAll(edgeRepository.getEdgesBy(currentStreet.getLatitude(), currentStreet.getLongitude()));

        for(Edge e : pQueue){
            Set<Edge> suggestion = new HashSet<>();
            if(e.getOtherNode(currentStreet).equals(endStreet)){
                suggestion.add(e);
                return suggestion;
            }else{
                if(e.getLightWeight() <= lightLevel && !checkedStreets.contains(e)) {
                    checkedStreets.add(e);
                    Set<Edge> theory = findRoute(e.getOtherNode(currentStreet), endStreet, lightLevel);
                    if(!theory.isEmpty()) {
                        suggestion.add(e);
                        suggestion.addAll(theory);
                        int distance = 0;
                        int finalDistance = 0;
                        for(Edge d : suggestion){
                            distance += d.getDistanceWeight();
                        }
                        for(Edge d : calculatedRoute)
                            finalDistance += d.getDistanceWeight();
                        if(finalDistance == 0 || finalDistance > distance){
                            calculatedRoute = suggestion;
                        }
                    }
                }
            }
            returnedRoute = calculatedRoute;

        }
        return returnedRoute;
    }


    /*public @ResponseBody JSONObject demo(){
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
    }*/

    @GetMapping(path = "/showConnectionsDemo")
    public @ResponseBody JSONObject getConnections(@RequestParam(value = "edgeId", defaultValue = "4108") int edgeId) {
    	JSONObject jo = new JSONObject();
    	jo.appendField("connections", connectionsRepository.getConnections(edgeId));
    	return jo;
    }

//    @GetMapping(path="/connectEdges")
//    public @ResponseBody String connectEdges() {
//        // This returns a JSON or XML with the users
//    	populateDatabase();
//        return "arrays";
//    }

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

                /* Looking through Edges and connecting edges that share a node.
                 * Adding shared nodes to table
                 * @author Ida
                 */
                for (Edge oldEdge : edgeRepository.findAll()) {
                	if (oldEdge.getNode1().equals(e.getNode1()) && !(oldEdge.getNode2().equals(e.getNode2()))//
                			&& !(connectionsRepository.getConnections(e.getId()).contains(oldEdge.getId()))) {
                		Connections c = new Connections(e.getId(), oldEdge.getId());
                		connectionsRepository.save(c);
                		}
                	}
                }
            	prev = n;
            }
        }
    }
