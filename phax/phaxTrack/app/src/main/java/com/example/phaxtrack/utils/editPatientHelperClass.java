package com.example.phaxtrack.utils;

public class editPatientHelperClass {

    String age, address, number, email;

    public editPatientHelperClass() {
    }

    public editPatientHelperClass(String age, String address, String number, String email) {
        this.age = age;
        this.address = address;
        this.number = number;
        this.email = email;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
