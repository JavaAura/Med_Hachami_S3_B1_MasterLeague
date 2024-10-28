package com.controller;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.App;
import com.model.Team;
import com.service.ITeamService;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;


public class TeamController {

    private final Logger logger = LoggerFactory.getLogger(TeamController.class);


    private ITeamService teamService;

    private Validator validator;


     @FXML
    private TextField txtSearch;

    @FXML
    private TableView<Team> teamTable;

    @FXML
    private TableColumn<Team, Long> columnId;


    @FXML
    private TableColumn<Team, String> columnName;

    @FXML
    private TableColumn<Team, Integer> columnRanking;

    
    
    @FXML
    private Label lblNote;
    
    @FXML
    private Label lblError;
    
    private List<Team> listTeams = new ArrayList();
    
    private ObservableList<Team> observableListTeams;


    @FXML
    private Button deleteBtn;

    @FXML
    private Button updateBtn;



    private Map<String, Label> errorLabels;

    @FXML
    private TextField teamNameField; 

    @FXML
    private TextField rankingField;

    @FXML
    private Label nameErrorLabel;

    @FXML
    private Label rankingErrorLabel;







    @FXML
    private Button showTeamBtn;


    public TeamController(){
        try {
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            validator = factory.getValidator();
         } catch (Exception e) {
            logger.error("error"+e);
         }
    }


    @FXML
    public void initialize() {
        teamService = (ITeamService) App.getApplicationContext().getBean("teamService");
        loadTeams(false);

        errorLabels = new HashMap<>();
        errorLabels.put("name", nameErrorLabel);
        errorLabels.put("ranking", rankingErrorLabel);
    }

     public boolean loadTeams(boolean cleanTable){
        
        try{
            List<Team> teams = teamService.getAllTeams();

           if(teams != null){
                if (cleanTable){
                    cleanTable();
                }
                
                definingColumn();
            
                setListTeams(teams);

                observableListTeams = FXCollections.observableArrayList(listTeams);
                
                teamTable.setItems(observableListTeams);
           }
        }catch(Exception e){
            // showInformationAlert("Error" , "" , "An error occurred while retrieving data",Alert.AlertType.ERROR);
            e.printStackTrace();
            return false;
        }
        
        return true;
    }
    
      

    public void loadTeams(List<Team> arrayListTeams ){
         try{
            cleanTable();
            observableListTeams = FXCollections.observableArrayList(arrayListTeams);
            teamTable.setItems(observableListTeams);
        }catch(Exception e){
            e.printStackTrace();
            // showInformationAlert("Error" , "" , "An error occurred while retrieving data",Alert.AlertType.ERROR);
        }
    }
    
    public void definingColumn(){
        if(this.columnId !=null){
            columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
            columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
            columnRanking.setCellValueFactory(new PropertyValueFactory<>("ranking"));

        }
    }

    private void cleanTable(){
        teamTable.getItems().clear();
    }


    public List<Team> getListTeams() {
        return listTeams;
    }

    public void setListTeams(List<Team> listTeams) {
        this.listTeams = listTeams ;
    }

   

    @FXML
    void actionSearch(ActionEvent event) {
       
    }

    @FXML
    void keyPressed(KeyEvent event) {
        lblError.setText("");
    }

     @FXML
    void save(ActionEvent event) throws IOException{
        clearErrorMessages();


        String teamName = teamNameField.getText();
        String teamRanking = rankingField.getText();

        Team newTeam =new Team();
        newTeam.setName(teamName);
        try {
            newTeam.setRanking(Integer.parseInt(teamRanking));
        } catch (NumberFormatException e) {
            nameErrorLabel.setText("Ranking must be a number.");
            return;
        }

        Set<ConstraintViolation<Team>> violations = validator.validate(newTeam);

        if (!violations.isEmpty()) {
            for (ConstraintViolation<Team> violation : violations) {
                String propertyName = violation.getPropertyPath().toString();
                String errorMessage = violation.getMessage();
                
                Label errorLabel = errorLabels.get(propertyName);
                if (errorLabel != null) {
                    errorLabel.setText(errorMessage);
                }
            }
            logger.info("viols");
        } else {
           Boolean addedBoolean =  this.teamService.addTeam(newTeam);
           if (addedBoolean) {
                showInformationAlert("Success","Team Added Successfully",  "the team has been added to the database successfully.",Alert.AlertType.INFORMATION);
                loadTeams(true);
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

    private void clearErrorMessages() {
        errorLabels.forEach((key, label) -> {
            if (label != null) {
                label.setText("");
            }
        });
    }

    public void clearTextInput(){
        teamNameField.setText("");
        rankingField.setText("");
        
    }

    @FXML
    public void update(ActionEvent event) throws IOException{
        if (teamTable.getSelectionModel().getSelectedIndex() > -1){
            Long id = teamTable.getSelectionModel().getSelectedItem().getId();
            String name = teamTable.getSelectionModel().getSelectedItem().getName();
            int ranking = teamTable.getSelectionModel().getSelectedItem().getRanking();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Team Information");
            alert.setHeaderText("Edit team "+id);



            Label nameLabel = new Label("Name:");
            nameLabel.setStyle("-fx-text-fill: white;");

            Label rankingLabel = new Label("Ranking:");
            rankingLabel.setStyle("-fx-text-fill: white;");

            TextField nameField = new TextField();
            nameField.setPromptText("Enter title");
            nameField.setText(name);
            nameField.setStyle("-fx-background-color: transparent; -fx-text-fill: #FFFF8D;-fx-border-color: #B7C3D7; -fx-border-radius: 12;-fx-padding: 10;-fx-font-size: 14px;");

            TextField rankingField = new TextField();
            rankingField.setPromptText("Enter ranking");
            rankingField.setText(String.valueOf(ranking));
            rankingField.setStyle("-fx-background-color: transparent; -fx-text-fill: #FFFF8D;-fx-border-color: #B7C3D7; -fx-border-radius: 12;-fx-padding: 10;-fx-font-size: 14px;");


            GridPane gridPane = new GridPane();
            gridPane.setHgap(10);
            gridPane.setVgap(10);
            gridPane.setPadding(new Insets(20, 150, 10, 10));  

            gridPane.add(nameLabel, 0, 0);
            gridPane.add(nameField, 1, 0);

            gridPane.add(rankingLabel, 0, 1);
            gridPane.add(rankingField, 1, 1);


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
                    String NameValue = nameField.getText();
                    String rankString = rankingField.getText();

                    if (NameValue !=null && !rankString.trim().isEmpty() ) {
                        Team t = new Team();
                        t.setId(id);
                        t.setName(NameValue);
                        t.setRanking(Integer.parseInt(rankString));
                        teamService.update(t);
                        loadTeams(true);
                    }
                    
                }
            });


        }
    }

    @FXML
    void delete(ActionEvent event){
        if (teamTable.getSelectionModel().getSelectedIndex() > -1) {
            Long id = teamTable.getSelectionModel().getSelectedItem().getId();
            Team t = new Team();
            t.setId(id);

             Alert dialogoExe = new Alert(Alert.AlertType.WARNING);
            ButtonType btnYes = new ButtonType("Yes");
            ButtonType btnNoAnswer = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            
            dialogoExe.setTitle("Attention!");
            dialogoExe.setHeaderText("Are you sure you want to delete ?");
            dialogoExe.setContentText("Delete " + teamTable.getSelectionModel().getSelectedItem().getName() + "?");
            dialogoExe.getButtonTypes().setAll(btnYes, btnNoAnswer);

            Scene scene = dialogoExe.getDialogPane().getScene();
            if (scene != null) {
                scene.getStylesheets().add(getClass().getResource("/assets/css/style.css").toExternalForm());
            }

            dialogoExe.getDialogPane().getStyleClass().add("custom-alert");

            dialogoExe.showAndWait().ifPresent(b -> {
                if (b == btnYes) {
                    this.teamService.delete(t);
                    loadTeams(true);
                }

            });


        }
    }

   
    @FXML
    public void gotoHome() throws IOException{
        App.setRoot("/home");
    }
    
    @FXML
    public void toTournamentPage() throws  IOException{
        App.setRoot("/Tournament/index");

    }

    @FXML
    public void gotToPlayerPage() throws IOException{
        App.setRoot("/Player/index");
    }

    @FXML
    public void toTeamPage() throws IOException{
        App.setRoot("/Team/index");
    }

}
