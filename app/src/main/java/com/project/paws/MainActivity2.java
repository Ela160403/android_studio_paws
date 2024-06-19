package com.project.paws;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity2 extends AppCompatActivity {
    private ImageButton btnGoto;
    private TextView signin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);

        btnGoto = findViewById(R.id.btnGoto);
        signin = findViewById(R.id.signin);

        btnGoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Load the animation
                Animation rotateAnimation = AnimationUtils.loadAnimation(MainActivity2.this, R.anim.rotate);
                // Start the animation
                btnGoto.startAnimation(rotateAnimation);

                // Add a delay to start the new activity after the animation ends
                btnGoto.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        Intent intent = new Intent(MainActivity2.this, MainActivity3.class);
                        startActivity(intent);
                    }
                }, 2000); // 2000 milliseconds = 2 seconds
            }
        });

        signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity2.this, alreadyuser.class);
                startActivity(intent);
            }
        });
    }
}
