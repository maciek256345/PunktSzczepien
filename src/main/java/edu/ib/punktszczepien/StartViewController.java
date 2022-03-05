package edu.ib.punktszczepien;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

public class StartViewController {
    /**
     * Klasa odpowiedzialna za zarządzanie
     * oknem startowym aplikacji. Zawiera metody
     * umożliwiające przejście do części aplikacji
     * przeznaczonej dla pacjenta lub dla kliniki.
     *
     * @author MS
     * @version 1.0
     * @since 2022-02-08
     */
    private Stage stage;
    private Scene scene;
    private Parent root;


    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button clinicBtn;

    @FXML
    private Label label;

    @FXML
    private Button patientBtn;

    /**
     * Metoda umożliwiająca przejście do
     * części aplikacji przeznaczonej dla
     * pracowników kliniki po wciśnięciu
     * przycisku.
     *
     * @param event
     * @throws IOException
     */
    @FXML
    void clinicButtonClicked(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("clinicView.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Clinic Application");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * Metoda umożliwiająca przejście do
     * części aplikacji przeznaczonej dla
     * pacjentów po wciśnięciu przycisku.
     *
     * @param event
     * @throws IOException
     */
    @FXML
    void patientBtnClicked(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("patientView.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Patient Application");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void initialize() {
        assert clinicBtn != null : "fx:id=\"clinicBtn\" was not injected: check your FXML file 'startView.fxml'.";
        assert label != null : "fx:id=\"label\" was not injected: check your FXML file 'startView.fxml'.";
        assert patientBtn != null : "fx:id=\"patientBtn\" was not injected: check your FXML file 'startView.fxml'.";

    }

}