package com.softwareengineering.recipeapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.softwareengineering.recipeapp.R;
import com.softwareengineering.recipeapp.modelsRandomRecipes.Step;

import java.util.List;

public class InstructionStepAdapter extends RecyclerView.Adapter<InstructionStepViewholder>{

    Context context;
    List<Step> list;

    public InstructionStepAdapter(Context context, List<Step> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public InstructionStepViewholder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new InstructionStepViewholder(LayoutInflater.from(context).inflate(R.layout.list_instructions_steps, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull InstructionStepViewholder holder, int position) {
        holder.textView_instruction_step_number.setText(String.valueOf(list.get(position).number));
        holder.textView_instruction_step_title.setText(list.get(position).step);
    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}

class InstructionStepViewholder extends RecyclerView.ViewHolder {

    TextView textView_instruction_step_number, textView_instruction_step_title, textView_instruction_step_description;

    public InstructionStepViewholder(@NonNull View itemView) {
        super(itemView);

        textView_instruction_step_number = itemView.findViewById(R.id.textView_instruction_step_number);
        textView_instruction_step_title = itemView.findViewById(R.id.textView_instruction_step_title);

    }
}