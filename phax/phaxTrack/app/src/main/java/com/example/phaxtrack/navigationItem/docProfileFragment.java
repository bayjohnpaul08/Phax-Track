package com.example.phaxtrack.navigationItem;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.phaxtrack.PatientInfo;
import com.example.phaxtrack.editDoctor;
import com.example.phaxtrack.patient_vaccine_button;
import com.example.phaxtrack.utils.DoctorHelperClass;
import com.example.phaxtrack.R;
import com.example.phaxtrack.utils.addPatientHelperClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.parceler.Parcels;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class docProfileFragment extends Fragment {

    private TextView getWorkArea, getHealth, getEmail, getUname, getLocation;
    private CircleImageView getDocImage;
    private DatabaseReference ref;
    private FirebaseAuth mAuth;
    private Button button;
    DoctorHelperClass docInfo;

    public docProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
       View view = inflater.inflate(R.layout.fragment_doc_profile, container, false);

       getWorkArea = (TextView) view.findViewById(R.id.getWorkArea);
       getHealth = (TextView) view.findViewById(R.id.getHealth);
       getEmail = (TextView) view.findViewById(R.id.getEmail);
       getUname = (TextView) view.findViewById(R.id.getUserName);
       getLocation = (TextView) view.findViewById(R.id.getLocation);
       getDocImage = (CircleImageView) view.findViewById(R.id.docImage);
       button = (Button) view.findViewById(R.id.button);

       button.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {
                openRegister(docInfo);
           }
       });

        mAuth = FirebaseAuth.getInstance();
        ref = FirebaseDatabase.getInstance().getReference().child("Doctor List").child(mAuth.getCurrentUser().getUid());

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String workAre = snapshot.child("workArea").getValue().toString();
                String health = snapshot.child("healthInstitution").getValue().toString();
                String uname = snapshot.child("username").getValue().toString();
                String image = snapshot.child("image").getValue().toString();
                String loc = snapshot.child("location").getValue().toString();
                String email = snapshot.child("email").getValue().toString();

                getWorkArea.setText(workAre);
                getHealth.setText(health);
                getEmail.setText(email);
                getUname.setText(uname);
                getLocation.setText(loc);
                Glide.with(getDocImage.getRootView()).load(image).into(getDocImage);

                docInfo = new DoctorHelperClass(image, email, uname, workAre, health, loc);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


        return view;
    }

    private void openRegister(DoctorHelperClass docInfo) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("info", docInfo);

        FragmentManager fragmentManager = (getActivity()).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        editDoctor fragment = new editDoctor();
        fragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.container_fragment, fragment).addToBackStack(null);
        fragmentTransaction.commit();
    }
}