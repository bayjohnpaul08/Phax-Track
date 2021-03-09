package com.example.phaxtrack.navigationItem;

import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.phaxtrack.DoctorLoginPage;
import com.example.phaxtrack.R;
import com.example.phaxtrack.adapters.TableAdapter;
import com.example.phaxtrack.registerPatient;
import com.example.phaxtrack.utils.TableHelperClass;
import com.example.phaxtrack.utils.addPatientHelperClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

public class immunizationFragment extends Fragment {

    private TextView immune_name, immune_id, immune_gender;
    private CircleImageView immune_image;
    private addPatientHelperClass helperClass;
    private RecyclerView recyclerView;
    private TableAdapter tableAdapter;
    private ArrayList<TableHelperClass> tableList = new ArrayList<>();
    private DatabaseReference reference;
    private Button update;
    private EditText inputVaccine, inputDate, inputHcp, inputClinic;
    TextView back;

    public immunizationFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_immunization, container, false);


        immune_name = (TextView) view.findViewById(R.id.immuneName);
        immune_id = (TextView) view.findViewById(R.id.immuneId);
        immune_gender = (TextView) view.findViewById(R.id.immuneGender);
        immune_image = (CircleImageView) view.findViewById(R.id.immuneImage);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        update = (Button) view.findViewById(R.id.button3);
        back = (TextView) view.findViewById(R.id.back);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        Bundle bundle = getArguments();
        helperClass = bundle.getParcelable("data");

        immune_name.setText(helperClass.getFirstName().toUpperCase() + " " + helperClass.getSurname().toUpperCase() + "'S IMMUNIZATION RECORD");
        immune_id.setText("Patient " + helperClass.getId());
        immune_gender.setText(helperClass.getGender() + " • " + helperClass.getAge());
        Glide.with(immune_image.getRootView()).load(helperClass.getImage()).placeholder(R.drawable.user_circle).into(immune_image);

        reference = FirebaseDatabase.getInstance().getReference("Vaccine History").child(helperClass.getId());
        setRecyclerView();


        update.setOnClickListener(new View.OnClickListener() {
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

        return view;
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

    private void setRecyclerView() {

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tableList.clear();
                if(snapshot.exists()){
                    for(DataSnapshot ds: snapshot.getChildren()){
                        snapshot.getKey();
                        TableHelperClass tableHelperClass = ds.getValue(TableHelperClass.class);
                        tableList.add(tableHelperClass);

                        tableAdapter = new TableAdapter(getContext(), tableList);
                        recyclerView.setAdapter(tableAdapter);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }

    private void openDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_vaccine_update, null);
        builder.setView(view);

        inputVaccine = (EditText) view.findViewById(R.id.input_Vaccine);
        inputDate = (EditText) view.findViewById(R.id.input_Date);
        inputHcp = (EditText) view.findViewById(R.id.input_hcp);
        inputClinic = (EditText) view.findViewById(R.id.input_Clinic);

        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());

        inputDate.setText(currentDate);


        final AlertDialog alertDialog = builder.create();
        view.findViewById(R.id.button_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveVaccine(alertDialog);
            }
        });

        if(alertDialog.getWindow() != null){
            alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        }
        alertDialog.show();
    }

    private void saveVaccine(final AlertDialog alertDialog) {
        String vaccine = inputVaccine.getText().toString().trim();
        String date = inputDate.getText().toString().trim();
        String hcp = inputHcp.getText().toString().trim();
        String clinic = inputClinic.getText().toString().trim();

        if(vaccine.isEmpty()){
            inputVaccine.setError("Vaccine is Required");
            inputVaccine.requestFocus();
            return;
        }

        if(hcp.isEmpty()){
            inputHcp.setError("HCP is Required");
            inputHcp.requestFocus();
            return;
        }

        if(clinic.isEmpty()){
            inputClinic.setError("Clinic is Required");
            inputClinic.requestFocus();
            return;
        }

        TableHelperClass table = new TableHelperClass(vaccine, date, hcp, clinic);
        reference.push().setValue(table).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getActivity(), "Vaccine Successfully Update", Toast.LENGTH_LONG).show();
                }
                else {
                    Toast.makeText(getActivity(), "Failed! Please Try again", Toast.LENGTH_LONG).show();
                }
                alertDialog.dismiss();

            }
        });
    }
}