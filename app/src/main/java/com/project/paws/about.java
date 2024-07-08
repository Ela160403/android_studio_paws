package com.project.paws;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class about extends Fragment {

    private TextView textAboutFeedback;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    public about() {
        // Required empty public constructor
    }

    public static about newInstance() {
        return new about();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_about, container, false);

        textAboutFeedback = view.findViewById(R.id.text_about_feedback);

        // Load feedback from Firestore
        loadFeedback();

        return view;
    }

    private void loadFeedback() {
        // Load feedback from Firestore and display in the TextView
        String userId = auth.getCurrentUser().getUid();
        db.collection("feedback").document(userId).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        String feedback = document.getString("feedback");
                        textAboutFeedback.setText(feedback);
                    } else {
                        textAboutFeedback.setText("No feedback available");
                    }
                } else {
                    textAboutFeedback.setText("Failed to load feedback");
                }
            }
        });
    }
}
