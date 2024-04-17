package com.mycompany.msats;

import java.time.LocalDateTime;
import java.util.Optional;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.Slider;
import javafx.scene.control.TextField;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;


public class App extends Application {
    private Stage stage;
    private Database db;
    private String url = "jdbc:mysql://sql5.freesqldatabase.com:3306/sql5699628";
    private String DB_username = "sql5699628";
    private String DB_password = "B395ypbazU";
    private int user_id;
    private String User_username = "";
    private String User_password = "";
    private String query = "";

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        
        try {
            // Register JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");
            // Open a connection
            Connection conn = DriverManager.getConnection(url, DB_username, DB_password);
            // Connection successful
            System.out.println("Connected to the database.");

            // Perform database operations here

            // Close the connection
            conn.close();
        } catch (SQLException e) {
            // Handle SQL exceptions
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            // Handle class not found exception
            e.printStackTrace();
        }
        
        showLoginScreen();
    }
    
    public boolean loginUser(String username, String password) {
        try (Connection conn = DriverManager.getConnection(url, DB_username, DB_password)) {
            String query = selectQuery("LogIn");
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            ResultSet rs = pstmt.executeQuery();
            // If result set has at least one row, login successful
            return rs.next();
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Login failed due to an exception
        }
    }
    
    public int getUserId(String username) {
        try (Connection conn = DriverManager.getConnection(url, DB_username, DB_password)) {
            String query = selectQuery("GetUserID");
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, username);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getInt("USER_ID");
            } else {
                return -1; // User not found
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return -1; // Error occurred while retrieving user ID
        }
    }
    
    public boolean createUser(String username, String password) {
        try (Connection conn = DriverManager.getConnection(url, DB_username, DB_password)) {
            String query = selectQuery("CreateAccount");
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0; // User creation successful if at least one row was affected
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // User creation failed due to an exception
        }
    }
    
    public ResultSet getUserScores(int userId) {
        try (Connection conn = DriverManager.getConnection(url, DB_username, DB_password)) {
            String query = selectQuery("GetScoresUser");
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, userId);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Error occurred while retrieving user scores
        }
    }
    
    public ResultSet getGlobalScores() {
        try (Connection conn = DriverManager.getConnection(url, DB_username, DB_password)) {
            String query = selectQuery("GetScoresGlobal");
            PreparedStatement pstmt = conn.prepareStatement(query);
            return pstmt.executeQuery();
        } catch (SQLException e) {
            e.printStackTrace();
            return null; // Error occurred while retrieving global scores
        }
    }
    
    public boolean saveScore(int userId, int score) {
        try (Connection conn = DriverManager.getConnection(url, DB_username, DB_password)) {
            String query = selectQuery("SaveScore");
            PreparedStatement pstmt = conn.prepareStatement(query);
            pstmt.setInt(1, userId);
            pstmt.setInt(2, score);
            pstmt.setDate(3, new java.sql.Date(System.currentTimeMillis())); // Assuming DATE column is of type DATE
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0; // Score saved successfully if at least one row was affected
        } catch (SQLException e) {
            e.printStackTrace();
            return false; // Score saving failed due to an exception
        }
    }
    
    private String selectQuery(String selection) {
        switch (selection) {
            case "LogIn": 
            query = "SELECT * FROM USERS WHERE USER_NAME = ? AND USER_PASS = ?";
            break;
        case "GetUserID":
            query = "SELECT USER_ID FROM USERS WHERE USER_NAME = ?";
            break;
        case "CreateAccount":
            query = "INSERT INTO USERS (USER_NAME, USER_PASS) VALUES (?, ?)";
            break;
        case "GetScoresUser":
            query = "SELECT * FROM SCORE WHERE USER_ID = ?";
            break;
        case "GetScoresGlobal":
            query = "SELECT * FROM SCORE ORDER BY SCORE DESC LIMIT 10";
            break;
        case "SaveScore":
            query = "INSERT INTO scores (USER_ID, SCORE, DATE) VALUES (?, ?, ?)";
            break;
        default:
            break;
        }
        return query;
    }
    

    private void showLoginScreen() {
        Label welcomeLabel = new Label("Welcome to Mobile Security Awareness Training!");
        Label loginLabel = new Label("Login or Create Account");
        Label usernameLabel = new Label("Username:");
        Label passwordLabel = new Label("Password:");
        
        TextField usernameField = new TextField();
        PasswordField passwordField = new PasswordField();
        
        Button loginButton = new Button("Login");
        Button createAccountButton = new Button("Create Account");

        GridPane root = new GridPane();
        root.setPadding(new Insets(10));
        root.setVgap(10);
        root.setHgap(10);

        root.add(welcomeLabel, 0, 0, 2, 1);
        root.add(loginLabel, 0, 1, 2, 1);
        root.add(usernameLabel, 0, 2);
        root.add(usernameField, 1, 2);
        root.add(passwordLabel, 0, 3);
        root.add(passwordField, 1, 3);
        root.add(loginButton, 0, 4);
        root.add(createAccountButton, 1, 4);

        loginButton.setOnAction(event -> {
            // Call method to show homepage, add database stuff here
            showHomePage();
        });
        
        // Add create account button logic
        createAccountButton.setOnAction(event -> {
            db.saveUser(usernameField.getText(), passwordField.getText());
            showHomePage();
        });

        Scene scene = new Scene(root, 640, 480);
        stage.setScene(scene);
        stage.setTitle("Mobile Security Awareness Training Software");
        stage.show();
    }

    private void showHomePage() {
        Label titleLabel = new Label("Mobile Security Awareness Training Software");
        Button playGameButton = new Button("Play Game");
        Button scoresButton = new Button("Your Scores/Global Leaderboard");
        Button settingsButton = new Button("Settings");
        Button exitButton = new Button("Exit Software");

        VBox root = new VBox(10);
        root.setPadding(new Insets(10));
        root.getChildren().addAll(titleLabel, playGameButton, scoresButton, settingsButton, exitButton);

        playGameButton.setOnAction(event -> {
            root.getChildren().clear();
            Label gameLabel = new Label("Game Screen Yippee :3");
            BorderPane.setAlignment(gameLabel, Pos.CENTER);
            Button pauseButton = new Button("Pause");
            BorderPane.setAlignment(pauseButton, Pos.TOP_LEFT);
            BorderPane.setMargin(pauseButton, new Insets(10));
            pauseButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    VBox pauseMenu = new VBox(10);
                    pauseMenu.setPadding(new Insets(20));
                    pauseMenu.setAlignment(Pos.CENTER);
                    Button exitPauseMenuButton = new Button("Exit Pause Menu");
                    Button changeVolumeButton = new Button("Change Volume");
                    Button restartGameButton = new Button("Restart Game");
                    Button saveAndExitButton = new Button("Save And Exit");
                    Button quitGameButton = new Button("Quit Game");
                    pauseMenu.getChildren().addAll(exitPauseMenuButton, changeVolumeButton, restartGameButton, saveAndExitButton, quitGameButton);
                    Scene pauseMenuScene = new Scene(pauseMenu, 300, 250);
                    Stage pauseMenuStage = new Stage();
                    pauseMenuStage.setScene(pauseMenuScene);
                    pauseMenuStage.setTitle("Pause Menu");
                    pauseMenuStage.show();
                    exitPauseMenuButton.setOnAction(event -> {
                        pauseMenuStage.close();
                    });
                    changeVolumeButton.setOnAction(event -> {
                        // Implement volume changing functionality, bring slider back from settings 
                    });
                    restartGameButton.setOnAction(event -> {
                        // Implement game restarting functionality
                    });
                    saveAndExitButton.setOnAction(event -> {
                        // Save functionality here
                        pauseMenuStage.close();
                        showHomePage();
                    });
                    quitGameButton.setOnAction(event -> {
                        Alert confirmQuitAlert = new Alert(Alert.AlertType.CONFIRMATION);
                        confirmQuitAlert.setTitle("Confirm Quit");
                        confirmQuitAlert.setHeaderText(null);
                        confirmQuitAlert.setContentText("Are you sure you want to quit the game?");
                        ButtonType quitButton = new ButtonType("Quit", ButtonBar.ButtonData.OK_DONE);
                        ButtonType cancelButton = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
                        confirmQuitAlert.getButtonTypes().setAll(quitButton, cancelButton);
                        Optional<ButtonType> result = confirmQuitAlert.showAndWait();
                        if (result.isPresent() && result.get() == quitButton) {
                            pauseMenuStage.close();
                            stage.close();
                        }
                    });
                }
            });
            BorderPane gamePane = new BorderPane();
            gamePane.setCenter(gameLabel);
            gamePane.setTop(pauseButton);
            root.getChildren().add(gamePane);
        });

        scoresButton.setOnAction(event -> {
            root.getChildren().clear();
            Button yourScoresButton = new Button("Your Scores");
            Button globalLeaderboardButton = new Button("Global Leaderboard");
            Button backButton = new Button("Back");
            yourScoresButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    root.getChildren().clear();
                    VBox scoresBox = new VBox(10);
                    scoresBox.setPadding(new Insets(10));
                    Label titleLabel = new Label("Your Scores");
                    titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
                    scoresBox.getChildren().add(titleLabel);
                    for (int i = 1; i <= 10; i++) {
                        Label scoreLabel = new Label("Game " + i + ": Score - " + (i * 100) + ", Played on: " + LocalDateTime.now());
                        scoresBox.getChildren().add(scoreLabel);
                    }
                    Button backButtonScores = new Button("Back");
                    backButtonScores.setOnAction(event -> showHomePage());
                    root.getChildren().addAll(scoresBox, backButtonScores);
                }
            });
            globalLeaderboardButton.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent e) {
                    root.getChildren().clear();
                    VBox leaderboardBox = new VBox(10);
                    leaderboardBox.setPadding(new Insets(10));
                    Label titleLabel = new Label("Global Leaderboard");
                    titleLabel.setStyle("-fx-font-size: 18px; -fx-font-weight: bold;");
                    leaderboardBox.getChildren().add(titleLabel);
                    for (int i = 1; i <= 10; i++) {
                        String username = "Player" + i;
                        int score = i * 1000;
                        LocalDateTime date = LocalDateTime.now().minusDays(i);
                        Label entryLabel = new Label("Username: " + username + ", Score: " + score + ", Date: " + date);
                        leaderboardBox.getChildren().add(entryLabel);
                    }
                    Button backButtonLeaderboard = new Button("Back");
                    backButtonLeaderboard.setOnAction(event -> showHomePage());
                    root.getChildren().addAll(leaderboardBox, backButtonLeaderboard);
                }
            });
            backButton.setOnAction(e -> {
                showHomePage();
            });
            BorderPane borderPane = new BorderPane();
            borderPane.setTop(new HBox(10, backButton, yourScoresButton, globalLeaderboardButton));
            root.getChildren().add(borderPane);
        });

        settingsButton.setOnAction(event -> {
            Stage settingsStage = new Stage();
            settingsStage.setTitle("Settings");
            GridPane settingsGrid = new GridPane();
            settingsGrid.setPadding(new Insets(10));
            settingsGrid.setVgap(10);
            settingsGrid.setHgap(10);
            settingsGrid.setAlignment(Pos.CENTER);
            Label settingsLabel = new Label("Settings");
            CheckBox notificationsCheckBox = new CheckBox("Enable Notifications");
            Slider volumeSlider = new Slider(0, 100, 50);
            Label volumeLabel = new Label("Volume:");
            Label volumeValueLabel = new Label();
            Button changePasswordButton = new Button("Change Password");
            Button privacySettingsButton = new Button("Privacy Settings");
            settingsGrid.add(settingsLabel, 0, 0, 2, 1);
            settingsGrid.add(notificationsCheckBox, 0, 1, 2, 1);
            settingsGrid.add(volumeLabel, 0, 2);
            settingsGrid.add(volumeSlider, 1, 2);
            settingsGrid.add(volumeValueLabel, 2, 2);
            settingsGrid.add(changePasswordButton, 0, 3);
            settingsGrid.add(privacySettingsButton, 1, 3);
            volumeSlider.valueProperty().addListener((obs, oldValue, newValue) -> {
                volumeValueLabel.setText(String.format("%.0f", newValue));
            });
            changePasswordButton.setOnAction(e -> {
                // Implement change password functionality
            });
            privacySettingsButton.setOnAction(e -> {
                // Implement privacy settings functionality
            });
            Scene settingsScene = new Scene(settingsGrid, 350, 150);
            settingsStage.setScene(settingsScene);
            settingsStage.show();
        });

        exitButton.setOnAction(event -> {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm Exit");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to exit?");
            ButtonType yesButton = new ButtonType("Yes", ButtonBar.ButtonData.YES);
            ButtonType noButton = new ButtonType("No", ButtonBar.ButtonData.NO);
            alert.getButtonTypes().setAll(yesButton, noButton);
            Optional<ButtonType> result = alert.showAndWait();
            if (result.isPresent() && result.get() == yesButton) {
                stage.close();
            }
        });

        Scene scene = new Scene(root, 640, 480);
        stage.setScene(scene);
        stage.setTitle("Mobile Security Awareness Training Software");
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}
