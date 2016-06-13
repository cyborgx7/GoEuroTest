/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package goeurotest;

import java.io.Console;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URL;
import org.apache.commons.io.IOUtils;

/**
 *
 * @author leonard
 */
public class GoEuroTest {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        
        Console console = System.console();
        
        if (args.length != 1) {
            if (console != null) console.writer().println("Usage: java -jar GoEuroTest.jar <CITY_NAME>");
            return;
        }
        
        URL url;
        
        try {
            url = new URL("http://api.goeuro.com/api/v2/position/suggest/en/" + args[0]);
        } catch (MalformedURLException ex) {
            if (console != null) console.writer().println("Malformed URL. Make sure the first parameter is the name of the city.");
            return;
        }
        
        String json;
        
        try {
            json = IOUtils.toString(url);
        } catch (IOException ex) {
            if (console != null) console.writer().println("Unable to contact the GoEuro API");
            return;
        }
        
        JsonParser parser = new JsonParser();
   
        try {
            JsonArray cities = parser.parse(json).getAsJsonArray();
            
            if (cities.size() == 0) {
                if (console != null) console.writer().println("No results found");
                return;
            }
            
            for (int i = 0; i < cities.size(); i++) {
                JsonObject city = cities.get(i).getAsJsonObject();
                String id = city.get("_id").getAsString();
                String name = city.get("name").getAsString();
                String type = city.get("type").getAsString();
                JsonObject geo_position = city.get("geo_position").getAsJsonObject();
                String latitude = geo_position.get("latitude").getAsString();
                String longitude = geo_position.get("longitude").getAsString();
                String entry = id + "," + name + "," + type + "," + latitude + "," + longitude;
                System.out.println(entry);

            }
        } catch (Exception e) {
            if (console != null) console.writer().println("Malformed API response.");
        }
    }

}
