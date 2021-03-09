package com.example.phaxtrack.utils;

public class VaccineHelperClass {
    String Vaccine_id, Vaccine_name;

    public VaccineHelperClass() {
    }

    public VaccineHelperClass(String vaccine_id, String vaccine_name) {
        Vaccine_id = vaccine_id;
        Vaccine_name = vaccine_name;
    }

    public String getVaccine_id() {
        return Vaccine_id;
    }

    public void setVaccine_id(String vaccine_id) {
        Vaccine_id = vaccine_id;
    }

    public String getVaccine_name() {
        return Vaccine_name;
    }

    public void setVaccine_name(String vaccine_name) {
        Vaccine_name = vaccine_name;
    }

    @Override
    public String toString() {
        return Vaccine_name;
    }
}
