package com.controller;


import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;

import com.App;
import com.model.Player;
import com.service.IPlayerService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.effect.DropShadow;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;

public class HomeController implements Initializable {

    @FXML
    private VBox pnItems;
    @FXML
    private Button btnOverview;

    @FXML
    private Button btnOrders;

    @FXML
    private Button btnCustomers;

    @FXML
    private Button btnPlayers;

    @FXML
    private Button btnTeams;

    @FXML
    private Button btnSettings;

    @FXML
    private Button btnSignout;

    @FXML
    private Pane pnlCustomer;

    @FXML
    private Pane pnlOrders;

    @FXML
    private Pane pnlOverview;

    @FXML
    private Pane pnlMenus;

     @FXML
    private TableView<Player> playerTable;

    @FXML
    private TableColumn<Player, Long> columnId;


    @FXML
    private TableColumn<Player, String> columnName;

     private List<Player> listPlayers = new ArrayList();
    
    private ObservableList<Player> observableListPlayers;

    private IPlayerService playerService;



    @Override
    public void initialize(URL location, ResourceBundle resources) {
      

        Object[][] playerData = {
            {1, "Player 1", 19, "FCB", "active"},
            {2, "Player 2", 21, "RMD", "inactive"},
            {3, "Player 3", 23, "JUV", "active"}
            // Add more data as needed
        };
        System.out.println("pnItems initialized: " + (pnItems != null));

        // Iterate over each player entry and create UI components
        // if (pnItems != null) {
        //     populatePlayerData(); // Only if pnItems is not null
        // }

        try {
        playerService = (IPlayerService) App.getApplicationContext().getBean("playerService");
            
        } catch (Exception e) {
            System.out.println("Error"+e.getMessage());
        }
        // loadPlayers(false);

    }

    private void populatePlayerData() {
        Object[][] playerData = {
            {1, "Player 1", 19, "FCB", "active"},
            {2, "Player 2", 21, "RMD", "inactive"},
            {3, "Player 3", 23, "JUV", "active"}
            // Add more data as needed
        };
    
        for (Object[] player : playerData) {
            addPlayerItem((int) player[0], (String) player[1], (int) player[2], (String) player[3], (String) player[4]);
        }
    }



    public void handleClicks(ActionEvent actionEvent) {
        // if (actionEvent.getSource() == btnCustomers) {
        //     pnlCustomer.setStyle("-fx-background-color : #1620A1");
        //     pnlCustomer.toFront();
        // }
        if (actionEvent.getSource() == btnPlayers) {
            pnlMenus.setStyle("-fx-background-color : #53639F");
            pnlMenus.toFront();
        }
        if (actionEvent.getSource() == btnTeams) {
            pnlOverview.setStyle("-fx-background-color : #02030A");
            pnlOverview.toFront();
        }
    //     if(actionEvent.getSource()==btnOrders)
    //     {
    //         pnlOrders.setStyle("-fx-background-color : #464F67");
    //         pnlOrders.toFront();
    //     }
    }
    private void addPlayerItem(int id, String name, int age, String team, String status) {
    HBox itemC = new HBox(80); // Spacing of 80 between elements
    itemC.setAlignment(javafx.geometry.Pos.CENTER);
    itemC.setPrefHeight(53.0);
    itemC.setPrefWidth(795.0);
    itemC.setStyle("-fx-background-color: #02030A; -fx-background-radius: 5; -fx-background-insets: 0;");

    // Add shadow effect
    DropShadow dropShadow = new DropShadow();
    dropShadow.setBlurType(javafx.scene.effect.BlurType.GAUSSIAN);
    dropShadow.setHeight(5.0);
    dropShadow.setRadius(2.0);
    dropShadow.setWidth(5.0);
    itemC.setEffect(dropShadow);

    // Icon ImageView
    ImageView icon = new ImageView(new Image(getClass().getResourceAsStream("/assets/images/icons8_GPS_Antenna_64px_1.png")));
    icon.setFitHeight(31.0);
    icon.setFitWidth(25.0);
    HBox.setMargin(icon, new Insets(0, 0, 0, 10)); // Margin left of 10

    // Labels with custom text fill color
    Label idLabel = new Label(String.valueOf(id));
    idLabel.setTextFill(javafx.scene.paint.Color.web("#b7c3d7"));

    Label nameLabel = new Label(name);
    nameLabel.setTextFill(javafx.scene.paint.Color.web("#b7c3d7"));

    Label ageLabel = new Label(String.valueOf(age));
    ageLabel.setTextFill(javafx.scene.paint.Color.web("#b7c3d7"));

    Label teamLabel = new Label(team);
    teamLabel.setTextFill(javafx.scene.paint.Color.web("#b7c3d7"));

    // Status Button
    Button statusButton = new Button(status);
    statusButton.setPrefHeight(10.0);
    statusButton.setPrefWidth(60.0);
    statusButton.setStyle("-fx-border-color: #2A73FF; -fx-border-radius: 20; -fx-background-color: transparent;");

    // Add all elements to the HBox
    itemC.getChildren().addAll(icon, idLabel, nameLabel, ageLabel, teamLabel, statusButton);

    // Add HBox to the VBox container
    pnItems.getChildren().add(itemC);
}

     public boolean loadPlayers(boolean cleanTable){
        
        try{
            List<Player> players = playerService.getAllPlayers();

           if(players != null){
            if (cleanTable){
                cleanTable();
            }
            
            definingColumn();
        
            setListPlayers(players);

            observableListPlayers = FXCollections.observableArrayList(listPlayers);
          
            playerTable.setItems(observableListPlayers);
            
           }else{
            System.out.println("Error");
           }
        }catch(Exception e){
            // showInformationAlert("Error" , "" , "An error occurred while retrieving data",Alert.AlertType.ERROR);
            e.printStackTrace();
            return false;
        }
        
        return true;
    }
    
      

    public void loadPlayers(List<Player> arrayListPlayers ){
         try{
            cleanTable();
            observableListPlayers = FXCollections.observableArrayList(arrayListPlayers);
            playerTable.setItems(observableListPlayers);
        }catch(Exception e){
            e.printStackTrace();
            // showInformationAlert("Error" , "" , "An error occurred while retrieving data",Alert.AlertType.ERROR);
        }
    }
    
    public void definingColumn(){
            columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
            columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
    }

    private void cleanTable(){
        playerTable.getItems().clear();
    }


    public List<Player> getListPlayers() {
        return listPlayers;
    }

    public void setListPlayers(List<Player> listPlayers) {
        this.listPlayers = listPlayers ;
    }

    @FXML
    public void gotoHome() throws IOException{
        App.setRoot("/home");
    }
    
    @FXML
    public void gotToPlayerPage() throws IOException{
        App.setRoot("/Player/index");
    }

    @FXML
    public void toTeamPage() throws IOException{
        App.setRoot("/Team/index");
    }

    @FXML
    public void toTournamentPage() throws  IOException{
        App.setRoot("/Tournament/index");

    }
}
