package com.example.phaxtrack;

import android.content.Context;
import android.content.SharedPreferences;

import com.example.phaxtrack.utils.addPatientHelperClass;
public class SessionManagement {
    SharedPreferences sharedPreferences;
    SharedPreferences.Editor editor;
    String SHARED_PREF_NAME = "session";
    String SESSION_KEY = "session_user";

    public SessionManagement(Context context){
        sharedPreferences = context.getSharedPreferences(SHARED_PREF_NAME,Context.MODE_PRIVATE);
        editor = sharedPreferences.edit();
    }

    public void saveSession(addPatientHelperClass user){
        //save session of user whenever user is logged in
        String id = user.getId();

        editor.putString(SESSION_KEY, id).commit();
    }

    public String getSession(){
        //return user id whose session is saved
        return sharedPreferences.getString(SESSION_KEY, "");
    }

    public void removeSession(){
        editor.putString(SESSION_KEY,"").commit();
    }
}