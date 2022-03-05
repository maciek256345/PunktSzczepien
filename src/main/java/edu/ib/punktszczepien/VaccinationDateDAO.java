package edu.ib.punktszczepien;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TextArea;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

public class VaccinationDateDAO {
    /**
     * Klasa zarządza operacjami na bazie danych
     * powiązanymi z datą szczepienia.
     *
     * @author MS
     * @version 1.0
     * @since 2022-02-08
     */
    private DBUtil dbUtil;
    private TextArea consoleTextArea;

    public VaccinationDateDAO(DBUtil dbUtil, TextArea consoleTextArea) {
        this.dbUtil = dbUtil;
        this.consoleTextArea = consoleTextArea;
    }


    /**
     * Metda odpowiedzialna za przeniesienie informacji
     * z obiektu ResultSet do obiektu kolekcyjnego ObservableList
     * przechowującego obiekty klasy VaccinationDate.
     *
     * @param rs
     * @return dates
     * @throws SQLException
     */
    public ObservableList<VaccinationDate> getLastVaccinationDate(ResultSet rs) throws SQLException {

        ObservableList<VaccinationDate> dates = FXCollections.observableArrayList();

        while (rs.next()) {

            VaccinationDate v = new VaccinationDate();
            v.setTermin(rs.getString("data_szczepienia"));

            dates.add(v);

        }

        return dates;
    }

    /**
     * Metoda zwracająca dostępne
     * terminy szczepień.
     *
     * @return availableDates
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public ObservableList<String> availableDates() throws SQLException, ClassNotFoundException {

        String selectStmt = "SELECT DATE_FORMAT(termin, '%Y-%m-%d %H:%i') as data_szczepienia FROM terminy;";

        try {
            ResultSet resultSet = dbUtil.dbExecuteQuery(selectStmt);
            ObservableList<VaccinationDate> dates = getLastVaccinationDate(resultSet);
            ObservableList<String> availableDates = FXCollections.observableArrayList();

            for (VaccinationDate d : dates) {
                availableDates.add(d.getTermin());
            }
            return availableDates;


        } catch (SQLException e) {

            throw e;
        }

    }

    /**
     * Metoda zwracająca datę ostatniego
     * szczepienia tego samego typu, co
     * szczepionka przekaza jako argument,
     * dla pacjenta identyfikowanego na podstawie
     * jego peselu, przekazanego jako drugi
     * argument metody.
     *
     * @param szczepionka
     * @param pesel
     * @return rezultat
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public String lastVaccineSameType(String szczepionka, String pesel) throws SQLException, ClassNotFoundException {
        VaccineDAO vaccineDAO = new VaccineDAO(dbUtil, consoleTextArea);
        ObservableList<Vaccine> vaccine = vaccineDAO.searchVaccines(szczepionka);
        Vaccine v = vaccine.get(0);
        int id_szczepionki = v.getId();

        String selectStmt = "select data_szczepienia from szczepienia_zrealizowane where id_pacjenta like '"
                + pesel + "' and id_szczepionki like '" + id_szczepionki + "' order by data_szczepienia desc limit 1;";
        String rezultat = "";

        try {
            ResultSet resultSet = dbUtil.dbExecuteQuery(selectStmt);
            ObservableList<VaccinationDate> lastDate = getLastVaccinationDate(resultSet);
            ObservableList<String> lastVaccinationDate = FXCollections.observableArrayList();

            for (VaccinationDate d : lastDate) {
                lastVaccinationDate.add(d.getTermin());
            }
            for (String lv : lastVaccinationDate) {
                rezultat = lv;
            }

        } catch (SQLException e) {
            throw e;

        }
        return rezultat;
    }


    /**
     * Metoda zwracająca datę ostatniego
     * szczepienia dowolnego typu, dla pacjenta
     * identyfikowanego na podstawie jego
     * peselu przekazanego jako argument metody.
     *
     * @param pesel
     * @return rezultat
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public String lastVaccineDifferentType(String pesel) throws SQLException, ClassNotFoundException {


        String selectStmt = "select data_szczepienia from szczepienia_zrealizowane where id_pacjenta like '"
                + pesel + "' order by data_szczepienia desc limit 1;";
        String rezultat = "";

        try {
            ResultSet resultSet = dbUtil.dbExecuteQuery(selectStmt);
            ObservableList<VaccinationDate> lastDate = getLastVaccinationDate(resultSet);
            ObservableList<String> lastVaccinationDate = FXCollections.observableArrayList();


            for (VaccinationDate d : lastDate) {
                lastVaccinationDate.add(d.getTermin());
            }
            for (String lv : lastVaccinationDate) {
                rezultat = lv;
            }

        } catch (SQLException e) {
            throw e;
        }

        return rezultat;
    }


}

