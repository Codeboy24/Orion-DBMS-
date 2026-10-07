package org.example;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Orion extends Application {
    
    @Override
    public void start(Stage stage) throws Exception {
        // Load the UI from the FXML file
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("/org/example/views/orion_dbms.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 450);
        
        stage.setTitle("Orion DBMS");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }
}