package com.project.paws;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.Map;

public class BirdFragment extends Fragment {

    private static final int PICK_IMAGE = 1;
    private static final int TAKE_PHOTO = 2;
    private Uri imageUri;
    private ImageView imagePreview;
    private Spinner breedSpinner, ageSpinner, colorSpinner;
    private EditText heightEditText, weightEditText, petNameEditText, vaccinationEditText, dosagesEditText;
    private Button choosePhotoButton, takePhotoButton, saveButton;
    private FirebaseFirestore db;

    public BirdFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bird, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        imagePreview = view.findViewById(R.id.imagePreview);
        breedSpinner = view.findViewById(R.id.breedSpinner);
        heightEditText = view.findViewById(R.id.heightEditText);
        weightEditText = view.findViewById(R.id.weightEditText);
        ageSpinner = view.findViewById(R.id.ageSpinner);
        colorSpinner = view.findViewById(R.id.colorSpinner);
        petNameEditText = view.findViewById(R.id.petNameEditText);
        vaccinationEditText = view.findViewById(R.id.vaccinationEditText);
        dosagesEditText = view.findViewById(R.id.dosagesEditText);
        choosePhotoButton = view.findViewById(R.id.choosePhotoButton);
        takePhotoButton = view.findViewById(R.id.takePhotoButton);
        saveButton = view.findViewById(R.id.saveButton);

        db = FirebaseFirestore.getInstance();

        // Populate Spinners
        ArrayAdapter<CharSequence> breedAdapter = ArrayAdapter.createFromResource(getContext(), R.array.bird_breed_array, android.R.layout.simple_spinner_item);
        breedAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        breedSpinner.setAdapter(breedAdapter);

        ArrayAdapter<CharSequence> ageAdapter = ArrayAdapter.createFromResource(getContext(), R.array.age_array, android.R.layout.simple_spinner_item);
        ageAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        ageSpinner.setAdapter(ageAdapter);

        ArrayAdapter<CharSequence> colorAdapter = ArrayAdapter.createFromResource(getContext(), R.array.color_array, android.R.layout.simple_spinner_item);
        colorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        colorSpinner.setAdapter(colorAdapter);

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
                saveBirdDetails();
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
                if (imageUri != null) {
                    imagePreview.setImageURI(imageUri);
                }
            } else if (requestCode == TAKE_PHOTO) {
                Bundle extras = data.getExtras();
                if (extras != null) {
                    // Retrieve the image captured by the camera
                    Bitmap photo = (Bitmap) extras.get("data");
                    // Convert bitmap to URI and set to imageUri
                    imageUri = getImageUri(getContext(), photo);
                    if (imageUri != null) {
                        imagePreview.setImageURI(imageUri);
                    }
                }
            }
        }
    }

    // Convert Bitmap to Uri
    private Uri getImageUri(Context context, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Title", null);
        return Uri.parse(path);
    }

    private void saveBirdDetails() {
        String breed = breedSpinner.getSelectedItem().toString();
        String height = heightEditText.getText().toString();
        String weight = weightEditText.getText().toString();
        String age = ageSpinner.getSelectedItem().toString();
        String color = colorSpinner.getSelectedItem().toString();
        String petName = petNameEditText.getText().toString();
        String vaccination = vaccinationEditText.getText().toString();
        String dosages = dosagesEditText.getText().toString();

        Map<String, Object> birdDetails = new HashMap<>();
        birdDetails.put("breed", breed);
        birdDetails.put("height", height);
        birdDetails.put("weight", weight);
        birdDetails.put("age", age);
        birdDetails.put("color", color);
        birdDetails.put("petName", petName);
        birdDetails.put("vaccination", vaccination);
        birdDetails.put("dosages", dosages);
        if (imageUri != null) {
            birdDetails.put("imageUri", imageUri.toString());
        }

        db.collection("birds")
                .add(birdDetails)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(getActivity(), "Bird details saved", Toast.LENGTH_SHORT).show();
                            clearFields();
                        } else {
                            Toast.makeText(getActivity(), "Error saving bird details", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void clearFields() {
        breedSpinner.setSelection(0);
        heightEditText.setText("");
        weightEditText.setText("");
        ageSpinner.setSelection(0);
        colorSpinner.setSelection(0);
        petNameEditText.setText("");
        vaccinationEditText.setText("");
        dosagesEditText.setText("");
        // Set your default image here
        imagePreview.setImageResource(R.drawable.bbird);
        imageUri = null;
    }
}
