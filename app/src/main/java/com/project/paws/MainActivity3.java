package com.project.paws;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class MainActivity3 extends AppCompatActivity {

    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private TextInputEditText email, password, c_password, name, age, gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main4);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        email = findViewById(R.id.email);
        password = findViewById(R.id.password);
        c_password = findViewById(R.id.c_password);
        name = findViewById(R.id.name);
        age = findViewById(R.id.age);
        gender = findViewById(R.id.gender);
        AppCompatButton btn_register = findViewById(R.id.btn_register);
        TextView loginNow = findViewById(R.id.loginNow);

        btn_register.setOnClickListener(v -> registerUser());

        loginNow.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity3.this, alreadyuser.class);
            startActivity(intent);
            finish();
        });
    }

    private void registerUser() {
        String emailTxt = Objects.requireNonNull(email.getText()).toString();
        String passwordTxt = Objects.requireNonNull(password.getText()).toString();
        String cPasswordTxt = Objects.requireNonNull(c_password.getText()).toString();
        String nameTxt = Objects.requireNonNull(name.getText()).toString();
        String ageTxt = Objects.requireNonNull(age.getText()).toString();
        String genderTxt = Objects.requireNonNull(gender.getText()).toString();

        if (!passwordTxt.equals(cPasswordTxt)) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return;
        }

        auth.createUserWithEmailAndPassword(emailTxt, passwordTxt).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                FirebaseUser firebaseUser = auth.getCurrentUser();
                if (firebaseUser != null) {
                    UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                            .setDisplayName(nameTxt)
                            .build();

                    firebaseUser.updateProfile(profileUpdates).addOnCompleteListener(profileTask -> {
                        if (profileTask.isSuccessful()) {
                            Map<String, Object> user = new HashMap<>();
                            user.put("name", nameTxt);
                            user.put("age", ageTxt);
                            user.put("gender", genderTxt);
                            db.collection("users").document(firebaseUser.getUid())
                                    .set(user)
                                    .addOnSuccessListener(aVoid -> {
                                        Toast.makeText(MainActivity3.this, "User registered successfully", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(MainActivity3.this, alreadyuser.class);
                                        startActivity(intent);
                                        finish();
                                    })
                                    .addOnFailureListener(e -> Toast.makeText(MainActivity3.this, "Registration failed", Toast.LENGTH_SHORT).show());
                        } else {
                            Toast.makeText(MainActivity3.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            } else {
                Toast.makeText(MainActivity3.this, "Registration failed", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
