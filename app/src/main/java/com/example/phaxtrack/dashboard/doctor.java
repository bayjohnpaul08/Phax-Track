package com.example.phaxtrack.dashboard;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.phaxtrack.DoctorAdapter;
import com.example.phaxtrack.R;
import com.example.phaxtrack.utils.DoctorHelperClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class doctor extends AppCompatActivity {

    private RecyclerView recyclerView;
    private Button button;
    private ArrayList<DoctorHelperClass> list;
    private DatabaseReference root;
    private DoctorAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doctor);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        button = (Button) findViewById(R.id.add);

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext()));

        getAllDoctor();

        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add();
            }
        });
    }

    private void getAllDoctor() {
        root = FirebaseDatabase.getInstance().getReference().child("Doctor List");

        root.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    list = new ArrayList<>();
                    for(DataSnapshot ds: snapshot.getChildren()){
                        DoctorHelperClass doctorHelperClass = ds.getValue(DoctorHelperClass.class);
                        list.add(doctorHelperClass);

                        adapter = new DoctorAdapter(list);
                        recyclerView.setAdapter(adapter);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void add() {
        startActivity(new Intent(doctor.this, registerDoctor.class));
    }
}