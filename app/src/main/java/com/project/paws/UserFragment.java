package com.project.paws;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Environment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class UserFragment extends Fragment {

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private TextView userName, userAge, userGender;
    private Button downloadButton;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        userName = view.findViewById(R.id.userName);
        userAge = view.findViewById(R.id.userAge);
        userGender = view.findViewById(R.id.userGender);
        downloadButton = view.findViewById(R.id.download);
        Button logout = view.findViewById(R.id.logout);
        Button update = view.findViewById(R.id.update);
        Button feedbackButton = view.findViewById(R.id.feedback);

        loadUserData();

        logout.setOnClickListener(v -> {
            auth.signOut();
            Intent intent = new Intent(getActivity(), alreadyuser.class);
            startActivity(intent);
            getActivity().finish();
        });

        update.setOnClickListener(v -> {
            // Replacing the fragment instead of starting a new activity
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame, new UpdateProfile())
                    .addToBackStack(null)
                    .commit();
        });

        feedbackButton.setOnClickListener(v -> {
            // Navigate to FeedbackFragment
            requireActivity().getSupportFragmentManager().beginTransaction()
                    .replace(R.id.frame, new feedback())
                    .addToBackStack(null)
                    .commit();
        });

        downloadButton.setOnClickListener(v -> {
            saveUserDataAsFile();
        });

        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Permission granted
            } else {
                // Permission denied
                Toast.makeText(getContext(), "Storage permission is required to save data", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void loadUserData() {
        if (auth.getCurrentUser() != null) {
            String userId = auth.getCurrentUser().getUid();
            db.collection("users").document(userId).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        String name = document.getString("name");
                        String age = document.getString("age");
                        String gender = document.getString("gender");

                        userName.setText("Name: " + name);
                        userAge.setText("Age: " + age);
                        userGender.setText("Gender: " + gender);
                    } else {
                        Toast.makeText(getActivity(), "User data not found", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Toast.makeText(getActivity(), "Failed to load user data", Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

    private void saveUserDataAsFile() {
        String nameText = userName.getText().toString();
        String ageText = userAge.getText().toString();
        String genderText = userGender.getText().toString();

        String userData = "Name: " + nameText + "\n" +
                "Age: " + ageText + "\n" +
                "Gender: " + genderText;

        File directory = new File(Environment.getExternalStorageDirectory(), "UserData");
        if (!directory.exists()) {
            directory.mkdirs();
        }

        File file = new File(directory, "userdata.txt");
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(userData.getBytes());
            Toast.makeText(getActivity(), "Data saved as file", Toast.LENGTH_SHORT).show();
        } catch (IOException e) {
            e.printStackTrace();
            Toast.makeText(getActivity(), "Failed to save data as file", Toast.LENGTH_SHORT).show();
        }
    }
}
