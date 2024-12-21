package com.softwareengineering.recipeapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.softwareengineering.recipeapp.R;
import com.softwareengineering.recipeapp.modelsRandomRecipes.InstructionsResponse;

import java.util.List;

public class FavoritesInstructionsAdapter extends RecyclerView.Adapter<FavoritesInstructionsViewHolder> {
    Context context;
    List<InstructionsResponse> list;

    public FavoritesInstructionsAdapter(Context context, List<InstructionsResponse> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public FavoritesInstructionsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new FavoritesInstructionsViewHolder(LayoutInflater.from(context).inflate(R.layout.list_favorite_instructions, parent, false));

    }

    @Override
    public void onBindViewHolder(@NonNull FavoritesInstructionsViewHolder holder, int position) {
        holder.textView_favorite_instruction_name.setText(list.get(position).name);
        holder.recyclerView_favorite_instruction_steps.setHasFixedSize(true);
        holder.recyclerView_favorite_instruction_steps.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
        InstructionStepAdapter stepAdapter = new InstructionStepAdapter(context, list.get(position).steps);
        holder.recyclerView_favorite_instruction_steps.setAdapter(stepAdapter);

    }

    @Override
    public int getItemCount() {
        return list.size();
    }
}
class FavoritesInstructionsViewHolder extends RecyclerView.ViewHolder {

    TextView textView_favorite_instruction_name;
    RecyclerView recyclerView_favorite_instruction_steps;

    public FavoritesInstructionsViewHolder(@NonNull View itemView) {
        super(itemView);
        textView_favorite_instruction_name = itemView.findViewById(R.id.textView_favorite_instruction_name);
        recyclerView_favorite_instruction_steps = itemView.findViewById(R.id.recyclerView_favorite_instruction_steps);
    }
}