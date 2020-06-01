package org.group72.server.script;

import com.jayway.jsonpath.JsonPath;
import net.minidev.json.JSONArray;
import org.group72.server.model.LightNode;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

public class LightScript {

    public ArrayList<LightNode> load(){
        String filePath = "/Users/earth/Desktop/lightrip/lightrip_server/src/main/resources/200601belysning.list";
        BufferedReader reader;
        ArrayList<LightNode> lightNodes = new ArrayList<>();
        try {
            reader = new BufferedReader(new FileReader(filePath));
            String line = reader.readLine();
            while(line != null){
                LightNode ln = extractPositionsFromJson(line);
                lightNodes.add(ln);
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


    private LightNode extractPositionsFromJson(String json){
        JSONArray light = JsonPath.read(json, "$.geometry.coordinates");
        double longitude = (double) light.get(0);
        double latitude = (double) light.get(1);


        LightNode l = new LightNode(latitude, longitude);
        return l;
    }
}
