package edu.ib.punktszczepien;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Date;

public class Patient {
    private String pesel;
    private LocalDate birthDate;

    public Patient(String pesel, Date birthDate) {
        this.pesel = pesel;
        this.birthDate = convertToLocalDateViaSqlDate(birthDate);
    }

    private LocalDate convertToLocalDateViaSqlDate(Date dateToConvert) {
        return new java.sql.Date(dateToConvert.getTime()).toLocalDate();
    }

    public String getPesel() {
        return pesel;
    }

    public LocalDate getBirthDate() {
        return birthDate;
    }

    public long getAge() {
        return LocalDate.from(birthDate).until(LocalDate.now(), ChronoUnit.YEARS);
    }
}
