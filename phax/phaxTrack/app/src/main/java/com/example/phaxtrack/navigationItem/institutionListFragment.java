package com.example.phaxtrack.navigationItem;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.phaxtrack.R;
import com.example.phaxtrack.utils.InstitutionHelperClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class institutionListFragment extends Fragment implements View.OnClickListener {
    private TextView hospital, clinic, healthCenter;

    public institutionListFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_institution_list, container, false);

        hospital = (TextView) view.findViewById(R.id.hospital);
        clinic = (TextView) view.findViewById(R.id.Clinic);
        healthCenter = (TextView) view.findViewById(R.id.HealthCenter);
        hospital.setOnClickListener(this);
        clinic.setOnClickListener(this);
        healthCenter.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.hospital:
                openHospital();
                break;

            case R.id.Clinic:
                openClinic();
                break;

            case R.id.HealthCenter:
                openHealth();
                break;
        }


    }

    private void openHealth() {
        FragmentManager fragmentManager = (getActivity()).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        healthCenterFragment fragment = new healthCenterFragment();

        fragmentTransaction.replace(R.id.container_fragment, fragment).addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void openClinic() {
        FragmentManager fragmentManager = (getActivity()).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        clinicFragment fragment = new clinicFragment();

        fragmentTransaction.replace(R.id.container_fragment, fragment).addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void openHospital() {
        FragmentManager fragmentManager = (getActivity()).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        hospitalFragment fragment = new hospitalFragment();

        fragmentTransaction.replace(R.id.container_fragment, fragment).addToBackStack(null);
        fragmentTransaction.commit();
    }
}