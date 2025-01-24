import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;


public class RequestsHolder {
    private static List<Request> requests;
    private static final String FILE_PATH = "requests.txt";

    public RequestsHolder() {
        this.requests = new ArrayList<>();
    }
    public static void addRequest(Request request) {
        requests.add(request);
        saveRequestsToFile(requests);
    }
    public void printRequests() {
        for (Request request : requests) {
            System.out.println("Type: " + request.getType());
            System.out.println("Creation Date: " + request.getCreationDate());
            System.out.println("Title: " + request.getTitle());
            System.out.println("Description: " + request.getDescription());
            System.out.println("Requester Username: " + request.getRequesterUsername());
            System.out.println("Resolver Username: " + request.getResolverUsername());
            System.out.println("------");
        }
    }

    public void addRequest(String requesterUsername, LocalDateTime creationDate, String resolverUsername, String requestType,String title,String description) {
        Scanner scanner = new Scanner(System.in);

        RequestType type = getTypeFromString(requestType);


        Request newRequest = new Request(type, creationDate, title, description, requesterUsername, resolverUsername);
        requests.add(newRequest);

        saveRequestsToFile(requests);
        saveRequestsToFileJson(requests);
    }
    private static List<Request> loadRequestsFromFile() {
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(FILE_PATH))) {
            List<Request> requests = (List<Request>) ois.readObject();
            return requests;
        } catch (IOException | ClassNotFoundException e) {
            System.out.println("Error loading requests from file. Creating a new list.");
            return new ArrayList<>();
        }
    }

    private static void saveRequestsToFile(List<Request> requests) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(FILE_PATH, true))) {
            for (Request request : requests) {
                writer.write("Type: " + request.getType());
                writer.newLine();
                writer.write("Creation Date: " + request.getCreationDate());
                writer.newLine();
                writer.write("Title: " + request.getTitle());
                writer.newLine();
                writer.write("Description: " + request.getDescription());
                writer.newLine();
                writer.write("Requester Username: " + request.getRequesterUsername());
                writer.newLine();
                writer.write("Resolver Username: " + request.getResolverUsername());
                writer.newLine();
                writer.write("------");
                writer.newLine();
            }
            System.out.println("Data appended to file successfully.");
        } catch (IOException e) {
            System.out.println("Error saving requests to file.");
            e.printStackTrace();
        }
    }


    private static void saveRequestsToFileJson(List<Request> requests) {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("../../../resources/input/requests.json", true))) {
            writer.write("[");
            writer.newLine();

            for (int i = 0; i < requests.size(); i++) {
                Request request = requests.get(i);
                writer.write("  {");
                writer.newLine();
                writer.write("    \"type\": \"" + request.getType() + "\",");
                writer.newLine();
                writer.write("    \"createdDate\": \"" + request.getCreationDate() + "\",");
                writer.newLine();
                writer.write("    \"title\": \"" + request.getTitle() + "\",");
                writer.newLine();
                writer.write("    \"description\": \"" + request.getDescription() + "\",");
                writer.newLine();
                writer.write("    \"requesterUsername\": \"" + request.getRequesterUsername() + "\",");
                writer.newLine();
                writer.write("    \"resolverUsername\": \"" + request.getResolverUsername() + "\"");
                writer.newLine();
                writer.write("  }");

                if (i < requests.size() - 1) {
                    writer.write(",");
                }

                writer.newLine();
            }

            writer.write("]");
            writer.newLine();

            System.out.println("Data appended to file successfully.");
        } catch (IOException e) {
            System.out.println("Error saving requests to file.");
            e.printStackTrace();
        }
    }

    private static RequestType getTypeFromString(String requestType) {
        try {
            return RequestType.valueOf(requestType.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("Invalid request type. Using default type OTHERS.");
            return RequestType.OTHERS;
        }
    }

    public enum RequestType {
        DELETE_ACCOUNT,
        ACTOR_ISSUE,
        MOVIE_ISSUE,
        OTHERS
    }

    public static class Request {
        private RequestType type;
        private LocalDateTime creationDate;
        private String title;
        private String description;
        private String requesterUsername;
        private String resolverUsername;

        public Request(RequestType type, LocalDateTime creationDate, String title, String description, String requesterUsername, String resolverUsername) {
            this.type = type;
            this.creationDate = LocalDateTime.now();
            this.title = title;
            this.description = description;
            this.requesterUsername = requesterUsername;
            this.resolverUsername = resolverUsername;
        }



        public LocalDateTime getCreationDate() {
            return creationDate;
        }

        public RequestType getType() {
            return type;
        }

        public String getTitle() {
            return title;
        }

        public String getDescription() {
            return description;
        }

        public String getRequesterUsername() {
            return requesterUsername;
        }

        public String getResolverUsername() {
            return resolverUsername;
        }
    }
}
