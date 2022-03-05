package edu.ib.punktszczepien;

import javafx.scene.control.TextArea;

import javax.sql.rowset.CachedRowSet;
import java.sql.*;


public class DBUtil {
    /**
     * Klasa zawiera metody odpowiedzialne za połączenie, rozłączenie z bazą danych,
     * wykonywanie zapytań oraz zawracanie ich wyników za pomocą klasy ResultSet
     *
     * @author MS
     * @version 1.0
     * @since 2022-02-08
     */

    private String userName;
    private String userPassword;
    private TextArea consoleTextArea;

    private Connection conn = null;

    public DBUtil(String userName, String userPassword, TextArea consoleTextArea) {
        this.userName = userName;
        this.userPassword = userPassword;
        this.consoleTextArea = consoleTextArea;
    }


    /**
     * Metoda odpowiedzialna za nawiązanie połączenia z bazą danych.
     *
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void dbConnect() throws SQLException, ClassNotFoundException {

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            consoleTextArea.appendText("No MySQL JDBC Driver found." + "\n");
            e.printStackTrace();
            throw e;
        }

        try {
            conn = DriverManager.getConnection(createURL(), userName, userPassword);
        } catch (SQLException e) {
            consoleTextArea.appendText("Błąd podczas łączenia, spróbuj ponownie !" + "\n");
            e.printStackTrace();
            throw e;
        }

    }

    /**
     * Metoda odpowiedzialba za rozłączenie z bazą danych.
     *
     * @throws SQLException
     */
    public void dbDisconnect() throws SQLException {

        try {

            if (conn != null && !conn.isClosed()) {
                conn.close();
            }
        } catch (Exception e) {
            throw e;
        }
    }

    /**
     * Metoda odpowiedzialna za zwrócenie adresu url do bazy danych.
     *
     * @return urlUSB
     */
    private String createURL() {

        StringBuilder urlSB = new StringBuilder("jdbc:mysql://localhost:3306/punkt_szczepien");
        return urlSB.toString();
    }

    /**
     * Metoda służy do wykonywania zapytań oraz
     * zwracania wyniku za pomocą klasy CatchedRowSet.
     *
     * @param queryStmt
     * @return crs
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public ResultSet dbExecuteQuery(String queryStmt) throws SQLException, ClassNotFoundException {

        PreparedStatement stmt = null;
        ResultSet resultSet = null;
        CachedRowSet crs;

        try {

            dbConnect();

            stmt = conn.prepareStatement(queryStmt);

            resultSet = stmt.executeQuery(queryStmt);

            crs = new CachedRowSetWrapper();

            crs.populate(resultSet);
        } catch (SQLException e) {
            consoleTextArea.appendText("Problem occurred at executeQuery operation. \n");
            throw e;
        } finally {
            if (resultSet != null) {
                resultSet.close();
            }
            if (stmt != null) {
                stmt.close();
            }
            dbDisconnect();
        }

        return crs;
    }

    /**
     * Metoda służy do wykonywania zapytań modyfikujących bazę danych.
     *
     * @param sqlStmt
     * @throws SQLException
     * @throws ClassNotFoundException
     */
    public void dbExecuteUpdate(String sqlStmt) throws SQLException, ClassNotFoundException {

        Statement stmt = null;
        try {
            dbConnect();
            stmt = conn.createStatement();
            stmt.executeUpdate(sqlStmt);

        } catch (SQLException e) {
            consoleTextArea.appendText("Problem occurred at executeUpdate operation. \n");
            throw e;
        } finally {
            if (stmt != null) {
                stmt.close();
            }
            dbDisconnect();
        }
    }

}
