package com.example.phaxtrack.dashboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phaxtrack.R;
import com.example.phaxtrack.adminDashboard;
import com.example.phaxtrack.utils.VaccineHelperClass;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class vaccine extends AppCompatActivity {

    private EditText VaccineId, VaccineName;
    private Button AddVaccine;
    DatabaseReference ref;
    VaccineHelperClass member;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vaccine);

        VaccineId = (EditText) findViewById(R.id.geVaccineId);
        VaccineName = (EditText) findViewById(R.id.geVaccineName);
        AddVaccine = (Button) findViewById(R.id.addVaccine);

        member = new VaccineHelperClass();
        AddVaccine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newVaccineAdded();
            }
        });
    }

    private void newVaccineAdded() {
        ref = FirebaseDatabase.getInstance().getReference().child("Vaccine List");
        String newId = VaccineId.getText().toString().trim();
        String newName = VaccineName.getText().toString().trim();

        member.setVaccine_name(newId + " - " + newName);
        ref.child(newId).setValue(member);
        Toast.makeText(this, "New Vaccine Successfully Added", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(vaccine.this, adminDashboard.class));
    }
}