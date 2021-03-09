package com.example.phaxtrack.utils;

import android.os.Parcelable;

import org.parceler.Parcel;

import java.io.Serializable;

public class SearchBarHelperClass implements Parcelable{
    private String image, fName, mName, lName,  id, age, birthday, address, email, vaccine_Name, institution_Name, vaccine_Date, date;

    public SearchBarHelperClass() {
    }

    public SearchBarHelperClass(String image, String fName, String mName, String lName, String id, String age, String birthday, String address, String email, String vaccine_Name, String institution_Name, String vaccine_Date, String date) {
        this.image = image;
        this.fName = fName;
        this.mName = mName;
        this.lName = lName;
        this.id = id;
        this.age = age;
        this.birthday = birthday;
        this.address = address;
        this.email = email;
        this.vaccine_Name = vaccine_Name;
        this.institution_Name = institution_Name;
        this.vaccine_Date = vaccine_Date;
        this.date = date;
    }

    protected SearchBarHelperClass(android.os.Parcel in) {
        image = in.readString();
        fName = in.readString();
        mName = in.readString();
        lName = in.readString();
        id = in.readString();
        age = in.readString();
        birthday = in.readString();
        address = in.readString();
        email = in.readString();
        vaccine_Name = in.readString();
        institution_Name = in.readString();
        vaccine_Date = in.readString();
    }

    @Override
    public void writeToParcel(android.os.Parcel dest, int flags) {
        dest.writeString(image);
        dest.writeString(fName);
        dest.writeString(mName);
        dest.writeString(lName);
        dest.writeString(id);
        dest.writeString(age);
        dest.writeString(birthday);
        dest.writeString(address);
        dest.writeString(email);
        dest.writeString(vaccine_Name);
        dest.writeString(institution_Name);
        dest.writeString(vaccine_Date);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<SearchBarHelperClass> CREATOR = new Creator<SearchBarHelperClass>() {
        @Override
        public SearchBarHelperClass createFromParcel(android.os.Parcel in) {
            return new SearchBarHelperClass(in);
        }

        @Override
        public SearchBarHelperClass[] newArray(int size) {
            return new SearchBarHelperClass[size];
        }
    };

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getfName() {
        return fName;
    }

    public void setfName(String fName) {
        this.fName = fName;
    }

    public String getmName() {
        return mName;
    }

    public void setmName(String mName) {
        this.mName = mName;
    }

    public String getlName() {
        return lName;
    }

    public void setlName(String lName) {
        this.lName = lName;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getVaccine_Name() {
        return vaccine_Name;
    }

    public void setVaccine_Name(String vaccine_Name) {
        this.vaccine_Name = vaccine_Name;
    }

    public String getInstitution_Name() {
        return institution_Name;
    }

    public void setInstitution_Name(String institution_Name) {
        this.institution_Name = institution_Name;
    }

    public String getVaccine_Date() {
        return vaccine_Date;
    }

    public void setVaccine_Date(String vaccine_Date) {
        this.vaccine_Date = vaccine_Date;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}