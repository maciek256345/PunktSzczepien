package edu.ib.punktszczepien;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class VaccinePointApplication extends Application {
    /**
     * Klasa odpowiadająca za uruchomienie
     * aplikacji punktu szczepień
     *
     * @author MS
     * @version 1.0
     * @since 2022-02-08
     */
    @Override
    public void start(Stage stage) throws IOException {

        FXMLLoader fxmlLoader = new FXMLLoader(VaccinePointApplication.class.getResource("startView.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 600, 400);
        stage.setTitle("Vaccine Application");
        stage.setScene(scene);
        stage.show();
    }

    public static void main(String[] args) {
        launch();
    }
}