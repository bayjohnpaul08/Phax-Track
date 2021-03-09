package com.example.phaxtrack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phaxtrack.utils.addPatientHelperClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.parceler.Parcels;

public class PatientLoginPage extends AppCompatActivity implements View.OnClickListener {

    private EditText inputId, inputPassword;
    private TextView register;
    private Button loginButton;
    private DatabaseReference reference;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_login_page);

        inputId = (EditText) findViewById(R.id.input_id);
        inputPassword = (EditText) findViewById(R.id.input_password);
        register = (TextView) findViewById(R.id.register);
        loginButton = (Button) findViewById(R.id.patient_login_button);
        progressBar = (ProgressBar) findViewById(R.id.progressBar5);
        progressBar.setVisibility(View.GONE);
        register.setOnClickListener(this);
        loginButton.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.patient_login_button:
                openAccount();
                break;

            case R.id.register:
                startActivity(new Intent(PatientLoginPage.this, registerPatient.class));
                break;
        }
    }


    public void openAccount() {
        final String  id = inputId.getText().toString().trim();
        final String  pass = inputPassword.getText().toString().trim();

        if(id.isEmpty()){
            emptyDialog();
            return;
        }

        if(pass.isEmpty()){
            emptyDialog();
            return;
        }

        if(pass.length() < 6){
            openDialog();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        reference = FirebaseDatabase.getInstance().getReference().child("Patient List");

        reference.child(id).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    String getPass = snapshot.child("password").getValue(String.class);

                    if(getPass.equals(pass)) {

                        String address = snapshot.child("address").getValue(String.class);
                        String birthday = snapshot.child("birthday").getValue(String.class);
                        String firstName = snapshot.child("firstName").getValue(String.class);
                        String gender = snapshot.child("gender").getValue(String.class);
                        String getId = snapshot.child("id").getValue(String.class);
                        String image = snapshot.child("image").getValue(String.class);
                        String middleName = snapshot.child("middleName").getValue(String.class);
                        String email = snapshot.child("email").getValue(String.class);
                        String password = snapshot.child("password").getValue(String.class);
                        String surname = snapshot.child("surname").getValue(String.class);
                        String number = snapshot.child("number").getValue(String.class);
                        String age = snapshot.child("age").getValue(String.class);

                        addPatientHelperClass helperClass = new addPatientHelperClass(address, birthday, firstName, age, gender, getId, image, middleName, password, surname, number, email);
                        Intent intent = new Intent( PatientLoginPage.this , PatientInfo.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("helperClass", Parcels.wrap(helperClass));
                        intent.putExtras(bundle);
                        startActivity(intent);

                    }else{
                        progressBar.setVisibility(View.GONE);
                        openDialog();
                    }
                }else {
                    progressBar.setVisibility(View.GONE);
                    openDialog();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void openDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(PatientLoginPage.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(PatientLoginPage.this).inflate(
                R.layout.dialog_patient_login,
                (ConstraintLayout) findViewById(R.id.dialog)
        );

        builder.setView(view);

        final AlertDialog alertDialog = builder.create();
        view.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        if(alertDialog.getWindow() != null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }

    private void emptyDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(PatientLoginPage.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(PatientLoginPage.this).inflate(
                R.layout.dialog_empty,
                (ConstraintLayout) findViewById(R.id.dialog3)
        );

        builder.setView(view);

        final AlertDialog alertDialog = builder.create();
        view.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });

        if(alertDialog.getWindow() != null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }
}