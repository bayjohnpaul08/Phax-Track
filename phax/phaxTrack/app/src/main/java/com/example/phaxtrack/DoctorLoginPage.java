package com.example.phaxtrack;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phaxtrack.dashboard.registerDoctor;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class DoctorLoginPage extends AppCompatActivity implements View.OnClickListener {

    private EditText mail, pass;
    private FirebaseAuth mAuth;
    private Button loginButton;
    private ProgressBar progressBar;
    private TextView signUp, forgotPass;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor_login_page);

        mAuth = FirebaseAuth.getInstance();

        mail = (EditText) findViewById(R.id.inputEmail);
        pass = (EditText) findViewById(R.id.input_password);
        signUp = (TextView) findViewById(R.id.signupText);
        loginButton = (Button) findViewById(R.id.loginButton);
        forgotPass = (TextView) findViewById(R.id.forgotPass);
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.GONE);
        loginButton.setOnClickListener(this);
        signUp.setOnClickListener(this);
        forgotPass.setOnClickListener(this);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.loginButton:
                doctorLogin();
                break;
            case R.id.signupText:
                startActivity(new Intent(DoctorLoginPage.this, registerDoctor.class));
                break;

            case R.id.forgotPass:
                newPass();
                break;
        }

    }

    private void newPass() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DoctorLoginPage.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(DoctorLoginPage.this).inflate(
                R.layout.dialog_forgot_pass,
                (ConstraintLayout) findViewById(R.id.dialog102)
        );

        builder.setView(view);

        final TextView message = (TextView) view.findViewById(R.id.pass_caption);
        final EditText req_Email = (EditText) view.findViewById(R.id.req_Email);

        final AlertDialog alertDialog = builder.create();
        view.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String getEmail = req_Email.getText().toString().trim();

                if(getEmail.isEmpty()){
                    message.setText("Email is required for verification!");
                    message.setTextColor(Color.RED);
                }
                else if(!Patterns.EMAIL_ADDRESS.matcher(getEmail).matches()){
                    message.setText("Please enter a valid email address!");
                    message.setTextColor(Color.RED);
                }else{
                    mAuth.sendPasswordResetEmail(getEmail).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(DoctorLoginPage.this, "Password link has been sent to your email.", Toast.LENGTH_LONG).show();
                            }else{
                                Toast.makeText(DoctorLoginPage.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });

                    alertDialog.dismiss();
                }

            }
        });

        if(alertDialog.getWindow() != null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }

    public void doctorLogin(){
        String email = mail.getText().toString().trim();
        String password = pass.getText().toString().trim();

        if(email.isEmpty()){
            emptyDialog();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            openDialog();
            return;
        }

        if (password.isEmpty()){
            emptyDialog();
            return;
        }

        if(password.length() < 6){
            openDialog();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){


                    if(mAuth.getCurrentUser().isEmailVerified()){
                        //redirect to profile
                        startActivity(new Intent(DoctorLoginPage.this, index.class));
                        finish();
                    }else{
                        progressBar.setVisibility(View.GONE);
                        verifyDialog();
                    }
                }
                else {
                    progressBar.setVisibility(View.GONE);
                    openDialog();
                }

            }
        });
    }

    private void verifyDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(DoctorLoginPage.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(DoctorLoginPage.this).inflate(
                R.layout.dialog_verify_email,
                (ConstraintLayout) findViewById(R.id.dialog101)
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

    private void openDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(DoctorLoginPage.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(DoctorLoginPage.this).inflate(
                R.layout.dialog_doctor_login,
                (ConstraintLayout) findViewById(R.id.dialog1)
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
        AlertDialog.Builder builder = new AlertDialog.Builder(DoctorLoginPage.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(DoctorLoginPage.this).inflate(
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
