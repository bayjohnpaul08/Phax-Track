package com.example.phaxtrack.utils;

public class immuneHelperClass {
    String clinics, date, hcp, vaccine;

    public immuneHelperClass() {
    }

    public immuneHelperClass(String clinics, String date, String hcp, String vaccine) {
        this.clinics = clinics;
        this.date = date;
        this.hcp = hcp;
        this.vaccine = vaccine;
    }

    public String getClinics() {
        return clinics;
    }

    public void setClinics(String clinics) {
        this.clinics = clinics;
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

    public String getVaccine() {
        return vaccine;
    }

    public void setVaccine(String vaccine) {
        this.vaccine = vaccine;
    }
}
