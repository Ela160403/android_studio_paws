package com.project.paws;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.project.paws.databinding.ActivityHomepagBinding;


public class Homepag extends AppCompatActivity {

    ActivityHomepagBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding=ActivityHomepagBinding.inflate(getLayoutInflater());

        setContentView(binding.getRoot());

    binding.bottomNavigationView.setOnItemSelectedListener(new BottomNavigationView.OnItemSelectedListener() {
        public boolean onNavigationItemSelected(MenuItem item) {
            if (item.getItemId() == R.id.home) {
                replaceFragment(new HomeFragment());
                return true;
            } else if (item.getItemId() == R.id.profile) {
                replaceFragment(new UserFragment());
                return true;
            } else if(item.getItemId()==R.id.settings){
                replaceFragment(new SettingsFragment());
                return true;
            }else {
                return false;
            }
        }
    });

    }
    private void replaceFragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(binding.frameLayout.getId(), fragment);
        fragmentTransaction.commit();
    }
}