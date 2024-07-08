package com.project.paws;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class UpdateProfile extends Fragment {

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private TextInputEditText name, age, gender;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_update_profile, container, false);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        name = view.findViewById(R.id.name);
        age = view.findViewById(R.id.age);
        gender = view.findViewById(R.id.gender);
        view.findViewById(R.id.btn_update).setOnClickListener(v -> updateUserProfile());

        return view;
    }

    private void updateUserProfile() {
        String userId = Objects.requireNonNull(auth.getCurrentUser()).getUid();
        String nameTxt = Objects.requireNonNull(name.getText()).toString();
        String ageTxt = Objects.requireNonNull(age.getText()).toString();
        String genderTxt = Objects.requireNonNull(gender.getText()).toString();

        Map<String, Object> userUpdates = new HashMap<>();
        userUpdates.put("name", nameTxt);
        userUpdates.put("age", ageTxt);
        userUpdates.put("gender", genderTxt);

        db.collection("users").document(userId)
                .update(userUpdates)
                .addOnSuccessListener(aVoid -> {
                    Toast.makeText(requireContext(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    requireActivity().getSupportFragmentManager().popBackStack();
                })
                .addOnFailureListener(e -> Toast.makeText(requireContext(), "Profile update failed", Toast.LENGTH_SHORT).show());
    }
}
