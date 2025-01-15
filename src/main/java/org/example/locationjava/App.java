package org.example.locationjava;

import javafx.application.Application;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import javafx.collections.FXCollections;
import javafx.geometry.Pos;
import javafx.geometry.HPos;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

public class App extends Application {
    private Agence agence;
    private ListView<Vehicule> vehiculeList;
    private ListView<Client> clientList;
    private ListView<Location> locationList;
    private ComboBox<Vehicule> vehiculeCombo;
    private ComboBox<Client> clientCombo;

    @Override
    public void start(Stage primaryStage) {
        showLoginPage(primaryStage);
    }

    private void showLoginPage(Stage primaryStage) {
        GridPane grid = new GridPane();
        grid.setAlignment(Pos.CENTER);
        grid.setPadding(new Insets(30));
        grid.setHgap(10);
        grid.setVgap(10);

        Label titleLabel = new Label("Bienvenue");
        titleLabel.setStyle("-fx-font-size: 24px; -fx-font-weight: Bold;");
        grid.add(titleLabel, 0, 0, 2, 1);

        Label userLabel = new Label("Utilisateur :");
        TextField userField = new TextField();
        grid.add(userLabel, 0, 1);
        grid.add(userField, 1, 1);

        Label passLabel = new Label("Mot de passe :");
        PasswordField passField = new PasswordField();
        grid.add(passLabel, 0, 2);
        grid.add(passField, 1, 2);

        Button loginBtn = new Button("Se connecter");
        grid.add(loginBtn, 1, 3);
        GridPane.setHalignment(loginBtn, HPos.RIGHT);

        loginBtn.setOnAction(e -> {
            if (authenticate(userField.getText(), passField.getText())) {
                showMainApp(primaryStage);
            } else {
                showAlert("Erreur", "Nom d'utilisateur ou mot de passe incorrect");
            }
        });

        Scene scene = new Scene(grid, 400, 200);
        primaryStage.setTitle("Formulaire d'authentification");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private boolean authenticate(String username, String password) {
        String sql = "SELECT * FROM login WHERE username = ? AND password = ?";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/authentication", "root", "Othmane123456789");
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            ResultSet rs = stmt.executeQuery();

            return rs.next();

        } catch (Exception e) {
            e.printStackTrace();
            showAlert("Erreur", "Problème de connexion à la base de données.");
            return false;
        }
    }

    private void showMainApp(Stage primaryStage) {
        agence = new Agence();
        agence.loadVehicules();
        agence.loadClients();
        agence.loadLocations();

        BorderPane mainLayout = new BorderPane();
        TabPane tabPane = new TabPane();

        Tab vehiculesTab = new Tab("Vehicules");
        vehiculesTab.setContent(createVehiculeTab());
        vehiculesTab.setClosable(false);

        Tab clientsTab = new Tab("Clients");
        clientsTab.setContent(createClientTab());
        clientsTab.setClosable(false);

        Tab locationsTab = new Tab("Locations");
        locationsTab.setContent(createLocationTab());
        locationsTab.setClosable(false);

        tabPane.getTabs().addAll(vehiculesTab, clientsTab, locationsTab);

        Button logoutBtn = new Button("Se déconnecter");
        logoutBtn.setOnAction(e -> showLoginPage(primaryStage));
        mainLayout.setTop(logoutBtn);
        BorderPane.setAlignment(logoutBtn, Pos.CENTER_RIGHT);
        BorderPane.setMargin(logoutBtn, new Insets(10));

        mainLayout.setCenter(tabPane);

        Scene scene = new Scene(mainLayout, 800, 600);
        primaryStage.setTitle("Car Rental System");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    private VBox createVehiculeTab() {
        TextField matriculeField = new TextField();
        matriculeField.setPromptText("Plate Number");
        TextField marqueField = new TextField();
        marqueField.setPromptText("Brand");
        TextField modeleField = new TextField();
        modeleField.setPromptText("Model");
        TextField prixField = new TextField();
        prixField.setPromptText("Price");
        TextField kilometerField = new TextField();
        kilometerField.setPromptText("Kilometers");

        Button addButton = new Button("Add Vehicle");
        addButton.setOnAction(e -> {
            try {
                int prix = Integer.parseInt(prixField.getText());
                int km = Integer.parseInt(kilometerField.getText());
                Vehicule vehicule = new Vehicule(
                        matriculeField.getText(),
                        marqueField.getText(),
                        modeleField.getText(),
                        prix,
                        km
                );
                agence.addVehicule(vehicule);
                updateVehiculeList();
                updateCombos();
                clearFields(matriculeField, marqueField, modeleField, prixField, kilometerField);
            } catch (NumberFormatException ex) {
                showAlert("Error", "Price and kilometers must be numbers");
            } catch (IOException ex) {
                showAlert("Error", "Failed to save vehicle");
            }
        });

        vehiculeList = new ListView<>();
        updateVehiculeList();

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        vbox.getChildren().addAll(
                matriculeField, marqueField, modeleField,
                prixField, kilometerField, addButton, vehiculeList
        );
        return vbox;
    }

    private VBox createClientTab() {
        TextField nomField = new TextField();
        nomField.setPromptText("Last Name");
        TextField prenomField = new TextField();
        prenomField.setPromptText("First Name");
        TextField ageField = new TextField();
        ageField.setPromptText("Age");

        Button addButton = new Button("Add Client");
        addButton.setOnAction(e -> {
            try {
                int age = Integer.parseInt(ageField.getText());
                Client client = new Client(nomField.getText(), prenomField.getText(), age);
                agence.addClient(client);
                updateClientList();
                updateCombos();
                clearFields(nomField, prenomField, ageField);
            } catch (NumberFormatException ex) {
                showAlert("Error", "Age must be a number");
            }
        });

        clientList = new ListView<>();
        updateClientList();

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        vbox.getChildren().addAll(nomField, prenomField, ageField, addButton, clientList);
        return vbox;
    }

    private VBox createLocationTab() {
        vehiculeCombo = new ComboBox<>();
        clientCombo = new ComboBox<>();
        updateCombos();

        Button rentButton = new Button("Rent Vehicle");
        rentButton.setOnAction(e -> {
            Vehicule vehicule = vehiculeCombo.getValue();
            Client client = clientCombo.getValue();
            if (vehicule != null && client != null) {
                if (agence.isCarAvailable(vehicule.getMatricule())) {
                    Location location = new Location(client, vehicule);
                    agence.addLocation(location);
                    vehicule.setAvailable(false);
                    updateLocationList();
                    updateVehiculeList();
                    updateCombos();
                } else {
                    showAlert("Error", "Vehicle is not available");
                }
            } else {
                showAlert("Error", "Please select both client and vehicle");
            }
        });

        Button returnButton = new Button("Return Vehicle");
        returnButton.setOnAction(e -> {
            Location selectedLocation = locationList.getSelectionModel().getSelectedItem();
            if (selectedLocation != null) {
                try {
                    agence.returnVehicle(selectedLocation.getClient().getClientId(), selectedLocation.getVehicule().getMatricule());
                    selectedLocation.getVehicule().setAvailable(true);
                    agence.locations.remove(selectedLocation); // Remove the location from the list
                    updateLocationList();
                    updateVehiculeList();
                    updateCombos();
                    showAlert("Success", "Vehicle returned successfully");
                } catch (Exception ex) {
                    showAlert("Error", "Failed to return vehicle: " + ex.getMessage());
                }
            } else {
                showAlert("Error", "Please select a location to return");
            }
        });

        locationList = new ListView<>();
        updateLocationList();

        VBox vbox = new VBox(10);
        vbox.setPadding(new Insets(10));
        vbox.getChildren().addAll(
                new Label("Select Vehicle:"), vehiculeCombo,
                new Label("Select Client:"), clientCombo,
                rentButton, returnButton, locationList
        );
        return vbox;
    }

    private void updateVehiculeList() {
        vehiculeList.setItems(FXCollections.observableArrayList(agence.vehicules));
    }

    private void updateClientList() {
        clientList.setItems(FXCollections.observableArrayList(agence.clients));
    }

    private void updateLocationList() {
        locationList.setItems(FXCollections.observableArrayList(agence.locations));
        locationList.setCellFactory(param -> new ListCell<>() {
            @Override
            protected void updateItem(Location location, boolean empty) {
                super.updateItem(location, empty);
                if (empty || location == null) {
                    setText(null);
                } else {
                    setText("Client: " + location.getClient().getNom() + ", Vehicle: " + location.getVehicule().getMatricule());
                }
            }
        });
    }


    private void updateCombos() {
        vehiculeCombo.setItems(FXCollections.observableArrayList(
                agence.vehicules.stream()
                        .filter(Vehicule::isAvailable)
                        .toList()
        ));
        clientCombo.setItems(FXCollections.observableArrayList(agence.clients));
    }

    private void clearFields(TextField... fields) {
        for (TextField field : fields) {
            field.clear();
        }
    }

    private void showAlert(String title, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(content);
        alert.showAndWait();
    }

    public static void main(String[] args) {
        launch(args);
    }
}
