package com.project.paws;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity2 extends AppCompatActivity {
    private Button btnGoto;
    private TextView signin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main2);

        btnGoto = findViewById(R.id.btnGoto);
        signin  = findViewById(R.id.signin);

        btnGoto.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity2.this,MainActivity3.class);
                startActivity(intent);
            }
        });
        signin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v){
                Intent intent = new Intent(MainActivity2.this, alreadyuser.class);
                startActivity(intent);
            }
        });

    }
}