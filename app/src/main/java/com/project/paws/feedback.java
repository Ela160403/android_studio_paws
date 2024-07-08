package com.project.paws;

import android.os.Bundle;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class feedback extends Fragment {

    private EditText textFeedback;
    private Button btnSubmit;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    public feedback() {
        // Required empty public constructor
    }

    public static feedback newInstance() {
        return new feedback();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_feedback, container, false);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        textFeedback = view.findViewById(R.id.edit_feedback);
        btnSubmit = view.findViewById(R.id.btn_submit);
        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                submitFeedback();
            }
        });

        // Load existing feedback from Firestore
        loadFeedback();

        return view;
    }

    private void loadFeedback() {
        // Load feedback from Firestore and display in the EditText
        String userId = auth.getCurrentUser().getUid();
        db.collection("feedback").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        String feedback = document.getString("feedback");
                        textFeedback.setText(feedback);
                    }
                } else {
                    Toast.makeText(requireContext(), "Failed to load feedback", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void submitFeedback() {
        // Get user's ID, name, and feedback text
        String userId = auth.getCurrentUser().getUid();
        String userName = auth.getCurrentUser().getDisplayName(); // Assuming you have set up user display name
        String feedbackText = textFeedback.getText().toString().trim();

        // Check if feedback text is not empty
        if (feedbackText.isEmpty()) {
            Toast.makeText(requireContext(), "Please enter your feedback", Toast.LENGTH_SHORT).show();
            return;
        }

        // Create a feedback object
        Feedback feedback = new Feedback(userName, feedbackText);

        // Save feedback to Firestore in a separate collection
        db.collection("all_feedback").add(feedback)
                .addOnCompleteListener(new OnCompleteListener<DocumentReference>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentReference> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(requireContext(), "Feedback submitted successfully", Toast.LENGTH_SHORT).show();
                            // Update or replace existing feedback for the user
                            updateFeedbackForUser(userId, feedbackText);
                        } else {
                            Toast.makeText(requireContext(), "Failed to submit feedback", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    private void updateFeedbackForUser(String userId, String feedbackText) {
        // Update or replace existing feedback for the user in a separate 'user_feedback' collection
        Map<String, Object> userFeedback = new HashMap<>();
        userFeedback.put("feedback", feedbackText);

        db.collection("user_feedback").document(userId).set(userFeedback)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Toast.makeText(requireContext(), "User feedback updated successfully", Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(requireContext(), "Failed to update user feedback", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    // Feedback model class
    public static class Feedback {
        private String userName;
        private String feedback;

        public Feedback() {
            // Default constructor required for Firestore
        }

        public Feedback(String userName, String feedback) {
            this.userName = userName;
            this.feedback = feedback;
        }

        public String getUserName() {
            return userName;
        }

        public String getFeedback() {
            return feedback;
        }
    }
}
