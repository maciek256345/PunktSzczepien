package edu.ib.punktszczepien;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TextArea;

import java.sql.ResultSet;
import java.sql.SQLException;

public class PatientViewDao {
    /**
     * Klasa zarządza powiązanymi z widokiem pacjenta
     * operacjami na bazie danych.
     *
     * @author MS
     * @version 1.0
     * @since 2022-02-08
     */
    private DBUtil dbUtil;
    private TextArea consoleTextArea;

    public PatientViewDao(DBUtil dbUtil, TextArea consoleTextArea) {
        this.dbUtil = dbUtil;
        this.consoleTextArea = consoleTextArea;
    }

    /**
     * Metda odpowiedzialna za przeniesienie informacji
     * z obiektu ResultSet do obiektu kolekcyjnego ObservableList
     * przechowującego obiekty klasy PatientView.
     *
     * @param rs
     * @return vaccines
     * @throws SQLException
     */
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

    /**
     * Metoda odpowiedzialna za zwrócenie
     * obiektu klasy PatientView dla przekazanego
     * jako argument metody peselu.
     *
     * @param pesel
     * @return patient
     * @throws SQLException
     */
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
            consoleTextArea.appendText("\nNie można pobrać pacjenta");
            throw e;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return patient;
    }


    /**
     * Metoda odpowiedzialna za zwrócenie
     * wszystkich zrealizowanych szczepień
     * oraz ich terminów dla pacjenta o danym peselu.
     *
     * @param pesel
     * @return vaccines
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public ObservableList<PatientView> showAllVaccines(String pesel) throws SQLException, ClassNotFoundException {

        String selectStmt = "SELECT typ_szczepienia, DATE_FORMAT(termin, '%d.%m.%Yr.  godz: %H:%i') as termin FROM pacjent where pesel like '" + pesel + "';";

        try {

            ResultSet resultSet = dbUtil.dbExecuteQuery(selectStmt);

            ObservableList<PatientView> vaccines = getVaccinesList(resultSet);

            return vaccines;


        } catch (SQLException e) {
            throw e;
        }

    }

    /**
     * Metoda odpowiedzialna za wstawienie
     * pacjenta do tabeli dane_pacjenta
     * w bazie danych.
     *
     * @param pesel
     * @param imie
     * @param nazwisko
     * @param nr_tel
     * @param data_ur
     * @throws SQLException
     * @throws ClassNotFoundException
     */
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

        } catch (SQLException e) {
            consoleTextArea.appendText("\nBłąd podczas operacji INSERT.");
            throw e;
        }
    }


    /**
     * Metoda odpowiedzialna za wstawienie
     * rejestracji na szczepienie do tabeli
     * rejestracja_na_szczepienie w bazie danych.
     *
     * @param nazwaSzczepionki
     * @param termin
     * @param pesel
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void insertVaccineRegistration(String nazwaSzczepionki, String termin, String pesel) throws SQLException, ClassNotFoundException {

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
            consoleTextArea.appendText("\nBłąd podczas operacji INSERT.");
            throw e;
        }


    }

    /**
     * Metoda odpowiedzialna za zmiane terminu
     * danego szczepienia w tabeli
     * rejestracja_na_szczepienie w bazie danych.
     *
     * @param nowaData
     * @param pesel
     * @param szczepionka
     * @throws SQLException
     * @throws ClassNotFoundException
     */
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
            consoleTextArea.appendText("\nBłąd podczas operacji UPDATE.");
            throw e;
        }


    }

    /**
     * Metoda odpowiedzialna za zwrócenie
     * informacji (nazwa + data) odnośnie
     * wszystkich szczepień, na które pacjent
     * jest zarejestrowany.
     *
     * @param pesel
     * @return vaccineRegistrations
     * @throws SQLException
     */

    public ObservableList<PatientView> getPatientVaccineRegistrations(String pesel) throws SQLException {
        ObservableList<PatientView> vaccineRegistrations = FXCollections.observableArrayList();
        String selectStmt = "SELECT nazwa, DATE_FORMAT(termin_szczepienia, '%d.%m.%Yr.  godz: %H:%i') as termin FROM punkt_szczepien where pesel like '" + pesel + "';";

        try {

            ResultSet rs = dbUtil.dbExecuteQuery(selectStmt);

            while (rs.next()) {

                PatientView p = new PatientView();
                p.setTyp_szczepienia(rs.getString("nazwa"));
                p.setTermin(rs.getString("termin"));
                vaccineRegistrations.add(p);
            }

        } catch (SQLException e) {
            consoleTextArea.appendText("\nBłąd podczas szukania rejestracji na szczepienie.");
            throw e;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return vaccineRegistrations;
    }


}
