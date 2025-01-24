import java.util.Map;
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
import java.util.HashMap;
import java.util.ArrayList;
import java.awt.GridLayout;

public class Series extends Production {
    private Integer releaseYear;
    private Integer numberOfSeasons;
    private Map<String, List<Episode>> episodesBySeason;
    public Series(String title) {

        super(title);
        this.episodesBySeason = Episode.getEpisodes(title);
    }

    @Override
    public void displayInfo() {
        Series name = new Series(title);
        System.out.println("Serial Title: " + title + " Duration: " + Movie.getDur(title) + " Release Year: " + Movie.getRY(title));
        //printEpisodes();

        }
    public void printEpisodes() {
        for (Map.Entry<String, List<Episode>> entry : episodesBySeason.entrySet()) {
            String seasonName = entry.getKey();
            List<Episode> episodes = entry.getValue();

            System.out.println("Season: " + seasonName);

            for (Episode episode : episodes) {
                System.out.println("Episode Name: " + episode.getEpisodeName());
                System.out.println("Duration: " + episode.getDuration());
                System.out.println();
            }
        }
    }
    public void printEpisodesGUI() {
        SwingUtilities.invokeLater(() -> {
            JFrame infoFrame = new JFrame("Production Information");
            infoFrame.setSize(500, 500);
            infoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JPanel infoPanel = new JPanel();
            infoPanel.setLayout(new GridLayout(0, 1));

            JScrollPane scrollPane = new JScrollPane(infoPanel);

            infoFrame.add(scrollPane);

            for (Map.Entry<String, List<Episode>> entry : episodesBySeason.entrySet()) {
                String seasonName = entry.getKey();
                List<Episode> episodes = entry.getValue();

                infoPanel.add(new JLabel("Season: " + seasonName));

                for (Episode episode : episodes) {
                    infoPanel.add(new JLabel("Episode Name: " + episode.getEpisodeName()));
                    infoPanel.add(new JLabel("Duration: " + episode.getDuration()));
                    infoPanel.add(new JLabel("-----"));
                }
            }

            infoFrame.setVisible(true);
        });
    }
}
