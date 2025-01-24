import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import javax.swing.*;
import java.awt.*;
import java.io.FileReader;
import java.util.ArrayList;
import java.util.List;

public class Actor {
    private String name;
    private List<Performance> performances;
    private String biography;

    public Actor(String name, List<Performance> performances, String biography) {
        this.name = name;
        this.performances = performances;
        this.biography = biography;
    }

    public String getName() {
        return name;
    }

    public List<Performance> getPerformances() {
        return performances;
    }

    public String getBiography() {
        return biography;
    }

    public void displayInfo() {
        System.out.println("Name: " + name);
        System.out.println("Biography: " + biography);
        System.out.println("Performances:");
        for (Performance performance : performances) {
            System.out.println("  Title: " + performance.getTitle());
            System.out.println("  Type: " + performance.getType());
        }
        System.out.println();
    }

    public static void displayActorInfoGUI(List<Actor> actors) {
        JFrame frame = new JFrame("Actors Information");
        frame.setSize(500, 500);
        frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        JPanel panel = new JPanel(new BorderLayout());

        JTextArea infoTextArea = new JTextArea();
        infoTextArea.setEditable(false);

        for (Actor actor : actors) {
            infoTextArea.append("Name: " + actor.getName() + "\n");
            infoTextArea.append("Biography: " + actor.getBiography() + "\n");
            infoTextArea.append("Performances:\n");
            for (Performance performance : actor.getPerformances()) {
                infoTextArea.append("  Title: " + performance.getTitle() + "\n");
                infoTextArea.append("  Type: " + performance.getType() + "\n");
            }
            infoTextArea.append("\n");
        }

        JScrollPane scrollPane = new JScrollPane(infoTextArea);
        panel.add(scrollPane, BorderLayout.CENTER);

        frame.add(panel);
        frame.setVisible(true);
    }

    public static class Performance {
        private String title;
        private String type;


        public Performance(String title, String type) {
            this.title = title;
            this.type = type;
        }

        public String getTitle() {
            return title;
        }

        public String getType() {
            return type;
        }
    }

    public static Actor createActorFromJSON(JSONObject actorData) {
        String name = (String) actorData.get("name");
        String biography = (String) actorData.get("biography");

        List<Performance> performances = new ArrayList<>();
        JSONArray performancesArray = (JSONArray) actorData.get("performances");
        for (Object performanceObj : performancesArray) {
            JSONObject performanceData = (JSONObject) performanceObj;
            String title = (String) performanceData.get("title");
            String type = (String) performanceData.get("type");
            performances.add(new Performance(title, type));
        }

        return new Actor(name, performances, biography);
    }



    public static List<Actor> readActorsFromJSON(String filePath) {
        List<Actor> actors = new ArrayList<>();

        JSONParser parser = new JSONParser();
        try (FileReader reader = new FileReader(filePath)) {
            JSONArray actorsArray = (JSONArray) parser.parse(reader);

            for (Object actorObj : actorsArray) {
                JSONObject actorData = (JSONObject) actorObj;
                Actor actor = createActorFromJSON(actorData);
                actors.add(actor);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        return actors;
    }


    public static void displayActorInfoByName(String actorName) {
        List<Actor> actors = readActorsFromJSON("../../../resources/input/actors.json");

        for (Actor actor : actors) {
            if (actor.getName().equalsIgnoreCase(actorName)) {
                JFrame frame = new JFrame("Actor Information");
                frame.setSize(500, 500);
                frame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                JPanel panel = new JPanel(new BorderLayout());

                JTextArea infoTextArea = new JTextArea();
                infoTextArea.setEditable(false);

                infoTextArea.append("Name: " + actor.getName() + "\n");
                infoTextArea.append("Biography: " + actor.getBiography() + "\n");
                infoTextArea.append("Performances:\n");
                for (Performance performance : actor.getPerformances()) {
                    infoTextArea.append("  Title: " + performance.getTitle() + "\n");
                    infoTextArea.append("  Type: " + performance.getType() + "\n");
                }

                JScrollPane scrollPane = new JScrollPane(infoTextArea);
                panel.add(scrollPane, BorderLayout.CENTER);

                frame.add(panel);
                frame.setVisible(true);
                return;
            }
        }

        System.out.println("Actor with name '" + actorName + "' not found.");
    }


    public static void displayActorInfoByNameStdout(String actorName, List<Actor> actors) {
        for (Actor actor : actors) {
            if (actor.getName().equalsIgnoreCase(actorName)) {
                System.out.println("Name: " + actor.getName());
                System.out.println("Biography: " + actor.getBiography());
                System.out.println("Performances:");
                for (Performance performance : actor.getPerformances()) {
                    System.out.println("  Title: " + performance.getTitle());
                    System.out.println("  Type: " + performance.getType());
                }
                System.out.println();
                return;
            }
        }
        System.out.println("Actor not found: " + actorName);
    }


    public static boolean isActorInList(String actorName, List<Actor> actors) {
        for (Actor actor : actors) {
            if (actor.getName().equalsIgnoreCase(actorName)) {
                return true;
            }
        }
        return false;
    }
}
