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

public class App extends Application {
    private Stage stage;

    @Override
    public void start(Stage stage) {
        this.stage = stage;
        showLoginScreen();
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
                    Button quitGameButton = new Button("Quit Game");
                    pauseMenu.getChildren().addAll(exitPauseMenuButton, changeVolumeButton, restartGameButton, quitGameButton);
                    Scene pauseMenuScene = new Scene(pauseMenu, 300, 200);
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
