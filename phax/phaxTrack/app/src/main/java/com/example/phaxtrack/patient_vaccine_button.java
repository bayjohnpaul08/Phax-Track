package com.example.phaxtrack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.phaxtrack.adapters.TableAdapter;
import com.example.phaxtrack.utils.TableHelperClass;
import com.example.phaxtrack.utils.addPatientHelperClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.parceler.Parcels;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class patient_vaccine_button extends AppCompatActivity {

    private CircleImageView image;
    private TextView getName, button_home;
    addPatientHelperClass helperClass;
    DatabaseReference reference;
    private RecyclerView recyclerView;
    private TableAdapter tableAdapter;
    private ArrayList<TableHelperClass> tableList = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_vaccine_button);

        image = (CircleImageView) findViewById(R.id.image);
        getName = (TextView) findViewById(R.id.vaccine_getName);
        button_home = (TextView) findViewById(R.id.homeButton);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(patient_vaccine_button.this));

        helperClass = (addPatientHelperClass) Parcels.unwrap(getIntent().getParcelableExtra("helperClass"));

        reference = FirebaseDatabase.getInstance().getReference("Patient List");

        Glide.with(image.getRootView()).load(helperClass.getImage()).placeholder(R.drawable.group_1).into(image);
        getName.setText("Hello, " + helperClass.getFirstName() + "!");

        button_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openInfo(helperClass);
            }
        });

        setRecyclerView(helperClass);
    }

    private void openInfo(addPatientHelperClass helperClass) {
        Intent intent = new Intent( patient_vaccine_button.this , PatientInfo.class);
        Bundle bundle = new Bundle();
        bundle.putParcelable("helperClass", Parcels.wrap(helperClass));
        intent.putExtras(bundle);
        startActivity(intent);
    }

    private void setRecyclerView(addPatientHelperClass helperClass) {



        DatabaseReference ref = FirebaseDatabase.getInstance().getReference("Vaccine History").child(helperClass.getId());

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                tableList.clear();
                if(snapshot.exists()){
                    for(DataSnapshot ds: snapshot.getChildren()){
                        snapshot.getKey();
                        TableHelperClass tableHelperClass = ds.getValue(TableHelperClass.class);
                        tableList.add(tableHelperClass);

                        tableAdapter = new TableAdapter(patient_vaccine_button.this, tableList);
                        recyclerView.setAdapter(tableAdapter);
                    }

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });


    }
}