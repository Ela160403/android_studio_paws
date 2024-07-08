package com.project.paws;

import static android.app.PendingIntent.getActivity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.project.paws.Pet;
import com.project.paws.PetListAdapter;
import com.project.paws.R;
import com.project.paws.Rabbit;

import java.util.ArrayList;
import java.util.List;

// PetInfoFragment.java
class PetInfoFragment extends Fragment {

    private ListView listViewPets;
    private List<Pet> petsList;
    private PetListAdapter petListAdapter;
    private FirebaseFirestore db;

    public PetInfoFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_petinfo, container, false);

        listViewPets = view.findViewById(R.id.listViewPets);
        petsList = new ArrayList<>();
        petListAdapter = new PetListAdapter(getActivity(), R.layout.item_pet_info, petsList);
        listViewPets.setAdapter(petListAdapter);

        db = FirebaseFirestore.getInstance();

        // Load pets from Firestore
        loadPets();

        // Set item click listener for handling pet details
        listViewPets.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Pet selectedPet = petsList.get(position);
                displayPetDetails(selectedPet);
            }
        });

        return view;
    }

    private void loadPets() {
        loadPetsFromCollection("cats");
        loadPetsFromCollection("dogs");
        loadPetsFromCollection("birds");
        loadPetsFromCollection("rabbits");
    }

    private void loadPetsFromCollection(String collectionName) {
        db.collection(collectionName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String name = document.getString("name");
                                String breed = document.getString("breed");
                                String imageUrl = document.getString("imageUrl");

                                Pet pet = null;
                                switch (collectionName) {
                                    case "cats":
                                        pet = new Cat(name, breed, imageUrl);
                                        break;
                                    case "dogs":
                                        pet = new Dog(name, breed, imageUrl);
                                        break;
                                    case "birds":
                                        pet = new Bird(name, breed, imageUrl);
                                        break;
                                    case "rabbits":
                                        pet = new Rabbit(name, breed, imageUrl);
                                        break;
                                }

                                if (pet != null) {
                                    petsList.add(pet);
                                }
                            }
                            petListAdapter.notifyDataSetChanged();
                        } else {
                            Toast.makeText(getActivity(), "Failed to load pets: " + collectionName, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void displayPetDetails(Pet pet) {
        Toast.makeText(getActivity(), "Name: " + pet.getName() + ", Breed: " + pet.getBreed(), Toast.LENGTH_SHORT).show();
        // Implement further actions as needed
    }
}
