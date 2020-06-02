package org.group72.server.script;

import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.group72.server.dao.LightRepository;
import org.group72.server.model.LightNode;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

@Service
public class LightScript {

    public void load(String filePath){
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

    @Autowired
    LightRepository lightRepository;

    private void extractPositionsFromJson(String json){
        JSONArray light = JsonPath.read(json, "$.geometry.coordinates");
        double longitude = (double) light.get(0);
        double latitude = (double) light.get(1);
        System.out.println("==" + latitude + ", " + longitude + ".");
        LightNode l = new LightNode(latitude, longitude);
        lightRepository.save(l);
    }
}
