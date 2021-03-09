package com.example.phaxtrack.dashboard;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phaxtrack.DoctorLoginPage;
import com.example.phaxtrack.R;
import com.example.phaxtrack.utils.DoctorHelperClass;
import com.example.phaxtrack.utils.InstitutionHelperClass;
import com.example.phaxtrack.utils.WorkAreaHelperClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.List;

public class registerDoctor extends AppCompatActivity implements View.OnClickListener {

    private static final String TAG = "failure";
    private EditText mEmail, mUsername, mLocation, mPassword, mWorkArea;
    private TextView loginText;
    private Button createButton;
    private FirebaseAuth mAuth;
    private AutoCompleteTextView mHealthInstitution;
    private String getWorkArea = "";
    private String getHealthInstitution = "";
    private ProgressBar progressBar;
    private List<WorkAreaHelperClass> workAreaHelperClassList;
    private List<InstitutionHelperClass> institutionHelperClassList;
    private DatabaseReference reference;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private ImageView docProfilePic;
    private Uri imageUri;
    Uri uri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_doctor);

        mAuth = FirebaseAuth.getInstance();
        storage = FirebaseStorage.getInstance();
        storageReference = storage.getReference();
        reference = FirebaseDatabase.getInstance().getReference();

        docProfilePic = (ImageView) findViewById(R.id.patientImage);
        mEmail = (EditText) findViewById(R.id.inputEmail);
        mUsername = (EditText) findViewById(R.id.inputUserName);
        mLocation = (EditText) findViewById(R.id.inputLocation);
        mPassword = (EditText) findViewById(R.id.input_password);
        mWorkArea = (EditText) findViewById(R.id.inputWorkArea);
        mHealthInstitution = (AutoCompleteTextView) findViewById(R.id.inputHealthInstitution);
        createButton = (Button) findViewById(R.id.searchButton);
        progressBar = (ProgressBar) findViewById(R.id.progressBar2);
        progressBar.setVisibility(View.GONE);
        loginText = (TextView) findViewById(R.id.loginText);

        mWorkArea.setVisibility(View.VISIBLE);


        loginText.setOnClickListener(this);
        docProfilePic.setOnClickListener(this);
        createButton.setOnClickListener(this);

//        chooseWorkArea();
        chooseInstitution();

    }

    private void chooseInstitution() {
        institutionHelperClassList = new ArrayList<>();

        reference.child("Institution List").addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String id = ds.getKey();
                        String name = ds.child("name").getValue().toString();
                        institutionHelperClassList.add(new InstitutionHelperClass(id, name));
                    }

                    ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.option_item, institutionHelperClassList);
                    mHealthInstitution.setAdapter(adapter);
                    mHealthInstitution.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                            getHealthInstitution = adapterView.getItemAtPosition(position).toString();
                        }

                        @Override
                        public void onNothingSelected(AdapterView<?> parent) {

                        }
                    });
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

//    private void chooseWorkArea() {
//            workAreaHelperClassList = new ArrayList<>();
//
//            reference.child("Work Area List").addValueEventListener(new ValueEventListener() {
//                @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
//                @Override
//                public void onDataChange(@NonNull DataSnapshot snapshot) {
//                    if (snapshot.exists()) {
//                        for (DataSnapshot ds : snapshot.getChildren()) {
//                            String id = ds.getKey();
//                            String name = ds.child("name").getValue().toString();
//                            workAreaHelperClassList.add(new WorkAreaHelperClass(id, name));
//                        }
//
//                        ArrayAdapter adapter = new ArrayAdapter(getApplicationContext(), R.layout.option_item, workAreaHelperClassList);
//                        mWorkArea.setAdapter(adapter);
//                        mWorkArea.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//                            @Override
//                            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
//                                getWorkArea = adapterView.getItemAtPosition(position).toString();
//
//                            }
//
//                            @Override
//                            public void onNothingSelected(AdapterView<?> parent) {
//
//                            }
//                        });
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError error) {
//
//                }
//            });
//        }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.patientImage:
                choosePicture();
                break;

            case R.id.searchButton:
                registerDoctor();
                break;

            case R.id.loginText:
                startActivity(new Intent(registerDoctor.this, DoctorLoginPage.class));
                break;
        }
    }

    private String uriToString(Uri uri) {
        if (uri != null) {
            return uri.toString();
        } else {
            return "https://firebasestorage.googleapis.com/v0/b/phax-f6447.appspot.com/o/images%2F1609306504650.png?alt=media&token=399bcd8a-98fd-4aaf-9508-a77e074ddb0a";
        }
    }

    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode== RESULT_OK && data != null && data.getData() != null){
            imageUri = data.getData();
            docProfilePic.setImageURI(imageUri);
            uploadPicture(imageUri);
        }
    }

    private void uploadPicture(Uri imageUri) {

        final ProgressDialog pd = new ProgressDialog(registerDoctor.this);
        pd.setTitle("Uploading Image...");
        pd.show();

        final StorageReference riversRef = storageReference.child("images/" + System.currentTimeMillis() + "." + getFileExtension(imageUri));

        riversRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        pd.dismiss();

                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isComplete());
                        uri = uriTask.getResult();

                        Snackbar.make(registerDoctor.this.findViewById(android.R.id.content), "Image Uploaded", Snackbar.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        pd.dismiss();
//                        Toast.makeText(registerDoctor.this, "Image Uploaded",Toast.LENGTH_LONG).show();
                        Toast.makeText( getApplicationContext(), "Failed to upload", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progressPercent = (100 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                        pd.setMessage("Progress: " + (int) progressPercent + "%");
                    }
                });
    }

    private void registerDoctor(){

        reference = FirebaseDatabase.getInstance().getReference("Doctor List");

        final String email = mEmail.getText().toString().trim();
        final String username = mUsername.getText().toString().trim();
        final String workArea = mWorkArea.getText().toString().trim();
        final String healthInstitution = mHealthInstitution.getText().toString().trim();
        final String location = mLocation.getText().toString().trim();
        final String password = mPassword.getText().toString().trim();
        final String image = uriToString(uri);

        if(email.isEmpty()){
            mEmail.setError("Email is Required");
            mEmail.requestFocus();
            return;
        }

        if(!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            mEmail.setError("Please provide valid Email");
            mEmail.requestFocus();
            return;
        }

        if(username.isEmpty()){
            mUsername.setError("Username is Required");
            mUsername.requestFocus();
            return;
        }

        if(workArea.isEmpty()){
            mWorkArea.setError("Work Area is Required");
            mWorkArea.requestFocus();
            return;
        }

        if(healthInstitution.isEmpty()){
            mHealthInstitution.setError("Health Institution is required");
            mHealthInstitution.requestFocus();
            return;
        }

        if(location.isEmpty()){
            mLocation.setError("Location is Required!");
            mLocation.requestFocus();
            return;
        }

        if(password.isEmpty()){
            mPassword.setError("Password is Required");
            mPassword.requestFocus();
            return;
        }

        if(password.length() < 6){
            mPassword.setError("Minimum length should be 6 characters!");
            mPassword.requestFocus();
            return;
        }

        progressBar.setVisibility(View.VISIBLE);
        mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if(task.isSuccessful()){
                    // send verification link
                    FirebaseUser user = mAuth.getCurrentUser();
                    user.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                DoctorHelperClass doctorHelperClass = new DoctorHelperClass(image, email, username, workArea, healthInstitution, location);
                                FirebaseDatabase.getInstance().getReference("Doctor List").child(FirebaseAuth.getInstance().getCurrentUser().getUid()).setValue(doctorHelperClass);
                                Toast.makeText(registerDoctor.this, "New " + workArea + " Successfully Registered, Please check you email for verification.", Toast.LENGTH_LONG).show();
                                mEmail.setText("");
                                mPassword.setText("");
                                startActivity(new Intent(registerDoctor.this, DoctorLoginPage.class));
                                finish();

                            }else {
                                Toast.makeText(registerDoctor.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
                else{
                    Toast.makeText(registerDoctor.this, "Failed to Register! Try Again!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    private String getFileExtension(Uri mUri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }

}