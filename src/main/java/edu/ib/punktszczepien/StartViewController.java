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

    @FXML
    void clinicButtonClicked(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("clinicView.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Clinic Application");
        stage.setScene(scene);
        stage.show();
    }

    @FXML
    void patientBtnClicked(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("patientView.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
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