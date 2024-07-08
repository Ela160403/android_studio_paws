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
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;

public class HomeFragment extends Fragment {

    private TextView userNameTextView;
    private RecyclerView recyclerView;
    private PetsAdapter petsAdapter;
    private List<String> petNames;
    private FirebaseFirestore db;
    private FirebaseUser user;
    private ImageView addPetImageView;
    private ImageView AddPetInfoView;
    public HomeFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);
        userNameTextView = view.findViewById(R.id.userName);
        recyclerView = view.findViewById(R.id.petsRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        addPetImageView = view.findViewById(R.id.addPetImageView);
        AddPetInfoView = view.findViewById(R.id.petinfoimage);

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        petNames = new ArrayList<>();
        petsAdapter = new PetsAdapter(petNames);
        recyclerView.setAdapter(petsAdapter);

        db = FirebaseFirestore.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();

        // Fetch and set the user's name
        if (user != null) {
            db.collection("users").document(user.getUid()).get()
                    .addOnSuccessListener(documentSnapshot -> {
                        if (documentSnapshot.exists()) {
                            String userName = documentSnapshot.getString("name");
                            if (userName != null && !userName.isEmpty()) {
                                userNameTextView.setText(userName);
                            } else {
                                userNameTextView.setText("User Name");
                            }
                        } else {
                            userNameTextView.setText("User Name");
                        }
                    })
                    .addOnFailureListener(e -> userNameTextView.setText("User Name"));
        } else {
            userNameTextView.setText("User Name");
        }

        // Add OnClickListener for addPetImageView
        addPetImageView.setOnClickListener(v -> {
            // Create a new instance of AddPetFragment
            Fragment addPetFragment = new AddPetFragment();

            // Get the FragmentManager and start a transaction
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();

            // Replace the current fragment with the AddPetFragment
            transaction.replace(R.id.frame, addPetFragment);

            // Add this transaction to the back stack
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
        });
        AddPetInfoView.setOnClickListener(v -> {
            // Create a new instance of PetInfoFragment
            Fragment petInfoFragment = new PetInfoFragment();

            // Get the FragmentManager and start a transaction
            FragmentTransaction transaction = getParentFragmentManager().beginTransaction();

            // Replace the current fragment with the PetInfoFragment
            transaction.replace(R.id.frame, petInfoFragment);

            // Add this transaction to the back stack
            transaction.addToBackStack(null);

            // Commit the transaction
            transaction.commit();
        });

        // Query Firestore for all pet names
        db.collection("cats")
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        // Handle error
                        return;
                    }

                    if (value != null) {
                        petNames.clear(); // Clear list before adding new items
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                // Assuming "petName" is the field name for pet names in Firestore
                                String petName = dc.getDocument().getString("petName");
                                petNames.add(petName);
                            }
                        }
                        petsAdapter.notifyDataSetChanged();
                    }
                });

        // Add listeners for other pet collections (dogs, birds, rabbits)
        // Example:
        fetchPetsFromCollection("dogs");
        fetchPetsFromCollection("birds");
        fetchPetsFromCollection("rabbits");
    }

    private void fetchPetsFromCollection(String collectionName) {
        db.collection(collectionName)
                .addSnapshotListener((value, error) -> {
                    if (error != null) {
                        // Handle error
                        return;
                    }

                    if (value != null) {
                        for (DocumentChange dc : value.getDocumentChanges()) {
                            if (dc.getType() == DocumentChange.Type.ADDED) {
                                // Assuming "petName" is the field name for pet names in Firestore
                                String petName = dc.getDocument().getString("petName");
                                petNames.add(petName);
                            }
                        }
                        petsAdapter.notifyDataSetChanged();
                    }
                });
    }
}
