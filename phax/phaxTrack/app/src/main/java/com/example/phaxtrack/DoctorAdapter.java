package com.example.phaxtrack;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.phaxtrack.utils.DoctorHelperClass;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class DoctorAdapter extends RecyclerView.Adapter<DoctorAdapter.MyViewHolder> {

    ArrayList<DoctorHelperClass> list;

    public DoctorAdapter(ArrayList<DoctorHelperClass> list){
        this.list = list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.user_list, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        final DoctorHelperClass helperClass = list.get(position);
        holder.email.setText(helperClass.getEmail());
        holder.name.setText(helperClass.getUsername());
        Glide.with(holder.image.getContext()).load(helperClass.getImage()).placeholder(R.drawable.image).diskCacheStrategy(DiskCacheStrategy.ALL).fitCenter().into(holder.image);
        //Picasso.get().load(helperClass.getImage()).resize(2000, 2000).placeholder(R.drawable.image).into(holder.image);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView email, name;
        ImageView image;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            email = (TextView) itemView.findViewById(R.id.email);
            name = (TextView) itemView.findViewById(R.id.name);
            image = (ImageView) itemView.findViewById(R.id.imageView);

        }
    }
}
