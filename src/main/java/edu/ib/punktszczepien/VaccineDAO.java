package edu.ib.punktszczepien;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.scene.control.TextArea;

import java.sql.ResultSet;
import java.sql.SQLException;

public class VaccineDAO {
    /**
     * Klasa zarządza operacjami na bazie danych
     * związanych z tabelą przechwoująca szczepionki.
     *
     * @author MS
     * @version 1.0
     * @since 2022-02-08
     */
    private DBUtil dbUtil;
    private TextArea consoleTextArea;

    public VaccineDAO(DBUtil dbUtil, TextArea consoleTextArea) {
        this.dbUtil = dbUtil;
        this.consoleTextArea = consoleTextArea;
    }

    /**
     * Metda odpowiedzialna za przeniesienie informacji
     * z obiektu ResultSet do obiektu kolekcyjnego ObservableList
     * przechowującego obiekty klasy Vaccine.
     *
     * @param rs
     * @return vaccines
     * @throws SQLException
     */
    public ObservableList<Vaccine> getAllVaccines(ResultSet rs) throws SQLException {

        ObservableList<Vaccine> vaccines = FXCollections.observableArrayList();

        while (rs.next()) {

            Vaccine v = new Vaccine();
            v.setId(rs.getInt("id"));
            v.setNazwa(rs.getString("nazwa"));
            vaccines.add(v);
        }

        return vaccines;
    }


    /**
     * Metoda zwracająca id oraz nazwe
     * dla szczepionki przekazanej jako
     * argument metody.
     *
     * @param n
     * @return vaccines
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public ObservableList<Vaccine> searchVaccines(String n) throws SQLException, ClassNotFoundException {

        String selectStmt = "SELECT * FROM szczepionki WHERE nazwa LIKE '%" + n + "%';";

        try {
            ResultSet resultSet = dbUtil.dbExecuteQuery(selectStmt);
            ObservableList<Vaccine> vaccines = getAllVaccines(resultSet);

            return vaccines;

        } catch (SQLException e) {

            throw e;
        }

    }

    /**
     * Metoda zwracająca wszystkie
     * dostępne w klinice
     * szczepionki, którymi pacjenci
     * mogą się zaszczepić.
     *
     * @return availableVaccines
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public ObservableList<String> availableVaccines() throws SQLException, ClassNotFoundException {

        String selectStmt = "SELECT * FROM szczepionki;";

        try {
            ResultSet resultSet = dbUtil.dbExecuteQuery(selectStmt);
            ObservableList<Vaccine> vaccines = getAllVaccines(resultSet);

            ObservableList<String> availableVaccines = FXCollections.observableArrayList();

            for (Vaccine v : vaccines) {
                availableVaccines.add(v.getNazwa());
            }

            return availableVaccines;

        } catch (SQLException e) {

            throw e;
        }

    }
}
