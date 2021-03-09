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
import com.example.phaxtrack.utils.InstitutionHelperClass;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class institution extends AppCompatActivity {

    private EditText InstitutionId, InstitutionName;
    private Button addInstitution;
    DatabaseReference ref;
    InstitutionHelperClass member;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_institution);
        InstitutionId = (EditText) findViewById(R.id.getInstitutionId);
        InstitutionName = (EditText) findViewById(R.id.getInstitutionName);
        addInstitution = (Button) findViewById(R.id.addInstitution);

        member = new InstitutionHelperClass();
        addInstitution.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newInstitutionAdded();
            }
        });
    }

    private void newInstitutionAdded() {
        ref = FirebaseDatabase.getInstance().getReference().child("Institution List");
        String newId = InstitutionId.getText().toString().trim();
        String newName = InstitutionName.getText().toString().trim();

        member.setName(newId + " - " + newName);
        ref.child(newId).setValue(member);
        Toast.makeText(this, "New Institution Successfully Added", Toast.LENGTH_SHORT).show();
        startActivity(new Intent(institution.this, adminDashboard.class));
    }
}