package edu.ib.punktszczepien;

import java.net.URL;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.chart.CategoryAxis;
import javafx.scene.chart.LineChart;
import javafx.scene.chart.NumberAxis;
import javafx.scene.chart.XYChart;
import javafx.scene.control.Button;
import javafx.stage.Stage;

/**
 * Klasa odpowiadająca za zarządzanie oknem 'Vaccine Stats' utworzonym po wcisnięciu przycisku "Wyświetl statystyki szczepień".
 * Zawiera metodę umożliwiającą wyświetlenie danych o szczepieniach zrealizowanych w zadanym dniu w postaci wykresu liniowego (showStats).
 *
 * @author AK
 * @version 1.0
 * @since 2022-02-08
 */
public class VaccineStatsViewController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private CategoryAxis categoryAxis;

    @FXML
    private NumberAxis numberAxis;

    @FXML
    private LineChart<?, ?> statsChart;

    @FXML
    private Button showStatsBtn;

    /**
     * Metoda umożliwiająca wyświetlenie danych o szczepieniach zrealizowanych w zadanym dniu w postaci wykresu liniowego.
     * Przechowywane dane za pośrednictwem mapy (para klucz-wartość).
     * Kluczem jest data szczepienia a wartością ilość osób zaszczepionych w danym dniu.
     *
     * @param event
     */
    @FXML
    void showStats(ActionEvent event) {
        Stage stage = (Stage) showStatsBtn.getScene().getWindow();
        LinkedHashMap<String, Integer> vaccineHistories = (LinkedHashMap<String, Integer>) stage.getUserData();

        XYChart.Series series = new XYChart.Series();

        for (Map.Entry<String, Integer> entry : vaccineHistories.entrySet()) {
            series.getData().add(new XYChart.Data(entry.getKey(), entry.getValue()));
        }

        statsChart.getData().add(series);
    }

    @FXML
    void initialize() {
        assert statsChart != null : "fx:id=\"statsChart\" was not injected: check your FXML file 'vaccineStatsView.fxml'.";
        assert showStatsBtn != null : "fx:id=\"showStatsBtn\" was not injected: check your FXML file 'vaccineStatsView.fxml'.";


    }
}
