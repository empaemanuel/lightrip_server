package org.group72.server.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import net.minidev.json.JSONObject;
import org.group72.server.dao.EdgeRepository;
import org.group72.server.model.Edge;
import org.group72.server.model.LightNode;
import org.group72.server.model.Node;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Controller
@RequestMapping(path="/edge")
public class EdgeController {

    @Autowired
    private EdgeRepository edgeRepository;  //repo handling edges


    /**
     * Finds and returns all edges from our database
     *
     * @return All edges from database
     */
    @GetMapping(path="/allEdges")
    public @ResponseBody Iterable<Edge> getAllEdges() {
        // This returns a JSON or XML with the users
        return edgeRepository.findAll();
    }

    /**
     * Iterate through all edges in database to calculate and update each ones distanceWeight
     *
     * @return message to know the task was successful
     */
    @RequestMapping(path="/updateEdgeDistance")
    public @ResponseBody String updateEdgeDistance(){
        int distanceWeight;
        for(Edge e: edgeRepository.findAll()){
            distanceWeight = getDistanceWeight(e);
            edgeRepository.setEdgeDistanceWeight(e.getNode1().getLatitude(), e.getNode1().getLongitude(), e.getNode2().getLatitude(), e.getNode2().getLongitude(), distanceWeight);
        }

        return "Distance weight updated, run forrest run";
    }

    /**
     * Find and return all edges from database below a certain lightWeight
     *
     * @param lightWeight retrieve all edges with a lightWeight value below this value
     * @return All edges below a certain lightWeight
     */
    @RequestMapping(path="/getByLight")
    public @ResponseBody JSONObject getEdgesByLight(@RequestParam int lightWeight){
        List<Edge> edgeList = edgeRepository.getEdgesBelowLightWeight(lightWeight);
        JSONObject jo = new JSONObject();
        jo.appendField("Edges: ", edgeList);
        return jo;
    }

    /**
     * Iterate through all edges in database to calculate and update each ones lightWeight using json file containing all lightNodes
     *
     * @return message to know the task was successful
     */
    @RequestMapping(path="/updateEdgeLights")
    public @ResponseBody String updateEdgeLights(){
        ObjectMapper objectMapper = new ObjectMapper();
        ArrayList<Map<String, String>> inputList;
        ArrayList<LightNode> lightList= new ArrayList<>();
        try {
            inputList = objectMapper.readValue(new FileInputStream("D:/AndroidFlutter/lightrip/lighttrip_server/src/main/resources/light_node.json"), ArrayList.class);
        }catch(Exception e){
            return e.getMessage();
        }
        for(int i = 0; i<inputList.size(); i++) {
            lightList.add(new LightNode(Double.parseDouble(inputList.get(i).get("latitude")), Double.parseDouble(inputList.get(i).get("longitude"))));
        }
        int lightWeight = 0;
        for (Edge e: edgeRepository.findAll()) {
            lightWeight = getLightWeight(e, lightList);
            edgeRepository.setEdgeLightWeight(e.getNode1().getLatitude(), e.getNode1().getLongitude(), e.getNode2().getLatitude(), e.getNode2().getLongitude(), lightWeight);
        }


        return "Edges updated with light! Let there be light!";
    }

    /**
     * Get the distance from an edge and scale it down to make it more manageable before returning it
     *
     * @return the distanceWeight of the edge
     */
    private int getDistanceWeight(Edge e){
        double distance = e.calculateDistance(e.getNode1().getLatitude(), e.getNode1().getLongitude(), e.getNode2().getLatitude(), e.getNode2().getLongitude());
        return (int)Math.ceil(distance/5);
    }


    /**
     * Calculates the lightWeight for a given node.
     * First find the coordinates for both nodes making up the edge, then
     * find the distance between the nodes. After that decide how many "rectangles"
     * is needed based off the distance. Next find the coordinates of each rectangle needed
     * between the 2 nodes. After that iterate through each point and find coordinates
     * to "draw" a rectangle around it with a given width and height. Using this rectangle
     * we can now find all lightNodes existing inside each of these rectangles and save the
     * amount found. Lastly before returning turn the number of lightNodes into an integer 1-10
     * using mathy math, lower number means higher density of lightNodes around the edge
     *
     * @param e The edge which we calculate the lightWeight for
     * @param lightList list containing all LightNodes
     * @return the calculated lightWeight
     */
    private int getLightWeight(Edge e, List<LightNode> lightList){
        int sumOfLights = 0;
        double tempMetersInLatLong = 0.0003;
        double metersPerRectangle = 10;
        double distance = e.calculateDistance(e.getNode1().getLatitude(), e.getNode1().getLongitude(), e.getNode2().getLatitude(), e.getNode2().getLongitude()); // get total length between the 2 nodes
        double rectanglesNeeded = (distance/metersPerRectangle)+1; //1 rectangle for every 10 meters, not sure if this is good what if the distance is not dividable by 10?
        double sizeOfLastRectangle = rectanglesNeeded - Math.floor(rectanglesNeeded);
        //next figure out where the place the rectangles - we need to figure out the path between the 2 nodes and place checkpoints every 10 meters
        double latDif = (e.getNode2().getLatitude()-e.getNode1().getLatitude())/rectanglesNeeded;    //total difference in latitude between the 2 nodes divided by amount of rectangles
        double longDif = (e.getNode2().getLongitude()-e.getNode1().getLongitude())/rectanglesNeeded;     ////total difference in longitude between the 2 nodes divided by amount of rectangles

        ArrayList<Node> pointList = new ArrayList<Node>((int)Math.ceil(rectanglesNeeded)+1); //create list to hold the "points" along the route from node1 to node2
        for(int i = 0; i<rectanglesNeeded+1; i++){  //forloop to add all the points lat/long values into our list
            pointList.add(new Node(e.getNode1().getLatitude()+(i*latDif), e.getNode1().getLongitude()+(i*longDif))); //for each point add the number point it is times
        }                                                                                                               //the distance from 1 point to another  //bit confusing but makes sense, i think

        for(int j = 0; j<pointList.size(); j++){  //now go through all points and get lights inside each rectangle
            Node node1;
            Node node2;
            if(pointList.size()-1 == j)//we at last elemento?
            {
                node1 = new Node(pointList.get(j).getLatitude() - (tempMetersInLatLong*sizeOfLastRectangle), pointList.get(j).getLongitude() - (tempMetersInLatLong*sizeOfLastRectangle));//bottom left corner of rectangle
                node2 = new Node(pointList.get(j).getLatitude() + (tempMetersInLatLong*sizeOfLastRectangle), pointList.get(j).getLongitude() + (tempMetersInLatLong*sizeOfLastRectangle));//top right corner of rectangle
            }else {
                node1 = new Node(pointList.get(j).getLatitude() - tempMetersInLatLong, pointList.get(j).getLongitude() - tempMetersInLatLong);//bottom left corner of rectangle
                node2 = new Node(pointList.get(j).getLatitude() + tempMetersInLatLong, pointList.get(j).getLongitude() + tempMetersInLatLong);//top right corner of rectangle
            }
            for(LightNode l : lightList){
                if(l.getLatitude()>node1.getLatitude() && l.getLatitude()<node2.getLatitude() && l.getLongitude()> node1.getLongitude() && l.getLongitude()<node2.getLongitude()){
                    sumOfLights++;
                }
            }
        }
        int returnWeight = Math.abs((int)Math.round(Math.log10(((Math.pow(sumOfLights, 2)/2)/rectanglesNeeded)+1)*5)-10); //complicated mathy things happening, dont worry about it, unless it breaks then we fucked (log10(x) x->infinity ~=2, 2*5 = 10 => 10 - 10 = 0 => maximum amount of light, 0 lights = x atleast 1 => log10(1) = 0, 0-10 = -10, abs(-10) = 10 = minimun amount of light
        if(returnWeight == 0) // no 0's allowed
            returnWeight++;
        return returnWeight;
    }
}
