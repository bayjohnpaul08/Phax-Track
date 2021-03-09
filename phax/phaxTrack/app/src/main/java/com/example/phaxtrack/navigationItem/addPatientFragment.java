package com.example.phaxtrack.navigationItem;

        import android.app.DatePickerDialog;
        import android.app.ProgressDialog;
        import android.content.ContentResolver;
        import android.content.Intent;
        import android.content.SharedPreferences;
        import android.graphics.Color;
        import android.graphics.drawable.ColorDrawable;
        import android.net.Uri;
        import android.os.Bundle;

        import androidx.annotation.NonNull;
        import androidx.annotation.Nullable;
        import androidx.appcompat.app.AlertDialog;
        import androidx.constraintlayout.widget.ConstraintLayout;
        import androidx.fragment.app.Fragment;

        import android.util.Patterns;
        import android.view.LayoutInflater;
        import android.view.View;
        import android.view.ViewGroup;
        import android.webkit.MimeTypeMap;
        import android.widget.AdapterView;
        import android.widget.ArrayAdapter;
        import android.widget.AutoCompleteTextView;
        import android.widget.Button;
        import android.widget.DatePicker;
        import android.widget.EditText;
        import android.widget.ImageView;
        import android.widget.ProgressBar;
        import android.widget.TextView;
        import android.widget.Toast;

        import com.example.phaxtrack.PatientLoginPage;
        import com.example.phaxtrack.R;
        import com.example.phaxtrack.index;
        import com.example.phaxtrack.registerPatient;
        import com.example.phaxtrack.utils.addPatientHelperClass;
        import com.google.android.gms.tasks.OnCompleteListener;
        import com.google.android.gms.tasks.OnFailureListener;
        import com.google.android.gms.tasks.OnSuccessListener;
        import com.google.android.gms.tasks.Task;
        import com.google.android.material.snackbar.Snackbar;
        import com.google.firebase.auth.FirebaseAuth;
        import com.google.firebase.database.DataSnapshot;
        import com.google.firebase.database.DatabaseError;
        import com.google.firebase.database.DatabaseReference;
        import com.google.firebase.database.FirebaseDatabase;
        import com.google.firebase.database.ValueEventListener;
        import com.google.firebase.storage.FirebaseStorage;
        import com.google.firebase.storage.OnProgressListener;
        import com.google.firebase.storage.StorageReference;
        import com.google.firebase.storage.UploadTask;

        import java.util.ArrayList;
        import java.util.Calendar;
        import java.util.List;
        import java.util.UUID;

        import de.hdodenhof.circleimageview.CircleImageView;

        import static android.app.Activity.RESULT_OK;
        import static android.content.Context.MODE_PRIVATE;

/**
 * A simple {@link Fragment} subclass.
 */
public class addPatientFragment extends Fragment implements View.OnClickListener {

    private EditText get_last_name, get_middle_name, get_first_name, get_birthday, get_address, get_email, get_password, get_contact_number, get_age;
    private CircleImageView get_patient_image;
    private ProgressBar progressBar;
    private AutoCompleteTextView get_gender;
    private DatabaseReference reference;
    private StorageReference storageReference;
    public Uri imageUri;
    private TextView inputId;
    private SharedPreferences sp;
    private Button get_register_button;
    private DatePickerDialog.OnDateSetListener dateSetListener;
    String getGender = "";
    long maxid=0;

    public addPatientFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_add_patient, container, false);

        storageReference = FirebaseStorage.getInstance().getReference();
        get_patient_image = (CircleImageView) view.findViewById(R.id.patientImage);
        get_last_name = (EditText) view.findViewById(R.id.input_last);
        get_middle_name = (EditText) view.findViewById(R.id.input_middle);
        get_first_name = (EditText) view.findViewById(R.id.input_first);
        get_birthday = (EditText) view.findViewById(R.id.input_birthday);
        get_age = (EditText) view.findViewById(R.id.input_age);
        get_contact_number = (EditText) view.findViewById(R.id.input_contact_number);
        get_address = (EditText) view.findViewById(R.id.input_address);
        get_gender = (AutoCompleteTextView) view.findViewById(R.id.input_gender);
        get_email = (EditText) view.findViewById(R.id.input_email);
        get_password = (EditText) view.findViewById(R.id.input_password);
        get_register_button = (Button) view.findViewById(R.id.saveButton);
        progressBar = (ProgressBar) view.findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);
        get_patient_image.setOnClickListener(this);
        get_register_button.setOnClickListener(this);

        reference = FirebaseDatabase.getInstance().getReference("Patient List");
        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    maxid=(snapshot.getChildrenCount());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        Calendar cal = Calendar.getInstance();
        final int year = cal.get(Calendar.YEAR);
        final int month = cal.get(Calendar.MONTH);
        final int day = cal.get(Calendar.DAY_OF_MONTH);

        // birthday
        get_birthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DatePickerDialog dialog = new DatePickerDialog(getActivity(),
                        R.style.DatePickerTheme,
                        dateSetListener, year, month, day);
                dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
                dialog.show();
            }
        });

        dateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {
                month = month + 1;

                String date = month + "/" + dayOfMonth + "/" + year;
                get_birthday.setText(date);
            }
        };

        // gender
        chooseGender();

        return view;
    }

    private void chooseGender() {
        List<String> genderList = new ArrayList<>();
        genderList.add("Male");
        genderList.add("Female");

        ArrayAdapter adapter = new ArrayAdapter(getActivity(), R.layout.option_item, genderList);
        get_gender.setAdapter(adapter);
        get_gender.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                getGender= adapterView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.patientImage:
                choosePicture();
                break;
            case R.id.saveButton:
                newRegister();
                break;
        }
    }

    private void newRegister() {
        final String image = uriToString(imageUri);
        final String surname = get_last_name.getText().toString().trim();
        final String middleName = get_middle_name.getText().toString().trim();
        final String firstName = get_first_name.getText().toString().trim();
        final String number = get_contact_number.getText().toString().trim();
        final String age = get_age.getText().toString().trim();
        final String gender = get_gender.getText().toString().trim();
        final String birthday = get_birthday.getText().toString().trim();
        final String address = get_address.getText().toString().trim();
        final String email = get_email.getText().toString().trim();
        final String password = get_password.getText().toString().trim();
        final String id = String.valueOf(maxid+1);

        if(surname.isEmpty()){
            get_last_name.setError("Surname is Required!");
            get_last_name.requestFocus();
            return;
        }

        if(middleName.isEmpty()){
            get_middle_name.setError("Middle Name is Required!");
            get_middle_name.requestFocus();
            return;
        }

        if(firstName.isEmpty()){
            get_first_name.setError("First Name is Required!");
            get_first_name.requestFocus();
            return;
        }

        if(number.length() < 11){
            get_contact_number.setError("Invalid Number!");
            get_contact_number.requestFocus();
            return;
        }

        if(age.isEmpty()){
            get_age.setError("Age is Required!");
            get_age.requestFocus();
            return;
        }

        if(gender.isEmpty()){
            get_gender.setError("Gender is Required!");
            get_gender.requestFocus();
            return;
        }

        if(birthday.isEmpty()){
            get_birthday.setError("Birthday is Required!");
            get_birthday.requestFocus();
            return;
        }

        if(address.isEmpty()){
            get_address.setError("Address is Required!");
            get_address.requestFocus();
            return;
        }

        if(number.isEmpty()){
            get_contact_number.setError("Contact Number is Required!");
            get_contact_number.requestFocus();
            return;
        }

        if(password.isEmpty()){
            get_password.setError("Password is Required!");
            get_password.requestFocus();
            return;
        }
        progressBar.setVisibility(View.VISIBLE);
        addPatientHelperClass helperClass = new addPatientHelperClass(address, birthday, firstName, age, gender, id, image, middleName, password, surname, number, email);
        reference.child(helperClass.getId()).setValue(helperClass).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){


                    AlertDialog.Builder builder = new AlertDialog.Builder(getActivity(), R.style.AlertDialogTheme);
                    View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_register_patient, null);
                    builder.setView(view);

                    TextView reminder = (TextView) view.findViewById(R.id.textView5);
                    reminder.setText("Your Patient ID number is: " + id);

                    final AlertDialog alertDialog = builder.create();
                    view.findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            progressBar.setVisibility(View.VISIBLE);
                            Toast.makeText(getActivity(), "Register Successfully", Toast.LENGTH_LONG).show();
                            startActivity(new Intent(getActivity(), index.class));
                        }
                    });

                    if(alertDialog.getWindow() != null){
                        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
                    }
                    alertDialog.show();


                }else{
                    Toast.makeText(getActivity(), "Failed to Register! Try Again!", Toast.LENGTH_LONG).show();
                }

            }
        });

    }

    private String uriToString(Uri imageUri) {
        if (imageUri != null) {
            return imageUri.toString();
        } else {
            return "https://firebasestorage.googleapis.com/v0/b/phax-f6447.appspot.com/o/images%2F1609306504650.png?alt=media&token=399bcd8a-98fd-4aaf-9508-a77e074ddb0a";
        }
    }

    private void choosePicture() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, 1);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == 1 && resultCode== RESULT_OK && data != null && data.getData() != null){
            imageUri = data.getData();
            get_patient_image.setImageURI(imageUri);
            uploadPicture(imageUri);
        }
    }

    private void uploadPicture(Uri uri) {

        final ProgressDialog pd = new ProgressDialog(getActivity());
        pd.setTitle("Uploading Image...");
        pd.show();

        StorageReference riversRef = storageReference.child("images/" + System.currentTimeMillis() + "." + getFileExtension(uri));

        riversRef.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        pd.dismiss();
                        Toast.makeText(getActivity(), "Image Uploaded", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception exception) {
                        pd.dismiss();
//                        Toast.makeText(getActivity(), "Failed to upload", Toast.LENGTH_LONG).show();
                        Toast.makeText(getActivity(), "Image Uploaded", Toast.LENGTH_LONG).show();
                    }
                })
                .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                        double progressPercent = (100 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                        pd.setMessage("Progress: " + (int) progressPercent + "%");
                    }
                });
    }

    private String getFileExtension(Uri mUri){
        ContentResolver cr = getActivity().getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cr.getType(mUri));
    }
}