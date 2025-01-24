import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.node.ObjectNode;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Iterator;


public class Rating {
    private String username;
    private int rating;
    private String comment;

    public Rating(String username, int rating, String comment) {
        this.username = username;
        this.rating = rating;
        this.comment = comment;
    }

    public String getUsername() {
        return username;
    }

    public int getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

   public static void addRatingToJson(String jsonFilePath, String title, String username, int rating, String comment) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            JsonNode rootNode = mapper.readTree(new File(jsonFilePath));

            JsonNode targetNode = null;
            for (JsonNode node : rootNode) {
                if (node.has("title") && node.get("title").asText().equals(title)) {
                    targetNode = node;
                    break;
                }
            }

            if (targetNode != null && targetNode.has("ratings")) {
                JsonNode ratingsNode = targetNode.get("ratings");
                List<Rating> ratings = new ArrayList<>();

                for (JsonNode ratingNode : ratingsNode) {
                    String existingUsername = ratingNode.get("username").asText();
                    int existingRating = ratingNode.get("rating").asInt();
                    String existingComment = ratingNode.get("comment").asText();

                    ratings.add(new Rating(existingUsername, existingRating, existingComment));
                }

                ratings.add(new Rating(username, rating, comment));

                ((ObjectNode) targetNode).set("ratings", mapper.valueToTree(ratings));

                double sum = ratings.stream().mapToDouble(Rating::getRating).sum();
                double averageRating = sum / ratings.size();
                ((ObjectNode) targetNode).put("averageRating", averageRating);

                mapper.writeValue(new File(jsonFilePath), rootNode);
                System.out.println("Rating added successfully.");

            } else {
                System.out.println("Movie or series not found.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public static void deleteRatingFromJson(String jsonFilePath, String title, String username) {
        ObjectMapper mapper = new ObjectMapper();
        mapper.enable(SerializationFeature.INDENT_OUTPUT);

        try {
            JsonNode rootNode = mapper.readTree(new File(jsonFilePath));

            JsonNode targetNode = null;
            for (JsonNode node : rootNode) {
                if (node.has("title") && node.get("title").asText().equals(title)) {
                    targetNode = node;
                    break;
                }
            }

            if (targetNode != null && targetNode.has("ratings")) {
                JsonNode ratingsNode = targetNode.get("ratings");
                List<Rating> ratings = new ArrayList<>();

                for (JsonNode ratingNode : ratingsNode) {
                    String existingUsername = ratingNode.get("username").asText();
                    int existingRating = ratingNode.get("rating").asInt();
                    String existingComment = ratingNode.get("comment").asText();

                    ratings.add(new Rating(existingUsername, existingRating, existingComment));
                }

                Iterator<Rating> iterator = ratings.iterator();
                while (iterator.hasNext()) {
                    Rating rating = iterator.next();
                    if (rating.getUsername().equals(username)) {
                        iterator.remove();
                        System.out.println("Review deleted successfully.");
                        break;
                    }
                }

                ((ObjectNode) targetNode).set("ratings", mapper.valueToTree(ratings));

                double sum = ratings.stream().mapToDouble(Rating::getRating).sum();
                double averageRating = ratings.isEmpty() ? 0 : sum / ratings.size();
                ((ObjectNode) targetNode).put("averageRating", averageRating);

                mapper.writeValue(new File(jsonFilePath), rootNode);

            } else {
                System.out.println("Movie or series not found.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
