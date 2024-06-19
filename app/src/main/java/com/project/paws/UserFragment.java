package com.project.paws;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserFragment extends Fragment {

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private TextView userName, userAge, userGender;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_user, container, false);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        userName = view.findViewById(R.id.userName);
        userAge = view.findViewById(R.id.userAge);
        userGender = view.findViewById(R.id.userGender);
        ImageView userPhoto = view.findViewById(R.id.userPhoto);
        Button update = view.findViewById(R.id.update);
        Button logout = view.findViewById(R.id.logout);

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

        return view;
    }

    private void loadUserData() {
        if (auth.getCurrentUser() != null) {
            String userId = auth.getCurrentUser().getUid();
            db.collection("users").document(userId).get().addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
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
}
