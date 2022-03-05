package edu.ib.punktszczepien;

import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class Vaccine {
    /**
     * Klasa zawierająca informacje
     * odnośnie szczepionek - ich id oraz nazwa.
     * Polom klasy przypisywane są obiekty klas właściwości.
     * Oprócz standardowych seterów i getterów
     * każdy atrybut posiada również getter właściwości.
     *
     * @author MS
     * @version 1.0
     * @since 2022-02-08
     */
    private IntegerProperty id;
    private StringProperty nazwa;

    public Vaccine() {
        this.id = new SimpleIntegerProperty();
        this.nazwa = new SimpleStringProperty();
    }


    public int getId() {
        return id.get();
    }

    public IntegerProperty idProperty() {
        return id;
    }

    public void setId(int id) {
        this.id.set(id);
    }

    public String getNazwa() {
        return nazwa.get();
    }

    public StringProperty nazwaProperty() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa.set(nazwa);
    }
}
