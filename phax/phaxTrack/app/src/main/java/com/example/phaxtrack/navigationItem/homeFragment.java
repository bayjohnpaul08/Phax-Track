package com.example.phaxtrack.navigationItem;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.example.phaxtrack.PatientInfo;
import com.example.phaxtrack.PatientLoginPage;
import com.example.phaxtrack.R;
import com.example.phaxtrack.adapters.SearchAdapter;
import com.example.phaxtrack.utils.DoctorHelperClass;
import com.example.phaxtrack.utils.SearchBarHelperClass;
import com.example.phaxtrack.utils.addPatientHelperClass;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.parceler.Parcels;
import org.w3c.dom.Text;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 */
public class homeFragment extends Fragment{
    private FirebaseAuth mAuth;
    private DatabaseReference root;
    private EditText searchBar;
    private Button searchButton;
    private TextView registerPatient;
    private onFragmentBtnSelected listener;
    private DatabaseReference ref;
    String pass;


    public homeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_home, container, false);

        mAuth = FirebaseAuth.getInstance();
        searchBar = (EditText) view.findViewById(R.id.inputEmail);
        searchButton = (Button) view.findViewById(R.id.searchButton);
        registerPatient = (TextView) view.findViewById(R.id.registerPatient);

        registerPatient.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                listener.onButtonSelected();
            }
        });

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                searchUsers();
            }
        });

        return view;
    }

    private void searchUsers() {
        final String search = searchBar.getText().toString().trim();
        root = FirebaseDatabase.getInstance().getReference("Patient List");

        ref = FirebaseDatabase.getInstance().getReference("Patient List").child(search);

        if(search.isEmpty()){
            openDialog();
        }else {

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                pass = snapshot.child("password").getValue(String.class);

                if (snapshot.exists()){

                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
                    View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_req_pass, null);
                    builder.setView(view);

                    final TextView header = (TextView) view.findViewById(R.id.textView5);
                    final TextView body = (TextView) view.findViewById(R.id.textView8);
                    final EditText req_pass = (EditText) view.findViewById(R.id.req_password);
                    final AlertDialog alertDialog1 = builder.create();

                    view.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if(!pass.equals(req_pass.getText().toString())){
                                header.setText("ACCESS DENIED");
                                body.setText("The password you entered is incorrect!");
                                body.setTextColor(Color.RED);
                            }

                            if(req_pass.getText().toString().isEmpty()){
                                header.setText("ACCESS DENIED");
                                body.setText("Password is required!");
                                body.setTextColor(Color.RED);
                            }

                            if(pass.equals(req_pass.getText().toString())){

                                root.child(search).addValueEventListener(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                                        if(snapshot.exists()){
                                            String address = snapshot.child("address").getValue(String.class);
                                            String birthday = snapshot.child("birthday").getValue(String.class);
                                            String firstName = snapshot.child("firstName").getValue(String.class);
                                            String gender = snapshot.child("gender").getValue(String.class);
                                            String id = snapshot.child("id").getValue(String.class);
                                            String image = snapshot.child("image").getValue(String.class);
                                            String middleName = snapshot.child("middleName").getValue(String.class);
                                            String email = snapshot.child("email").getValue(String.class);
                                            String password = snapshot.child("password").getValue(String.class);
                                            String surname = snapshot.child("surname").getValue(String.class);
                                            String number = snapshot.child("number").getValue(String.class);
                                            String age = snapshot.child("age").getValue(String.class);

                                            addPatientHelperClass helperClass = new addPatientHelperClass(address, birthday, firstName, age, gender, id, image, middleName, password, surname, number, email);

                                            Bundle bundle = new Bundle();
                                            bundle.putParcelable("data", helperClass);

                                            FragmentManager fragmentManager = ((AppCompatActivity) getContext()).getSupportFragmentManager();
                                            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

                                            userInfoFragment fragment = new userInfoFragment();
                                            fragment.setArguments(bundle);

                                            fragmentTransaction.replace(R.id.container_fragment, fragment).addToBackStack(null);
                                            fragmentTransaction.commit();
                                            alertDialog1.dismiss();
                                        }
                                    }

                                    @Override
                                    public void onCancelled(@NonNull DatabaseError error) {

                                    }
                                });
                            }
                        }
                    });
                    if(alertDialog1.getWindow() != null){
                        alertDialog1.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                    }
                    alertDialog1.show();
                }else{
                    openDialog();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        }

    }

    private void openDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_search_user, null);
        builder.setView(view);

        final AlertDialog alertDialog = builder.create();
        view.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
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

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if(context instanceof onFragmentBtnSelected){
            listener = (onFragmentBtnSelected) context;
        }else{
            throw new ClassCastException(context.toString() + "must implement listener");
        }
    }

    public interface onFragmentBtnSelected{
        public void onButtonSelected();
    }

}