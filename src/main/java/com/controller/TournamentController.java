package com.controller;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
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
import com.model.Tournament;
import com.model.enums.TournamentStatut;
import com.service.ITournamentService;

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
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.GridPane;




public class TournamentController {

    private final Logger logger = LoggerFactory.getLogger(TournamentController.class);



    private ITournamentService tournamentService;

     @FXML
    private TextField txtSearch;
   
    private Validator validator;

    @FXML
    private TextField tournamentNameField;

    @FXML
    private DatePicker dateStartField;

    @FXML
    private DatePicker dateEndField;

    @FXML
    private TextField numberSpectatorsField;

    @FXML
    private TextField estimatedDurationField;

    @FXML
    private TextField timePauseField;
    
    @FXML
    private ComboBox<TournamentStatut> tournamentStatutFiled;
    @FXML
    private Label nameErrorLabel;
    @FXML
    private Label dateStartErrorLabel;
    @FXML
    private Label dateEndErrorLabel;
    @FXML
    private Label spectatorsErrorLabel;
    @FXML
    private Label durationErrorLabel;
    @FXML
    private Label pauseErrorLabel;
    @FXML
    private Label statutErrorLabel;

    private Map<String, Label> errorLabels;


  

    
    
    @FXML
    private Label lblNote;
    
    @FXML
    private Label lblError;


      @FXML
    private TableView<Tournament> tournamentTable;

    @FXML
    private TableColumn<Tournament, Long> columnId;

    @FXML
    private TableColumn<Tournament, DatePicker> columnDateStart;

    @FXML
    private TableColumn<Tournament, DatePicker> columnDateEnd;

    @FXML
    private TableColumn<Tournament, Integer> columnNumOfSpectators;

    @FXML
    private TableColumn<Tournament, TournamentStatut> columnStatus;


    @FXML
    private TableColumn<Tournament, String> columnName;
    
    private List<Tournament> listTournaments = new ArrayList();
    
    private ObservableList<Tournament> observableListTournaments;

    // //  @FXML

    @FXML
    private Button deleteBtn;

    @FXML
    private Button updateBtn;




    // @FXML
    // private Button showTeamBtn;

    public TournamentController(){
         try {
            ValidatorFactory factory = Validation.buildDefaultValidatorFactory();
            validator = factory.getValidator();
         } catch (Exception e) {
            logger.error("error"+e);
         }
    }



    @FXML
    public void initialize() {
        tournamentService = (ITournamentService) App.getApplicationContext().getBean("tournamentService");

        tournamentStatutFiled.setItems(FXCollections.observableArrayList(TournamentStatut.values()));

         errorLabels = new HashMap<>();
        errorLabels.put("name", nameErrorLabel);
        errorLabels.put("dateStart", dateStartErrorLabel);
        errorLabels.put("endDate", dateEndErrorLabel);
        errorLabels.put("numberSpectators", spectatorsErrorLabel);
        errorLabels.put("estimatedDuration", durationErrorLabel);
        errorLabels.put("timePause", pauseErrorLabel);
        errorLabels.put("statut", statutErrorLabel);

        loadTournaments(false);

       
    }

     public boolean loadTournaments(boolean cleanTable){
        
        try{
            List<Tournament> tournaments = tournamentService.getAllTournaments();

           if(tournaments != null){
            if (cleanTable){
                cleanTable();
            }
            
            definingColumn();
        
            setListTournaments(tournaments);

            observableListTournaments = FXCollections.observableArrayList(listTournaments);
          
            tournamentTable.setItems(observableListTournaments);
            
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
    
    public void loadTournaments(List<Tournament> arrayListTeams ){
         try{
            cleanTable();
            observableListTournaments = FXCollections.observableArrayList(arrayListTeams);
            tournamentTable.setItems(observableListTournaments);
        }catch(Exception e){
            e.printStackTrace();
         
        }
    }
    
    public void definingColumn(){
        if(this.columnId !=null){
            columnId.setCellValueFactory(new PropertyValueFactory<>("id"));
            columnName.setCellValueFactory(new PropertyValueFactory<>("name"));
            columnDateStart.setCellValueFactory(new PropertyValueFactory<>("dateStart"));
            columnDateEnd.setCellValueFactory(new PropertyValueFactory<>("endDate"));
            columnNumOfSpectators.setCellValueFactory(new PropertyValueFactory<>("numberSpectators"));
            columnStatus.setCellValueFactory(new PropertyValueFactory<>("statut"));


        }
    }

    private void cleanTable(){
        tournamentTable.getItems().clear();
    }


    public List<Tournament> getListTournaments() {
        return listTournaments;
    }

    public void setListTournaments(List<Tournament> listTournaments) {
        this.listTournaments = listTournaments ;
    }


     @FXML
    private void Save(ActionEvent event) {
        clearErrorMessages();

        // Retrieve data from form fields
        String tournamentName = tournamentNameField.getText();
        String dateStart = dateStartField.getValue() != null ? dateStartField.getValue().toString() : "";
        String dateEnd = dateEndField.getValue() != null ? dateEndField.getValue().toString() : "";
        String numberSpectators = numberSpectatorsField.getText();
        String estimatedDuration = estimatedDurationField.getText();
        String timePause = timePauseField.getText();
        TournamentStatut selectedStatut = tournamentStatutFiled.getValue();

        // Date Parsing
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        Date parsedDateStart = null;
        Date parsedDateEnd = null;

        try {
            parsedDateStart = dateFormat.parse(dateStart);
            parsedDateEnd = dateFormat.parse(dateEnd);
        } catch (ParseException e) {
            dateStartErrorLabel.setText("Invalid date format. Use yyyy-MM-dd.");
        }

        Tournament newTournament = new Tournament();
        newTournament.setName(tournamentName);
        newTournament.setDateStart(parsedDateStart);
        newTournament.setEndDate(parsedDateEnd);

        try {
            newTournament.setEstimatedDuration(Integer.parseInt(estimatedDuration));
        } catch (NumberFormatException e) {
            durationErrorLabel.setText("Duration must be a number.");
        }

        try {
            newTournament.setNumberSpectators(Integer.parseInt(numberSpectators));
        } catch (NumberFormatException e) {
            spectatorsErrorLabel.setText("Spectators count must be a number.");
        }

        try {
            newTournament.setTimePause(Integer.parseInt(timePause));
        } catch (NumberFormatException e) {
            pauseErrorLabel.setText("Pause time must be a number.");
        }

        newTournament.setStatut(selectedStatut);

        Set<ConstraintViolation<Tournament>> violations = validator.validate(newTournament);

        if (!violations.isEmpty()) {
            for (ConstraintViolation<Tournament> violation : violations) {
                String propertyName = violation.getPropertyPath().toString();
                String errorMessage = violation.getMessage();
                
                Label errorLabel = errorLabels.get(propertyName);
                if (errorLabel != null) {
                    errorLabel.setText(errorMessage);
                }
            }
        } else {
           Boolean addedBoolean =  this.tournamentService.addTournament(newTournament);
           if (addedBoolean) {
                showInformationAlert("Success","Tournament Added Successfully",  "the tournament has been added to the database successfully.",Alert.AlertType.INFORMATION);
                loadTournaments(true);
            }else{
                showInformationAlert("Error","Try again","",Alert.AlertType.ERROR);
                
            }
        }
    }
 

    @FXML
    public void delete(ActionEvent event){
        if (tournamentTable.getSelectionModel().getSelectedIndex() > -1) {
            Long id = tournamentTable.getSelectionModel().getSelectedItem().getId(); 
            Tournament T = new Tournament();
            T.setId(id);

             Alert dialogoExe = new Alert(Alert.AlertType.WARNING);
            ButtonType btnYes = new ButtonType("Yes");
            ButtonType btnNoAnswer = new ButtonType("Cancel", ButtonBar.ButtonData.CANCEL_CLOSE);
            
            dialogoExe.setTitle("Attention!");
            dialogoExe.setHeaderText("Are you sure you want to delete ?");
            dialogoExe.setContentText("Delete " + tournamentTable.getSelectionModel().getSelectedItem().getName() + "?");
            dialogoExe.getButtonTypes().setAll(btnYes, btnNoAnswer);

            Scene scene = dialogoExe.getDialogPane().getScene();
            if (scene != null) {
                scene.getStylesheets().add(getClass().getResource("/assets/css/style.css").toExternalForm());
            }

            // Apply custom style class to the alert dialog
            dialogoExe.getDialogPane().getStyleClass().add("custom-alert");

            dialogoExe.showAndWait().ifPresent(b -> {
                if (b == btnYes) {
                    this.tournamentService.delete(T);
                    loadTournaments(true);
                }

            });
        }
    }
    private void clearErrorMessages() {
        errorLabels.forEach((key, label) -> {
            if (label != null) {
                label.setText("");
            }
        });
    }

    public void showInformationAlert(String title,String header,String Context, Alert.AlertType type) {
        Alert alert = new Alert(type);
        alert.setTitle(title);
        alert.setHeaderText(header);

        alert.showAndWait().ifPresent(response -> {
            if (response == ButtonType.OK) {
                clearTextInput();
            }
        });
    }   

    public void clearTextInput(){
        tournamentNameField.setText("");
        numberSpectatorsField.setText("");
        estimatedDurationField.setText("");
        timePauseField.setText("");
    }

   


    @FXML
    void actionSearch(ActionEvent event) {
       
    }

    @FXML
    void keyPressed(KeyEvent event) {
        lblError.setText("");
    }


 
    @FXML
    private void update(ActionEvent event) {
        logger.info("entr 1");
    if (tournamentTable.getSelectionModel().getSelectedIndex() == -1) return;
    logger.info("entr 2");

    // Get selected tournament data
    Tournament selectedTournament = tournamentTable.getSelectionModel().getSelectedItem();
    Long id = selectedTournament.getId();
    String name = selectedTournament.getName();
    Date startDate =  selectedTournament.getDateStart();
    Date endDate =  selectedTournament.getEndDate();
    int numberSpectators = selectedTournament.getNumberSpectators();
    TournamentStatut status = selectedTournament.getStatut();

    // Set up alert dialog
    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
    alert.setTitle("Tournament Information");
    alert.setHeaderText("Edit Tournament " + id);

     Label nameLabel = new Label("Name:");
    // Label startDateLabel = new Label("Start Date:");
    // Label endDateLabel = new Label("End Date:");
    Label spectatorsLabel = new Label("Number of Spectators:");
    Label statusLabel = new Label("Status:");
    nameLabel.setStyle("-fx-text-fill: white;");
    spectatorsLabel.setStyle("-fx-text-fill: white;");
    statusLabel.setStyle("-fx-text-fill: white;");


    TextField nameField = new TextField(name);
    nameField.setPromptText("Enter tournament name");
    nameField.setStyle("-fx-background-color: transparent; -fx-text-fill: #FFFF8D; -fx-border-color: #B7C3D7; -fx-border-radius: 12; -fx-padding: 10; -fx-font-size: 14px;");

    // LocalDate startDateLocal = startDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
    // LocalDate endDateLocal = endDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();

    // // // Use LocalDate values for DatePicker
    // DatePicker startDatePicker = new DatePicker(startDateLocal);
    // DatePicker endDatePicker = new DatePicker(endDateLocal);

   

    TextField spectatorsField = new TextField(String.valueOf(numberSpectators));
    spectatorsField.setPromptText("Enter number of spectators");
    spectatorsField.setStyle("-fx-background-color: transparent; -fx-text-fill: #FFFF8D; -fx-border-color: #B7C3D7; -fx-border-radius: 12; -fx-padding: 10; -fx-font-size: 14px;");

    ComboBox<TournamentStatut> statusComboBox = new ComboBox<>(FXCollections.observableArrayList(TournamentStatut.values()));
    statusComboBox.setValue(status);
    statusComboBox.setStyle("-fx-background-color: white; -fx-text-fill: #FFFF8D; -fx-border-color: #B7C3D7; -fx-border-radius: 12; -fx-padding: 10; -fx-font-size: 14px;");



     // Layout setup
     GridPane gridPane = new GridPane();
     gridPane.setHgap(10);
     gridPane.setVgap(10);
     gridPane.setPadding(new Insets(20, 150, 10, 10));

    gridPane.add(nameLabel, 0, 0);
    gridPane.add(nameField, 1, 0);
    // gridPane.add(startDateLabel, 0, 1);
    // gridPane.add(startDatePicker, 1, 1);
    // gridPane.add(endDateLabel, 0, 2);
    // gridPane.add(endDatePicker, 1, 2);
    gridPane.add(spectatorsLabel, 0, 3);
    gridPane.add(spectatorsField, 1, 3);
    gridPane.add(statusLabel, 0, 4);
    gridPane.add(statusComboBox, 1, 4);
   
 
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

    // // Define input fields and labels with styling
   
 
   


    alert.showAndWait().ifPresent(response -> {
        if (response == ButtonType.OK) {
        }
    });

    // alert.showAndWait().ifPresent(response -> {
    //     if (response == ButtonType.OK) {
    //         String updatedName = nameField.getText().trim();
    //         LocalDate updatedStartDate = startDatePicker.getValue();
    //         LocalDate updatedEndDate = endDatePicker.getValue();
    //         String updatedSpectatorsText = spectatorsField.getText().trim();
    //         TournamentStatut updatedStatus = statusComboBox.getValue();
    
    //         if (updatedName.isEmpty() || updatedSpectatorsText.isEmpty() || updatedStartDate == null || updatedEndDate == null) {
    //             logger.warn("Some fields are empty, update aborted.");
    //             return;
    //         }
    
    //         // Parse spectators count
    //         int updatedNumberSpectators;
    //         try {
    //             updatedNumberSpectators = Integer.parseInt(updatedSpectatorsText);
    //         } catch (NumberFormatException e) {
    //             logger.warn("Invalid number of spectators input, update aborted.");
    //             return;
    //         }
    
    //         // Set updated values in selectedTournament
    //         selectedTournament.setName(updatedName);
    //         selectedTournament.setDateStart(Date.from(updatedStartDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
    //         selectedTournament.setEndDate(Date.from(updatedEndDate.atStartOfDay(ZoneId.systemDefault()).toInstant()));
    //         selectedTournament.setNumberSpectators(updatedNumberSpectators);
    //         selectedTournament.setStatut(updatedStatus);
    
    //         // Log and persist changes
    //         logger.info("Updated Tournament: name=" + updatedName + ", startDate=" + updatedStartDate +
    //                 ", endDate=" + updatedEndDate + ", spectators=" + updatedNumberSpectators + ", status=" + updatedStatus);
    
    //         tournamentService.update(selectedTournament);
    
    //         // Refresh table
    //         loadTournaments(true);
    //     }
    // });

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
