package edu.ib.punktszczepien;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TextArea;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.stream.Collectors;

/**
 * Klasa zarządzająca operacjami na bazie danych (wyszukiwanie, dodawanie, aktualizowanie, usuwanie) dla okna aplikacji z poziomu pracownika kliniki.
 * Zawiera metody odpowiadające za:
 * pobieranie danych na temat akualnych zapisów na szczepienia (getVaccineRegistrations),
 * pobieranie danych do wyświetlania statystyk szczepień zrealizowanych (getVaccineStats),
 * pobieranie danych na temat odbytych szczepień (getVaccineHistory),
 * dodawanie nowych terminów szczepień (insertVaccineDate),
 * aktualizowanie zaszczepienia wybranych pacjentów (updateVaccineRegistration),
 * dodawanie zaszczepionego pacjenta do tabeli szczepienia_zrealizowane (insertVaccineHistory).
 *
 * @author AK
 * @version 1.0
 * @since 2022-02-08
 */
public class ClinicViewDAO {
    private DBUtil dbUtil;
    private TextArea consoleTextArea;

    public ClinicViewDAO(DBUtil dbUtil, TextArea consoleTextArea) {
        this.dbUtil = dbUtil;
        this.consoleTextArea = consoleTextArea;
    }

    /**
     * Metoda umożliwiająca pobieranie danych na temat akualnych zapisów na szczepienia.
     *
     * @return vaccineRegistrations
     * @throws SQLException
     */
    public ObservableList<VaccineRegistration> getVaccineRegistrations() throws SQLException {
        ObservableList<VaccineRegistration> vaccineRegistrations = FXCollections.observableArrayList();
        String selectStmt = "SELECT imie, nazwisko, pesel, nr_tel, nazwa, id_szczepienia, id_szczepionki, DATE_FORMAT(termin_szczepienia, '%Y-%m-%d %H:%i') as termin FROM punkt_szczepien;";

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
                v.setIdSzczepienia(rs.getString("id_szczepienia"));
                v.setIdSzczepionki(rs.getString("id_szczepionki"));

                vaccineRegistrations.add(v);
            }

//            consoleTextArea.appendText(selectStmt + "\n");

        } catch (SQLException e) {
            consoleTextArea.appendText("Błąd podczas pobierania aktualnych zapisów na szczepienia. \n");
            throw e;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return vaccineRegistrations;
    }

    /**
     * Metoda umożliwiająca pobieranie danych do wyświetlania statystyk szczepień zrealizowanych.
     *
     * @return map
     * @throws SQLException
     */
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

    /**
     * Metoda umożlwiająca pobieranie danych na temat odbytych szczepień.
     *
     * @return vaccineHistories
     * @throws SQLException
     */
    public ObservableList<VaccineHistory> getVaccineHistory() throws SQLException {
        ObservableList<VaccineHistory> vaccineHistories = FXCollections.observableArrayList();
        String selectStmt = "SELECT imie, nazwisko, pesel, nr_tel, nazwa, id_szczepienia, id_szczepionki, DATE_FORMAT(data_szczepienia, '%Y-%m-%d %H:%i') as termin FROM punkt_szczepien_historia;";

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
                vh.setIdSzczepienia(rs.getString("id_szczepienia"));
                vh.setIdSzczepionki(rs.getString("id_szczepionki"));

                vaccineHistories.add(vh);
            }


        } catch (SQLException e) {
            consoleTextArea.appendText("Błąd podczas pobierania historii szczepień. \n");
            throw e;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return vaccineHistories;
    }

    /**
     * Metoda umożlwiająca dodawanie nowych terminów szczepień.
     *
     * @param date
     * @throws SQLException
     */
    public void insertVaccineDate(String date) throws SQLException {
        StringBuilder sb = new StringBuilder("INSERT INTO terminy(termin) VALUES('");
        sb.append(date);
        sb.append("');");

        String insertStmt = sb.toString();

        try {

            dbUtil.dbExecuteUpdate(insertStmt);
//            consoleTextArea.appendText(insertStmt + "\n");
            consoleTextArea.appendText("Dodano termin szczepienia." + "\n");

        } catch (SQLException e) {
            consoleTextArea.appendText("Błąd podczas dodawania nowego terminu szczepienia." + "\n");
            throw e;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda umożliwiająca aktualizowanie zaszczepienia wybranych pacjentów.
     *
     * @param vaccineRegistration
     * @throws SQLException
     */
    public void updateVaccineRegistration(VaccineRegistration vaccineRegistration) throws SQLException {
        StringBuilder sb = new StringBuilder("UPDATE rejestracja_na_szczepienie SET zaszczepiony = 1 WHERE id_szczepienia = ");
        sb.append(vaccineRegistration.getIdSzczepienia());
        sb.append(";");
        String updateStmt = sb.toString();

        try {
            dbUtil.dbExecuteUpdate(updateStmt);
//            consoleTextArea.appendText(updateStmt + "\n");
            consoleTextArea.appendText("Zaktualizowano rejestrację na szczepienie." + "\n");
        } catch (SQLException e) {
            consoleTextArea.appendText("Nie udało się zaktualizować rejestracji na szczepienie." + "\n");
            throw e;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * Metoda umożliwiająca dodawanie zaszczepionego pacjenta do tabeli szczepienia_zrealizowane.
     *
     * @param vaccineRegistration
     * @throws SQLException
     */
    public void insertVaccineHistory(VaccineRegistration vaccineRegistration) throws SQLException {
        StringBuilder sb = new StringBuilder("INSERT INTO szczepienia_zrealizowane(id_pacjenta, id_szczepionki, id_szczepienia, data_szczepienia) VALUES('");
        sb.append(vaccineRegistration.getPesel());
        sb.append("', ");
        sb.append(vaccineRegistration.getIdSzczepionki());
        sb.append(", ");
        sb.append(vaccineRegistration.getIdSzczepienia());
        sb.append(", '");
        sb.append(vaccineRegistration.getTermin());
        sb.append("');");

        String insertStmt = sb.toString();
        try {
            dbUtil.dbExecuteUpdate(insertStmt);
//            consoleTextArea.appendText(insertStmt + "\n");
            consoleTextArea.appendText("Potwierdzono realizację szczepienia." + "\n");
        } catch (SQLException e) {
            consoleTextArea.appendText("Nie udało się potwierdzić zrealizowanego szczepienia." + "\n");
            throw e;
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }
}
