package com.example.phaxtrack.navigationItem;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.phaxtrack.R;
import com.example.phaxtrack.utils.addPatientHelperClass;
import com.example.phaxtrack.utils.commentHelperClass;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class commentFragment extends Fragment implements View.OnClickListener {

    private TextView comment_name, comment_id, comment_gender, edit_comment;
    private CircleImageView comment_image;
    private addPatientHelperClass helperClass;
    private TextView text_comment;
    private EditText editText_comment;
    private Button save_changes;
    private DatabaseReference reference;
    private ArrayList<commentHelperClass> list = new ArrayList<>();
    TextView back;

    public commentFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_comment, container, false);

        comment_name = (TextView) view.findViewById(R.id.comments_Name);
        comment_id = (TextView) view.findViewById(R.id.comments_Id);
        comment_gender = (TextView) view.findViewById(R.id.comments_Gender);
        comment_image = (CircleImageView) view.findViewById(R.id.comments_Image);
        text_comment = (TextView) view.findViewById(R.id.text_comment);
        editText_comment = (EditText) view.findViewById(R.id.editText_comment);
        edit_comment = (TextView) view.findViewById(R.id.edit_comment);
        edit_comment.setOnClickListener(this);
        save_changes = (Button) view.findViewById(R.id.save_changes);
        save_changes.setOnClickListener(this);
        back = (TextView) view.findViewById(R.id.back);
        back.setOnClickListener(this);

        Bundle bundle = getArguments();
        helperClass = bundle.getParcelable("data");

        comment_name.setText("COMMENTS FOR " + helperClass.getFirstName().toUpperCase() + " " + helperClass.getSurname().toUpperCase());
        comment_id.setText("Patient " + helperClass.getId());
        comment_gender.setText(helperClass.getGender() + " • " + helperClass.getAge());
        Glide.with(comment_image.getRootView()).load(helperClass.getImage()).placeholder(R.drawable.user_circle).into(comment_image);

        reference = FirebaseDatabase.getInstance().getReference("Comments").child(helperClass.getId());

        reference.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if(snapshot.exists()){
                    commentHelperClass model = snapshot.getValue(commentHelperClass.class);

                    text_comment.setText(model.getComment());
                    editText_comment.setText(model.getComment());
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        text_comment.setVisibility(View.VISIBLE);
        editText_comment.setVisibility(View.GONE);

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.edit_comment:
                editComment();
                break;

            case R.id.save_changes:
                saveButton(helperClass);
                break;

            case R.id.back:
                backBtn();
                break;
        }
    }

    private void editComment() {
        text_comment.setVisibility(View.GONE);
        editText_comment.setVisibility(View.VISIBLE);
    }

    private void saveButton(addPatientHelperClass helperClass) {

        reference = FirebaseDatabase.getInstance().getReference("Comments").child(helperClass.getId());

        editText_comment.setVisibility(View.GONE);
        text_comment.setVisibility(View.VISIBLE);

        String getComment = editText_comment.getText().toString().trim();

        text_comment.setText(getComment);

        commentHelperClass model = new commentHelperClass(getComment);
        reference.setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    Toast.makeText(getActivity(), "Save Changes Successfully", Toast.LENGTH_LONG).show();
                }else {
                    Toast.makeText(getActivity(), "Failed", Toast.LENGTH_LONG).show();
                }
            }
        });
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