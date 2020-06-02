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

import javax.swing.plaf.synth.SynthScrollBarUI;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

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


    //private Set<Edge> checkedStreets = new HashSet<>();

    /**
     * Http call to generate a route between the gives nodes with regard to
     * minimum required light weight
     *
     * @param startLat latitude of the start node
     * @param startLong longitude of the start node
     * @param endLat latitude of the end node
     * @param endLong longitude of the end node
     * @param lightLevel minimum required light weigh of edges we can use to generate the route
     * @return An array of nodes as JSON.
     */
    @GetMapping(path = "/get_route")
    public @ResponseBody JSONObject generateRoute(@RequestParam double startLat, @RequestParam double startLong, @RequestParam double endLat, @RequestParam double endLong, @RequestParam Integer lightLevel){
        Node startStreet = new Node(startLat, startLong);   //turn the coordinates into node objects
        Node endStreet = new Node(endLat, endLong);
        List<Node> nodeList = getClosestNode(startStreet, endStreet);  //find the closest node to the input parameters from our database and save them in a list, index 0=startNode, index 1=endNode
        if(nodeList == null) //make sure we found a nearby node
            return null;    //if no node found, exit
        startStreet = nodeList.get(0);
        endStreet = nodeList.get(1);
        JSONObject response = new JSONObject();
        JSONArray routeArray = new JSONArray();
        ArrayList<Node> tempSet = findRoute(startStreet, endStreet, lightLevel);
        routeArray.addAll(tempSet);
        response.appendField("route", routeArray);
        return response;
    }

    /**
     * This method performs a breadth first search and saves every step of each path and removes the ancestor list when all descendants are added.
     * A priority queue makes sure that the current path checked is always the shortest so that when it does find the final destination it has followed the shortest path
     *
     * @param currentStreet
     * @param endStreet
     * @param lightLevel
     * @return finalRoute
     */

    public ArrayList<Node> findRoute(Node currentStreet, Node endStreet, int lightLevel){

        for(Edge e : edgeRepository.getEdgesBy(endStreet.getLatitude(), endStreet.getLongitude())){
            System.err.println("Adjacent to end node: " + e.getOtherNode(endStreet).toString();
        }

        ArrayList<Node> finalRoute = new ArrayList<>(); //The final list of nodes that will be our path
        HashSet<Node> checkedNodes = new HashSet<>(); //The list of nodes we have already been on and therefore already found the shortest path to
        PriorityQueue<NodeContainer> frontier = new PriorityQueue<>(); //A queue of node lists which orders by how long in distance each list is
        ArrayList<Node> initList = new ArrayList<>(); //Initial list to start the queue with

        initList.add(currentStreet); //Initial list gets the first street
        frontier.add(new NodeContainer(initList)); //Queue gets a list with only the first street
        System.err.println("initList size: "+ initList.size());
        System.err.println("frontier size: "+ frontier.size());

        while(!frontier.isEmpty()) {  //While queue still has streets to check
            NodeContainer n = frontier.poll(); //Check the first list, i.e. the one that has the shortest traversal so far and remove from queue.
            Node latest = n.getNodes().get(n.getNodes().size() - 1); //Get the latest node in the list
            System.err.println("latest node: " + latest);
                for (Edge e : edgeRepository.getEdgesBy(latest.getLatitude(), latest.getLongitude())) {
                    Node foundNode = e.getOtherNode(latest);//Iterate through all paths the last in the list can take.
                    if(foundNode.equals(endStreet)){
                        finalRoute = n.getNodes();
                        finalRoute.add(foundNode);
                        System.err.println("Final route found!");
                        return finalRoute;
                    }
                    else if (e.getLightWeight() <= lightLevel && !checkedNodes.contains(foundNode)) {
                        checkedNodes.add(e.getOtherNode(latest));
                        System.err.println("CheckedNodes size: "+checkedNodes.size());
                        System.err.println("new edge found: "+ e.toString());
                        ArrayList<Node> path = new ArrayList<>(); //Create a new list
                        path.addAll(n.getNodes()); //Add all nodes from previous list
                        path.add(e.getOtherNode(latest)); //And add the new one we found
                        frontier.add(new NodeContainer(path)); //Add to queue
                    }

            }
        }

        return finalRoute; //If here, there is no path.
    }

    /*public ArrayList<Node> findParallelRoute(Node currentStreet, Node endStreet, int lightLevel){
        ArrayList<Node> finalRoute = new ArrayList<>(); //The final list of nodes that will be our path
        HashSet<Node> checkedNodes = new HashSet<>(); //The list of nodes we have already been on and therefore already found the shortest path to
        PriorityQueue<NodeContainer> frontier = new PriorityQueue<>(); //A queue of node lists which orders by how long in distance each list is
        ArrayList<Node> initList = new ArrayList<>(); //Initial list to start the queue with

        initList.add(currentStreet);     //Initial list gets the first street
        frontier.add(new NodeContainer(initList)); //Queue gets a list with only the first street

        while(!frontier.isEmpty()) {  //While queue still has streets to check
            NodeContainer n = frontier.peek(); //Check the first list, i.e. the one that has the shortest traversal so far.
            Node latest = n.getNodes().get(n.getNodes().size() - 1); //Get the latest node in the list
            if (latest.equals(endStreet)) {  //If the latest node in the list is the end street, we are done and can return the list.
                finalRoute = n.getNodes();
                return finalRoute;
            } else {
                checkedNodes.add(latest); //If not done, mark the latest in the list as checked
                List<Edge> edges = edgeRepository.getEdgesBy(latest.getLatitude(), latest.getLongitude());
                edges.parallelStream().forEach(e -> {
                    ArrayList<Node> path = new ArrayList<>(); //Create a new list
                    path.addAll(n.getNodes()); //Add all nodes from previous list
                    path.add(e.getOtherNode(latest)); //And add the new one we found
                    frontier.add(new NodeContainer(path)); //Add to queue
                });
            }
            frontier.remove(n); //After we've created paths for all variations of the latest, we can remove the ancestor
        }

        return finalRoute; //If here, there is no path.
    }*/

    private List<Node> getClosestNode(Node startNode, Node endNode){
        boolean nodesFound = false;
        boolean startNodeFound = false;
        boolean endNodeFound = false;
        double searchRadius = 0.0002;
        int loopsDone = 0;

        while(!nodesFound && loopsDone<5) {
            for (Node n : nodeRepository.findAll()) {
                if(n.getLatitude() < startNode.getLatitude()+searchRadius && n.getLatitude()> startNode.getLatitude()-searchRadius && n.getLongitude() < startNode.getLongitude()+searchRadius && n.getLongitude() > startNode.getLongitude()-searchRadius && !startNodeFound){
                    startNodeFound = true;
                    System.err.println(startNode.toString());
                    startNode = n;
                    System.err.println("StartNode Found!");
                    System.err.println(n.toString());
                }
                if(n.getLatitude() < endNode.getLatitude()+searchRadius && n.getLatitude()> endNode.getLatitude()-searchRadius && n.getLongitude() < endNode.getLongitude()+searchRadius && n.getLongitude() > endNode.getLongitude()-searchRadius && !endNodeFound){
                    endNodeFound = true;
                    System.err.println(endNode.toString());
                    endNode = n;
                    System.err.println("EndNode Found!");
                    System.err.println(n.toString());
                }
            }
            if(startNodeFound && endNodeFound)
                nodesFound = true;
            searchRadius += 2;
            loopsDone++;
            if(loopsDone==5&&!nodesFound)
                return null;
        }

        ArrayList<Node> nodeList = new ArrayList<Node>();
        nodeList.add(startNode);
        nodeList.add(endNode);
        return nodeList;
    }



    /*public ArrayList<Edge> findRoute(Node currentStreet, Node endStreet, int lightLevel){
        ArrayList<Edge> returnedRoute = new ArrayList<>();
        ArrayList<Edge> calculatedRoute = new ArrayList<>();
        ArrayList<Edge> edgeQueue = new ArrayList<>();
        for(Edge e : edgeRepository.getEdgesBy(currentStreet.getLatitude(), currentStreet.getLongitude())){
            if(!checkedStreets.contains(e)){
                edgeQueue.add(e);
            }
        }
        for(Edge e : edgeQueue) {
            checkedStreets.add(e);
                ArrayList<Edge> suggestion = new ArrayList<>();
                if (e.getOtherNode(currentStreet).equals(endStreet)) {
                    suggestion.add(e);
                    return suggestion;
                }else{
                    if (e.getLightWeight() <= lightLevel) {
                        ArrayList<Edge> theory = findRoute(e.getOtherNode(currentStreet), endStreet, lightLevel);
                        if (!theory.isEmpty()) {
                            suggestion.add(e);
                            suggestion.addAll(theory);
                            int distance = 0;
                            int finalDistance = 0;
                            for (Edge d : suggestion) {
                                distance += d.getDistanceWeight();
                            }
                            for (Edge d : calculatedRoute)
                                finalDistance += d.getDistanceWeight();
                            if (finalDistance == 0 || finalDistance > distance) {
                                calculatedRoute = suggestion;
                            }
                        }
                    }
                }
                returnedRoute = calculatedRoute;
            }
        return returnedRoute;
    }

    public ArrayList<Node> getNodeRoute(Node startNode, ArrayList<Edge> edges){
        ArrayList<Node> finalRoute = new ArrayList<>();
        finalRoute.add(startNode);
        for(Edge e : edges){
            if(finalRoute.contains(e.getNode1())){
                finalRoute.add(e.getNode2());
            }else{
                finalRoute.add(e.getNode1());
            }
        }
        return finalRoute;
    }*/

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


    class NodeContainer implements Comparable<NodeContainer>{
        ArrayList<Node> nodes;
        private Integer finalDistance;



        NodeContainer(ArrayList<Node> nodes){
            this.nodes = nodes;
            setFinalDistance();
        }

        private void setFinalDistance() {
            finalDistance = 0;
            Node previous = null;
            for(Node n : nodes){
                if(previous != null){
                    finalDistance += edgeRepository.getEdge(previous.getLatitude(), previous.getLongitude(), n.getLatitude(), n.getLongitude()).getDistanceWeight();
                }
                previous = n;
            }
        }

        public Integer getFinalDistance() {
            return finalDistance;
        }

        public void addNode(Node node){
            nodes.add(node);
            setFinalDistance();
        }

        public ArrayList<Node> getNodes() {
            return nodes;
        }

        @Override
        public boolean equals(Object o){
            if(o == this){
                return true;
            }
            if(!(o instanceof NodeContainer)){
                return false;
            }

            NodeContainer n = (NodeContainer) o;

            return nodes.equals(n.getNodes());
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(nodes);
        }

        @Override
        public int compareTo(NodeContainer nodeContainer){
            return finalDistance.compareTo(nodeContainer.getFinalDistance());
        }
    }





}

