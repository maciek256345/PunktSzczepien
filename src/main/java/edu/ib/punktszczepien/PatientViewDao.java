package edu.ib.punktszczepien;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TextArea;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PatientViewDao {
    private DBUtil dbUtil;
    private TextArea consoleTextArea;

    public PatientViewDao(DBUtil dbUtil, TextArea consoleTextArea) {
        this.dbUtil = dbUtil;
        this.consoleTextArea = consoleTextArea;
    }

    public ObservableList<PatientView> getVaccinesList(ResultSet rs) throws SQLException {

        ObservableList<PatientView> vaccines = FXCollections.observableArrayList();

        while (rs.next()) {

            PatientView p = new PatientView();
            p.setTyp_szczepienia(rs.getString("typ_szczepienia"));
            p.setTermin(rs.getString("termin"));

            vaccines.add(p);

        }

        return vaccines;
    }

    public Patient getPatient(String pesel) throws SQLException {
        String selectStmt = "SELECT pesel, data_urodzenia FROM dane_pacjenta where pesel like '" + pesel + "';";
        Patient patient = null;

        try {

            ResultSet rs = dbUtil.dbExecuteQuery(selectStmt);

            while (rs.next()) {

                PatientView p = new PatientView();
                patient = new Patient(rs.getString("pesel"), rs.getDate("data_urodzenia"));
            }

        } catch (SQLException e) {
            consoleTextArea.appendText("Could not get patient. \n");
            throw e;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return patient;
    }


    public ObservableList<PatientView> showAllVaccines(String pesel) throws SQLException, ClassNotFoundException {

        String selectStmt = "SELECT typ_szczepienia, DATE_FORMAT(termin, '%m.%d.%Yr.  godz: %H:%i') as termin FROM pacjent where pesel like '" + pesel + "';";

        try {

            ResultSet resultSet = dbUtil.dbExecuteQuery(selectStmt);

            ObservableList<PatientView> vaccines = getVaccinesList(resultSet);
            consoleTextArea.appendText(selectStmt + "\n");

            return vaccines;


        } catch (SQLException e) {
            throw e;
        }

    }


    public void insertPatient(String pesel, String imie, String nazwisko, String nr_tel, String data_ur) throws SQLException, ClassNotFoundException {

        StringBuilder sb = new StringBuilder("INSERT INTO dane_pacjenta(pesel, imie, nazwisko, nr_tel, data_urodzenia) VALUES('");
        sb.append(pesel);
        sb.append("','");
        sb.append(imie);
        sb.append("','");
        sb.append(nazwisko);
        sb.append("','");
        sb.append(nr_tel);
        sb.append("','");
        sb.append(data_ur);
        sb.append("');");
        String insertStmt = sb.toString();

        try {

            dbUtil.dbExecuteUpdate(insertStmt);
            consoleTextArea.appendText(insertStmt + "\n");

        } catch (SQLException e) {
            consoleTextArea.appendText("Error occurred while INSERT Operation." + "\n");
            throw e;
        }
    }


    public void insertVaccineRegistration(String nazwaSzczepionki, String termin, String pesel) throws SQLException, ClassNotFoundException {
        Patient patient = this.getPatient(pesel);
        long age = patient.getAge();

        if (age < 18) {
            consoleTextArea.appendText("\n" + "Patient is not adult, could not register for vaccination" + "\n");
            return;
        }

        StringBuilder sb = new StringBuilder("insert into rejestracja_na_szczepienie(id_szczepionki," +
                " termin_szczepienia, id_pacjenta, zaszczepiony) VALUES(");

        VaccineDAO vaccineDAO = new VaccineDAO(dbUtil, consoleTextArea);
        ObservableList<Vaccine> vaccine = vaccineDAO.searchVaccines(nazwaSzczepionki);
        Vaccine v = vaccine.get(0);
        int id_szczepionki = v.getId();

        sb.append(id_szczepionki);
        sb.append(",'");
        sb.append(termin);
        sb.append("','");
        sb.append(pesel);
        sb.append("',");
        sb.append(false);
        sb.append(");");
        String insertStmt = sb.toString();

        try {
            dbUtil.dbExecuteUpdate(insertStmt);

        } catch (SQLException e) {
            consoleTextArea.appendText("Error occurred while INSERT Operation." + "\n");
            throw e;
        }


    }

    public void changeDate(String nowaData, String pesel, String szczepionka) throws SQLException, ClassNotFoundException {
        VaccineDAO vaccineDAO = new VaccineDAO(dbUtil, consoleTextArea);
        ObservableList<Vaccine> vaccine = vaccineDAO.searchVaccines(szczepionka);
        Vaccine v = vaccine.get(0);
        int id_szczepionki = v.getId();

        StringBuilder sb = new StringBuilder("UPDATE rejestracja_na_szczepienie SET termin_szczepienia = '");
        sb.append(nowaData);
        sb.append("' where id_pacjenta = '");
        sb.append(pesel);
        sb.append("' and id_szczepionki ='");
        sb.append(id_szczepionki);
        sb.append("';");
        String updateStmt = sb.toString();

        try {
            dbUtil.dbExecuteUpdate(updateStmt);

        } catch (SQLException e) {
            consoleTextArea.appendText("Error occurred while UPDATE Operation." + "\n");
            throw e;
        }


    }




}
