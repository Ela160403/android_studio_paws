package com.project.paws;

import android.content.Context;
import android.widget.Toast;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public abstract class Pet {
    private String name;
    private String breed;
    private String imageUrl;

    private List<Pet> petsList;
    private PetListAdapter petListAdapter;
    private FirebaseFirestore db;

    public Pet() {
        petsList = new ArrayList<>();
        db = FirebaseFirestore.getInstance();
    }

    public Pet(String name, String breed, String imageUrl) {
        this.name = name;
        this.breed = breed;
        this.imageUrl = imageUrl;
    }

    public String getName() {
        return name;
    }

    public String getBreed() {
        return breed;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public List<Pet> getPetsList() {
        return petsList;
    }

    public void setPetListAdapter(PetListAdapter petListAdapter) {
        this.petListAdapter = petListAdapter;
    }

    public void loadPetsFromCollection(String collectionName, final Context context) {
        db.collection(collectionName)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            petsList.clear(); // Clear existing list before adding new data
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                String name = document.getString("name");
                                String breed = document.getString("breed");
                                String imageUrl = document.getString("imageUrl");

                                // Determine the type of pet based on collectionName
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
                            if (petListAdapter != null) {
                                petListAdapter.notifyDataSetChanged(); // Notify adapter of data change
                            }
                        } else {
                            Toast.makeText(context, "Failed to load pets: " + collectionName, Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
