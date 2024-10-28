package com;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class App extends Application  {
    private static final Logger logger = LoggerFactory.getLogger(App.class);

    private static Scene scene;


    private Stage stage;

    private static ApplicationContext context;


    @Override
    public void start(Stage primaryStage){
        context = new ClassPathXmlApplicationContext("applicationContext.xml");


        try {
            this.stage = primaryStage;

            scene = new Scene(loadFXML("/home"), 1666, 1500);
            this.stage.setTitle("MasterLeague");
            this.stage.setScene(scene);
            this.stage.show();
        } catch (Exception e) {
            logger.error("Failed to start app: " + e.getLocalizedMessage());
            e.printStackTrace();
        }
    }




    public static void setRoot(String fxml) throws IOException {
        logger.info("String fxml "+fxml);
        scene.setRoot(loadFXML(fxml));
    }

    public static Parent loadFXML(String fxml) {
        logger.info("Loading FXML from: " + fxml + ".fxml");
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource(fxml + ".fxml"));
        try {
            return fxmlLoader.load();
        } catch (IOException e) {
            logger.error("Failed to load FXML: " + e.getMessage());
            return  null;
        }

    }

    public static ApplicationContext getApplicationContext() {
        return context;
    }
    
    public static void main(String[] args) {
        launch();
    }


}
