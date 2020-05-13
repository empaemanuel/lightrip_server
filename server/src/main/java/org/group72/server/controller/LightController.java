package org.group72.server.controller;

import org.group72.server.dao.LightRepository;
import org.group72.server.model.LightNode;
import org.group72.server.model.Geometry;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
//import java.io.InputStream;
import java.util.ArrayList;
//import com.fasterxml.jackson.core.type.TypeReference;
//import com.fasterxml.jackson.databind.ObjectMapper;


/**
 * This class defines the API for handling CRUD requests
 * of Lights.
 */
@Controller
@RequestMapping(path="/light")
public class LightController {
    @Autowired
    private LightRepository lightRepository;

    @PostMapping(path="/addLight") // Map ONLY POST Requests
    public @ResponseBody
    String addNewlight (@RequestParam Geometry geometry) {
        // @ResponseBody means the returned String is the response, not a view name
        // @RequestParam means it is a parameter from the GET or POST request

        LightNode l = new LightNode();
        l.setGeometry(geometry);
        lightRepository.save(l);
        return "Saved light";
    }

    @GetMapping(path="/allLights")
    public @ResponseBody Iterable<LightNode> getAllLights() {
        // This returns a JSON or XML with the users
        return lightRepository.findAll();
    }


    @GetMapping(path="/illuminate")
    public @ResponseBody String illuminate(){
    	enlighten();
//		ObjectMapper mapper = new ObjectMapper();
//		TypeReference<List<LightNode>> typeReference = new TypeReference<List<LightNode>>(){};
//		InputStream inputStream = TypeReference.class.getResourceAsStream("C:/Users/Idaso/Documents/vitabergbelysning.json");
//		try {
//			List<LightNode> lights = mapper.readValue(inputStream,typeReference);
//			lightRepository.saveAll(lights);
//		} catch (IOException e){
//			System.out.println("Unable to save users: " + e.getMessage());
//		}
		return "Saved lights!";
    }
    
    private void enlighten(){
        String filePath = "/Users/idaso/documents/vitabergbelysning.list";

        BufferedReader reader;
        try {
            reader = new BufferedReader(new FileReader(filePath));
            String line = reader.readLine();
            while(line != null){
                extractPositionsFromJson(line);
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

        for(int i=0; i < points.size(); i++) {
            JSONArray point = (JSONArray) points.get(i);
            double longitude = (double) point.get(0);
            double latitude = (double) point.get(1);
            
            ArrayList<Double> list = new ArrayList<Double>();
            list.add(longitude);
            list.add(latitude);
            Geometry g = new Geometry();
            g.setCoordinates(list);
            LightNode l = new LightNode();
            l.setGeometry(g);
            lightRepository.save(l);
        }
    }

    
}