package com.example.phaxtrack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseException;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.PhoneAuthCredential;
import com.google.firebase.auth.PhoneAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.rilixtech.widget.countrycodepicker.CountryCodePicker;

import java.util.concurrent.TimeUnit;

public class otp extends AppCompatActivity {

    public static final String TAG = "TAG";
    FirebaseAuth mAuth;
    DatabaseReference reference;
    EditText number, codeNumber;
    TextView state;
    Button nextBtn;
    ProgressBar progressBar;
    CountryCodePicker countryCode;
    String verificationId;
    PhoneAuthProvider.ForceResendingToken token;
    Boolean verificationInProgress = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_otp);

        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("Patient List");
        number = (EditText) findViewById(R.id.number);
        codeNumber = (EditText) findViewById(R.id.codeNumber);
        state = (TextView) findViewById(R.id.state);
        nextBtn = (Button) findViewById(R.id.nextBtn);
        progressBar = (ProgressBar) findViewById(R.id.progressBar3);
        countryCode = (CountryCodePicker) findViewById(R.id.ccp);

        nextBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!verificationInProgress){

                    if(!number.getText().toString().isEmpty() && number.getText().toString().length() == 10){

                        String phoneNum = "+"+countryCode.getSelectedCountryCode()+number.getText().toString();
                        Log.d(TAG, "onClick: Phone NO -> " + phoneNum);
                        progressBar.setVisibility(View.VISIBLE);
                        state.setText("Sending OTP..");
                        state.setVisibility(View.VISIBLE);
                        requestOTP(phoneNum);


                    }else{
                        number.setError("Phone Number is not valid");
                    }
                }else{

                    String userOTP = codeNumber.getText().toString();
                    if(!userOTP.isEmpty() && userOTP.length() == 6){
                        PhoneAuthCredential credential = PhoneAuthProvider.getCredential(verificationId, userOTP);
                        verifyAuth(credential);

                    }else{
                        codeNumber.setError("Valid otp is required!");
                    }
                }

            }
        });
    }

//    @Override
//    protected void onStart() {
//        super.onStart();
//
//        if(mAuth.getCurrentUser() != null){
//            progressBar.setVisibility(View.VISIBLE);
//            state.setText("Checking...");
//            state.setVisibility(View.VISIBLE);
//        }
//    }

    private void verifyAuth(PhoneAuthCredential credential) {
        mAuth.signInWithCredential(credential).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    progressBar.setVisibility(View.VISIBLE);
                    startActivity(new Intent(otp.this, registerPatient.class));
                    finish();

                    Toast.makeText(otp.this, "Authentication is successful", Toast.LENGTH_SHORT).show();
                }else{
                    Toast.makeText(otp.this, "Authentication is failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void requestOTP(String phoneNum) {
        PhoneAuthProvider.getInstance().verifyPhoneNumber(phoneNum, 60L, TimeUnit.SECONDS, this, new PhoneAuthProvider.OnVerificationStateChangedCallbacks() {

            @Override
            public void onCodeSent(@NonNull String s, @NonNull PhoneAuthProvider.ForceResendingToken forceResendingToken) {
                super.onCodeSent(s, forceResendingToken);

                progressBar.setVisibility(View.GONE);
                state.setVisibility(View.GONE);
                codeNumber.setVisibility(View.VISIBLE);
                verificationId = s;
                token = forceResendingToken;
                nextBtn.setText("Verify");
                verificationInProgress = true;
            }

            @Override
            public void onCodeAutoRetrievalTimeOut(@NonNull String s) {
                super.onCodeAutoRetrievalTimeOut(s);
            }

            @Override
            public void onVerificationCompleted(@NonNull PhoneAuthCredential phoneAuthCredential) {

            }

            @Override
            public void onVerificationFailed(@NonNull FirebaseException e) {
                Toast.makeText(otp.this, "Cannot create account" + e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }
}