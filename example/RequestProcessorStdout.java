import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;

import javax.swing.*;
import javax.swing.border.EmptyBorder;
import java.awt.*;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

public class RequestProcessorStdout {

    public static void printRequestsForResolver(String filePath, String username) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
            String line;
            StringBuilder currentEntry = new StringBuilder();

            while ((line = br.readLine()) != null) {
                if (line.equals("------")) {

                    processEntryForResolver(currentEntry.toString(), username);

                    currentEntry = new StringBuilder();
                } else {

                    currentEntry.append(line).append("\n");
                }
            }

            if (currentEntry.length() > 0) {
                processEntryForResolver(currentEntry.toString(), username);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void processEntryForResolver(String entry, String username) {

        String resolverUsername = extractValue(entry, "Resolver Username:");

        if (username.equals(resolverUsername)) {
            System.out.println("Matching Entry:\n" + entry);
        }
    }


    private static String extractValue(String entry, String key) {
        String[] lines = entry.split("\n");
        for (String line : lines) {
            if (line.startsWith(key)) {
                return line.substring(key.length()).trim();
            }
        }
        return null;
    }

}
