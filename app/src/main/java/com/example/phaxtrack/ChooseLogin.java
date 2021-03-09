package com.example.phaxtrack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ChooseLogin extends AppCompatActivity {
    private Button button, button1;
    FirebaseAuth firebaseAuth;
    FirebaseUser firebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_choose_login);

        button = findViewById(R.id.healthCareLogin);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openHealthCareLogin();
            }
        });

        button1 = findViewById(R.id.patientLogin);
        button1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openPatientLogin();
            }
        });

        firebaseAuth = FirebaseAuth.getInstance();
        firebaseUser = firebaseAuth.getCurrentUser();

    }
        public void openHealthCareLogin(){
        Intent intent = new Intent (this, DoctorLoginPage.class);
        startActivity(intent);
        }
        public void openPatientLogin(){
        Intent intent = new Intent (this, PatientLoginPage.class);
        startActivity(intent);
        }

}