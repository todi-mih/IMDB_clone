import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import java.io.*;
import java.util.Iterator;
import java.util.Scanner;

public class JsonUpdater {

    public static void updateField(String title, String fieldName, String updatedValue, String jsonFileName) {
        ObjectMapper objectMapper = new ObjectMapper();

        try {
            JsonNode rootNode = objectMapper.readTree(new File(jsonFileName));

            if (rootNode.isArray()) {
                ArrayNode jsonArray = (ArrayNode) rootNode;
                boolean updated = false;

                for (JsonNode entry : jsonArray) {
                    if (entry.has("title") && entry.get("title").asText().equalsIgnoreCase(title)) {
                        if (entry.has(fieldName)) {
                            if ("directors".equalsIgnoreCase(fieldName) || "actors".equalsIgnoreCase(fieldName) || "genres".equalsIgnoreCase(fieldName)) {
                                updateArrayField((ArrayNode) entry.get(fieldName), updatedValue);
                            } else {
                                ((ObjectNode) entry).put(fieldName, updatedValue);
                            }

                            updated = true;
                            System.out.println("Field updated successfully for the title: " + title);
                            break;
                        }
                    }
                }

                if (!updated) {
                    System.out.println("Title not found in the JSON array or field not found.");
                    return;
                }

                try (BufferedWriter writer = new BufferedWriter(new FileWriter(jsonFileName))) {
                    writer.write(objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(jsonArray));
                }

            } else {
                System.out.println("Invalid JSON structure.");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void updateArrayField(ArrayNode arrayNode, String updatedValue) {
        Iterator<JsonNode> iterator = arrayNode.elements();
        while (iterator.hasNext()) {
            JsonNode element = iterator.next();
            if (element.isTextual() && element.asText().equalsIgnoreCase(updatedValue)) {
                System.out.println("Value already exists in the array.");
                return;
            }
        }
        arrayNode.add(updatedValue);
    }

}
