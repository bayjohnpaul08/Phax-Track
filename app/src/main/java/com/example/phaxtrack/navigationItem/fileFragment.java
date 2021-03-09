package com.example.phaxtrack.navigationItem;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.phaxtrack.R;
import com.example.phaxtrack.utils.FileHelperClass;
import com.example.phaxtrack.utils.addPatientHelperClass;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
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

import de.hdodenhof.circleimageview.CircleImageView;

import static android.app.Activity.RESULT_OK;

public class fileFragment extends Fragment {

    private TextView file_name, file_id, file_gender;
    private CircleImageView file_image;
    private addPatientHelperClass helperClass;
    private ImageButton fileButton;
    private RecyclerView recyclerView;
    StorageReference storageReference;
    DatabaseReference databaseReference;
    TextView inputFile;
    private ListView listView;
    private List<FileHelperClass> list;
    TextView back;

    public fileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_file, container, false);

        file_name = (TextView) view.findViewById(R.id.file_Name);
        file_id = (TextView) view.findViewById(R.id.file_Id);
        file_gender = (TextView) view.findViewById(R.id.file_Gender);
        file_image = (CircleImageView) view.findViewById(R.id.file_Image);
        fileButton = (ImageButton) view.findViewById(R.id.fileButton);
        listView = (ListView) view.findViewById(R.id.list_view);
        list = new ArrayList<>();
        back = (TextView) view.findViewById(R.id.back);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                FileHelperClass file = list.get(position);

                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setType("application/pdf");
                intent.setData(Uri.parse(file.getUrl()));
                startActivity(intent);
            }
        });

        Bundle bundle = getArguments();
        helperClass = bundle.getParcelable("data");

        storageReference = FirebaseStorage.getInstance().getReference();
        databaseReference = FirebaseDatabase.getInstance().getReference("Files and Documents").child(helperClass.getId());

        file_name.setText(helperClass.getFirstName().toUpperCase() + " " + helperClass.getSurname().toUpperCase() + "'S DOCUMENTS");
        file_id.setText("Patient " + helperClass.getId());
        file_gender.setText(helperClass.getGender() + " • " + helperClass.getAge());
        Glide.with(file_image.getRootView()).load(helperClass.getImage()).placeholder(R.drawable.user_circle).into(file_image);

        fileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backBtn();
            }
        });

        retrievePDFFILE();
        return view;
    }

    private void retrievePDFFILE() {
        databaseReference = FirebaseDatabase.getInstance().getReference("Files and Documents").child(helperClass.getId());
        databaseReference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                list.clear();
                for(DataSnapshot ds: snapshot.getChildren()){
                    FileHelperClass file = ds.getValue(FileHelperClass.class);
                    list.add(file);
                }
                String[] uploadsName = new String[list.size()];

                for(int i = 0; i < uploadsName.length; i++){
                    uploadsName[i] = list.get(i).getName();
                }

                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getActivity(), android.R.layout.simple_list_item_1, uploadsName){
                    @NonNull
                    @Override
                    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                        View view = super.getView(position, convertView, parent);
                        TextView textView = (TextView) view.findViewById(android.R.id.text1);

                        textView.setTextColor(Color.WHITE) ;
                        textView.setTextSize(20);
                        return view;
                    }
                };

                listView.setAdapter(adapter);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void selectPDF() {
        Intent intent = new Intent();
        intent.setType("application/pdf");
        intent.setAction(intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "PDF FILE SELECT"), 12);
    }

    private void openDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_upload_file, null);
        builder.setView(view);

        inputFile = (TextView) view.findViewById(R.id.button2);

        final AlertDialog alertDialog = builder.create();

        inputFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectPDF();
                alertDialog.dismiss();
            }
        });


        if(alertDialog.getWindow() != null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable final Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode == 12 && resultCode== RESULT_OK && data != null && data.getData() != null){
            inputFile.setText(data.getDataString().substring(data.getDataString().lastIndexOf("/") + 1));
                    uploadFileToFirebase(data.getData());
        }
    }

    private void uploadFileToFirebase(Uri data) {
        final ProgressDialog progressDialog = new ProgressDialog(getActivity());
        progressDialog.setTitle("File is loading...");
        progressDialog.show();

        StorageReference reference = storageReference.child("upload" + System.currentTimeMillis() + ".pdf");

        reference.putFile(data)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isComplete());
                        Uri uri = uriTask.getResult();

                        FileHelperClass helperClass = new FileHelperClass(inputFile.getText().toString(), uri.toString());
                        databaseReference.child(databaseReference.push().getKey()).setValue(helperClass);
                        Toast.makeText(getActivity(), "File Upload", Toast.LENGTH_LONG).show();
                        progressDialog.dismiss();

                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_LONG).show();
            }
        }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {

                double progress = (100.0* snapshot.getBytesTransferred()/snapshot.getTotalByteCount());
                progressDialog.setMessage("File Uploaded..." + (int) progress+ "%");
            }
        });
    }

    private void backBtn() {
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", helperClass);

        FragmentManager fragmentManager = (getActivity()).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        userInfoFragment fragment = new userInfoFragment();
        fragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.container_fragment, fragment).addToBackStack(null);
        fragmentTransaction.commit();
    }
}