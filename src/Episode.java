import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;

import java.io.FileReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Episode {
    private String episodeName;
    private String duration;

    public Episode(String episodeName, String duration) {
        this.episodeName = episodeName;
        this.duration = duration;
    }

    public static Map<String, List<Episode>> getEpisodes(String seriesTitle) {
        Map<String, List<Episode>> episodesBySeason = new HashMap<>();
        JSONParser parser = new JSONParser();

        try (FileReader reader = new FileReader("../../../resources/input/production.json")) {
            JSONArray jsonArray = (JSONArray) parser.parse(reader);

            for (Object seriesObj : jsonArray) {
                JSONObject seriesData = (JSONObject) seriesObj;
                String title = (String) seriesData.get("title");

                if (title.equalsIgnoreCase(seriesTitle)) {
                    JSONObject seasonsObject = (JSONObject) seriesData.get("seasons");

                    for (Object seasonKey : seasonsObject.keySet()) {
                        String seasonName = (String) seasonKey;
                        JSONArray episodesArray = (JSONArray) seasonsObject.get(seasonName);

                        List<Episode> episodeList = new ArrayList<>();

                        for (Object episodeObj : episodesArray) {
                            JSONObject episodeData = (JSONObject) episodeObj;
                            String episodeName = (String) episodeData.get("episodeName");
                            String duration = (String) episodeData.get("duration");

                            Episode episode = new Episode(episodeName, duration);
                            episodeList.add(episode);
                        }

                        episodesBySeason.put(seasonName, episodeList);
                    }

                    return episodesBySeason;
                }
            }

            System.out.println("Series with title '" + seriesTitle + "' not found.");
        } catch (Exception e) {
            e.printStackTrace();
        }

        return episodesBySeason;
    }
    public String getEpisodeName() {
        return episodeName;
    }

    public String getDuration() {
        return duration;
    }


}
