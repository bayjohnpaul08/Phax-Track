package com.example.phaxtrack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.phaxtrack.utils.AdverseAnsHelperClass;
import com.example.phaxtrack.utils.addPatientHelperClass;
import com.example.phaxtrack.utils.editPatientHelperClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import org.parceler.Parcels;
import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

public class patient_information_button extends AppCompatActivity {

    private CircleImageView image;
    private TextView getName, getFullname, getGender, getAge, getAddress, getNumber, getEmail, button_home, edit;
    addPatientHelperClass helperClass;
    DatabaseReference reference;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_information_button);

        image = (CircleImageView) findViewById(R.id.image);
        getName = (TextView) findViewById(R.id.getName);
        getFullname = (TextView) findViewById(R.id.getFullName);
        getGender = (TextView) findViewById(R.id.getGender);
        getAge = (TextView) findViewById(R.id.getTheAge);
        getAddress = (TextView) findViewById(R.id.getAddress);
        getNumber = (TextView) findViewById(R.id.getContactNumber);
        getEmail = (TextView) findViewById(R.id.getEmailAddress);
        button_home = (TextView) findViewById(R.id.homeButton);
        edit = (TextView) findViewById(R.id.edit);

        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openEdit(helperClass);
            }
        });

        helperClass = (addPatientHelperClass) Parcels.unwrap(getIntent().getParcelableExtra("helperClass"));

        reference = FirebaseDatabase.getInstance().getReference("Patient List");

        Glide.with(image.getRootView()).load(helperClass.getImage()).placeholder(R.drawable.group_1).into(image);
        getName.setText("Hello, " + helperClass.getFirstName() + "!");
        getFullname.setText(helperClass.getSurname() + ", " + helperClass.getFirstName() + " " + helperClass.getMiddleName() );
        getGender.setText(helperClass.getGender());
        getAge.setText(helperClass.getAge() + " years old");
        getAddress.setText(helperClass.getAddress());
        getNumber.setText(helperClass.getNumber());
        getEmail.setText(helperClass.getEmail());

        button_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInfo(helperClass);
            }
        });
    }

    private void openEdit(addPatientHelperClass helperClass) {
        Intent intent = new Intent( patient_information_button.this , patientEdit.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("helperClass", Parcels.wrap(helperClass));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void openInfo(addPatientHelperClass helperClass) {
        Intent intent = new Intent( patient_information_button.this , PatientInfo.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("helperClass", Parcels.wrap(helperClass));
        intent.putExtras(bundle);
        startActivity(intent);
    }
}