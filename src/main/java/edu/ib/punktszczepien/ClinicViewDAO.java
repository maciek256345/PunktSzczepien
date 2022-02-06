package edu.ib.punktszczepien;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TextArea;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ClinicViewDAO {
    private DBUtil dbUtil;
    private TextArea consoleTextArea;

    public ClinicViewDAO(DBUtil dbUtil, TextArea consoleTextArea) {
        this.dbUtil = dbUtil;
        this.consoleTextArea = consoleTextArea;
    }

    public ObservableList<VaccineRegistration> getVaccineRegistrations() throws SQLException {
        ObservableList<VaccineRegistration> vaccineRegistrations = FXCollections.observableArrayList();
        String selectStmt = "SELECT imie, nazwisko, pesel, nr_tel, nazwa, DATE_FORMAT(termin_szczepienia, '%d.%m.%Yr. %H:%i') as termin FROM punkt_szczepien;";

        try {

            ResultSet rs = dbUtil.dbExecuteQuery(selectStmt);


            while (rs.next()) {

                VaccineRegistration v = new VaccineRegistration();
                v.setImie(rs.getString("imie"));
                v.setNazwisko(rs.getString("nazwisko"));
                v.setPesel(rs.getString("pesel"));
                v.setNr_tel(rs.getString("nr_tel"));
                v.setNazwa(rs.getString("nazwa"));
                v.setTermin(rs.getString("termin"));

                vaccineRegistrations.add(v);
            }

            consoleTextArea.appendText(selectStmt + "\n");

        } catch (SQLException e) {
            consoleTextArea.appendText("While searching vaccine registrations, an error occurred. \n");
            throw e;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return vaccineRegistrations;
    }

    public LinkedHashMap<String, Integer> getVaccineStats() throws SQLException {
        List<VaccineHistory> vaccineHistories = getVaccineHistory().stream().collect(Collectors.toList());
        LinkedHashMap<String, Integer> map = new LinkedHashMap();

        for (int i = 0; i < vaccineHistories.size(); i++) {
            String termin = vaccineHistories.get(i).getTermin();
            String key = termin.split("\\s+")[0];

            if (map.containsKey(key)) {
                int oldValue = map.get(key);
                map.replace(key, oldValue, oldValue + 1);
            } else {
                map.put(key, 1);
            }
        }

        return map;
    }

    public ObservableList<VaccineHistory> getVaccineHistory() throws SQLException {
        ObservableList<VaccineHistory> vaccineHistories = FXCollections.observableArrayList();
        String selectStmt = "SELECT imie, nazwisko, pesel, nr_tel, nazwa, DATE_FORMAT(data_szczepienia, '%d.%m.%Yr. %H:%i') as termin FROM punkt_szczepien_historia;";

        try {

            ResultSet rs = dbUtil.dbExecuteQuery(selectStmt);

            while (rs.next()) {

                VaccineHistory vh = new VaccineHistory();
                vh.setNazwisko(rs.getString("nazwisko"));
                vh.setPesel(rs.getString("pesel"));
                vh.setNr_tel(rs.getString("nr_tel"));
                vh.setNazwa(rs.getString("nazwa"));
                vh.setTermin(rs.getString("termin"));
                vh.setImie(rs.getString("imie"));

                vaccineHistories.add(vh);
            }

            consoleTextArea.appendText(selectStmt + "\n");

        } catch (SQLException e) {
            consoleTextArea.appendText("While searching players, an error occurred. \n");
            throw e;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return vaccineHistories;
    }

    public void insertVaccineDate(String date) throws SQLException {
        StringBuilder sb = new StringBuilder("INSERT INTO terminy(termin) VALUES('");
        sb.append(date);
        sb.append("');");

        String insertStmt = sb.toString();

        try {

            dbUtil.dbExecuteUpdate(insertStmt);
            consoleTextArea.appendText(insertStmt + "\n");
            consoleTextArea.appendText("Dodano termin szczepienia." + "\n");

        } catch (SQLException e) {
            consoleTextArea.appendText("Error occurred while inserting new vaccine termin." + "\n");
            throw e;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
