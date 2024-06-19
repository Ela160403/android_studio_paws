package com.project.paws;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private TextView userNameTextView;
    private ImageView addPetImageView;
    private RecyclerView petsRecyclerView;
    private List<String> petNames;
    private PetsAdapter petsAdapter;

    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        userNameTextView = view.findViewById(R.id.userName);
        addPetImageView = view.findViewById(R.id.addPetImageView);
        petsRecyclerView = view.findViewById(R.id.petsRecyclerView);

        // Set up RecyclerView
        petNames = new ArrayList<>();
        petsAdapter = new PetsAdapter(petNames);
        petsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        petsRecyclerView.setAdapter(petsAdapter);

        // Fetch and set the user's name
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        if (user != null) {
            String userName = user.getDisplayName();
            if (userName != null && !userName.isEmpty()) {
                userNameTextView.setText(userName);
            } else {
                userNameTextView.setText("User Name");
            }
        } else {
            userNameTextView.setText("User Name");
        }

        // Navigate to AddPetFragment on addPetImageView click
        addPetImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Create a new instance of AddPetFragment
                Fragment addPetFragment = new AddPetFragment();

                // Get the FragmentManager and start a transaction
                FragmentTransaction transaction = getParentFragmentManager().beginTransaction();

                // Replace the current fragment with the AddPetFragm ent
                transaction.replace(R.id.frame, addPetFragment);

                // Add this transaction to the back stack
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();
            }
        });

        return view;
    }

    // Method to update RecyclerView with pet names
    public void updatePetList(String petName) {
        petNames.add(petName);
        petsAdapter.notifyDataSetChanged();
    }
}
