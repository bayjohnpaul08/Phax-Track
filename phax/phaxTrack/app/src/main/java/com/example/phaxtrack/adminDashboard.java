package com.example.phaxtrack;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.phaxtrack.dashboard.doctor;
import com.example.phaxtrack.dashboard.institution;
import com.example.phaxtrack.dashboard.registerDoctor;
import com.example.phaxtrack.dashboard.vaccine;

public class adminDashboard extends AppCompatActivity implements View.OnClickListener {

    Button addDoctor, getVaccine, getInstitution;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_dashboard);

        addDoctor = (Button) findViewById(R.id.addDoctor);
        addDoctor.setOnClickListener(this);
        getVaccine = (Button) findViewById(R.id.newVaccine);
        getVaccine.setOnClickListener(this);
        getInstitution = (Button) findViewById(R.id.newInstitution);
        getInstitution.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.addDoctor:
                startActivity(new Intent(adminDashboard.this, doctor.class));
                break;
            case R.id.newVaccine:
                startActivity(new Intent(adminDashboard.this, vaccine.class));
                break;
            case R.id.newInstitution:
                startActivity(new Intent(adminDashboard.this, institution.class));
                break;
        }
    }
}