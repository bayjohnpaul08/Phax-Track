package com.example.phaxtrack.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phaxtrack.R;
import com.example.phaxtrack.utils.AdverseAnsHelperClass;

import java.util.ArrayList;

public class AdverseAdapter extends RecyclerView.Adapter<AdverseAdapter.MyViewHolder> {

    ArrayList<AdverseAnsHelperClass> list;
    Context context;

    public AdverseAdapter(ArrayList<AdverseAnsHelperClass> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public AdverseAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.adverse_list_ans,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdverseAdapter.MyViewHolder holder, int position) {
        AdverseAnsHelperClass helperClass = list.get(position);
        holder.ans1.setText(helperClass.getAns1());
        holder.ans2.setText(helperClass.getAns2());
        holder.ans3.setText(helperClass.getAns3());
        holder.date.setText(helperClass.getDate());

    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView ans1, ans2, ans3, date;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            ans1 = (TextView) itemView.findViewById(R.id.ans1);
            ans2 = (TextView) itemView.findViewById(R.id.ans2);
            ans3 = (TextView) itemView.findViewById(R.id.ans3);
            date = (TextView) itemView.findViewById(R.id.date);
        }
    }
}
