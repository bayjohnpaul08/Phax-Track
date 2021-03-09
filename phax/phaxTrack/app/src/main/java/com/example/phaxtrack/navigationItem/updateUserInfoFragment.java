package com.example.phaxtrack.navigationItem;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.phaxtrack.R;
import com.example.phaxtrack.index;
import com.example.phaxtrack.utils.addPatientHelperClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class updateUserInfoFragment extends Fragment implements View.OnClickListener {

    private EditText updateFname, updateMname, updateLname, updateAge, updateBirthday, updateEmail, updateAddress, updateNum, updateGender;
    private String fName, mName, lName, age, birthday, email, address, num, gender;
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
    Uri imageUri;
    String getGender = "";

    public updateUserInfoFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_update_user_info, container, false);

//        mAuth = FirebaseAuth.getInstance();
//        storage = FirebaseStorage.getInstance();
//        storageReference = storage.getReference();
//        rootNode = FirebaseDatabase.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("Patient List");

        updateFname = (EditText) view.findViewById(R.id.update_first);
        updateMname = (EditText) view.findViewById(R.id.update_middle);
        updateLname = (EditText) view.findViewById(R.id.update_last);
        updateAge = (EditText) view.findViewById(R.id.update_age);
        updateBirthday = (EditText) view.findViewById(R.id.update_birthday);
        updateNum = (EditText) view.findViewById(R.id.update_contact_number);
        updateGender = (EditText) view.findViewById(R.id.update_gender);
        updateAddress = (EditText) view.findViewById(R.id.update_address);
        updateEmail = (EditText) view.findViewById(R.id.update_email);
        updateImage = (CircleImageView) view.findViewById(R.id.update_image);
        updateButton = (Button) view.findViewById(R.id.saveButton);
        updateButton.setOnClickListener(this);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        updateBirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar cal = Calendar.getInstance();
                int year = cal.get(Calendar.YEAR);
                int month = cal.get(Calendar.MONTH);
                int day = cal.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        getActivity(),
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
        
        allData();

        return view;
    }

    private void allData() {
        Bundle bundle = getArguments();
        helperClass = bundle.getParcelable("data");

        fName = helperClass.getFirstName();
        mName = helperClass.getMiddleName();
        lName = helperClass.getSurname();
        age = helperClass.getAge();
        birthday = helperClass.getBirthday();
        email = helperClass.getEmail();
        address = helperClass.getAddress();
        num = helperClass.getNumber();
        gender = helperClass.getGender();

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
        }
    }

    private void updateInfo() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_save_changes, null);
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

//        if(!image.equals(updateProfilePic)) {
//            reference.child(helperClass.getId()).child("image").setValue(updateProfilePic);
//        }else {
//            imageUri.toString();
//        }

                Toast.makeText(getActivity(),"Profile Is Successfully Updated", Toast.LENGTH_SHORT).show();

                FragmentManager fm = getActivity().getSupportFragmentManager();
                fm.popBackStack();

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
}

//        updateProfilePic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                choosePicture();
//            }
//        });
//
//        updateButton.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                if(!fName.equals(updateFname.getText().toString().trim())){
//                    reference.child(helperClass.getId()).child("fName").setValue(updateFname.getText().toString().trim());
//                }
//
//                if(!mName.equals(updateMname.getText().toString().trim())) {
//                    reference.child(helperClass.getId()).child("mName").setValue(updateMname.getText().toString().trim());
//                }
//
//                if(!lName.equals(updateLname.getText().toString().trim())){
//                    reference.child(helperClass.getId()).child("lName").setValue(updateLname.getText().toString().trim());
//                }
//
//                if(!age.equals(updateAge.getText().toString().trim())){
//                    reference.child(helperClass.getId()).child("age").setValue(updateAge.getText().toString().trim());
//                }
//
//                if(!birthday.equals(updateBirthday.getText().toString().trim())){
//                    reference.child(helperClass.getId()).child("birthday").setValue(updateBirthday.getText().toString().trim());
//                }
//
//                if(!address.equals(updateAddress.getText().toString().trim())){
//                    reference.child(helperClass.getId()).child("address").setValue(updateAddress.getText().toString().trim());
//                }
//
//                if(!email.equals(updateEmail.getText().toString().trim())){
//                    reference.child(helperClass.getId()).child("email").setValue(updateEmail.getText().toString().trim());
//                }

//                if(!image.equals(updateProfilePic)) {
//                    reference.child(helperClass.getId()).child("image").setValue(updateProfilePic);
//                }else {
//                    imageUri.toString();
//                }

//                Toast.makeText(getActivity(), helperClass.getId() + " Profile Is Successfully Updated", Toast.LENGTH_SHORT).show();
//                startActivity(new Intent(getActivity(), index.class));
//            }
//        });
//

//
//        return view;
//    }
//
//    private void showAllUserData() {

        //Picasso.get().load(image).resize(2000, 2000).placeholder(R.drawable.image).into(updateProfilePic);

//    }

//    private String uriToString(Uri imageUri) {
//        if(imageUri !=  null){
//            return updateProfilePic.toString();
//        }else{
//            return helperClass.getImage();
//        }
//    }

//    private void choosePicture() {
//        Intent intent = new Intent();
//        intent.setType("image/*");
//        intent.setAction(Intent.ACTION_GET_CONTENT);
//        startActivityForResult(intent, 1);
//    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode == 1 && resultCode== RESULT_OK && data != null && data.getData() != null){
//            imageUri = data.getData();
//            updateProfilePic.setImageURI(imageUri);
//            uploadPicture();
//        }
//    }

//    private void uploadPicture() {
//
//        final ProgressDialog pd = new ProgressDialog(this.getActivity());
//        pd.setTitle("Uploading Image...");
//        pd.show();
//
//        final String randomKey = UUID.randomUUID().toString();
//        StorageReference riversRef = storageReference.child("images/" + randomKey);
//
//        riversRef.putFile(imageUri)
//                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//                        pd.dismiss();
//                        Snackbar.make(getActivity().findViewById(android.R.id.content), "Image Uploaded", Snackbar.LENGTH_LONG).show();
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception exception) {
//                        pd.dismiss();
//                        Toast.makeText(getActivity().getApplicationContext(), "Failed to upload", Toast.LENGTH_LONG).show();
//                    }
//                })
//                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
//                    @Override
//                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
//                        double progressPercent = (100 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
//                        pd.setMessage("Progress: " + (int) progressPercent + "%");
//                    }
//                });
//    }
//}