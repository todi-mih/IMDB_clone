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
import java.awt.GridLayout;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.Dimension;
import java.util.ArrayList;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


public class GUI {
    public static void createAndShowGUI() {
        JFrame frame = new JFrame("IMDb Login");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        placeLoginComponents(panel, frame);
        frame.add(panel);

        frame.setSize(400, 300);
        frame.setLocationRelativeTo(null);
        frame.setVisible(true);
    }

    public static void placeLoginComponents(JPanel panel, JFrame frame) {
        panel.setLayout(null);

        JLabel emailLabel = new JLabel("Email:");
        emailLabel.setBounds(10, 20, 80, 25);
        panel.add(emailLabel);

        JTextField emailText = new JTextField(20);
        emailText.setBounds(100, 20, 165, 25);
        panel.add(emailText);

        JLabel passwordLabel = new JLabel("Password:");
        passwordLabel.setBounds(10, 50, 80, 25);
        panel.add(passwordLabel);

        JPasswordField passwordText = new JPasswordField(20);
        passwordText.setBounds(100, 50, 165, 25);
        panel.add(passwordText);

        JButton loginButton = new JButton("Login");
        loginButton.setBounds(10, 80, 80, 25);
        panel.add(loginButton);

        JLabel resultLabel = new JLabel("");
        resultLabel.setBounds(10, 110, 300, 25);
        panel.add(resultLabel);

        loginButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String enteredEmail = emailText.getText();
                String enteredPassword = new String(passwordText.getPassword());

                boolean authenticated = IMDB.authenticateUser(enteredEmail, enteredPassword);

                if (authenticated) {
                    String username = IMDB.getUsernameByEmail(enteredEmail);
                    String userType = IMDB.getUserTypeByEmail(enteredEmail);
                    resultLabel.setText("Welcome back, " + username + "!");
                    createAndShowOptionsGUI(frame, userType,username);
                } else {
                    resultLabel.setText("Invalid email or password. Please try again.");
                }
            }
        });
    }

    public static void createAndShowOptionsGUI(JFrame previousFrame, String userType,String username) {
        JFrame optionsFrame = new JFrame("IMDb Options");
        optionsFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        JPanel panel = new JPanel();
        placeOptionsComponents(panel, optionsFrame, userType,username);
        optionsFrame.add(panel);

        optionsFrame.setSize(400, 300);
        optionsFrame.setLocationRelativeTo(null);
        optionsFrame.setVisible(true);

        previousFrame.dispose();
    }

    public static void placeOptionsComponents(JPanel panel, JFrame optionsFrame, String userType,String username) {
        panel.setLayout(null);

        displayOptions(userType, panel, optionsFrame,username);

    }

    public static void displayOptions(String userType, JPanel panel, JFrame optionsFrame,String username) {
        JLabel optionsLabel = new JLabel("Available options:");
        optionsLabel.setBounds(10, 20, 300, 25);
        panel.add(optionsLabel);

        switch (userType) {
            case "Regular":
                addOptionLabel(panel, "1) View production detail", 50, optionsFrame, username);
                addOptionLabel(panel, "2) View actors detail", 80, optionsFrame, username);
                addOptionLabel(panel, "3) View notifications", 110, optionsFrame, username);
                addOptionLabel(panel, "4) Search for actor/movie/series", 140, optionsFrame, username);
                addOptionLabel(panel, "5) Add/Delete actor/movie/series to/from favorites", 170, optionsFrame, username);
                addOptionLabel(panel, "6) Create/Remove a request", 200, optionsFrame, username);
                addOptionLabel(panel, "10) Add/Delete a review for a production", 230, optionsFrame, username);
                addOptionLabel(panel, "12) Logout", 320, optionsFrame, username);
                break;

            case "Contributor":
                addOptionLabel(panel, "1) View production detail", 50, optionsFrame, username);
                addOptionLabel(panel, "2) View actors detail", 80, optionsFrame, username);
                addOptionLabel(panel, "3) View notifications", 110, optionsFrame, username);
                addOptionLabel(panel, "4) Search for actor/movie/series", 140, optionsFrame, username);
                addOptionLabel(panel, "5) Add/Delete actor/movie/series to/from favorites", 170, optionsFrame, username);
                addOptionLabel(panel, "6) Create/Remove a request", 200, optionsFrame, username);
                addOptionLabel(panel, "7) Add/Delete a production/actor from the system", 230, optionsFrame, username);
                addOptionLabel(panel, "8) View and solve received requests", 260, optionsFrame, username);
                addOptionLabel(panel, "9) Update information about the product/actors", 290, optionsFrame, username);
                addOptionLabel(panel, "12) Logout", 320, optionsFrame, username);
                break;

            case "Admin":
                addOptionLabel(panel, "1) View production detail", 50, optionsFrame, username);
                addOptionLabel(panel, "2) View actors detail", 80, optionsFrame, username);
                addOptionLabel(panel, "3) View notifications", 110, optionsFrame, username);
                addOptionLabel(panel, "4) Search for actor/movie/series", 140, optionsFrame, username);
                addOptionLabel(panel, "5) Add/Delete actor/movie/series to/from favorites", 170, optionsFrame, username);
                addOptionLabel(panel, "7) Add/Delete a production/actor from the system", 200, optionsFrame, username);
                addOptionLabel(panel, "8) View and solve received requests", 230, optionsFrame, username);
                addOptionLabel(panel, "9) Update information about the product/actors", 260, optionsFrame, username);
                addOptionLabel(panel, "11) Add/delete a user from the system", 290, optionsFrame, username);
                addOptionLabel(panel, "12) Logout", 320, optionsFrame, username);
                break;
            default:
                JLabel errorLabel = new JLabel("Unknown user type.");
                errorLabel.setBounds(10, 100, 300, 25);
                panel.add(errorLabel);
        }
    }

    public static void addOptionLabel(JPanel panel, String optionText, int yPosition, JFrame optionsFrame, String username) {
        JLabel optionLabel = new JLabel(optionText);
        optionLabel.setBounds(10, yPosition, 300, 25);
        panel.add(optionLabel);

        optionLabel.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                handleOption(optionText, optionsFrame,username);
            }
        });
    }

    private static void handleOption(String option, JFrame optionsFrame,String username) {
        String optionNumber = option.replaceAll("\\D+", "");

        int selectedOption = Integer.parseInt(optionNumber);

        switch (selectedOption) {
            case 1:
                GUI guiInstance = new GUI();
                guiInstance.displayAllProductionsInfoGUI();

                break;
            case 2:
                List<Actor> actors2 = Actor.readActorsFromJSON("../../../resources/input/actors.json");


                Actor.displayActorInfoGUI(actors2);

                break;

            case 3:
                System.out.println("Executing option 3");
                SwingUtilities.invokeLater(() -> {
                    RequestProcessor requestProcessor = new RequestProcessor();
                    requestProcessor.printRequestsForResolver("requests.txt", "a");
                    requestProcessor.setVisible(true);
                });

                break;

            case 4:
                System.out.println("Executing option 4");

                String title = showSearchGUI();
                List<Actor> actors3 = Actor.readActorsFromJSON("../../../resources/input/actors.json");

                if (Actor.isActorInList(title, actors3)) {
                    Actor.displayActorInfoByName(title);
                }
                Movie movie = new Movie(title);
                Series series = new Series(title);
                Production production;

                String productionType = Production.getType(title);
                if ("Movie".equalsIgnoreCase(productionType)) {
                    production = new Movie(title);
                } else if ("Series".equalsIgnoreCase(productionType)) {
                    production = new Series(title);
                } else {
                    System.out.println("Unknown production type");
                    return;
                }

                List<String> directors = production.getDirectors();
                List<String> actors = production.getActors();
                List<String> genres = production.getGenres();
                List<Rating> ratings = production.getRatings(title);
                String plot = production.getPlot(title);
                double avgRating = production.getAverageRating(title);
                JFrame infoFrame = new JFrame("Production Information");
                infoFrame.setSize(500, 500);
                infoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

                JPanel infoPanel = new JPanel();
                infoPanel.setLayout(new GridLayout(0, 1));
                infoFrame.add(infoPanel);
                infoPanel.add(new JLabel("Title: " + title));
                infoPanel.add(new JLabel("Duration: " + Movie.getDur(title) ));
                infoPanel.add(new JLabel("Release Year: " +  Movie.getRY(title)));
                infoPanel.add(new JLabel("Directors: " + directors));
                infoPanel.add(new JLabel("Actors: " + actors));
                infoPanel.add(new JLabel("Genres: " + genres));
                infoPanel.add(new JLabel("Avg rating: " + avgRating));
                infoPanel.add(new JLabel("Plot : " + plot));
                for (Rating rating : ratings) {
                    infoPanel.add(new JLabel("Username: " + rating.getUsername()));
                    infoPanel.add(new JLabel("Rating: " + rating.getRating()));
                    infoPanel.add(new JLabel("Comment: " + rating.getComment()));
                    infoPanel.add(new JLabel("-----"));
                }
                if ("Series".equalsIgnoreCase(productionType)) {
                    series.printEpisodesGUI();
                }

                infoFrame.setVisible(true);

                break;

            case 5:
                System.out.println("Executing option 5");

                List<String> searchResultss = new ArrayList<>();

                SearchGUIforFavs("Enter search term", searchResultss);

                String  actionn = searchResultss.get(0);
                String namee = searchResultss.get(1);
                String  poaa = searchResultss.get(2);

                IMDB.updateFavs(username,"../../../resources/input/accounts.json",actionn,namee,poaa);


                break;

            case 6:
                System.out.println("Executing option 6");

                List<String> searchResults = new ArrayList<>();
                SearchGUIforRequest("Enter search term", searchResults);

                String  type = searchResults.get(0);
                String titleofReq = searchResults.get(1);
                String description = searchResults.get(2);
                String resolverUsername = searchResults.get(3);
                LocalDateTime creationDate = LocalDateTime.now();
                DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                String formattedCreationDate = creationDate.format(formatter);

                RequestsHolder requestsHolder = new RequestsHolder();

                requestsHolder.addRequest(username, creationDate, resolverUsername, type,titleofReq,description);

                break;

            case 7:
                System.out.println("Executing option 7");
                List<String> searchResultsss = new ArrayList<>();

                SearchGUIforFavs("Enter search term", searchResultsss);

                String  actionnn = searchResultsss.get(0);
                String nameee = searchResultsss.get(1);
                String  poaaa = searchResultsss.get(2);

                IMDB.updateContributions(username,"../../../resources/input/accounts.json",actionnn,nameee,poaaa);


                break;

            case 8:
                System.out.println("Executing option 8");
//same as 3 ??
                break;

            case 9:
                System.out.println("Executing option 9");
                List<String> searchResultssss = new ArrayList<>();

                SearchGUIforchange("Enter search term", searchResultssss);

                String  titletochange = searchResultssss.get(0);
                String fieldName = searchResultssss.get(1);
                String  updatedValue = searchResultssss.get(2);
                JsonUpdater.updateField(titletochange, fieldName, updatedValue, "../../../resources/input/production.json");

                break;

            case 10:
                System.out.println("Executing option 10");
                List<String> searchReviws = new ArrayList<>();
                SearchGUIforReviews("Enter search term", searchReviws);

                String  addordel = searchReviws.get(0);
                String nameofprodtoreviw = searchReviws.get(1);
                if ("delete".equals(addordel)) {
                    Rating ratingInst = new Rating(username,0,"null");

                    ratingInst.deleteRatingFromJson("../../../resources/input/production.json",nameofprodtoreviw,username);
                    break;
                }
                String ratingvalstr = searchReviws.get(2);
                String ratingdes = searchReviws.get(3);
                int ratingval = Integer.parseInt(ratingvalstr);



                Rating ratingInstance = new Rating(username, ratingval, ratingdes);

                ratingInstance.addRatingToJson("../../../resources/input/production.json", nameofprodtoreviw, username, ratingval, ratingdes);


                break;

            case 11:
                System.out.println("Executing option 11");


                break;

            default:
                System.out.println("Invalid option");
                break;
        }
    }


    public static String showSearchGUI() {
        JTextField searchField = new JTextField(20);

        int result = JOptionPane.showConfirmDialog(
                null,
                new Object[]{"Search:", searchField},
                "Search Application",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION) {
            return searchField.getText();
        } else {

            return null;
        }
    }

    public static void SearchGUIforRequest(String prompt, List<String> searchResults) {
        JTextField searchField1 = new JTextField(20);
        JTextField searchField2 = new JTextField(20);
        JTextField searchField3 = new JTextField(20);
        JTextField searchField4 = new JTextField(20);

        Object[] message = {
                prompt + " type:", searchField1,
                prompt + " title:", searchField2,
                prompt + " description:", searchField3,
                prompt + " resolverusername:", searchField4
        };

        int result = JOptionPane.showConfirmDialog(
                null,
                message,
                "Search Application",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION) {
            searchResults.add(searchField1.getText());
            searchResults.add(searchField2.getText());
            searchResults.add(searchField3.getText());
            searchResults.add(searchField4.getText());
        }
    }
    public static void SearchGUIforFavs(String prompt, List<String> searchResults) {
        JTextField searchField1 = new JTextField(20);
        JTextField searchField2 = new JTextField(20);
        JTextField searchField3 = new JTextField(20);

        Object[] message = {
                prompt + " Add or del:", searchField1,
                prompt + " name:", searchField2,
                prompt + " Actors or Prod:", searchField3,
        };

        int result = JOptionPane.showConfirmDialog(
                null,
                message,
                "Search Application",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION) {
            searchResults.add(searchField1.getText());
            searchResults.add(searchField2.getText());
            searchResults.add(searchField3.getText());
        }
    }

    public static void SearchGUIforchange(String prompt, List<String> searchResults) {
        JTextField searchField1 = new JTextField(20);
        JTextField searchField2 = new JTextField(20);
        JTextField searchField3 = new JTextField(20);

        Object[] message = {
                prompt + " Title to change:", searchField1,
                prompt + " Field to change:", searchField2,
                prompt + " Updated field:", searchField3,
        };

        int result = JOptionPane.showConfirmDialog(
                null,
                message,
                "Search Application",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION) {
            searchResults.add(searchField1.getText());
            searchResults.add(searchField2.getText());
            searchResults.add(searchField3.getText());
        }
    }
    public static void SearchGUIforReviews(String prompt, List<String> searchResults) {
        JTextField searchField1 = new JTextField(20);
        JTextField searchField2 = new JTextField(20);
        JTextField searchField3 = new JTextField(20);
        JTextField searchField4 = new JTextField(20);

        Object[] message = {
                prompt + " Add or del:", searchField1,
                prompt + " title:", searchField2,
                prompt + " Rating val:", searchField3,
                prompt + " description:", searchField4
        };

        int result = JOptionPane.showConfirmDialog(
                null,
                message,
                "Search Application",
                JOptionPane.OK_CANCEL_OPTION
        );

        if (result == JOptionPane.OK_OPTION) {
            searchResults.add(searchField1.getText());
            searchResults.add(searchField2.getText());
            searchResults.add(searchField3.getText());
            searchResults.add(searchField4.getText());
        }
    }

    public void displayAllProductionsInfoGUI() {
        String filePath = "../../../resources/input/production.json";

        try (FileReader reader = new FileReader(filePath)) {
            JSONParser jsonParser = new JSONParser();
            JSONArray productionsArray = (JSONArray) jsonParser.parse(reader);

            JFrame infoFrame = new JFrame("All Productions Information");
            infoFrame.setSize(800, 600);
            infoFrame.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

            JPanel mainPanel = new JPanel();
            mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
            infoFrame.add(mainPanel);

            for (Object productionObj : productionsArray) {
                JSONObject productionData = (JSONObject) productionObj;
                String title = (String) productionData.get("title");

                JPanel infoPanel = new JPanel();
                infoPanel.setLayout(new GridLayout(0, 1));

                addTitleInfoToPanel(infoPanel, title, productionData);

                mainPanel.add(infoPanel);
                mainPanel.add(Box.createRigidArea(new Dimension(0, 10)));            }

            JScrollPane scrollPane = new JScrollPane(mainPanel);
            infoFrame.add(scrollPane);

            infoFrame.setVisible(true);

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void addTitleInfoToPanel(JPanel infoPanel, String title, JSONObject productionData) {
        String type = (String) productionData.get("type");
        Production production;
        Series series = new Series(title);
        if ("Movie".equalsIgnoreCase(type)) {
            production = new Movie(title);
        } else if ("Series".equalsIgnoreCase(type)) {
            production = new Series(title);
        } else {
            System.out.println("Unknown production type");
            return;
        }

        List<String> directors = production.getDirectors();
        List<String> actors = production.getActors();
        List<String> genres = production.getGenres();
        List<Rating> ratings = production.getRatings(title);
        String plot = (String) productionData.get("plot");
        double avgRating = (double) productionData.get("averageRating");

        infoPanel.add(new JLabel("Title: " + title));
        infoPanel.add(new JLabel("Type: " + type));
        infoPanel.add(new JLabel("Directors: " + directors));
        infoPanel.add(new JLabel("Actors: " + actors));
        infoPanel.add(new JLabel("Genres: " + genres));
        infoPanel.add(new JLabel("Avg rating: " + avgRating));
        infoPanel.add(new JLabel("Plot: " + plot));
        for (Rating rating : ratings) {
            infoPanel.add(new JLabel("Username: " + rating.getUsername()));
            infoPanel.add(new JLabel("Rating: " + rating.getRating()));
            infoPanel.add(new JLabel("Comment: " + rating.getComment()));
            infoPanel.add(new JLabel("-----"));
        }

    }

}