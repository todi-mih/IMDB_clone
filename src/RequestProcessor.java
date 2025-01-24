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

public class RequestProcessor extends JFrame {

    private JTextArea resultTextArea;

    public RequestProcessor() {
        setTitle("Request Processor");
        setSize(500, 400);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel mainPanel = new JPanel(new BorderLayout());
        mainPanel.setBorder(new EmptyBorder(10, 10, 10, 10));

        resultTextArea = new JTextArea();
        resultTextArea.setEditable(false);

        JScrollPane scrollPane = new JScrollPane(resultTextArea);
        mainPanel.add(scrollPane, BorderLayout.CENTER);

        add(mainPanel);

        setLocationRelativeTo(null);
    }

    public void printRequestsForResolver(String filePath, String username) {
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

    private void processEntryForResolver(String entry, String username) {
        String resolverUsername = extractValue(entry, "Resolver Username:");

        if (username.equals(resolverUsername)) {
            appendToResultTextArea("Matching Entry:\n" + entry);
        }
    }

    private String extractValue(String entry, String key) {
        String[] lines = entry.split("\n");
        for (String line : lines) {
            if (line.startsWith(key)) {
                return line.substring(key.length()).trim();
            }
        }
        return null;
    }

    private void appendToResultTextArea(String text) {
        resultTextArea.append(text + "\n");
    }

}
