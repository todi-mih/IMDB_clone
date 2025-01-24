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

public abstract class Production implements Comparable<Production> {
    protected String title;
    protected List<String> directors;
    protected List<String> actors;
    protected List<String> genres;
    protected List<Rating> ratings;
    protected String plot;
    protected double averageRating;

    public Production(String title) {
        this.title = title;

    }
    /*public Production(String title, List<String> directors, List<String> actors, List<String> genres,
                      List<Rating> ratings, String plot, double averageRating) {
        this.title = title;
        this.directors = directors;
        this.actors = actors;
        this.genres = genres;
        this.ratings = ratings;
        this.plot = plot;
        this.averageRating = averageRating;
    }*/

    public void setTitle(String title) {
        this.title = title;
    }

    public static String getFromUserInput() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    public static int getIntFromUserInput() {
        Scanner scanner = new Scanner(System.in);
        while (true) {
            try {
                return scanner.nextInt();
            } catch (java.util.InputMismatchException e) {
                System.out.println("Invalid input. Please enter an integer.");
                scanner.nextLine();
            }
        }
    }
    public static String getType(String userTitle) {
        try {
            JSONParser parser = new JSONParser();
            JSONArray productions = (JSONArray) parser.parse(new FileReader("../../../resources/input/production.json"));

            for (Object obj : productions) {
                JSONObject production = (JSONObject) obj;
                String productionTitle = (String) production.get("title");

                if (productionTitle.equalsIgnoreCase(userTitle)) {
                    return (String) production.get("type");
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return null;
    }
    public List<String> getDirectors() {
        List<String> directors = new ArrayList<>();

        try {

            JSONParser parser = new JSONParser();
            JSONArray productions = (JSONArray) parser.parse(new FileReader("../../../resources/input/production.json"));


            for (Object obj : productions) {
                JSONObject production = (JSONObject) obj;
                String productionTitle = (String) production.get("title");


                if (productionTitle.equalsIgnoreCase(title)) {

                    JSONArray directorsArray = (JSONArray) production.get("directors");
                    for (Object directorObj : directorsArray) {
                        directors.add((String) directorObj);
                    }
                    break;
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return directors;
    }

    public List<String> getActors() {
        List<String> actors = new ArrayList<>();

        try {

            JSONParser parser = new JSONParser();
            JSONArray productions = (JSONArray) parser.parse(new FileReader("../../../resources/input/production.json"));


            for (Object obj : productions) {
                JSONObject production = (JSONObject) obj;
                String productionTitle = (String) production.get("title");

                if (productionTitle.equalsIgnoreCase(title)) {
                    JSONArray ActorsArray = (JSONArray) production.get("actors");
                    for (Object ActorObj : ActorsArray) {
                        actors.add((String) ActorObj);
                    }
                    break;
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return actors;
    }

    public List<String> getGenres() {
        List<String> genres = new ArrayList<>();

        try {

            JSONParser parser = new JSONParser();
            JSONArray productions = (JSONArray) parser.parse(new FileReader("../../../resources/input/production.json"));


            for (Object obj : productions) {
                JSONObject production = (JSONObject) obj;
                String productionTitle = (String) production.get("title");


                if (productionTitle.equalsIgnoreCase(title)) {

                    JSONArray GenresArray = (JSONArray) production.get("genres");
                    for (Object GenreObj : GenresArray) {
                        genres.add((String) GenreObj);
                    }
                    break;
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return genres;
    }

    public static List<Rating> getRatings(String userTitle) {
        List<Rating> ratings = new ArrayList<>();

        try {

            JSONParser parser = new JSONParser();
            JSONArray productions = (JSONArray) parser.parse(new FileReader("../../../resources/input/production.json"));


            for (Object obj : productions) {
                JSONObject production = (JSONObject) obj;
                String productionTitle = (String) production.get("title");


                if (productionTitle.equalsIgnoreCase(userTitle)) {

                    JSONArray ratingsArray = (JSONArray) production.get("ratings");
                    for (Object ratingObj : ratingsArray) {
                        JSONObject ratingJson = (JSONObject) ratingObj;
                        String username = (String) ratingJson.get("username");
                        int rating = ((Long) ratingJson.get("rating")).intValue();
                        String comment = (String) ratingJson.get("comment");

                        Rating ratingObj2 = new Rating(username, rating, comment);
                        ratings.add(ratingObj2);
                    }
                    break;
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return ratings;
    }

    public String getPlot(String userTitle) {
        String plot = null;

        try {
            JSONParser parser = new JSONParser();
            JSONArray productions = (JSONArray) parser.parse(new FileReader("../../../resources/input/production.json"));

            for (Object obj : productions) {
                JSONObject production = (JSONObject) obj;
                String productionTitle = (String) production.get("title");

                if (productionTitle.equalsIgnoreCase(userTitle)) {
                    plot = (String) production.get("plot");
                    break;
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return plot;
    }

    public double getAverageRating(String userTitle) {
        double averageRating = 0.0;

        try {
            JSONParser parser = new JSONParser();
            JSONArray productions = (JSONArray) parser.parse(new FileReader("../../../resources/input/production.json"));

            for (Object obj : productions) {
                JSONObject production = (JSONObject) obj;
                String productionTitle = (String) production.get("title");

                if (productionTitle.equalsIgnoreCase(userTitle)) {

                    averageRating = (Double) production.get("averageRating");
                    break;
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return averageRating;
    }

    public abstract void displayInfo();

    @Override
    public int compareTo(Production other) {
        return this.title.compareTo(other.title);
    }


}
