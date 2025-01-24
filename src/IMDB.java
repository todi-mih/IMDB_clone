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
import java.util.Map;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.FileWriter;
import java.util.Iterator;
import org.json.simple.JSONValue;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import java.util.Collections;
import java.util.Comparator;

public class IMDB {
    private static final String ACCOUNTS_JSON_PATH = "../../../resources/input/accounts.json";


    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Welcome to IMDb!");

        System.out.print("Do you want to use the terminal (T) or graphical interface (G)? ");
        String interfaceChoice = scanner.nextLine();

        if (interfaceChoice.equalsIgnoreCase("T")) {
            boolean authenticated = false;
            String username = null;
            String userType = null;

            while (!authenticated) {
                System.out.print("Enter your email: ");
                String email = scanner.nextLine();

                System.out.print("Enter your password: ");
                String password = scanner.nextLine();

                authenticated = authenticateUser(email, password);

                if (authenticated) {
                    username = getUsernameByEmail(email);
                    userType = getUserTypeByEmail(email);
                    System.out.println("Welcome back, " + username + "! (Terminal)");
                } else {
                    System.out.println("Invalid email or password. Please try again.");
                }
            }

            displayOptions(userType,username);

        } else if (interfaceChoice.equalsIgnoreCase("G")) {
            SwingUtilities.invokeLater(() -> GUI.createAndShowGUI());
        } else {
            System.out.println("Invalid choice. Please choose either T or G.");
        }

        scanner.close();
    }

    private static void displayOptions(String userType,String username) {
        System.out.println("Available options:");

        switch (userType) {
            case "Regular":
                System.out.println("1) View production detail");
                System.out.println("2) View actors detail");
                System.out.println("3) View notifications");
                System.out.println("4) Search for actor/movie/series");
                System.out.println("5) Add/Delete actor/movie/series to/from favorites");
                System.out.println("6) Create/Remove a request");
                System.out.println("10) Add/Delete a review for a production");
                System.out.println("12) Logout");
           break;
            case "Contributor":
                System.out.println("1) View production detail");
                System.out.println("2) View actors detail");
                System.out.println("3) View notifications");
                System.out.println("4) Search for actor/movie/series");
                System.out.println("5) Add/Delete actor/movie/series to/from favorites");
                System.out.println("6) Create/Remove a request");
                System.out.println("7) Add/Delete a production/actor from the system");
                System.out.println("8) View and solve received requests");
                System.out.println("9) Update information about the product/actors");
                System.out.println("12) Logout");
           break;
            case "Admin":
                System.out.println("1) View production detail");
                System.out.println("2) View actors detail");
                System.out.println("3) View notifications");
                System.out.println("4) Search for actor/movie/series");
                System.out.println("5) Add/Delete actor/movie/series to/from favorites");
                System.out.println("7) Add/Delete a production/actor from the system");
                System.out.println("8) View and solve received requests");
                System.out.println("9) Update information about the product/actors");
                System.out.println("11)Add/delete a user from the system");
                System.out.println("12) Logout");
                break;
            default:
                System.out.println("Unknown user type.");
        }
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the option number (1-12): ");
        int option = scanner.nextInt();
        scanner.nextLine();

        if (option == 12) {
            System.out.println("Exiting IMDb. Goodbye!");
            System.exit(0);
        }

        handleOption(userType, option,username);

displayOptions(userType,username);

    scanner.close();
}

    private static void handleOption(String userType, int option,String username) {

        switch (option) {
            case 1:
                System.out.println("Executing option 1 for user type " + userType);
               String filePath = "../../../resources/input/production.json";

                try (FileReader reader = new FileReader(filePath)) {
                    JSONParser jsonParser = new JSONParser();
                    JSONArray productionsArray = (JSONArray) jsonParser.parse(reader);

                    List<JSONObject> productionsList = (List<JSONObject>) productionsArray;

                    Collections.sort(productionsList, new Comparator<JSONObject>() {
                        @Override
                        public int compare(JSONObject production1, JSONObject production2) {
                            double avgRating1 = Double.parseDouble(production1.get("averageRating").toString());
                            double avgRating2 = Double.parseDouble(production2.get("averageRating").toString());

                            return Double.compare(avgRating2, avgRating1);
                        }
                    });

                    for (JSONObject productionData : productionsList) {
                        System.out.println("Title: " + productionData.get("title"));
                        System.out.println("Type: " + productionData.get("type"));
                        System.out.println("Directors: " + productionData.get("directors"));
                        System.out.println("Actors: " + productionData.get("actors"));
                        System.out.println("Genres: " + productionData.get("genres"));
                        System.out.println("averageRating: " + productionData.get("averageRating"));

                        System.out.println("Ratings:");
                        JSONArray ratingsArray = (JSONArray) productionData.get("ratings");
                        for (Object ratingObj : ratingsArray) {
                            JSONObject ratingData = (JSONObject) ratingObj;
                            System.out.println("Username: " + ratingData.get("username"));
                            System.out.println("Rating: " + ratingData.get("rating"));
                            System.out.println("Comment: " + ratingData.get("comment"));
                            System.out.println("-----");
                        }

                        System.out.println("------------------------------");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

             break;
            case 2:
                System.out.println("Executing option 2 for user type " + userType);

                List<Actor> actors2 = Actor.readActorsFromJSON("../../../resources/input/actors.json");

                Collections.sort(actors2, (a1, a2) -> a1.getName().compareToIgnoreCase(a2.getName()));

                for (Actor actor : actors2) {
                    actor.displayInfo();
                }



                break;
            case 3:
                System.out.println("Executing option 3 for user type " + userType);

                RequestProcessorStdout requestProcessorInstance = new RequestProcessorStdout();
                requestProcessorInstance.printRequestsForResolver("requests.txt", username);

                break;
            case 4:
                System.out.println("Executing option 4 for user type " + userType);
                System.out.print("What would you like to search for: ");

                String title = Production.getFromUserInput();

                List<Actor> actors3 = Actor.readActorsFromJSON("../../../resources/input/actors.json");

                if (Actor.isActorInList(title, actors3)) {
                    Actor.displayActorInfoByNameStdout(title,actors3);
                }

                Movie movie = new Movie(title);
                Series series = new Series(title);

                Production production;

                String productionType = Production.getType(title);

                if ("Movie".equalsIgnoreCase(productionType)) {
                    production = new Movie(title);
                    movie.displayInfo();
                } else if ("Series".equalsIgnoreCase(productionType)) {
                    production = new Series(title);
                    series.displayInfo();


                } else {
                    System.out.println("Unknown production type");
                    return;
                }

                List<String> directors = production.getDirectors();
                List<String> actors = production.getActors();
                List<String> genres = production.getGenres();
                List<Rating> ratings = production.getRatings(title);
                String plot =production.getPlot(title);
                double avgrating = production.getAverageRating(title);

                System.out.println("Directors for " + title + ": " + directors);
                System.out.println("Actors for " + title + ": " + actors);
                System.out.println("Genres for " + title + ": " + genres);
                System.out.println("Ratings for " + title + ":");
                for (Rating rating : ratings) {
                    System.out.println("Username: " + rating.getUsername());
                    System.out.println("Rating: " + rating.getRating());
                    System.out.println("Comment: " + rating.getComment());
                    System.out.println("-----");
                }
                System.out.println("Plot for " + title + ": " + plot );
                System.out.println("AvgRating for " + title + ": " + avgrating );
                if ("Series".equalsIgnoreCase(productionType)) {
                    series.printEpisodes();
                }

                break;

            case 5:
                System.out.println("Executing option 5 for user type " + userType);
                System.out.println("What action :");
                String actionn = Production.getFromUserInput();
                System.out.println("Production or actors :");
                String poaa = Production.getFromUserInput();


                System.out.println("What name :");
                String namee = Production.getFromUserInput();

                updateFavs(username,"../../../resources/input/accounts.json",actionn,namee,poaa);

                break;
            case 6:
                if (userType.equals("Admin")) {
                    System.out.println("You can't use this option");
                    break;
                }

                System.out.println("Executing option 6 for user type " + userType);

                System.out.println("Type of request? ");
                String requestType = Production.getFromUserInput();
                LocalDateTime creationDate = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedCreationDate = creationDate.format(formatter);

                System.out.println("Resolver username? ");

                String resolverUsername = Production.getFromUserInput();
                System.out.print("Enter the title: ");
                String titleofReq = Production.getFromUserInput();

                System.out.print("Enter the description: ");
                String description = Production.getFromUserInput();
                RequestsHolder requestsHolder = new RequestsHolder();

                requestsHolder.addRequest(username, creationDate, resolverUsername, requestType,titleofReq,description);

                break;

            case 7:
                System.out.println("Executing option 7 for user type " + userType);

                System.out.println("What action :");
                String action = Production.getFromUserInput();
                System.out.println("Production or actors :");
                String poa = Production.getFromUserInput();


                System.out.println("What name :");
                String name = Production.getFromUserInput();

                updateContributions(username,"../../../resources/input/accounts.json",action,name,poa);

                break;
            case 8:
                System.out.println("Executing option 8 for user type " + userType);
//same as 3 ??
                break;
            case 9:
                System.out.println("Executing option 9 for user type " + userType);
                System.out.println("Name of prod to change: ");

               String titletochange = Production.getFromUserInput();
                System.out.println("Name of field to change: ");
             String fieldName = Production.getFromUserInput();
                System.out.println("Updated value: ");
           String updatedValue = Production.getFromUserInput();
                JsonUpdater.updateField(titletochange, fieldName, updatedValue, "../../../resources/input/production.json");

                break;
            case 10:
                System.out.println("Executing option 10 for user type " + userType);

                if (userType.equals("Regular")) {
                    System.out.println("Would you like to add or delete?");
                    System.out.println("Press 1 for adding and 2 for deleting: ");
                    int actionforrating = Production.getIntFromUserInput();
                    System.out.println("Whats the name of the production: ");
                    String nameofprodtoreviw = Production.getFromUserInput();
                    if (actionforrating == 2 ){
                        Rating ratingInst = new Rating(username,0,"null");
                        ratingInst.deleteRatingFromJson("../../../resources/input/production.json",nameofprodtoreviw,username);
                        break;
                    }


                    System.out.println("Enter the value of the production: ");
                    int ratingval = Production.getIntFromUserInput();

                    System.out.println("Enter the description of the production: ");

                    String ratingdes = Production.getFromUserInput();

                    Rating ratingInstance = new Rating(username, ratingval, ratingdes);

                    ratingInstance.addRatingToJson("../../../resources/input/actors.json", nameofprodtoreviw, username, ratingval, ratingdes);

                    break;
                }else {
                    System.out.println("You cant perform this command");
                    break;
                }


            case 11:
                System.out.println("Executing option 11 for user type " + userType);
                if (userType.equals("Admin")) {

                    break;
                }else {
                    System.out.println("You cant perform this command");
                    break;
                }


            default:
                System.out.println("Invalid option");
                break;
        }
    }




    public static boolean authenticateUser(String enteredEmail, String enteredPassword) {
        JSONParser parser = new JSONParser();

        try {
            JSONArray accountsArray = (JSONArray) parser.parse(new FileReader(ACCOUNTS_JSON_PATH));

            for (Object accountObj : accountsArray) {
                JSONObject account = (JSONObject) accountObj;
                JSONObject information = (JSONObject) account.get("information");
                JSONObject credentials = (JSONObject) information.get("credentials");

                String email = ((String) credentials.get("email")).trim();
                String password = (String) credentials.get("password");
                String username = (String) account.get("username");

                if (enteredEmail.trim().equalsIgnoreCase(email) && enteredPassword.equals(password)) {
                    return true;
                }
            }
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return false;
    }

public static  String getUserTypeByEmail(String enteredEmail){

    JSONParser parser = new JSONParser();

    try {
        JSONArray accountsArray = (JSONArray) parser.parse(new FileReader(ACCOUNTS_JSON_PATH));

        for (Object accountObj : accountsArray) {
            JSONObject account = (JSONObject) accountObj;
            JSONObject information = (JSONObject) account.get("information");
            JSONObject credentials = (JSONObject) information.get("credentials");

            String email = ((String) credentials.get("email")).trim();
            String usertype = (String) account.get("userType");

            if (enteredEmail.trim().equalsIgnoreCase(email)) {

                return usertype;
            }
        }

        System.out.println("Email not found: " + enteredEmail);

    } catch (IOException | ParseException e) {
        e.printStackTrace();
    }

    return "Error";
}
    public static String getUsernameByEmail(String enteredEmail) {
        JSONParser parser = new JSONParser();

        try {
            JSONArray accountsArray = (JSONArray) parser.parse(new FileReader(ACCOUNTS_JSON_PATH));

            for (Object accountObj : accountsArray) {
                JSONObject account = (JSONObject) accountObj;
                JSONObject information = (JSONObject) account.get("information");
                JSONObject credentials = (JSONObject) information.get("credentials");

                String email = ((String) credentials.get("email")).trim();
                String username = (String) account.get("username");

                if (enteredEmail.trim().equalsIgnoreCase(email)) {
                    System.out.println("Authentication successful!");
                    System.out.println("Welcome back, " + username + "!");
                    return username;
                }
            }

            System.out.println("Email not found: " + enteredEmail);

        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

        return "Error";
    }


    public static void getNot(String fileName, String usernameToMatch) throws IOException {
        try (BufferedReader reader = new BufferedReader(new FileReader(fileName))) {
            String line;
            boolean isInRequest = false;
            StringBuilder requestInfo = new StringBuilder();

            while ((line = reader.readLine()) != null) {
                if (line.startsWith("Resolver Username:")) {

                    String resolverUsername = line.substring("Resolver Username: ".length()).trim();
                    if (resolverUsername.equals(usernameToMatch)) {
                        isInRequest = true;
                    } else {
                        isInRequest = false;
                    }
                }

                if (isInRequest) {

                    requestInfo.append(line).append("\n");

                    if (line.equals("------")) {

                        System.out.println(requestInfo.toString());

                        requestInfo = new StringBuilder();
                        isInRequest = false;
                    }
                }
            }
        }
    }

    public static void updateContributions(String username, String jsonFileName, String action, String contributionName, String contributionType) {
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader(jsonFileName)) {
            JSONArray jsonArray = (JSONArray) jsonParser.parse(reader);

            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject userObject = (JSONObject) jsonArray.get(i);
                String currentUser = (String) userObject.get("username");

                if (currentUser.equals(username)) {

                    JSONArray contributions;
                    if ("actors".equalsIgnoreCase(contributionType)) {
                        contributions = (JSONArray) userObject.get("actorsContribution");
                    } else if ("productions".equalsIgnoreCase(contributionType)) {
                        contributions = (JSONArray) userObject.get("productionsContribution");
                    } else {
                        System.out.println("Invalid contribution type.");
                        return;
                    }


                    if ("add".equalsIgnoreCase(action)) {

                        if (!contributions.contains(contributionName)) {
                            contributions.add(contributionName);
                            System.out.println("Contribution added successfully.");
                        } else {
                            System.out.println("Contribution already exists.");
                        }
                    } else if ("delete".equalsIgnoreCase(action)) {

                        if (contributions.contains(contributionName)) {
                            contributions.remove(contributionName);
                            System.out.println("Contribution deleted successfully.");
                        } else {
                            System.out.println("Contribution does not exist.");
                        }
                    } else {
                        System.out.println("Invalid action.");
                    }


                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

                    try (FileWriter fileWriter = new FileWriter(jsonFileName)) {
                        objectMapper.writeValue(fileWriter, jsonArray);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    return;
                }
            }

            System.out.println("User not found.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void updateFavs(String username, String jsonFileName, String action, String contributionName, String contributionType) {
        JSONParser jsonParser = new JSONParser();

        try (FileReader reader = new FileReader(jsonFileName)) {
            JSONArray jsonArray = (JSONArray) jsonParser.parse(reader);

            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject userObject = (JSONObject) jsonArray.get(i);
                String currentUser = (String) userObject.get("username");

                if (currentUser.equals(username)) {
                    JSONArray contributions;
                    if ("actors".equalsIgnoreCase(contributionType)) {
                        contributions = (JSONArray) userObject.get("favoriteActors");
                    } else if ("productions".equalsIgnoreCase(contributionType)) {
                        contributions = (JSONArray) userObject.get("favoriteProductions");
                    } else {
                        System.out.println("Invalid contribution type.");
                        return;
                    }

                    if ("add".equalsIgnoreCase(action)) {

                        if (!contributions.contains(contributionName)) {
                            contributions.add(contributionName);
                            System.out.println("Favs added successfully.");
                        } else {
                            System.out.println("Favs already exists.");
                        }
                    } else if ("delete".equalsIgnoreCase(action)) {

                        if (contributions.contains(contributionName)) {
                            contributions.remove(contributionName);
                            System.out.println("Favs deleted successfully.");
                        } else {
                            System.out.println("Favs does not exist.");
                        }
                    } else {
                        System.out.println("Invalid action.");
                    }

                    ObjectMapper objectMapper = new ObjectMapper();
                    objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

                    try (FileWriter fileWriter = new FileWriter(jsonFileName)) {
                        objectMapper.writeValue(fileWriter, jsonArray);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    return;
                }
            }

            System.out.println("User not found.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
