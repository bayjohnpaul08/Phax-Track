package com.example.phaxtrack.navigationItem;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phaxtrack.R;
import com.example.phaxtrack.index;
import com.example.phaxtrack.utils.InstitutionHelperClass;
import com.example.phaxtrack.utils.SearchBarHelperClass;
import com.example.phaxtrack.utils.VaccineHelperClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class inputVaccineFragment extends Fragment{

    List<InstitutionHelperClass> InstitutionList;
    List<VaccineHelperClass> VaccineList;
    AutoCompleteTextView vaccine;
    AutoCompleteTextView institution;
    FirebaseDatabase rootNode;
    private DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    FirebaseAuth mAuth;
    TextView date;
    private Button addVaccine;
    SearchBarHelperClass member;
    String vaccineGet = "";
    String institutionGet = "";

    public inputVaccineFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_input_vaccine, container, false);

        mAuth = FirebaseAuth.getInstance();
        vaccine = (AutoCompleteTextView) view.findViewById(R.id.inputVaccine);
        institution = (AutoCompleteTextView) view.findViewById(R.id.inputInstitution);
        date = (TextView) view.findViewById(R.id.date);
        addVaccine = (Button) view.findViewById(R.id.newVaccine);

        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        // Get the current date
        date.setText(currentDate);

        openVaccine();
        openInstitution();

        member = new SearchBarHelperClass();
        addVaccine.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(vaccineGet.equals(vaccine.getText().toString())){
                    Toast.makeText(getActivity(), "Please Complete The Selection!", Toast.LENGTH_SHORT).show();
                }else if(institutionGet.equals(institution.getText().toString())){
                    Toast.makeText(getActivity(), "Please Complete The Selection!", Toast.LENGTH_SHORT).show();
                }else{
                    newVaccine();
                }

            }
        });

        return view;
    }

    private void openVaccine() {
        VaccineList = new ArrayList<>();

        ref.child("Vaccine List").addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String VaccineID = ds.getKey();
                        String VaccineName = ds.child("vaccine_name").getValue().toString();
                        VaccineList.add(new VaccineHelperClass(VaccineID, VaccineName));
                    }

                    ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.option_item, VaccineList);
                    vaccine.setAdapter(adapter);
                    vaccine.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                                vaccineGet = adapterView.getItemAtPosition(position).toString();
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
    private void openInstitution() {
         InstitutionList = new ArrayList<>();

        ref.child("Institution List").addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.JELLY_BEAN_MR1)
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    for (DataSnapshot ds : snapshot.getChildren()) {
                        String InstitutionID = ds.getKey();
                        String InstitutionName = ds.child("institution_name").getValue().toString();
                        InstitutionList.add(new InstitutionHelperClass(InstitutionID, InstitutionName));
                    }

                    ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.option_item, InstitutionList);
                    institution.setAdapter(adapter);
                    institution.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                        @Override
                        public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                            institutionGet= adapterView.getItemAtPosition(position).toString();
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

    private void newVaccine() {
        Bundle bundle = getArguments();
        SearchBarHelperClass helperClass = bundle.getParcelable("data");

        rootNode = FirebaseDatabase.getInstance();
        ref = rootNode.getReference().child("Doctor List").child(mAuth.getCurrentUser().getUid()).child("Patient List").child(helperClass.getId());

            ref.child("date").setValue(date.getText().toString());
            member.setVaccine_Date(date.getText().toString());
            member.setVaccine_Name(vaccine.getText().toString());
            member.setInstitution_Name(institution.getText().toString());
            ref.child("Immunization").push().setValue(member);

            Toast.makeText(getActivity(),  "New Vaccine Is Successfully Added To " + helperClass.getId(), Toast.LENGTH_LONG).show();
            startActivity(new Intent(this.getActivity(), index.class));
    }
}