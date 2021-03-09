package com.example.phaxtrack.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.phaxtrack.R;
import com.example.phaxtrack.utils.TableHelperClass;

import org.w3c.dom.Text;

import java.util.List;

public class TableAdapter extends RecyclerView.Adapter<TableAdapter.MyViewHolder> {

    Context context;
    List<TableHelperClass> tableList;

    public TableAdapter(Context context, List<TableHelperClass> tableList) {
        this.context = context;
        this.tableList = tableList;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_table_layout, parent, false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {

        if(tableList != null & tableList.size() > 0){
            TableHelperClass helperClass = tableList.get(position);
            holder.vaccine_item.setText(helperClass.getVaccine());
            holder.date_item.setText(helperClass.getDate());
            holder.hcp_item.setText(helperClass.getHcp());
            holder.clinics_item.setText(helperClass.getClinics());

        }
        else {
            return;
        }
    }

    @Override
    public int getItemCount() {
        return tableList.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {

        private TextView vaccine_item, date_item, hcp_item, clinics_item;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);

            vaccine_item = (TextView) itemView.findViewById(R.id.vaccine_item);
            date_item = (TextView) itemView.findViewById(R.id.date_item);
            hcp_item = (TextView) itemView.findViewById(R.id.hcp_item);
            clinics_item = (TextView) itemView.findViewById(R.id.clinics_item);
        }
    }
}
