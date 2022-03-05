package edu.ib.punktszczepien;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class VaccinationDate {
    /**
     * Klasa zawierjąca indromacje odnośnie
     * dostępnego terminu szczepienia.
     * Polom klasy przypisywane są obiekty klas właściwości.
     * Oprócz standardowych seterów i getterów
     * każdy atrybut posiada również getter właściwości.
     *
     * @author MS
     * @version 1.0
     * @since 2022-02-08
     */
    private StringProperty termin;

    public VaccinationDate() {
        this.termin = new SimpleStringProperty();
    }

    public String getTermin() {
        return termin.get();
    }

    public StringProperty terminProperty() {
        return termin;
    }

    public void setTermin(String termin) {
        this.termin.set(termin);
    }
}
