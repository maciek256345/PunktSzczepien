package edu.ib.punktszczepien;

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
    /**
     * Klasa odpowiedzialna za zarządzanie oknem aplikacji
     * z poziomu pacjenta. Zawiera metody umożliwiające:
     * zmianę terminu szczepienia, potwierdzenie peselu,
     * zalogowanie do bazy danych, rozłączenie z bazą danych,
     * określenie czy użytkownik jest nowym pacjentem czy nie,
     * powrót do okna startowego, dodanie nowego pacjenta,
     * zarejestrowanie na szczepienie, zmianę terminu szczepienia,
     * aktualizowanie tabeli, ustawienie wartości w tabelach,
     * ustawienie wartości w choiceboxach.
     *
     * @author MS
     * @version 1.0
     * @since 2022-02-08
     */
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
    private TextField surnameTextField;

    @FXML
    private TextField userTextField;

    @FXML
    private AnchorPane usersMenu;

    @FXML
    private TableColumn<PatientView, String> vaccinationDateCol;

    @FXML
    private TableColumn<PatientView, String> comingVaccinationDateCol;

    @FXML
    private TableColumn<PatientView, String> comingVaccinationTypeCol;

    @FXML
    private TableView<PatientView> comingVaccinationsTableView;

    @FXML
    private ChoiceBox<String> vaccinationDateChoiceBox;

    @FXML
    private ChoiceBox<String> vaccinationTypeChoiceBox;

    @FXML
    private TableView<PatientView> vaccinationHistoryTableView;

    @FXML
    private TableColumn<PatientView, String> vaccinationTypeCol;

    @FXML
    private Tab comingVaccinationsTab;

    @FXML
    private Tab vaccinationHistoryTab;

    @FXML
    private TabPane vaccinationPan;

    /**
     * Metoda umożliwiająca zmianę terminu szczepienia
     * po wybraniu daty oraz szczepienia, którego
     * termin ma być zmieniony, jeżeli spełnione
     * są reguły biznesowe dotyczące odległości
     * między szczepieniami tego samego i różnego
     * typu.
     *
     * @param event
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @FXML
    void changeDateBtnClicked(ActionEvent event) throws SQLException, ClassNotFoundException {

        PatientView patientView = comingVaccinationsTableView.getSelectionModel().getSelectedItem();

        patientViewDao = new PatientViewDao(dbUtil, consoleTextArea);
        VaccinationDateDAO vaccinationDateDAO = new VaccinationDateDAO(dbUtil, consoleTextArea);

        String pesel = peselTextField.getText();
        String data = vaccinationDateChoiceBox.getValue();
        String szczepionka = patientView.getTyp_szczepienia();

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
            long liczbaDni = ChronoUnit.DAYS.between(dataLocalDateTime, dataWybranaLocalDateTime);
            if (osatnieSzczepienieTegoSamegoTypu.equals("")) {

                if (liczbaDni > 21) {
                    patientViewDao.changeDate(data, pesel, szczepionka);
                    consoleTextArea.appendText("\nPomyślnie zmieniono termin");
                } else {
                    consoleTextArea.appendText("\nNiezachowany odstęp czasowy między szczepieniami różnego typu (21 dni)");
                }
            } else {
                LocalDateTime dataLocalDateTime2 = LocalDateTime.parse(osatnieSzczepienieTegoSamegoTypu, formatter);
                long liczbaDni2 = ChronoUnit.DAYS.between(dataLocalDateTime2, dataWybranaLocalDateTime);
                if (liczbaDni > 21) {
                    if (liczbaDni2 > 365) {
                        patientViewDao.changeDate(data, pesel, szczepionka);
                        consoleTextArea.appendText("\nPomyślnie zmieniono termin");
                    } else {
                        consoleTextArea.appendText("\nNiezachowny odstęp czasowy między szczepieniami tego samego typu (365 dni)");
                    }
                } else {
                    if (liczbaDni2 < 365) {
                        consoleTextArea.appendText("\nNiezachowny odstęp czasowy między szczepieniami tego samego typu (365 dni)");
                    } else {
                        consoleTextArea.appendText("\nNiezachowany odstęp czasowy między szczepieniami różnego typu (21 dni)");
                    }

                }


            }

        }
        updateTable();
    }

    /**
     * Metoda potwierdzająca wprowadzenie peselu
     * przez użytkownika, sprawdzająca również
     * jego poprawność.
     *
     * @param event
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @FXML
    void confirmationBtnClicked(ActionEvent event) throws SQLException, ClassNotFoundException {
        String pesel = peselTextField.getText();

        if (pesel.length() != 11) {
            consoleTextArea.appendText("\nWprowadzono niepoprawny pesel");
            return;
        }

        newPatientBtn.setDisable(false);
        oldPatientBtn.setDisable(false);
    }


    /**
     * Metoda umożliwiająca użytkownikowi
     * nawiązanie połączenia z bazą danych.
     *
     * @param event
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @FXML
    void connectBtnClicked(ActionEvent event) throws SQLException, ClassNotFoundException {
        dbUtil = new DBUtil(userTextField.getText(), passwordTextField.getText(), consoleTextArea);
        dbUtil.dbConnect();
        consoleTextArea.appendText("Przydzielono dostęp dla pacjenta \"" + userTextField.getText() + "\".");
        connectButton.setDisable(true);
        disconnectButton.setDisable(false);
        peselTextField.setDisable(false);
        confrimationBtn.setDisable(false);
    }

    /**
     * Metoda umożliwiająca użytkownikowi
     * rozłączenie z bazą danych.
     *
     * @param event
     * @throws SQLException
     */
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
        registerVaccinationBtn.setDisable(true);
        changeDateBtn.setDisable(true);
        newPatientBtn.setDisable(true);
        oldPatientBtn.setDisable(true);
        vaccinationPan.setDisable(true);
    }

    /**
     * Metoda aktywująca pole do wpisania
     * przez pacjenta jego danych w celu
     * dodania do bazy pacjentów.
     *
     * @param event
     */
    @FXML
    void newPatientClicked(ActionEvent event) {
        nameTextField.setDisable(false);
        surnameTextField.setDisable(false);
        dateTextField.setDisable(false);
        savePatientData.setDisable(false);
        phoneNumberTextField.setDisable(false);
    }

    /**
     * Metoda aktywująca pola dostępne
     * dla pacjentów figurujących już
     * w bazie pacjentów.
     *
     * @param event
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @FXML
    void oldPatientClicked(ActionEvent event) throws SQLException, ClassNotFoundException {
        vaccinationTypeChoiceBox.setDisable(false);
        vaccinationDateChoiceBox.setDisable(false);
        vaccinationHistoryTableView.setDisable(false);
        registerVaccinationBtn.setDisable(false);
        changeDateBtn.setDisable(false);
        vaccinationPan.setDisable(false);
        setTables();
        setChoiceBoxes();
    }

    /**
     * Metoda umożliwiająca powrót
     * do okna startowego.
     *
     * @param event
     * @throws IOException
     */
    @FXML
    void patientComeBackBtnClicked(ActionEvent event) throws IOException {
        root = FXMLLoader.load(getClass().getResource("startView.fxml"));
        stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        scene = new Scene(root);
        stage.setTitle("Vaccine Application");
        stage.setScene(scene);
        stage.show();

    }

    /**
     * Metoda umożliwiająca pacjentowi
     * rejestrację na szczepienie, jeśli
     * spełnione są reguł biznesowe
     * dotyczące odlełości między szczepieniami
     * tego samego i dowolnego typu.
     *
     * @param event
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @FXML
    void registerBtnClicked(ActionEvent event) throws SQLException, ClassNotFoundException {
        patientViewDao = new PatientViewDao(dbUtil, consoleTextArea);
        Patient patient = this.patientViewDao.getPatient((peselTextField.getText()));
        long age = patient.getAge();

        if (age < 18) {
            consoleTextArea.appendText("\nPacjent jest nieletni, nie może się zarejestrować na szczepienie");
            return;
        }

        patientViewDao = new PatientViewDao(dbUtil, consoleTextArea);
        VaccinationDateDAO vaccinationDateDAO = new VaccinationDateDAO(dbUtil, consoleTextArea);

        String data = vaccinationDateChoiceBox.getValue();
        String szczepionka = vaccinationTypeChoiceBox.getValue();

        String osatnieSzczepienieTegoSamegoTypu = vaccinationDateDAO.lastVaccineSameType(szczepionka, peselTextField.getText());
        String ostatnieSzczepienieDowolnegoTypu = vaccinationDateDAO.lastVaccineDifferentType(peselTextField.getText());
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
        LocalDateTime dataWybranaLocalDateTime = LocalDateTime.parse(data, formatter);

        if (ostatnieSzczepienieDowolnegoTypu.equals("")) {
            patientViewDao.insertVaccineRegistration(szczepionka, data, peselTextField.getText());
            consoleTextArea.appendText("\nPomyślnie zarejestrowano na szczepienie");
        } else {
            ostatnieSzczepienieDowolnegoTypu = ostatnieSzczepienieDowolnegoTypu.replace("T", " ");
            osatnieSzczepienieTegoSamegoTypu = osatnieSzczepienieTegoSamegoTypu.replace("T", " ");
            LocalDateTime dataLocalDateTime = LocalDateTime.parse(ostatnieSzczepienieDowolnegoTypu, formatter);
            long liczbaDni = ChronoUnit.DAYS.between(dataLocalDateTime, dataWybranaLocalDateTime);

            if (osatnieSzczepienieTegoSamegoTypu.equals("")) {

                if (liczbaDni > 21) {
                    patientViewDao.insertVaccineRegistration(szczepionka, data, peselTextField.getText());
                    consoleTextArea.appendText("\nPomyślnie zarejestrowano");
                } else {
                    consoleTextArea.appendText("\nNiezachowany odstęp czasowy między szczepieniami różnego typu (21 dni)");
                }
            } else {
                LocalDateTime dataLocalDateTime2 = LocalDateTime.parse(osatnieSzczepienieTegoSamegoTypu, formatter);
                long liczbaDni2 = ChronoUnit.DAYS.between(dataLocalDateTime2, dataWybranaLocalDateTime);
                if (liczbaDni > 21) {
                    if (liczbaDni2 > 365) {
                        patientViewDao.insertVaccineRegistration(szczepionka, data, peselTextField.getText());
                        consoleTextArea.appendText("\nPomyślnie zarejestrowano");
                    } else {

                        consoleTextArea.appendText("\nNiezachowny odstęp czasowy między szczepieniami tego samego typu (365 dni)");
                    }
                } else {
                    if (liczbaDni2 < 365) {
                        consoleTextArea.appendText("\nNiezachowny odstęp czasowy między szczepieniami tego samego typu (365 dni)");
                    } else {
                        consoleTextArea.appendText("\nNiezachowany odstęp czasowy między szczepieniami różnego typu (21 dni)");
                    }

                }


            }


        }
        updateTable();
    }

    /**
     * Metoda umożliwiająca dodanie pacjenta
     * do bazy pacjentów na podstawie wprowadzonych
     * przez niego danych.
     *
     * @param event
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    @FXML
    void saveDataBtnClicked(ActionEvent event) throws SQLException, ClassNotFoundException {
        vaccinationTypeChoiceBox.setDisable(false);
        vaccinationDateChoiceBox.setDisable(false);
        vaccinationHistoryTableView.setDisable(false);

        registerVaccinationBtn.setDisable(false);
        changeDateBtn.setDisable(false);

        String imie = nameTextField.getText();
        String nazwisko = surnameTextField.getText();
        String pesel = peselTextField.getText();
        String numerTelefonu = phoneNumberTextField.getText();
        String dataUrodzenia = dateTextField.getText();

        if (pesel.length() != 11) {
            consoleTextArea.appendText("\nWprowadzono niepoprawny pesel");
            return;
        }

        if (numerTelefonu.length() != 9) {
            consoleTextArea.appendText("\nWprowadzono niepoprawny numer telefonu");
            return;
        }

        try {

            if (!nameTextField.getText().equals(null)) {
                patientViewDao = new PatientViewDao(dbUtil, consoleTextArea);
                patientViewDao.insertPatient(pesel, imie, nazwisko, numerTelefonu, dataUrodzenia);
                consoleTextArea.appendText("\nNowy pacjent " + imie + " " + nazwisko + " dodany.");
            }

        } catch (SQLException e) {
            consoleTextArea.appendText("\nBłąd podczas dodawania pacjenta.");
            throw e;
        }
        setTables();
        setChoiceBoxes();
        vaccinationPan.setDisable(false);
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
        assert surnameTextField != null : "fx:id=\"surnameTextField\" was not injected: check your FXML file 'patientView.fxml'.";
        assert userTextField != null : "fx:id=\"userTextField\" was not injected: check your FXML file 'patientView.fxml'.";
        assert vaccinationDateCol != null : "fx:id=\"vaccinationDateCol\" was not injected: check your FXML file 'patientView.fxml'.";
        assert vaccinationHistoryTableView != null : "fx:id=\"vaccinationHistoryTableView\" was not injected: check your FXML file 'patientView.fxml'.";
        assert vaccinationTypeCol != null : "fx:id=\"vaccinationTypeCol\" was not injected: check your FXML file 'patientView.fxml'.";


        vaccinationPan.getSelectionModel().selectedItemProperty().addListener(
                new ChangeListener<Tab>() {
                    /**
                     * Metoda ta wywoływana jest podczas zmiany
                     * kart między szczepieniami zrealizowanymi
                     * oraz nadchodzącymi dla danego pacjenta.
                     * W podglądzie nadchodzących szczepień
                     * wyświetlane są dane zawarte w widoku punkt_szczepien.
                     * W podglądzie szczepień zrealizowanych
                     * wyświetlane są dane zawarte w widoku pacjent.
                     *
                     * @param ov
                     * @param oldTab - poprzednio wybrane okno
                     * @param newTab - aktualnie wybrane okno
                     */
                    @Override
                    public void changed(ObservableValue<? extends Tab> ov, Tab oldTab, Tab newTab) {
                        String id = newTab.getId();
                        patientViewDao = new PatientViewDao(dbUtil, consoleTextArea);
                        if (id.equals(vaccinationHistoryTab.getId())) {
                            try {
                                ObservableList<PatientView> racketData = patientViewDao.showAllVaccines(peselTextField.getText());
                                vaccinationHistoryTableView.setItems(racketData);
                            } catch (SQLException | ClassNotFoundException e) {
                                e.printStackTrace();
                            }
                        } else {

                            try {
                                ObservableList<PatientView> racketData = patientViewDao.getPatientVaccineRegistrations(peselTextField.getText());
                                comingVaccinationsTableView.setItems(racketData);
                            } catch (SQLException e) {
                                e.printStackTrace();
                            }

                        }
                    }
                }
        );


    }

    /**
     * Metoda aktualizująca infromacje
     * prezentowane w tabeleli nadchodzących
     * szczepień.
     *
     * @throws SQLException
     */
    public void updateTable() throws SQLException {
        try {
            comingVaccinationTypeCol.setCellValueFactory(new PropertyValueFactory<PatientView, String>("typ_szczepienia"));
            comingVaccinationDateCol.setCellValueFactory(new PropertyValueFactory<PatientView, String>("termin"));
            comingVaccinationsTableView.getItems().clear();
            ObservableList<PatientView> racketData = patientViewDao.getPatientVaccineRegistrations(peselTextField.getText());
            comingVaccinationsTableView.setItems(racketData);

        } catch (SQLException e) {
            consoleTextArea.appendText("\nProblem podczas pobierania szczepien z BD");
            throw e;
        }
    }

    /**
     * Metoda umożliwiająca ustawienie
     * zrealizowanych i nadchodzących
     * szczepień w tabelach.
     *
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void setTables() throws SQLException, ClassNotFoundException {
        patientViewDao = new PatientViewDao(dbUtil, consoleTextArea);
        try {
            vaccinationTypeCol.setCellValueFactory(new PropertyValueFactory<PatientView, String>("typ_szczepienia"));
            vaccinationDateCol.setCellValueFactory(new PropertyValueFactory<PatientView, String>("termin"));
            vaccinationHistoryTableView.getItems().clear();
            ObservableList<PatientView> racketData = patientViewDao.showAllVaccines(peselTextField.getText());
            vaccinationHistoryTableView.setItems(racketData);

        } catch (SQLException | ClassNotFoundException e) {
            consoleTextArea.appendText("\nProblem podczas pobierania szczepien z BD");
            throw e;
        }

        try {
            comingVaccinationTypeCol.setCellValueFactory(new PropertyValueFactory<PatientView, String>("typ_szczepienia"));
            comingVaccinationDateCol.setCellValueFactory(new PropertyValueFactory<PatientView, String>("termin"));
            comingVaccinationsTableView.getItems().clear();
            ObservableList<PatientView> racketData = patientViewDao.getPatientVaccineRegistrations(peselTextField.getText());
            comingVaccinationsTableView.setItems(racketData);

        } catch (SQLException e) {
            consoleTextArea.appendText("\nProblem podczas pobierania szczepien z BD");
            throw e;
        }

    }

    /**
     * Metoda umożliwiająca ustawienie
     * dostępnych terminów i szczepień
     * w choice boxach.
     *
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void setChoiceBoxes() throws SQLException, ClassNotFoundException {
        vaccinationTypeChoiceBox.setValue("Szczepionki");
        vaccinationDateChoiceBox.setValue("Terminy");

        VaccineDAO vaccineDAO = new VaccineDAO(dbUtil, consoleTextArea);
        ObservableList<String> szczepionki = vaccineDAO.availableVaccines();
        vaccinationTypeChoiceBox.setItems(szczepionki);
        VaccinationDateDAO vaccinationDateDAO = new VaccinationDateDAO(dbUtil, consoleTextArea);
        ObservableList<String> terminy = vaccinationDateDAO.availableDates();
        vaccinationDateChoiceBox.setItems(terminy);
    }


}
