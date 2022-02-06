package edu.ib.punktszczepien;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.ResourceBundle;

public class PatientViewController {
    private Stage stage;
    private Scene scene;
    private Parent root;
    private DBUtil dbUtil;
    private PatientViewDao patientViewDao;


    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private Button changeDateBtn;

    @FXML
    private Button confrimationBtn;

    @FXML
    private Button connectButton;

    @FXML
    private TextArea consoleTextArea;

    @FXML
    private TextField dateTextField;

    @FXML
    private Button disconnectButton;

    @FXML
    private TextField nameTextField;

    @FXML
    private Button newPatientBtn;

    @FXML
    private Button oldPatientBtn;

    @FXML
    private PasswordField passwordTextField;

    @FXML
    private TextField peselTextField;

    @FXML
    private TextField phoneNumberTextField;

    @FXML
    private Button registerVaccinationBtn;

    @FXML
    private Button savePatientData;

    @FXML
    private Button showHistoryBtn;

    @FXML
    private TextField surnameTextField;

    @FXML
    private TextField userTextField;

    @FXML
    private AnchorPane usersMenu;

    @FXML
    private TableColumn<PatientView, String> vaccinationDateCol;

    @FXML
    private ChoiceBox<String> vaccinationDateChoiceBox;

    @FXML
    private ChoiceBox<String> vaccinationTypeChoiceBox;

    @FXML
    private TableView<PatientView> vaccinationHistoryTableView;

    @FXML
    private TableColumn<PatientView, String> vaccinationTypeCol;

    @FXML
    void changeDateBtnClicked(ActionEvent event) throws SQLException, ClassNotFoundException {
        patientViewDao = new PatientViewDao(dbUtil, consoleTextArea);
        VaccinationDateDAO vaccinationDateDAO = new VaccinationDateDAO(dbUtil, consoleTextArea);
   
        String pesel = peselTextField.getText();
        String data = vaccinationDateChoiceBox.getValue();
        String szczepionka = vaccinationTypeChoiceBox.getValue();

        String osatnieSzczepienieTegoSamegoTypu = vaccinationDateDAO.lastVaccineSameType(szczepionka, peselTextField.getText());
        String ostatnieSzczepienieDowolnegoTypu = vaccinationDateDAO.lastVaccineDifferentType(peselTextField.getText());

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dataWybranaLocalDateTime = LocalDateTime.parse(data, formatter);

        if (ostatnieSzczepienieDowolnegoTypu.equals("")) {
            patientViewDao.changeDate(data, pesel, szczepionka);
            consoleTextArea.appendText("\nPomyślnie zmieniono termin");
        } else {
            ostatnieSzczepienieDowolnegoTypu = ostatnieSzczepienieDowolnegoTypu.replace("T", " ");
            osatnieSzczepienieTegoSamegoTypu = osatnieSzczepienieTegoSamegoTypu.replace("T", " ");

            LocalDateTime dataLocalDateTime = LocalDateTime.parse(ostatnieSzczepienieDowolnegoTypu, formatter);

            long liczbaDni2;
            if (osatnieSzczepienieTegoSamegoTypu.equals("")) {
                liczbaDni2 = 366;
            } else {
                LocalDateTime dataLocalDateTime2 = LocalDateTime.parse(osatnieSzczepienieTegoSamegoTypu, formatter);
                liczbaDni2 = ChronoUnit.DAYS.between(dataLocalDateTime2, dataWybranaLocalDateTime);
            }

            long liczbaDni = ChronoUnit.DAYS.between(dataLocalDateTime, dataWybranaLocalDateTime);

            if (liczbaDni > 21 || liczbaDni2 < 365) {
                if (!osatnieSzczepienieTegoSamegoTypu.equals("")) {
                    if (liczbaDni2 > 365) {
                        patientViewDao.changeDate(data, pesel, szczepionka);
                        consoleTextArea.appendText("\nPomyślnie zmieniono termin");
                        return;
                    } else {
                        consoleTextArea.appendText("\nNiezachowny odstęp czasowy między szczepieniami tego samego typu (365 dni)");
                        return;
                    }
                } else {
                    patientViewDao.changeDate(data, pesel, szczepionka);
                    consoleTextArea.appendText("\nPomyślnie zmieniono termin");
                }
            } else {
                consoleTextArea.appendText("\nNiezachowany odstęp czasowy między szczepieniami różego typu (21 dni)");
            }

        }
    }

    @FXML
    void confirmationBtnClicked(ActionEvent event) {
        vaccinationTypeChoiceBox.setValue("Szczepionki");
        vaccinationDateChoiceBox.setValue("Terminy");

        newPatientBtn.setDisable(false);
        oldPatientBtn.setDisable(false);
    }


    @FXML
    void connectBtnClicked(ActionEvent event) throws SQLException, ClassNotFoundException {
        dbUtil = new DBUtil(userTextField.getText(), passwordTextField.getText(), consoleTextArea);
        dbUtil.dbConnect();
        consoleTextArea.appendText("Access granted for user \"" + userTextField.getText() + "\"." + "\n");
        connectButton.setDisable(true);
        disconnectButton.setDisable(false);
        peselTextField.setDisable(false);
        confrimationBtn.setDisable(false);

    }

    @FXML
    void disconnectBtnClicked(ActionEvent event) throws SQLException {
        dbUtil.dbDisconnect();
        connectButton.setDisable(false);
        disconnectButton.setDisable(true);

        peselTextField.setDisable(true);
        confrimationBtn.setDisable(true);
        nameTextField.setDisable(true);
        surnameTextField.setDisable(true);
        dateTextField.setDisable(true);
        savePatientData.setDisable(true);
        phoneNumberTextField.setDisable(true);
        vaccinationTypeChoiceBox.setDisable(true);
        vaccinationDateChoiceBox.setDisable(true);
        vaccinationHistoryTableView.setDisable(true);
        showHistoryBtn.setDisable(true);
        registerVaccinationBtn.setDisable(true);
        changeDateBtn.setDisable(true);
        newPatientBtn.setDisable(false);
        oldPatientBtn.setDisable(false);
    }

    @FXML
    void newPatientClicked(ActionEvent event) {
        nameTextField.setDisable(false);
        surnameTextField.setDisable(false);
        dateTextField.setDisable(false);
        savePatientData.setDisable(false);
        phoneNumberTextField.setDisable(false);

    }

    @FXML
    void oldPatientClicked(ActionEvent event) throws SQLException, ClassNotFoundException {
        vaccinationTypeChoiceBox.setDisable(false);
        vaccinationDateChoiceBox.setDisable(false);
        vaccinationHistoryTableView.setDisable(false);
        showHistoryBtn.setDisable(false);
        registerVaccinationBtn.setDisable(false);
        changeDateBtn.setDisable(false);

        VaccineDAO vaccineDAO = new VaccineDAO(dbUtil, consoleTextArea);
        ObservableList<String> szczepionki = vaccineDAO.availableVaccines();
        vaccinationTypeChoiceBox.setItems(szczepionki);
        VaccinationDateDAO vaccinationDateDAO = new VaccinationDateDAO(dbUtil, consoleTextArea);
        ObservableList<String> terminy = vaccinationDateDAO.availableDates();
        vaccinationDateChoiceBox.setItems(terminy);

    }

    @FXML
    void patientComeBackBtnClicked(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("startView.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Vaccine Application");
        stage.setScene(scene);
        stage.show();

    }

    @FXML
    void registerBtnClicked(ActionEvent event) throws SQLException, ClassNotFoundException {
        patientViewDao = new PatientViewDao(dbUtil, consoleTextArea);
        VaccinationDateDAO vaccinationDateDAO = new VaccinationDateDAO(dbUtil, consoleTextArea);

        String data = vaccinationDateChoiceBox.getValue();
        String szczepionka = vaccinationTypeChoiceBox.getValue();

        String osatnieSzczepienieTegoSamegoTypu = vaccinationDateDAO.lastVaccineSameType(szczepionka, peselTextField.getText());
        String ostatnieSzczepienieDowolnegoTypu = vaccinationDateDAO.lastVaccineDifferentType(peselTextField.getText());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dataWybranaLocalDateTime = LocalDateTime.parse(data, formatter);
        System.out.println(osatnieSzczepienieTegoSamegoTypu);

        if (ostatnieSzczepienieDowolnegoTypu.equals("")) {
                patientViewDao.insertVaccineRegistration(szczepionka, data, peselTextField.getText());
                consoleTextArea.appendText("\nPomyślnie zarejestrowano na szczepienie");
        } else {
            ostatnieSzczepienieDowolnegoTypu = ostatnieSzczepienieDowolnegoTypu.replace("T", " ");
            osatnieSzczepienieTegoSamegoTypu = osatnieSzczepienieTegoSamegoTypu.replace("T", " ");

            LocalDateTime dataLocalDateTime = LocalDateTime.parse(ostatnieSzczepienieDowolnegoTypu, formatter);

            long liczbaDni2;
            if (osatnieSzczepienieTegoSamegoTypu.equals("")) {
                liczbaDni2 = 366;
            } else {
                LocalDateTime dataLocalDateTime2 = LocalDateTime.parse(osatnieSzczepienieTegoSamegoTypu, formatter);
                liczbaDni2 = ChronoUnit.DAYS.between(dataLocalDateTime2, dataWybranaLocalDateTime);
            }

            long liczbaDni = ChronoUnit.DAYS.between(dataLocalDateTime, dataWybranaLocalDateTime);

            if (liczbaDni > 21 || liczbaDni2 < 365) {
                if (!osatnieSzczepienieTegoSamegoTypu.equals("")) {
                    if (liczbaDni2 > 365) {
                        patientViewDao.insertVaccineRegistration(szczepionka, data, peselTextField.getText());
                        consoleTextArea.appendText("\nPomyślnie zarejestrowano na szczepienie");
                        return;
                    } else {
                        consoleTextArea.appendText("\nNiezachowny odstęp czasowy między szczepieniami tego samego typu (365 dni)");
                        return;
                    }
                } else {
                    patientViewDao.insertVaccineRegistration(szczepionka, data, peselTextField.getText());
                    consoleTextArea.appendText("\nPomyślnie zarejestrowano na szczepienie");
                }
            } else {
                consoleTextArea.appendText("\nNiezachowany odstęp czasowy między szczepieniami różego typu (21 dni)");
            }
        }

    }

    @FXML
    void saveDataBtnClicked(ActionEvent event) throws SQLException, ClassNotFoundException {
        vaccinationTypeChoiceBox.setDisable(false);
        vaccinationDateChoiceBox.setDisable(false);
        vaccinationHistoryTableView.setDisable(false);
        showHistoryBtn.setDisable(false);
        registerVaccinationBtn.setDisable(false);
        changeDateBtn.setDisable(false);

        String imie = nameTextField.getText();
        String nazwisko = surnameTextField.getText();
        String pesel = peselTextField.getText();
        String numerTelefonu = phoneNumberTextField.getText();
        String dataUrodzenia = dateTextField.getText();

        try {

            if (!nameTextField.getText().equals(null)) {
                patientViewDao = new PatientViewDao(dbUtil, consoleTextArea);
                patientViewDao.insertPatient(pesel, imie, nazwisko, numerTelefonu, dataUrodzenia);
                consoleTextArea.appendText("New Patient " + imie + " " + nazwisko + " inserted." + "\n");
            }

        } catch (SQLException e) {
            consoleTextArea.appendText("Error occurred while inserting patient.\n");
            throw e;
        }

        VaccineDAO vaccineDAO = new VaccineDAO(dbUtil, consoleTextArea);
        ObservableList<String> szczepionki = vaccineDAO.availableVaccines();
        vaccinationTypeChoiceBox.setItems(szczepionki);
        VaccinationDateDAO vaccinationDateDAO = new VaccinationDateDAO(dbUtil, consoleTextArea);
        ObservableList<String> terminy = vaccinationDateDAO.availableDates();
        vaccinationDateChoiceBox.setItems(terminy);

    }

    @FXML
    void showHistoryBtnClicked(ActionEvent event) throws SQLException, ClassNotFoundException {
        try {
            vaccinationHistoryTableView.getItems().clear();
            vaccinationTypeCol.setCellValueFactory(new PropertyValueFactory<PatientView, String>("typ_szczepienia"));
            vaccinationDateCol.setCellValueFactory(new PropertyValueFactory<PatientView, String>("termin"));
            patientViewDao = new PatientViewDao(dbUtil, consoleTextArea);
            ObservableList<PatientView> racketData = patientViewDao.showAllVaccines(peselTextField.getText());
            vaccinationHistoryTableView.setItems(racketData);

        } catch (SQLException | ClassNotFoundException e) {
            consoleTextArea.appendText("Error occurred while getting vaccines from DB.\n");
            throw e;
        }

    }

    @FXML
    void initialize() throws SQLException, ClassNotFoundException {
        assert changeDateBtn != null : "fx:id=\"changeDateBtn\" was not injected: check your FXML file 'patientView.fxml'.";
        assert connectButton != null : "fx:id=\"connectButton\" was not injected: check your FXML file 'patientView.fxml'.";
        assert consoleTextArea != null : "fx:id=\"consoleTextArea\" was not injected: check your FXML file 'patientView.fxml'.";
        assert dateTextField != null : "fx:id=\"dateTextField\" was not injected: check your FXML file 'patientView.fxml'.";
        assert disconnectButton != null : "fx:id=\"disconnectButton\" was not injected: check your FXML file 'patientView.fxml'.";
        assert nameTextField != null : "fx:id=\"nameTextField\" was not injected: check your FXML file 'patientView.fxml'.";
        assert newPatientBtn != null : "fx:id=\"newPatientBtn\" was not injected: check your FXML file 'patientView.fxml'.";
        assert oldPatientBtn != null : "fx:id=\"oldPatientBtn\" was not injected: check your FXML file 'patientView.fxml'.";
        assert passwordTextField != null : "fx:id=\"passwordTextField\" was not injected: check your FXML file 'patientView.fxml'.";
        assert peselTextField != null : "fx:id=\"peselTextField\" was not injected: check your FXML file 'patientView.fxml'.";
        assert phoneNumberTextField != null : "fx:id=\"phoneNumberTextField\" was not injected: check your FXML file 'patientView.fxml'.";
        assert registerVaccinationBtn != null : "fx:id=\"registerVaccinationBtn\" was not injected: check your FXML file 'patientView.fxml'.";
        assert savePatientData != null : "fx:id=\"savePatientData\" was not injected: check your FXML file 'patientView.fxml'.";
        assert showHistoryBtn != null : "fx:id=\"showHistoryBtn\" was not injected: check your FXML file 'patientView.fxml'.";
        assert surnameTextField != null : "fx:id=\"surnameTextField\" was not injected: check your FXML file 'patientView.fxml'.";
        assert userTextField != null : "fx:id=\"userTextField\" was not injected: check your FXML file 'patientView.fxml'.";
        assert vaccinationDateCol != null : "fx:id=\"vaccinationDateCol\" was not injected: check your FXML file 'patientView.fxml'.";
        assert vaccinationHistoryTableView != null : "fx:id=\"vaccinationHistoryTableView\" was not injected: check your FXML file 'patientView.fxml'.";
        assert vaccinationTypeCol != null : "fx:id=\"vaccinationTypeCol\" was not injected: check your FXML file 'patientView.fxml'.";
    }

}
