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
     * A priority queue makes sure that the current path checked is always the shortest so that when it does find the final destination it has followed the shortest path.
     * If the shortest current path is not at the end point, it will create alternate paths for every edge the last node has, remove the current shortest route as it is checked, and then add
     * all the alternate paths to the queue. The queue sorts immediately and you repeat the process until either the route is found or the queue is empty. To ensure that repetition
     * or long alternate paths doesn't happen, the method keeps track of which nodes are checked, then updates it and excludes them in every iteration.
     *
     * @param currentStreet
     * @param endStreet
     * @param lightLevel
     * @return finalRoute
     *
     * @author Magnus P.
     */

    public ArrayList<Node> findRoute(Node currentStreet, Node endStreet, int lightLevel){
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
                    if (e.getLightWeight() <= lightLevel && !checkedNodes.contains(foundNode)) {
                        if(foundNode.equals(endStreet)){
                            finalRoute = n.getNodes();
                            finalRoute.add(foundNode);
                            System.err.println("Final route found!");
                            return finalRoute;
                        }
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

    /**
     *
     * @param startNode
     * @param endNode
     * @return
     */

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

    @GetMapping(path = "/showConnectionsDemo")
    public @ResponseBody JSONObject getConnections(@RequestParam(value = "edgeId", defaultValue = "4108") int edgeId) {
        JSONObject jo = new JSONObject();
        jo.appendField("connections", connectionsRepository.getConnections(edgeId));
        return jo;
    }

    /**
     * The class below stores an ArrayList of Nodes along with a integer for the distance the nodes traverse in total.
     * This assures that the list of nodes maintain their given order when we return the route. The class implements Comparable
     * so that the queue in the findRoute() method knows how to order the objects.
     *
     * @author Magnus P.
     */

    class NodeContainer implements Comparable<NodeContainer>{
        ArrayList<Node> nodes;
        private Integer finalDistance;

        NodeContainer(ArrayList<Node> nodes){
            this.nodes = nodes;
            setFinalDistance();
        }

        /**
         * Method to calculate traversed distance. It iterates through every node, keeps track of which was the previous node,
         * and if there is a previous node it gets the edge between those nodes from the edge repository and adds it's distance
         * weight to the final distance.
         *
         * @param
         * @return
         */
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

        /**
         * Override of the compareTo() method. Compares the total traversed distance between two nodeContainer objects.
         *
         * @param nodeContainer
         * @return
         */

        @Override
        public int compareTo(NodeContainer nodeContainer){
            return finalDistance.compareTo(nodeContainer.getFinalDistance());
        }
    }
}

