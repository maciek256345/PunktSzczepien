package edu.ib.punktszczepien;

import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

public class PatientView {
    /**
     * Klasa zawiera infromacje dotyczące typu szczepienia
     * oraz terminu jego wykonania. Polom klasy przypisywane
     * są obiekty klas właściwości.
     * Oprócz standardowych seterów i getterów
     * każdy atrybut posiada również getter właściwości.
     *
     * @author MS
     * @version 1.0
     * @since 2022-02-08
     */
    private StringProperty typ_szczepienia;
    private StringProperty termin;

    public PatientView() {
        this.typ_szczepienia = new SimpleStringProperty();
        this.termin = new SimpleStringProperty();

    }

    public String getTyp_szczepienia() {
        return typ_szczepienia.get();
    }

    public StringProperty typ_szczepieniaProperty() {
        return typ_szczepienia;
    }

    public void setTyp_szczepienia(String typ_szczepienia) {
        this.typ_szczepienia.set(typ_szczepienia);
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
