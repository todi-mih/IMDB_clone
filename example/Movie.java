import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import java.util.Scanner;
import java.io.FileReader;
import java.io.IOException;
import java.util.List;

import java.util.ArrayList;
public class Movie extends Production {
    public Movie(String title) {
        super(title);
    }

    @Override
    public void displayInfo() {
        System.out.println("Movie Title: " + title + " Duration: " + getDur(title) + " Release Year: " + getRY(title));
    }

    public static String getDur(String userTitle) {
        try {
            JSONParser parser = new JSONParser();
            JSONArray productions = (JSONArray) parser.parse(new FileReader("../../../resources/input/production.json"));

            for (Object obj : productions) {
                JSONObject production = (JSONObject) obj;
                String productionTitle = (String) production.get("title");

                if (productionTitle.equalsIgnoreCase(userTitle)) {
                    return (String) production.get("duration");
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return null;
    }
    public static long getRY(String userTitle) {
        try {
            JSONParser parser = new JSONParser();
            JSONArray productions = (JSONArray) parser.parse(new FileReader("../../../resources/input/production.json"));

            for (Object obj : productions) {
                JSONObject production = (JSONObject) obj;
                String productionTitle = (String) production.get("title");

                if (productionTitle.equalsIgnoreCase(userTitle)) {
                    Object releaseYearObj = production.get("releaseYear");

                    if (releaseYearObj != null) {
                        return ((Number) releaseYearObj).longValue();
                    }
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return -1;
    }

}
