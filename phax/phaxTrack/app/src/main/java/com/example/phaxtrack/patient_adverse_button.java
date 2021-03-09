package com.example.phaxtrack;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.phaxtrack.utils.AdverseAnsHelperClass;
import com.example.phaxtrack.utils.addPatientHelperClass;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import org.parceler.Parcels;

import java.text.DateFormat;
import java.util.Calendar;

import de.hdodenhof.circleimageview.CircleImageView;

public class patient_adverse_button extends AppCompatActivity implements View.OnClickListener {
    private CircleImageView image;
    private TextView getName, home, save_edit;
    private EditText ans1, ans2, ans3, getPass;
    private TextView date;
    addPatientHelperClass helperClass;
    private DatabaseReference reference;
    String pass;
    String passFromDB;
    String getAns1, getAns2, getAns3, getDate;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_patient_adverse_button);

        image = (CircleImageView) findViewById(R.id.image);
        getName = (TextView) findViewById(R.id.getName);
        ans1 = (EditText) findViewById(R.id.ans1);
        ans2 = (EditText) findViewById(R.id.ans2);
        ans3 = (EditText) findViewById(R.id.ans3);
        home = (TextView) findViewById(R.id.home);
        save_edit = (TextView) findViewById(R.id.save_edit);
        date = (TextView) findViewById(R.id.date);
        home.setOnClickListener(this);
        save_edit.setOnClickListener(this);

        Calendar calendar = Calendar.getInstance();
        String currentDate = DateFormat.getDateInstance(DateFormat.FULL).format(calendar.getTime());
        date.setText(currentDate);

        helperClass = (addPatientHelperClass) Parcels.unwrap(getIntent().getParcelableExtra("helperClass"));
        passFromDB = helperClass.getPassword();

        Glide.with(image.getRootView()).load(helperClass.getImage()).placeholder(R.drawable.group_1).into(image);
        getName.setText("Hello, " + helperClass.getFirstName() + "!");

        reference = FirebaseDatabase.getInstance().getReference("Adverse Answer").child(helperClass.getId());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.home:
                openInfo();
                break;

            case R.id.save_edit:
                SaveAdverse();
                break;
        }
    }

    private void openInfo() {
            Intent intent = new Intent( patient_adverse_button.this , PatientInfo.class);
            Bundle bundle = new Bundle();
            bundle.putParcelable("helperClass", Parcels.wrap(helperClass));
            intent.putExtras(bundle);
            startActivity(intent);
    }

    private void SaveAdverse() {
        getAns1 = ans1.getText().toString().trim();
        getAns2 = ans2.getText().toString().trim();
        getAns3 = ans3.getText().toString().trim();
        getDate = date.getText().toString().trim();

        if(getAns1.isEmpty() || getAns2.isEmpty() || getAns3.isEmpty()){
            Toast.makeText(patient_adverse_button.this, "Please answer all fields!", Toast.LENGTH_LONG).show();
        }
        else{
            AlertDialog.Builder builder = new AlertDialog.Builder(patient_adverse_button.this, R.style.AlertDialogTheme);
            final View view = LayoutInflater.from(patient_adverse_button.this).inflate(
                    R.layout.dialog_adverse,
                    (ConstraintLayout) findViewById(R.id.adverse)
            );

            builder.setView(view);
            final TextView caption = (TextView) view.findViewById(R.id.caption);
            getPass = (EditText) view.findViewById(R.id.patient_password);

            final AlertDialog alertDialog = builder.create();
            view.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(getPass.getText().toString().isEmpty())
                    {
                        caption.setText("Password is Required!");
                        caption.setTextColor(Color.RED);
                        return;
                    }

                    if(!passFromDB.equals(getPass.getText().toString()))
                    {
                        caption.setText("Wrong Password, Try Again!");
                        caption.setTextColor(Color.RED);
                        return;
                    }

                    if (passFromDB.equals(getPass.getText().toString())) {
                        AdverseAnsHelperClass ans = new AdverseAnsHelperClass(getAns1, getAns2, getAns3, getDate);
                        reference.push().setValue(ans);
                        Intent intent = new Intent( patient_adverse_button.this , PatientInfo.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("helperClass", Parcels.wrap(helperClass));
                        intent.putExtras(bundle);
                        startActivity(intent);
                        Toast.makeText(patient_adverse_button.this, "Answer Submit Successfully", Toast.LENGTH_LONG).show();

                        alertDialog.dismiss();
                    }
                }
            });

            if(alertDialog.getWindow() != null){
                alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
            }
            alertDialog.show();
        }


    }
}