package com.example.phaxtrack.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phaxtrack.R;
import com.example.phaxtrack.utils.SearchBarHelperClass;

import java.util.List;

public class SearchVaccineAdapter extends RecyclerView.Adapter<SearchVaccineAdapter.MyViewHolder> {
    List<SearchBarHelperClass> mList;
    Context context;


    public SearchVaccineAdapter(Context context, List<SearchBarHelperClass> mList) {
        this.mList = mList;
        this.context = context;
    }

    @NonNull
    @Override
    public SearchVaccineAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.vaccine_list,parent,false);
        return new SearchVaccineAdapter.MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SearchVaccineAdapter.MyViewHolder holder, int position) {
        //get data
        SearchBarHelperClass helperClass = mList.get(position);
        holder.vaccine_Name.setText(helperClass.getVaccine_Name());
        holder.institution_Name.setText(helperClass.getInstitution_Name());
        holder.vaccine_Date.setText(helperClass.getVaccine_Date());
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        TextView vaccine_Name, institution_Name, vaccine_Date;
        public MyViewHolder(View itemView){
            super(itemView);

            vaccine_Name = itemView.findViewById(R.id.vaccineName);
            institution_Name = itemView.findViewById(R.id.institutionName);
            vaccine_Date = itemView.findViewById(R.id.vaccineDate);
        }
    }
}
