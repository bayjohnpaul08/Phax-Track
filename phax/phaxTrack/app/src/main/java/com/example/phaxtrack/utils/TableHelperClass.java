package com.example.phaxtrack.utils;

public class TableHelperClass {
    String vaccine, date, hcp, clinics;

    public TableHelperClass() {
    }

    public TableHelperClass(String vaccine, String date, String hcp, String clinics) {
        this.vaccine = vaccine;
        this.date = date;
        this.hcp = hcp;
        this.clinics = clinics;
    }

    public String getVaccine() {
        return vaccine;
    }

    public void setVaccine(String vaccine) {
        this.vaccine = vaccine;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getHcp() {
        return hcp;
    }

    public void setHcp(String hcp) {
        this.hcp = hcp;
    }

    public String getClinics() {
        return clinics;
    }

    public void setClinics(String clinics) {
        this.clinics = clinics;
    }
}
