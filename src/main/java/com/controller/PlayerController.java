package com.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.App;
import com.model.Dto.TeamOption;
import com.model.Player;
import com.model.Team;
import com.service.IPlayerService;
import com.service.ITeamService;

import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;


public class PlayerController {

    private final Logger logger = LoggerFactory.getLogger(PlayerController.class);



    private IPlayerService playerService;
    private ITeamService teamService;

    private Validator validator;

     @FXML
    private TextField txtSearch;

    @FXML
    private TableView<Player> playerTable;

    @FXML
    private TableColumn<Player, Long> columnId;


    @FXML
    private TableColumn<Player, String> columnName;

    @FXML
    private TableColumn<Player, Integer> columnAge;

    @FXML
    private TableColumn<Player, String> columnTeamName;

    
    
    @FXML
    private Label lblNote;
    
    @FXML
    private Label lblError;
    
    private List<Player> listPlayers = new ArrayList();
    
    private ObservableList<Player> observableListPlayers;


    @FXML
    private TextField playerNameField;


    @FXML
    private TextField ageField;

    @FXML
    private ComboBox<TeamOption> teamsField;

    private Map<String, Label> errorLabels;

    @FXML
    private Label nameErrorLabel;

    @FXML
    private Label ageErrorLabel;

    @FXML
    private Label teamErrorLabel;


  public PlayerController(){
     try {
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            validator = factory.getValidator();
         } catch (Exception e) {
            logger.error("error"+e);
         }
  }


    @FXML
    public void initialize() {  
        playerService = (IPlayerService) App.getApplicationContext().getBean("playerService");
        teamService = (ITeamService) App.getApplicationContext().getBean("teamService");

        loadPlayers(false);


        List<TeamOption> teamOptions = teamService.getAllTeams().stream()
        .map(team -> new TeamOption(team.getId(), team.getName()))
        .collect(Collectors.toList());

        teamsField.setItems(FXCollections.observableArrayList(teamOptions));

        errorLabels = new HashMap<>();
        errorLabels.put("name", nameErrorLabel);
        errorLabels.put("age", ageErrorLabel);
        errorLabels.put("team", teamErrorLabel);


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
        }
    }
    
    public void definingColumn(){
        if(this.columnId !=null && this.columnName != null && this.columnAge!=null && this.columnTeamName != null){
            columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
            columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
            columnAge.setCellValueFactory(new PropertyValueFactory<>("age"));
        
            columnTeamName.setCellValueFactory(cellData -> {
                Team team = cellData.getValue().getTeam();
                return new SimpleStringProperty(team != null ? team.getName() : "No Team");
            });
        }
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
    public void delete(ActionEvent event){
         if (playerTable.getSelectionModel().getSelectedIndex() > -1) {
            Long id = playerTable.getSelectionModel().getSelectedItem().getId();
            Player p = new Player();
            p.setId(id);

             Alert dialogoExe = new Alert(Alert.AlertType.WARNING);
            ButtonType btnYes = new ButtonType("Yes");
            ButtonType btnNoAnswer = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            
            dialogoExe.setTitle("Attention!");
            dialogoExe.setHeaderText("Are you sure you want to delete ?");
            dialogoExe.setContentText("Delete " + playerTable.getSelectionModel().getSelectedItem().getName() + "?");
            dialogoExe.getButtonTypes().setAll(btnYes, btnNoAnswer);

            Scene scene = dialogoExe.getDialogPane().getScene();
            if (scene != null) {
                scene.getStylesheets().add(getClass().getResource("/assets/css/style.css").toExternalForm());
            }

            dialogoExe.getDialogPane().getStyleClass().add("custom-alert");

            dialogoExe.showAndWait().ifPresent(b -> {
                if (b == btnYes) {
                    this.playerService.delete(p);
                    loadPlayers(true);
                }

            });


        }
    }


    @FXML
    public void update(ActionEvent event){
         if (playerTable.getSelectionModel().getSelectedIndex() == -1)return;
         
         Player selectedPlayer = playerTable.getSelectionModel().getSelectedItem();
         Long id = selectedPlayer.getId();
         String name = selectedPlayer.getName();
         int age = selectedPlayer.getAge();
         Team currentTeam = selectedPlayer.getTeam();
     
         // Set up alert dialog
         Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
         alert.setTitle("Player Information");
         alert.setHeaderText("Edit player " + id);
     
         // Define input fields and labels with styling
         Label nameLabel = new Label("Name:");
         Label ageLabel = new Label("Age:");
         Label teamLabel = new Label("Team:");
         nameLabel.setStyle("-fx-text-fill: white;");
         ageLabel.setStyle("-fx-text-fill: white;");
         teamLabel.setStyle("-fx-text-fill: white;");
     
         TextField nameField = new TextField(name);
         nameField.setPromptText("Enter name");
         nameField.setStyle("-fx-background-color: transparent; -fx-text-fill: #FFFF8D; -fx-border-color: #B7C3D7; -fx-border-radius: 12; -fx-padding: 10; -fx-font-size: 14px;");
     
         TextField ageField = new TextField(String.valueOf(age));
         ageField.setPromptText("Enter age");
         ageField.setStyle("-fx-background-color: transparent; -fx-text-fill: #FFFF8D; -fx-border-color: #B7C3D7; -fx-border-radius: 12; -fx-padding: 10; -fx-font-size: 14px;");
     
         // Load teams and set up ComboBox
         List<TeamOption> teamOptions = teamService.getAllTeams().stream()
                 .map(team -> new TeamOption(team.getId(), team.getName()))
                 .collect(Collectors.toList());
     
         ComboBox<TeamOption> teamComboBox = new ComboBox<>(FXCollections.observableArrayList(teamOptions));
         teamComboBox.setValue(new TeamOption(currentTeam.getId(), currentTeam.getName()));
         teamComboBox.setStyle("-fx-background-color: white; -fx-text-fill: #FFFF8D; -fx-border-color: #B7C3D7; -fx-border-radius: 12; -fx-padding: 10; -fx-font-size: 14px;");
     
         // Layout setup
         GridPane gridPane = new GridPane();
         gridPane.setHgap(10);
         gridPane.setVgap(10);
         gridPane.setPadding(new Insets(20, 150, 10, 10));
         gridPane.add(nameLabel, 0, 0);
         gridPane.add(nameField, 1, 0);
         gridPane.add(ageLabel, 0, 1);
         gridPane.add(ageField, 1, 1);
         gridPane.add(teamLabel, 0, 2);
         gridPane.add(teamComboBox, 1, 2);
     
         alert.getDialogPane().setContent(gridPane);
         alert.getDialogPane().setMinWidth(500);
         alert.getDialogPane().setMinHeight(500);



        
      
    

  


            Scene scene = alert.getDialogPane().getScene();
            if (scene != null) {
                scene.getStylesheets().add(getClass().getResource("/assets/css/style.css").toExternalForm());
            }

            alert.getDialogPane().getStyleClass().add("custom-alert");

            alert.getDialogPane().setContent(gridPane);

        
            alert.getDialogPane().setMinWidth(500);
            alert.getDialogPane().setMinHeight(500);

            alert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    String _name = nameField.getText().trim();
                    String _ageText = ageField.getText().trim();
        
                    if (_name.isEmpty() || _ageText.isEmpty()) {
                        logger.warn("Name or age is empty, update aborted.");
                        return;
                    }
        
                    int _age;
                    try {
                        _age = Integer.parseInt(_ageText);
                    } catch (NumberFormatException e) {
                        logger.warn("Invalid age input, update aborted.");
                        return;
                    }
        
                    // Update player details
                    Player updatedPlayer = new Player();
                    updatedPlayer.setId(id);
                    updatedPlayer.setName(_name);
                    updatedPlayer.setAge(_age);
        
                    TeamOption selectedTeamOption = teamComboBox.getValue();
                    if (selectedTeamOption != null) {
                        Team updatedTeam = new Team();
                        updatedTeam.setId(selectedTeamOption.getId());
                        updatedPlayer.setTeam(updatedTeam);
                    } else {
                        updatedPlayer.setTeam(currentTeam); // Keep current team if no new selection
                    }
        
                    logger.info("Updated Player: name=" + updatedPlayer.getName() + ", age=" + updatedPlayer.getAge() + ", teamId=" + updatedPlayer.getTeam().getId());
        
                    // Persist changes
                    playerService.update(updatedPlayer);
        
                    // Refresh table
                    loadPlayers(true);
                }
            });
      


    }
  
  
  
    @FXML
    void actionSearch(ActionEvent event) {
       
    }

    @FXML
    void keyPressed(KeyEvent event) {
        lblError.setText("");
    }

    @FXML
    public void save(ActionEvent event){
        clearErrorMessages();

        TeamOption selectedTeam = teamsField.getSelectionModel().getSelectedItem();
              Long teamId = null;
              String teamName = "";
            String playerName = playerNameField.getText();
            String playerAge = ageField.getText();
            if (selectedTeam != null) {
                 teamId = teamsField.getValue().getId();      
                 teamName = teamsField.getValue().getName(); 
            }


            Player newPlayer = new Player();
            newPlayer.setName(playerName);
            try {
                newPlayer.setAge(Integer.parseInt(playerAge));
            } catch (NumberFormatException e) {
                nameErrorLabel.setText("Age must be a number.");
                return;
            }
            
            
            newPlayer.setTeam(new Team(teamId, teamName));

            Set<ConstraintViolation<Player>> violations = validator.validate(newPlayer);

            if (!violations.isEmpty()) {
                for (ConstraintViolation<Player> violation : violations) {
                    String propertyName = violation.getPropertyPath().toString();
                    String errorMessage = violation.getMessage();
                    
                    Label errorLabel = errorLabels.get(propertyName);
                    if (errorLabel != null) {
                        errorLabel.setText(errorMessage);
                    }
                }
            } else {
                Boolean addedBoolean =  this.playerService.addPlayer(newPlayer);
                if (addedBoolean) {
                        showInformationAlert("Success","Player Added Successfully",  "the player has been added to the database successfully.",Alert.AlertType.INFORMATION);
                        loadPlayers(true);
                    }else{
                        showInformationAlert("Error","Try again","",Alert.AlertType.ERROR);
                        
                    }
            }
            

            
        
    }

    public void showInformationAlert(String title,String header,String Context, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);

        Scene scene = alert.getDialogPane().getScene();

        if (scene != null) {
            scene.getStylesheets().add(getClass().getResource("/assets/css/style.css").toExternalForm());
        }
        alert.getDialogPane().getStyleClass().add("custom-alert");
      


        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                clearTextInput();
            }
        });
    }   

    public void clearTextInput(){
        playerNameField.setText("");
        ageField.setText("");
        
    }

    private void clearErrorMessages() {
        errorLabels.forEach((key, label) -> {
            if (label != null) {
                label.setText("");
            }
        });
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
    public void toTournamentPage() throws  IOException{
        App.setRoot("/Tournament/index");

    }

    @FXML
    public void toTeamPage() throws IOException{
        App.setRoot("/Team/index");
    }

}
