package com.softwareengineering.recipeapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.softwareengineering.recipeapp.R;

import java.util.List;

public class AllergyAdapter extends RecyclerView.Adapter<AllergyViewHolder>{

    Context context;
    List<String> allergyList;

    public AllergyAdapter(Context context, List <String> allergyList){
        this.context = context;
        this.allergyList = allergyList;
    }

    @NonNull
    @Override
    public AllergyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.list_allergies, parent, false);
        return new AllergyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AllergyViewHolder holder, int position) {
        String allergy = allergyList.get(position);
        holder.allergyName.setText(allergy);
    }

    @Override
    public int getItemCount() {
        return allergyList.size();
    }
}


class AllergyViewHolder extends RecyclerView.ViewHolder{
    TextView allergyName;

    public AllergyViewHolder(@NonNull View itemView) {
        super(itemView);
        allergyName = itemView.findViewById(R.id.textView_listed_allergies);
    }
}
