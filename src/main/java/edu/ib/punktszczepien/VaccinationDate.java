package edu.ib.punktszczepien;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class VaccinationDate {
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
