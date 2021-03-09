package com.example.phaxtrack.navigationItem;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.phaxtrack.R;
import com.example.phaxtrack.adapters.AdverseAdapter;
import com.example.phaxtrack.utils.AdverseAnsHelperClass;
import com.example.phaxtrack.utils.addPatientHelperClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class adverseFragment extends Fragment {

    private TextView adverseName, adverseId, adverseGender;
    private CircleImageView adverseImage;
    private addPatientHelperClass helperClass;
    private DatabaseReference reference;
    TextView back;
    RecyclerView recyclerView;
    AdverseAdapter adapter;
    ArrayList<AdverseAnsHelperClass> list = new ArrayList<>();


    public adverseFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_adverse, container, false);

        adverseName = (TextView) view.findViewById(R.id.adverse_Name);
        adverseId = (TextView) view.findViewById(R.id.adverse_Id);
        adverseGender = (TextView) view.findViewById(R.id.adverse_Gender);
        adverseImage = (CircleImageView) view.findViewById(R.id.adverse_Image);
        back = (TextView) view.findViewById(R.id.back);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerView);
        // for Layout
        RecyclerView.LayoutManager recycle = new
                LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false);
        recyclerView.setLayoutManager(recycle);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                backBtn();
            }
        });

        Bundle bundle = getArguments();
        helperClass = bundle.getParcelable("data");

        adverseName.setText("ADVERSE EVENT EXPERIENCED BY " + helperClass.getFirstName().toUpperCase() + " " + helperClass.getSurname().toUpperCase());
        adverseId.setText("Patient " + helperClass.getId());
        adverseGender.setText(helperClass.getGender() + " • " + helperClass.getAge());
        Glide.with(adverseImage.getRootView()).load(helperClass.getImage()).placeholder(R.drawable.user_circle).into(adverseImage);

        reference = FirebaseDatabase.getInstance().getReference("Adverse Answer").child(helperClass.getId());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    list.clear();
                    for(DataSnapshot ds: snapshot.getChildren()){
                        snapshot.getKey();
                        AdverseAnsHelperClass helperClass = ds.getValue(AdverseAnsHelperClass.class);
                        list.add(helperClass);
                    }
                    adapter = new AdverseAdapter(list, getContext());
                    recyclerView.setAdapter(adapter);
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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
}