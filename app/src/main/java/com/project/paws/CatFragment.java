package com.project.paws;
import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class CatFragment extends Fragment {

    private static final int PICK_IMAGE = 1;
    private static final int TAKE_PHOTO = 2;
    private Uri imageUri;
    private EditText breedEditText, heightEditText, weightEditText, ageEditText, colorEditText, petNameEditText, vaccinationEditText, dosagesEditText;
    private Button choosePhotoButton, takePhotoButton, saveButton;
    private FirebaseFirestore db;

    public CatFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_cat, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        breedEditText = view.findViewById(R.id.breedEditText);
        heightEditText = view.findViewById(R.id.heightEditText);
        weightEditText = view.findViewById(R.id.weightEditText);
        ageEditText = view.findViewById(R.id.ageEditText);
        colorEditText = view.findViewById(R.id.colorEditText);
        petNameEditText = view.findViewById(R.id.petNameEditText);
        vaccinationEditText = view.findViewById(R.id.vaccinationEditText);
        dosagesEditText = view.findViewById(R.id.dosagesEditText);
        choosePhotoButton = view.findViewById(R.id.choosePhotoButton);
        takePhotoButton = view.findViewById(R.id.takePhotoButton);
        saveButton = view.findViewById(R.id.saveButton);

        db = FirebaseFirestore.getInstance();

        choosePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                choosePhoto();
            }
        });

        takePhotoButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                takePhoto();
            }
        });

        saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveCatDetails();
            }
        });
    }

    private void choosePhoto() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE);
    }

    private void takePhoto() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (intent.resolveActivity(getActivity().getPackageManager()) != null) {
            startActivityForResult(intent, TAKE_PHOTO);
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == PICK_IMAGE) {
                imageUri = data.getData();
            } else if (requestCode == TAKE_PHOTO) {
                imageUri = data.getData();
            }
        }
    }

    private void saveCatDetails() {
        String breed = breedEditText.getText().toString();
        String height = heightEditText.getText().toString();
        String weight = weightEditText.getText().toString();
        String age = ageEditText.getText().toString();
        String color = colorEditText.getText().toString();
        String petName = petNameEditText.getText().toString();
        String vaccination = vaccinationEditText.getText().toString();
        String dosages = dosagesEditText.getText().toString();

        Map<String, Object> catDetails = new HashMap<>();
        catDetails.put("breed", breed);
        catDetails.put("height", height);
        catDetails.put("weight", weight);
        catDetails.put("age", age);
        catDetails.put("color", color);
        catDetails.put("petName", petName);
        catDetails.put("vaccination", vaccination);
        catDetails.put("dosages", dosages);
        if (imageUri != null) {
            catDetails.put("imageUri", imageUri.toString());
        }

        db.collection("cats")
                .add(catDetails)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Cat details saved", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getActivity(), "Error saving cat details", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
