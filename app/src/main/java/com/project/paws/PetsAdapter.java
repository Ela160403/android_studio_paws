package com.project.paws;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class PetsAdapter extends RecyclerView.Adapter<PetsAdapter.PetViewHolder> {

    private List<String> petNames;

    public PetsAdapter(List<String> petNames) {
        this.petNames = petNames;
    }

    @NonNull
    @Override
    public PetViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_pet, parent, false);
        return new PetViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PetViewHolder holder, int position) {
        String petName = petNames.get(position);
        holder.petNameTextView.setText(petName);
    }

    @Override
    public int getItemCount() {
        return petNames.size();
    }

    public static class PetViewHolder extends RecyclerView.ViewHolder {
        TextView petNameTextView;

        public PetViewHolder(View itemView) {
            super(itemView);
            petNameTextView = itemView.findViewById(R.id.petNameTextView);
        }
    }
}
