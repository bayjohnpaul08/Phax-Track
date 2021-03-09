package com.example.phaxtrack.utils;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class addPatientHelperClass implements Parcelable {

    String address, birthday, firstName, age, gender, id, image, middleName, password, surname, number, email;

    public addPatientHelperClass() {
    }

    public addPatientHelperClass(String address, String birthday, String firstName, String age, String gender, String id, String image, String middleName, String password, String surname, String number, String email) {
        this.address = address;
        this.birthday = birthday;
        this.firstName = firstName;
        this.age = age;
        this.gender = gender;
        this.id = id;
        this.image = image;
        this.middleName = middleName;
        this.password = password;
        this.surname = surname;
        this.number = number;
        this.email = email;
    }

    protected addPatientHelperClass(android.os.Parcel in) {
        address = in.readString();
        birthday = in.readString();
        firstName = in.readString();
        age = in.readString();
        gender = in.readString();
        id = in.readString();
        image = in.readString();
        middleName = in.readString();
        password = in.readString();
        surname = in.readString();
        number = in.readString();
        email = in.readString();

    }

    public static final Creator<addPatientHelperClass> CREATOR = new Creator<addPatientHelperClass>() {
        @Override
        public addPatientHelperClass createFromParcel(android.os.Parcel in) {
            return new addPatientHelperClass(in);
        }

        @Override
        public addPatientHelperClass[] newArray(int size) {
            return new addPatientHelperClass[size];
        }
    };



    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getAge() {
        return age;
    }

    public void setAge(String age) {
        this.age = age;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
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

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getMiddleName() {
        return middleName;
    }

    public void setMiddleName(String middleName) {
        this.middleName = middleName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeString(address);
        dest.writeString(birthday);
        dest.writeString(firstName);
        dest.writeString(age);
        dest.writeString(gender);
        dest.writeString(id);
        dest.writeString(image);
        dest.writeString(middleName);
        dest.writeString(password);
        dest.writeString(surname);
        dest.writeString(number);
        dest.writeString(email);
    }
}