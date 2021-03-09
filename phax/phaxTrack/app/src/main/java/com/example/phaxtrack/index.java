package com.example.phaxtrack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.phaxtrack.navigationItem.addPatientFragment;
import com.example.phaxtrack.navigationItem.docProfileFragment;
import com.example.phaxtrack.navigationItem.homeFragment;
import com.example.phaxtrack.navigationItem.institutionListFragment;
import com.example.phaxtrack.navigationItem.menuPhaxTrack;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import de.hdodenhof.circleimageview.CircleImageView;


public class index extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, homeFragment.onFragmentBtnSelected{

    DrawerLayout drawerLayout;
    ActionBarDrawerToggle actionBarDrawerToggle;
    Toolbar toolbar;
    NavigationView navigationView;
    FragmentManager fragmentManager;
    FragmentTransaction fragmentTransaction;
    FirebaseAuth mAuth;
    FirebaseUser currentUser;
    DatabaseReference ref;
    TextView navName;
    CircleImageView navImage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_index);
        toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawerLayout = findViewById(R.id.drawer);
        navigationView=findViewById(R.id.navigationView);
        navigationView.setNavigationItemSelectedListener(this);

        actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(actionBarDrawerToggle);
        actionBarDrawerToggle.setDrawerIndicatorEnabled(true);
        actionBarDrawerToggle.syncState();

        //default start fragment
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.add(R.id.container_fragment, new homeFragment());
        fragmentTransaction.commit();

        // header hooks
        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();
        ref = FirebaseDatabase.getInstance().getReference().child("Doctor List").child(currentUser.getUid());

        View headerView = navigationView.getHeaderView(0);
        navName = headerView.findViewById(R.id.docName);
        navImage = headerView.findViewById(R.id.imageProfile);

        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                String username = snapshot.child("username").getValue().toString();
                String image = snapshot.child("image").getValue().toString();

               navName.setText(username);
               Glide.with(navImage.getRootView()).load(image).placeholder(R.drawable.user_circle).into(navImage);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        drawerLayout.closeDrawer(GravityCompat.START);
        if(item.getItemId() == R.id.menuPatient){
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment,new homeFragment()).addToBackStack(null);
            fragmentTransaction.commit();
        }
        if(item.getItemId() == R.id.menuPhax){
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment, new menuPhaxTrack()).addToBackStack(null);
            fragmentTransaction.commit();
        }
        if(item.getItemId() == R.id.menuInstitutions){
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment, new institutionListFragment()).addToBackStack(null);
            fragmentTransaction.commit();
        }
        if(item.getItemId() == R.id.menuProfile){
            fragmentManager = getSupportFragmentManager();
            fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_fragment, new docProfileFragment()).addToBackStack(null);
            fragmentTransaction.commit();
        }
        if(item.getItemId() == R.id.menuLogout){
            FirebaseAuth.getInstance().signOut();
            Intent loginActivity = new Intent(getApplicationContext(), ChooseLogin.class);
//            loginActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
//            loginActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(loginActivity);
        }

        return true;
    }

    @Override
    public void onButtonSelected() {
        fragmentManager = getSupportFragmentManager();
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.container_fragment, new addPatientFragment()).addToBackStack(null);
        fragmentTransaction.commit();
    }
}