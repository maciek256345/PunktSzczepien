package edu.ib.punktszczepien;

/**
 * Klasa stanowiąca odzwierciedlenie widoku punkt_szczepien_historia z bazy danych.
 * Pozwala na uzyskanie dostępu do danych dotyczących szczepień zrealizowanych.
 *
 * @author AK
 * @version 1.0
 * @since 2022-02-08
 */
public class VaccineHistory {
    private String imie;
    private String nazwisko;
    private String pesel;
    private String nr_tel;
    private String nazwa;
    private String termin;
    private String idSzczepienia;
    private String idSzczepionki;

    public String getIdSzczepienia() {
        return idSzczepienia;
    }

    public void setIdSzczepienia(String idSzczepienia) {
        this.idSzczepienia = idSzczepienia;
    }

    public String getIdSzczepionki() {
        return idSzczepionki;
    }

    public void setIdSzczepionki(String idSzczepionki) {
        this.idSzczepionki = idSzczepionki;
    }

    public String getImie() {
        return imie;
    }

    public void setImie(String imie) {
        this.imie = imie;
    }

    public String getNazwisko() {
        return nazwisko;
    }

    public void setNazwisko(String nazwisko) {
        this.nazwisko = nazwisko;
    }

    public String getPesel() {
        return pesel;
    }

    public void setPesel(String pesel) {
        this.pesel = pesel;
    }

    public String getNr_tel() {
        return nr_tel;
    }

    public void setNr_tel(String nr_tel) {
        this.nr_tel = nr_tel;
    }

    public String getNazwa() {
        return nazwa;
    }

    public void setNazwa(String nazwa) {
        this.nazwa = nazwa;
    }

    public String getTermin() {
        return termin;
    }

    public void setTermin(String termin) {
        this.termin = termin;
    }
}
