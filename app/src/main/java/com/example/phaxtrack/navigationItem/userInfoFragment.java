package com.example.phaxtrack.navigationItem;

import android.annotation.SuppressLint;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.phaxtrack.R;
import com.example.phaxtrack.adapters.SearchVaccineAdapter;
import com.example.phaxtrack.utils.SearchBarHelperClass;
import com.example.phaxtrack.utils.addPatientHelperClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * A simple {@link Fragment} subclass.
 */
public class userInfoFragment extends Fragment implements View.OnClickListener {

    private TextView getName, getId, getGender, getFullName, getBirthday, getNum, getAddress;
    private CircleImageView getImage;
    private TextView immune, file, comments, adverse;
    DatabaseReference ref = FirebaseDatabase.getInstance().getReference();
    FirebaseAuth mAuth;
    ArrayList<SearchBarHelperClass> myArrayList = new ArrayList<>();
    private RecyclerView recyclerView;
    SearchVaccineAdapter adapter;
    addPatientHelperClass helperClass;

    public userInfoFragment() {
        // Required empty public constructor
    }

    @SuppressLint("SetTextI18n")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_user_info, container, false);

        getImage = (CircleImageView) view.findViewById(R.id.update_image);
        getName = (TextView) view.findViewById(R.id.getName);
        getId = (TextView) view.findViewById(R.id.getId);
        getGender = (TextView) view.findViewById(R.id.getGender);
        getFullName = (TextView) view.findViewById(R.id.getFullName);
        getBirthday = (TextView) view.findViewById(R.id.getBirthday);
        getNum = (TextView) view.findViewById(R.id.getNum);
        getAddress = (TextView) view.findViewById(R.id.getAddress);
        immune = (TextView) view.findViewById(R.id.immune);
        immune.setOnClickListener(this);
        file = (TextView) view.findViewById(R.id.files);
        file.setOnClickListener(this);
        comments = (TextView) view.findViewById(R.id.comments);
        comments.setOnClickListener(this);
        adverse = (TextView) view.findViewById(R.id.adverse);
        adverse.setOnClickListener(this);

        Bundle bundle = getArguments();
        helperClass = bundle.getParcelable("data");

        getName.setText(helperClass.getFirstName().toUpperCase() + " " + helperClass.getSurname().toUpperCase() + "'S WALL");
        getId.setText("Patient " + helperClass.getId());
        getGender.setText(helperClass.getGender() + " • " + helperClass.getAge());
        getFullName.setText(helperClass.getFirstName() + " " + helperClass.getMiddleName() + " " + helperClass.getSurname());
        getBirthday.setText(helperClass.getBirthday());
        getNum.setText(helperClass.getNumber());
        getAddress.setText(helperClass.getAddress());

        Glide.with(getImage.getRootView()).load(helperClass.getImage()).placeholder(R.drawable.user_circle).into(getImage);

        return view;
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.immune:
                openImmune(helperClass);
                break;

            case R.id.files:
                openFile(helperClass);
                break;

            case R.id.comments:
                openComments(helperClass);
                break;

            case R.id.adverse:
                openAdverse(helperClass);
                break;
        }
    }

    private void openAdverse(addPatientHelperClass helperClass) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", helperClass);

        FragmentManager fragmentManager = (getActivity()).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        adverseFragment fragment = new adverseFragment();
        fragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.container_fragment, fragment).addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void openComments(addPatientHelperClass helperClass) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", helperClass);

        FragmentManager fragmentManager = (getActivity()).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        commentFragment fragment = new commentFragment();
        fragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.container_fragment, fragment).addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void openFile(addPatientHelperClass helperClass) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", helperClass);

        FragmentManager fragmentManager = (getActivity()).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        fileFragment fragment = new fileFragment();
        fragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.container_fragment, fragment).addToBackStack(null);
        fragmentTransaction.commit();
    }

    private void openImmune(addPatientHelperClass helperClass) {
        Bundle bundle = new Bundle();
        bundle.putParcelable("data", helperClass);

        FragmentManager fragmentManager = (getActivity()).getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        immunizationFragment fragment = new immunizationFragment();
        fragment.setArguments(bundle);

        fragmentTransaction.replace(R.id.container_fragment, fragment).addToBackStack(null);
        fragmentTransaction.commit();
    }
//
//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        // get item id
//        switch (item.getItemId()){
//            case R.id.update:
//                newUpdateAdded(helperClass);
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }
//
//    void newUpdateAdded(addPatientHelperClass helperClass) {
//        Bundle bundle = new Bundle();
//        bundle.putParcelable("data", helperClass);
//
//        FragmentManager fragmentManager = (getActivity()).getSupportFragmentManager();
//        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//        updateUserInfoFragment fragment = new updateUserInfoFragment();
//        fragment.setArguments(bundle);
//
//        fragmentTransaction.replace(R.id.container_fragment, fragment).addToBackStack(null);
//        fragmentTransaction.commit();
//    }
//
//    @Override
//    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
//
//        inflater.inflate(R.menu.menu_update_patient, menu) ;
//        super.onCreateOptionsMenu(menu, inflater);
//    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        setHasOptionsMenu(true); // to show menu in fragment
        super.onCreate(savedInstanceState);
    }


}










//        mAuth = FirebaseAuth.getInstance();
////        name = (TextView) view.findViewById(R.id.name);
////        id = (TextView) view.findViewById(R.id.idNo);
////        email = (TextView) view.findViewById(R.id.email);
////        age = (TextView) view.findViewById(R.id.age);
////        birthday = (TextView) view.findViewById(R.id.birthday);
////        address = (TextView) view.findViewById(R.id.address);
////        imageView = (CircleImageView) view.findViewById(R.id.profilePic);
////
////        recyclerView = (RecyclerView) view.findViewById(R.id.listViewPatient);
////        recyclerView.setHasFixedSize(true);
////        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
////
////
////        Bundle bundle = getArguments();
////        helperClass = bundle.getParcelable("data");
////
////        id.setText(helperClass.getId());
////        name.setText(helperClass.getfName()+" "+helperClass.getmName()+" "+helperClass.getlName());
////        email.setText(helperClass.getEmail());
////        age.setText(helperClass.getAge());
////        birthday.setText(helperClass.getBirthday());
////        address.setText(helperClass.getAddress());
////        Glide.with(imageView.getRootView()).load(helperClass.getImage()).placeholder(R.drawable.image).into(imageView);
////        //Picasso.get().load(helperClass.getImage()).resize(2000, 2000).placeholder(R.drawable.image).into(imageView);
////
////        ref =FirebaseDatabase.getInstance().getReference().child("Doctor List").child(mAuth.getCurrentUser().getUid()).child("Patient List").child(helperClass.getId()).child("Immunization");
////
////        ref.addChildEventListener(new ChildEventListener() {
////            @Override
////            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
////                SearchBarHelperClass searchBarHelperClass = snapshot.getValue(SearchBarHelperClass.class);
//////                snapshot.getKey();
////                myArrayList.add(0, searchBarHelperClass);
////                adapter = new SearchVaccineAdapter(getActivity(), myArrayList);
////                recyclerView.setAdapter(adapter);
////                adapter.notifyDataSetChanged();
////            }
////
////            @Override
////            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
////                adapter.notifyDataSetChanged();
////            }
////
////            @Override
////            public void onChildRemoved(@NonNull DataSnapshot snapshot) {
////
////            }
////
////            @Override
////            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
////
////            }
////
////            @Override
////            public void onCancelled(@NonNull DatabaseError error) {
////
////            }
////        });
////
////        return view;
////    }
////
////
////
////

////

////
////    void newVaccineAdded(SearchBarHelperClass helperClass) {
////
////        Bundle bundle = new Bundle();
////        bundle.putParcelable("data", helperClass);
////
////        FragmentManager fragmentManager = (getActivity()).getSupportFragmentManager();
////        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
////
////        inputVaccineFragment fragment = new inputVaccineFragment();
////        fragment.setArguments(bundle);
////
////        fragmentTransaction.replace(R.id.container_fragment, fragment).addToBackStack(null);
////        fragmentTransaction.commit();
////    }
////
////    @Override
////    public void onCreate(@Nullable Bundle savedInstanceState) {
////        setHasOptionsMenu(true); // to show menu in fragment
////        super.onCreate(savedInstanceState);
////    }
////
////}