package com.project.paws;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;


public class AddPetFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_add_pet, container, false);

        ImageView catImageView = view.findViewById(R.id.catImageView);
        ImageView dogImageView = view.findViewById(R.id.dogImageView);
        ImageView birdImageView = view.findViewById(R.id.birdImageView);
        ImageView rabbitImageView = view.findViewById(R.id.rabbitImageView);

        catImageView.setOnClickListener(v -> navigateToFragment(new CatFragment()));
        dogImageView.setOnClickListener(v -> navigateToFragment(new DogFragment()));
        birdImageView.setOnClickListener(v -> navigateToFragment(new BirdFragment()));
        rabbitImageView.setOnClickListener(v -> navigateToFragment(new RabbitFragment()));

        return view;
    }

    private void navigateToFragment(Fragment fragment) {
        FragmentTransaction transaction = getParentFragmentManager().beginTransaction();
        transaction.replace(R.id.frame, fragment);  // Assuming you have a container for fragments in your activity layout
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
