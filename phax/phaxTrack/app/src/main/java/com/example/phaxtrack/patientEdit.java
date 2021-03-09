package com.example.phaxtrack;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.FragmentManager;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.phaxtrack.utils.addPatientHelperClass;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.parceler.Parcels;

import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class patientEdit extends AppCompatActivity implements View.OnClickListener {

    private EditText updateFname, updateMname, updateLname, updateAge, updateBirthday, updateEmail, updateAddress, updateNum, updateGender;
    private String fName, mName, lName, age, birthday, email, address, num, gender, image;
    private CircleImageView updateImage;
    private Button updateButton;
    addPatientHelperClass helperClass;
    ProgressBar progressBar;
    private FirebaseAuth mAuth;
    private DatabaseReference reference;
    private FirebaseStorage storage;
    private StorageReference storageReference;
    private FirebaseDatabase rootNode;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    public Uri imageUri;
    Uri uri;
    String getGender = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_edit);

        reference = FirebaseDatabase.getInstance().getReference("Patient List");
        helperClass = (addPatientHelperClass) Parcels.unwrap(getIntent().getParcelableExtra("helperClass"));
        storageReference = FirebaseStorage.getInstance().getReference();

        updateFname = (EditText) findViewById(R.id.update_first);
        updateMname = (EditText) findViewById(R.id.update_middle);
        updateLname = (EditText) findViewById(R.id.update_last);
        updateAge = (EditText) findViewById(R.id.update_age);
        updateBirthday = (EditText) findViewById(R.id.update_birthday);
        updateNum = (EditText) findViewById(R.id.update_contact_number);
        updateGender = (EditText) findViewById(R.id.update_gender);
        updateAddress = (EditText) findViewById(R.id.update_address);
        updateEmail = (EditText) findViewById(R.id.update_email);
        updateImage = (CircleImageView) findViewById(R.id.update_image);
        updateButton = (Button) findViewById(R.id.saveButton);
        updateButton.setOnClickListener(this);
        updateImage.setOnClickListener(this);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        updateBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        patientEdit.this,
                        android.R.style.Theme_DeviceDefault_Light_Dialog_NoActionBar,
                        mDateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
                dialog.show();
            }
        });

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                month = month + 1;

                String date = month + "/" + dayOfMonth + "/" + year;
                updateBirthday.setText(date);
            }
        };

        allData(helperClass);
    }

    
    private void allData(addPatientHelperClass helperClass) {

        fName = helperClass.getFirstName();
        mName = helperClass.getMiddleName();
        lName = helperClass.getSurname();
        age = helperClass.getAge();
        birthday = helperClass.getBirthday();
        email = helperClass.getEmail();
        address = helperClass.getAddress();
        num = helperClass.getNumber();
        gender = helperClass.getGender();
        image = helperClass.getImage();

        updateFname.setText(fName);
        updateMname.setText(mName);
        updateLname.setText(lName);
        updateEmail.setText(email);
        updateAge.setText(age);
        updateBirthday.setText(birthday);
        updateAddress.setText(address);
        updateGender.setText(gender);
        updateNum.setText(num);

        Glide.with(updateImage.getRootView()).load(helperClass.getImage()).placeholder(R.drawable.user_circle).into(updateImage);

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.saveButton:
                updateInfo();
                break;
                
            case R.id.update_image:
                choosePicture();
                break;
        }
    }

    private void updateInfo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(patientEdit.this, R.style.AlertDialogTheme);
        View view = LayoutInflater.from(patientEdit.this).inflate(
                R.layout.dialog_save_changes,
                (ConstraintLayout) findViewById(R.id.dialog100)
        );

        builder.setView(view);

        final AlertDialog alertDialog = builder.create();
        view.findViewById(R.id.button5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!fName.equals(updateFname.getText().toString().trim())){
                    reference.child(helperClass.getId()).child("firstName").setValue(updateFname.getText().toString().trim());
                }

                if(!mName.equals(updateMname.getText().toString().trim())) {
                    reference.child(helperClass.getId()).child("middleName").setValue(updateMname.getText().toString().trim());
                }

                if(!lName.equals(updateLname.getText().toString().trim())){
                    reference.child(helperClass.getId()).child("surname").setValue(updateLname.getText().toString().trim());
                }

                if(!age.equals(updateAge.getText().toString().trim())){
                    reference.child(helperClass.getId()).child("age").setValue(updateAge.getText().toString().trim());
                }

                if(!birthday.equals(updateBirthday.getText().toString().trim())){
                    reference.child(helperClass.getId()).child("birthday").setValue(updateBirthday.getText().toString().trim());
                }

                if(!address.equals(updateAddress.getText().toString().trim())){
                    reference.child(helperClass.getId()).child("address").setValue(updateAddress.getText().toString().trim());
                }

                if(!email.equals(updateEmail.getText().toString().trim())){
                    reference.child(helperClass.getId()).child("email").setValue(updateEmail.getText().toString().trim());
                }

                if(!gender.equals(updateGender.getText().toString().trim())){
                    reference.child(helperClass.getId()).child("gender").setValue(updateGender.getText().toString().trim());
                }

                if(!num.equals(updateNum.getText().toString().trim())){
                    reference.child(helperClass.getId()).child("number").setValue(updateNum.getText().toString().trim());
                }

                if(uri != null){
                    reference.child(helperClass.getId()).child("image").setValue(uri.toString());
                }else{
                    reference.child(helperClass.getId()).child("image").setValue(helperClass.getImage());
                }

                Toast.makeText(getApplicationContext(),"Profile Is Successfully Updated", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent( patientEdit.this , patient_information_button.class);
                Bundle bundle = new Bundle();
                bundle.putParcelable("helperClass", Parcels.wrap(helperClass));
                intent.putExtras(bundle);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);


                alertDialog.dismiss();
            }
        });

        view.findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
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
            updateImage.setImageURI(imageUri);
            uploadPicture(imageUri);
        }
    }

    private void uploadPicture(final Uri imageUri) {

        final ProgressDialog pd = new ProgressDialog(this);
        pd.setTitle("Uploading Image...");
        pd.show();

        StorageReference riversRef = storageReference.child("images/" + System.currentTimeMillis() + "." + getFileExtension(imageUri));

        riversRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        pd.dismiss();
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isComplete());
                        uri = uriTask.getResult();


                        Toast.makeText(patientEdit.this, "Image Uploaded", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        Toast.makeText(patientEdit.this, "Failed to upload", Toast.LENGTH_LONG).show();
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

    private String getFileExtension(Uri mUri){
        ContentResolver cr = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }
}