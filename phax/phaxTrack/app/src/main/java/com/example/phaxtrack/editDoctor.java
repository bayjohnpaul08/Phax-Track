package com.example.phaxtrack;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.phaxtrack.navigationItem.docProfileFragment;
import com.example.phaxtrack.navigationItem.userInfoFragment;
import com.example.phaxtrack.utils.DoctorHelperClass;
import com.example.phaxtrack.utils.addPatientHelperClass;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import org.parceler.Parcels;

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class editDoctor extends Fragment {
    private CircleImageView editImage;
    private EditText editUser;
    private EditText editEmail;
    private EditText editLoc;
    private EditText editWork;
    private EditText editHealth;
    private String image;
    private Button save;
    private ProgressBar progressBar;
    private DatabaseReference reference;
    private DoctorHelperClass docInfo;
    private FirebaseAuth mAuth;
    String user, email, loc, work, health;
    public Uri imageUri;
    Uri uri;
    private StorageReference storageReference;


    public editDoctor() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_edit_doctor, container, false);

        editImage = (CircleImageView) view.findViewById(R.id.update_docImage);
        editUser = (EditText) view.findViewById(R.id.doc_username);
        editEmail = (EditText) view.findViewById(R.id.doc_email);
        editLoc = (EditText) view.findViewById(R.id.doc_loc);
        editWork = (EditText) view.findViewById(R.id.doc_workArea);
        editHealth = (EditText) view.findViewById(R.id.doc_health);
        save = (Button) view.findViewById(R.id.saveDocButton);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        mAuth = FirebaseAuth.getInstance();
        reference = FirebaseDatabase.getInstance().getReference("Doctor List").child(mAuth.getCurrentUser().getUid());
        storageReference = FirebaseStorage.getInstance().getReference();

        Bundle bundle = getArguments();
        docInfo = bundle.getParcelable("info");


        user= docInfo.getUsername();
        email = docInfo.getEmail();
        loc = docInfo.getLocation();
        work = docInfo.getWorkArea();
        health = docInfo.getHealthInstitution();
//        image = uriToString(uri);


        editUser.setText(user);
        editEmail.setText(email);
        editLoc.setText(loc);
        editWork.setText(work);
        editHealth.setText(health);
        Glide.with(editImage.getRootView()).load(docInfo.getImage()).into(editImage);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveEdit();
            }
        });

        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePicture();
            }
        });

        return view;
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
            editImage.setImageURI(imageUri);
            uploadPicture(imageUri);
        }
    }

    private void uploadPicture(final Uri imageUri) {

        final ProgressDialog pd = new ProgressDialog(getActivity());
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


                        Toast.makeText(getActivity(), "Image Uploaded", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        pd.dismiss();
//                        Toast.makeText(registerPatient.this, "Image Uploaded", Toast.LENGTH_LONG).show();
                        Toast.makeText(getActivity(), "Failed to upload", Toast.LENGTH_LONG).show();
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

    private void saveEdit() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_save_changes, null);
        builder.setView(view);

        final AlertDialog alertDialog = builder.create();
        view.findViewById(R.id.button5).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!user.equals(editUser.getText().toString().trim())){
                    reference.child("username").setValue(editUser.getText().toString().trim());
                }

                if(!email.equals(editEmail.getText().toString().trim())){
                    reference.child("email").setValue(editEmail.getText().toString().trim());
                }

                if(!loc.equals(editLoc.getText().toString().trim())){
                    reference.child("location").setValue(editLoc.getText().toString().trim());
                }

                if(!work.equals(editWork.getText().toString().trim())){
                    reference.child("workArea").setValue(editWork.getText().toString().trim());
                }

                if(!health.equals(editHealth.getText().toString().trim())){
                    reference.child("healthInstitution").setValue(editHealth.getText().toString().trim());
                }

                if(uri != null) {
                    reference.child("image").setValue(uri.toString());
                }else{
                    reference.child("image").setValue(docInfo.getImage());
                }

                Bundle bundle = new Bundle();
                bundle.putParcelable("data", docInfo);

                FragmentManager fragmentManager = (getActivity()).getSupportFragmentManager();
                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                docProfileFragment fragment = new docProfileFragment();
                fragment.setArguments(bundle);

                fragmentTransaction.replace(R.id.container_fragment, fragment).addToBackStack(null);
                fragmentTransaction.commit();

                Toast.makeText(getActivity(), "Your profile is successfully updated.", Toast.LENGTH_SHORT).show();

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

    private String getFileExtension(Uri mUri){
        ContentResolver cr = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }
}