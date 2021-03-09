package com.example.phaxtrack;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.phaxtrack.utils.addPatientHelperClass;

import org.parceler.Parcels;
import org.w3c.dom.Text;

import de.hdodenhof.circleimageview.CircleImageView;

public class PatientInfo extends AppCompatActivity implements View.OnClickListener {

    private TextView getName, PatientButton, VaccineButton, AdverseButton, logout;
    private CircleImageView imageView;
    addPatientHelperClass helperClass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_info);

        getName = (TextView) findViewById(R.id.getName);
        imageView = (CircleImageView) findViewById(R.id.image);
        PatientButton = (TextView) findViewById(R.id.patientButton);
        VaccineButton = (TextView) findViewById(R.id.VaccineButton);
        AdverseButton = (TextView) findViewById(R.id.adverseButton);
        logout = (TextView) findViewById(R.id.logout);
        PatientButton.setOnClickListener(this);
        VaccineButton.setOnClickListener(this);
        AdverseButton.setOnClickListener(this);
        logout.setOnClickListener(this);

        helperClass = (addPatientHelperClass) Parcels.unwrap(getIntent().getParcelableExtra("helperClass"));

        getName.setText("Hello, " + helperClass.getFirstName() + "!");
        Glide.with(imageView.getRootView()).load(helperClass.getImage()).placeholder(R.drawable.group_1).into(imageView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.patientButton:
                openPatientButton(helperClass);
                break;

            case R.id.VaccineButton:
                openVaccineButton(helperClass);
                break;

            case R.id.adverseButton:
                openAdverseButton(helperClass);
                break;

            case R.id.logout:
                log();
                break;
        }
    }

    private void log() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PatientInfo.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(PatientInfo.this).inflate(
                R.layout.dialog_logout,
                (ConstraintLayout) findViewById(R.id.dialog103)
        );

        builder.setView(view);

        final AlertDialog alertDialog = builder.create();
        view.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                startActivity(new Intent(PatientInfo.this, ChooseLogin.class));
                finish();
            }
        });

        if(alertDialog.getWindow() != null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }

    private void openPatientButton(addPatientHelperClass helperClass) {
        Intent intent = new Intent( PatientInfo.this , patient_information_button.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("helperClass", Parcels.wrap(helperClass));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void openVaccineButton(addPatientHelperClass helperClass) {
        Intent intent = new Intent( PatientInfo.this , patient_vaccine_button.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("helperClass", Parcels.wrap(helperClass));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void openAdverseButton(addPatientHelperClass helperClass) {
        Intent intent = new Intent( PatientInfo.this , patient_adverse_button.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("helperClass", Parcels.wrap(helperClass));
        intent.putExtras(bundle);
        startActivity(intent);
    }

}