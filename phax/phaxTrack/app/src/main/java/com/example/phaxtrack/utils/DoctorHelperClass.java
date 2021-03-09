package com.example.phaxtrack.utils;

import android.net.Uri;
import android.os.Parcel;
import android.os.Parcelable;

public class DoctorHelperClass implements Parcelable {
    String image, email, username, workArea, healthInstitution, location;

    public DoctorHelperClass() {
    }

    public DoctorHelperClass( String image, String email, String username, String workArea, String healthInstitution, String location) {

        this.image = image;
        this.email = email;
        this.username = username;
        this.workArea = workArea;
        this.healthInstitution = healthInstitution;
        this.location = location;
    }

    protected DoctorHelperClass(Parcel in) {
        image = in.readString();
        email = in.readString();
        username = in.readString();
        workArea = in.readString();
        healthInstitution = in.readString();
        location = in.readString();
    }

    public static final Creator<DoctorHelperClass> CREATOR = new Creator<DoctorHelperClass>() {
        @Override
        public DoctorHelperClass createFromParcel(Parcel in) {
            return new DoctorHelperClass(in);
        }

        @Override
        public DoctorHelperClass[] newArray(int size) {
            return new DoctorHelperClass[size];
        }
    };

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getWorkArea() {
        return workArea;
    }

    public void setWorkArea(String workArea) {
        this.workArea = workArea;
    }

    public String getHealthInstitution() {
        return healthInstitution;
    }

    public void setHealthInstitution(String healthInstitution) {
        this.healthInstitution = healthInstitution;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(image);
        dest.writeString(email);
        dest.writeString(username);
        dest.writeString(workArea);
        dest.writeString(healthInstitution);
        dest.writeString(location);
    }
}