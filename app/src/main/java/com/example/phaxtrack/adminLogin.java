package com.example.phaxtrack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;


public class adminLogin extends AppCompatActivity implements View.OnClickListener {

    private EditText mail, pass;
    private FirebaseAuth mAuth;
    private Button loginButton;
    private ProgressBar progressBar;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_login_page);

        mAuth = FirebaseAuth.getInstance();

        mail = (EditText) findViewById(R.id.inputEmail);
        pass = (EditText) findViewById(R.id.input_password);
        loginButton = (Button) findViewById(R.id.patient_login_button);
        loginButton.setOnClickListener(this);
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.GONE);

    }

    @Override
    public void onClick(View v) {
        adminLogin();
    }

    public void adminLogin(){
        String email = mail.getText().toString().trim();
        String password = pass.getText().toString().trim();

        if(email.isEmpty()){
            mail.setError("Email is Required");
            mail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            mail.setError("Please enter a valid email!");
            mail.requestFocus();
            return;
        }

        if (password.isEmpty()){
            pass.setError("Password is Required!");
            pass.requestFocus();
            return;
        }

        if(password.length() < 6){
            pass.setError("Minimum password length is 6 characters!");
            pass.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //redirect to profile
                    startActivity(new Intent(adminLogin.this, adminDashboard.class));
                    finish();
                }
                else {
                    Toast.makeText(adminLogin.this, "Failed to login! Please check you credentials!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
}