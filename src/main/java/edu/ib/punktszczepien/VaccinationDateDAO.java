package edu.ib.punktszczepien;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TextArea;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Random;

public class VaccinationDateDAO {
    private DBUtil dbUtil;
    private TextArea consoleTextArea;

    public VaccinationDateDAO(DBUtil dbUtil, TextArea consoleTextArea) {
        this.dbUtil = dbUtil;
        this.consoleTextArea = consoleTextArea;
    }

    public ObservableList<VaccinationDate> getDates(ResultSet rs) throws SQLException {

        ObservableList<VaccinationDate> dates = FXCollections.observableArrayList();

        while (rs.next()) {

            VaccinationDate v = new VaccinationDate();
            v.setTermin(rs.getString("termin"));

            dates.add(v);

        }

        return dates;
    }

    public ObservableList<VaccinationDate> getLastVaccinationDate(ResultSet rs) throws SQLException {

        ObservableList<VaccinationDate> dates = FXCollections.observableArrayList();

        while (rs.next()) {

            VaccinationDate v = new VaccinationDate();
            v.setTermin(rs.getString("data_szczepienia"));

            dates.add(v);

        }

        return dates;
    }


    public ObservableList<String> availableDates() throws SQLException, ClassNotFoundException {

        String selectStmt = "SELECT DATE_FORMAT(termin, '%Y-%m-%d %H:%i') as termin FROM terminy;";

        try {
            ResultSet resultSet = dbUtil.dbExecuteQuery(selectStmt);
            ObservableList<VaccinationDate> dates = getDates(resultSet);
            ObservableList<String> availableDates = FXCollections.observableArrayList();

            for(VaccinationDate d:dates){
                availableDates.add(d.getTermin());
            }
            return availableDates;


        } catch (SQLException e) {

            throw e;
        }

    }

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

            for(VaccinationDate d:lastDate){
                lastVaccinationDate.add(d.getTermin());
            }
            for(String lv:lastVaccinationDate){
                rezultat = lv;
            }

        } catch (SQLException e) {
            throw e;

        }
        return rezultat;
    }

    public String lastVaccineDifferentType(String pesel) throws SQLException, ClassNotFoundException {


        String selectStmt = "select data_szczepienia from szczepienia_zrealizowane where id_pacjenta like '"
                + pesel + "' order by data_szczepienia desc limit 1;";
        String rezultat = "";

        try {
            ResultSet resultSet = dbUtil.dbExecuteQuery(selectStmt);
            ObservableList<VaccinationDate> lastDate = getLastVaccinationDate(resultSet);
            ObservableList<String> lastVaccinationDate = FXCollections.observableArrayList();


            for(VaccinationDate d:lastDate){
                lastVaccinationDate.add(d.getTermin());
            }
            for(String lv:lastVaccinationDate){
                rezultat = lv;
            }

        } catch (SQLException e) {
            throw e;
        }

        return rezultat;
    }



}
