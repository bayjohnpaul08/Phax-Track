package com.example.phaxtrack.utils;

public class PatientInfoHelperClass {
    String image, surname, middleName, firstName, birthday, address, password, gender, id;

    public PatientInfoHelperClass() {
    }

    public PatientInfoHelperClass(String image, String surname, String middleName, String firstName, String birthday, String address, String password, String gender, String id) {
        this.image = image;
        this.surname = surname;
        this.middleName = middleName;
        this.firstName = firstName;
        this.birthday = birthday;
        this.address = address;
        this.password = password;
        this.gender = gender;
        this.id = id;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}

