package edu.ib.punktszczepien;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

public class ClinicViewController {

    private Stage stage;
    private Scene scene;
    private Parent root;
    private DBUtil dbUtil;
    private ClinicViewDAO clinicViewDAO;

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private TabPane clinicVaccineTableTab;

    @FXML
    private Button connectButton;

    @FXML
    private TextArea consoleTextArea;

    @FXML
    private Button disconnectButton;

    @FXML
    private TableColumn<VaccineHistory, String> historyDate;

    @FXML
    private TableColumn<VaccineHistory, String> historyLastName;

    @FXML
    private TableColumn<VaccineHistory, String> historyName;

    @FXML
    private TableColumn<VaccineHistory, String> historyPesel;

    @FXML
    private TableColumn<VaccineHistory, String> historyPhone;

    @FXML
    private TableColumn<VaccineHistory, String> historyVaccine;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private TableColumn<VaccineRegistration, String> registrationsDate;

    @FXML
    private TableColumn<VaccineRegistration, String> registrationsLastName;

    @FXML
    private TableColumn<VaccineRegistration, String> registrationsName;

    @FXML
    private TableColumn<VaccineRegistration, String> registrationsPesel;

    @FXML
    private TableColumn<VaccineRegistration, String> registrationsPhone;

    @FXML
    private TableColumn<VaccineRegistration, String> registrationsVaccine;

    @FXML
    private Tab vaccineHistoryTab;

    @FXML
    private TableView<VaccineHistory> vaccineHistoryTable;

    @FXML
    private Tab vaccineRegistrationTab;

    @FXML
    private TableView<VaccineRegistration> vaccineRegistrationTable;

    @FXML
    private DatePicker datePicker;

    @FXML
    private TextField hourField;

    @FXML
    private TextField minutesField;

    @FXML
    private Button vaccineDateBtn;

    @FXML
    private Button vaccineStatsBtn;

    @FXML
    void connectBtnClicked(ActionEvent event) throws ClassNotFoundException, SQLException {
        String clinicUser = "root";
        dbUtil = new DBUtil(clinicUser, passwordTextField.getText(), consoleTextArea);
        dbUtil.dbConnect();
        clinicViewDAO = new ClinicViewDAO(dbUtil, consoleTextArea);
        consoleTextArea.appendText("Access granted for clinic" + "\n");
        connectButton.setDisable(true);
        disconnectButton.setDisable(false);
        clinicVaccineTableTab.setDisable(false);
        datePicker.setDisable(false);
        hourField.setDisable(false);
        minutesField.setDisable(false);
        vaccineDateBtn.setDisable(false);
        vaccineStatsBtn.setDisable(false);

        ObservableList<VaccineRegistration> vaccineRegistrations =  clinicViewDAO.getVaccineRegistrations();
        vaccineRegistrationTable.setItems(vaccineRegistrations);
    }

    @FXML
    void disconnectBtnClicked(ActionEvent event) throws SQLException {
        datePicker.setDisable(true);
        minutesField.setDisable(true);
        hourField.setDisable(true);
        disconnectButton.setDisable(true);
        vaccineDateBtn.setDisable(true);
        vaccineStatsBtn.setDisable(true);
        clinicVaccineTableTab.setDisable(true);
        connectButton.setDisable(false);

        dbUtil.dbDisconnect();
    }

    @FXML
    void clinicComeBackBtnClicked(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("startView.fxml"));
        stage = (Stage)((Node)event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Vaccine Application");
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    void addVaccineDate(ActionEvent event) {
        try {
            String hours = hourField.getText();
            String minutes = minutesField.getText();
            LocalDate calendarDate = datePicker.getValue();
            LocalDateTime date = calendarDate.atTime(Integer.parseInt(hours),Integer.parseInt(minutes));

            clinicViewDAO.insertVaccineDate(date.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
            hourField.clear();
            minutesField.clear();
            datePicker.getEditor().clear();
            datePicker.setValue(null);
        } catch (Exception e) {
            consoleTextArea.appendText("Wprowadzono niepoprawną datę.\n");
        }
    }

    @FXML
    void showVaccineStats(ActionEvent event) throws IOException, SQLException {

        root = FXMLLoader.load(getClass().getResource("vaccineStatsView.fxml"));
        Stage stage = new Stage();
        stage.setTitle("Vaccine stats");
        stage.setScene(new Scene(root, 600, 400));
        stage.setUserData(clinicViewDAO.getVaccineStats());
        stage.show();
    }

    @FXML
    void initialize() {
        assert clinicVaccineTableTab != null : "fx:id=\"clinicVaccineTableTab\" was not injected: check your FXML file 'clinicView.fxml'.";
        assert connectButton != null : "fx:id=\"connectButton\" was not injected: check your FXML file 'clinicView.fxml'.";
        assert consoleTextArea != null : "fx:id=\"consoleTextArea\" was not injected: check your FXML file 'clinicView.fxml'.";
        assert datePicker != null : "fx:id=\"datePicker\" was not injected: check your FXML file 'clinicView.fxml'.";
        assert disconnectButton != null : "fx:id=\"disconnectButton\" was not injected: check your FXML file 'clinicView.fxml'.";
        assert historyDate != null : "fx:id=\"historyDate\" was not injected: check your FXML file 'clinicView.fxml'.";
        assert historyLastName != null : "fx:id=\"historyLastName\" was not injected: check your FXML file 'clinicView.fxml'.";
        assert historyName != null : "fx:id=\"historyName\" was not injected: check your FXML file 'clinicView.fxml'.";
        assert historyPesel != null : "fx:id=\"historyPesel\" was not injected: check your FXML file 'clinicView.fxml'.";
        assert historyPhone != null : "fx:id=\"historyPhone\" was not injected: check your FXML file 'clinicView.fxml'.";
        assert historyVaccine != null : "fx:id=\"historyVaccine\" was not injected: check your FXML file 'clinicView.fxml'.";
        assert hourField != null : "fx:id=\"hourField\" was not injected: check your FXML file 'clinicView.fxml'.";
        assert minutesField != null : "fx:id=\"minutesField\" was not injected: check your FXML file 'clinicView.fxml'.";
        assert passwordTextField != null : "fx:id=\"passwordTextField\" was not injected: check your FXML file 'clinicView.fxml'.";
        assert registrationsDate != null : "fx:id=\"registrationsDate\" was not injected: check your FXML file 'clinicView.fxml'.";
        assert registrationsLastName != null : "fx:id=\"registrationsLastName\" was not injected: check your FXML file 'clinicView.fxml'.";
        assert registrationsName != null : "fx:id=\"registrationsName\" was not injected: check your FXML file 'clinicView.fxml'.";
        assert registrationsPesel != null : "fx:id=\"registrationsPesel\" was not injected: check your FXML file 'clinicView.fxml'.";
        assert registrationsPhone != null : "fx:id=\"registrationsPhone\" was not injected: check your FXML file 'clinicView.fxml'.";
        assert registrationsVaccine != null : "fx:id=\"registrationsVaccine\" was not injected: check your FXML file 'clinicView.fxml'.";
        assert vaccineHistoryTab != null : "fx:id=\"vaccineHistoryTab\" was not injected: check your FXML file 'clinicView.fxml'.";
        assert vaccineHistoryTable != null : "fx:id=\"vaccineHistoryTable\" was not injected: check your FXML file 'clinicView.fxml'.";
        assert vaccineRegistrationTab != null : "fx:id=\"vaccineRegistrationTab\" was not injected: check your FXML file 'clinicView.fxml'.";
        assert vaccineRegistrationTable != null : "fx:id=\"vaccineRegistrationTable\" was not injected: check your FXML file 'clinicView.fxml'.";
        assert vaccineDateBtn != null : "fx:id=\"vaccineDateBtn\" was not injected: check your FXML file 'clinicView.fxml'.";
        assert vaccineStatsBtn != null : "fx:id=\"vaccineStatsBtn\" was not injected: check your FXML file 'clinicView.fxml'.";

        clinicVaccineTableTab.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Tab>() {
                    @Override
                    public void changed(ObservableValue<? extends Tab> ov, Tab oldTab, Tab newTab) {
                        String id = newTab.getId();

                        if (id.equals(vaccineHistoryTab.getId())) {
                            ObservableList<VaccineHistory> vaccineHistories = null;
                            try {
                                vaccineHistories = clinicViewDAO.getVaccineHistory();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }
                            vaccineHistoryTable.setItems(vaccineHistories);
                        } else {
                            ObservableList<VaccineRegistration> vaccineRegistrations = null;
                            try {
                                vaccineRegistrations = clinicViewDAO.getVaccineRegistrations();
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }

                            vaccineRegistrationTable.setItems(vaccineRegistrations);
                        }
                    }
                }
        );

        historyName.setCellValueFactory(
                new PropertyValueFactory<VaccineHistory, String>("imie")
        );
        historyLastName.setCellValueFactory(
                new PropertyValueFactory<VaccineHistory, String>("nazwisko")
        );
        historyPesel.setCellValueFactory(
                new PropertyValueFactory<VaccineHistory, String>("pesel")
        );
        historyPhone.setCellValueFactory(
                new PropertyValueFactory<VaccineHistory, String>("nr_tel")
        );
        historyVaccine.setCellValueFactory(
                new PropertyValueFactory<VaccineHistory, String>("nazwa")
        );
        historyDate.setCellValueFactory(
                new PropertyValueFactory<VaccineHistory, String>("termin")
        );

        registrationsName.setCellValueFactory(
                new PropertyValueFactory<VaccineRegistration, String>("imie")
        );
        registrationsLastName.setCellValueFactory(
                new PropertyValueFactory<VaccineRegistration, String>("nazwisko")
        );
        registrationsPesel.setCellValueFactory(
                new PropertyValueFactory<VaccineRegistration, String>("pesel")
        );
        registrationsPhone.setCellValueFactory(
                new PropertyValueFactory<VaccineRegistration, String>("nr_tel")
        );
        registrationsVaccine.setCellValueFactory(
                new PropertyValueFactory<VaccineRegistration, String>("nazwa")
        );
        registrationsDate.setCellValueFactory(
                new PropertyValueFactory<VaccineRegistration, String>("termin")
        );

    }

}
