package com.project.paws;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import com.project.paws.Pet;

import java.util.List;

public class PetListAdapter extends ArrayAdapter<Pet> {

    private Context mContext;
    private int mResource;

    public PetListAdapter(@NonNull Context context, int resource, @NonNull List<Pet> pets) {
        super(context, resource, pets);
        this.mContext = context;
        this.mResource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(mContext);
            convertView = inflater.inflate(mResource, parent, false);
        }

        Pet pet = getItem(position);

        if (pet != null) {
            ImageView imageViewPet = convertView.findViewById(R.id.imageViewPet);
            TextView textViewName = convertView.findViewById(R.id.textViewName);
            TextView textViewBreed = convertView.findViewById(R.id.textViewBreed);

            // Set pet's image (if available)
            // imageViewPet.setImageResource(pet.getImageResource()); // Uncomment if you have a specific image resource
            // Set default image
            imageViewPet.setImageResource(R.drawable.pawsv); // Example placeholder image

            textViewName.setText(pet.getName());
            textViewBreed.setText(pet.getBreed());

        }

        return convertView;
    }
}
