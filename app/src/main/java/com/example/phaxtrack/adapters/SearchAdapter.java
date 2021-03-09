
package com.example.phaxtrack.adapters;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.phaxtrack.R;
import com.example.phaxtrack.navigationItem.userInfoFragment;
import com.example.phaxtrack.utils.SearchBarHelperClass;
import com.example.phaxtrack.utils.addPatientHelperClass;

import java.util.ArrayList;

import de.hdodenhof.circleimageview.CircleImageView;

public class SearchAdapter extends RecyclerView.Adapter<SearchAdapter.MyViewHolder>{

    Context context;
    ArrayList<addPatientHelperClass> list;

      public SearchAdapter(ArrayList<addPatientHelperClass> list, Context context){
          this.list = list;
          this.context = context;
      }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_list_item,parent,false);
        return new MyViewHolder(view);

    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
          final addPatientHelperClass helperClass = list.get(position);
//        final SearchBarHelperClass helperClass = list.get(position);
//        holder.date.setText(helperClass.getDate());
//        holder.id.setText(helperClass.getId());
//        holder.name.setText(helperClass.getlName() + ", " + helperClass.getfName() + " " + helperClass.getmName());
//        Glide.with(holder.image.getContext()).load(helperClass.getImage()).placeholder(R.drawable.image).into(holder.image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

//                Bundle bundle = new Bundle();
//                bundle.putParcelable("data", helperClass);
//
//                FragmentManager fragmentManager = ((AppCompatActivity)context).getSupportFragmentManager();
//                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
//
//                userInfoFragment fragment = new userInfoFragment();
//                fragment.setArguments(bundle);
//
//                fragmentTransaction.replace(R.id.container_fragment, fragment).addToBackStack(null);
//                fragmentTransaction.commit();
            }
        });

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView name, id, date;
        CircleImageView image;
        public MyViewHolder(View itemView){
            super(itemView);
            name = itemView.findViewById(R.id.name);
            id = itemView.findViewById(R.id.idNo);
            image = itemView.findViewById(R.id.image);
            date = itemView.findViewById(R.id.dateOfVaccine);

        }
    }
}
