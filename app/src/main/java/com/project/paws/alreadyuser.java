package com.project.paws;
import android.content.Intent;
import android.text.TextUtils;
import android.view.View;
import android.widget.TextView;
import android.os.Bundle;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class alreadyuser extends AppCompatActivity {
    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_alreadyuser);
        TextInputEditText email = findViewById(R.id.emailId);
        TextInputEditText password = findViewById(R.id.Password);
        AppCompatButton button = findViewById(R.id.getStartedButton);

        mAuth = FirebaseAuth.getInstance();
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String emailtxt = String.valueOf(email.getText());
                String passwordtxt = String.valueOf(password.getText());

                if(TextUtils.isEmpty(emailtxt) | TextUtils.isEmpty(passwordtxt)){
                    Toast.makeText(alreadyuser.this, "Please enter required fields", Toast.LENGTH_SHORT).show();

                }else {
                    mAuth.signInWithEmailAndPassword(emailtxt, passwordtxt)
                            .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                                @Override
                                public void onComplete(@NonNull Task<AuthResult> task) {
                                    if(task.isSuccessful()){
                                        Toast.makeText(alreadyuser.this, "Login succesfully", Toast.LENGTH_SHORT).show();
                                        Intent intent = new Intent(getApplicationContext(), Homepag.class);
                                        startActivity(intent);
                                        finish();
                                    }else {
                                        Toast.makeText(alreadyuser.this, "Login failed", Toast.LENGTH_SHORT).show();
                                    }
                                }
                            });
                }


            }
        });



    }
}