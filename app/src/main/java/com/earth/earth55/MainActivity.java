package com.earth.earth55;

import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigationView);

        Fragment homeefragment = new HomeeFragment();
        Fragment profilefragment = new profileFragment();
        Fragment settingfragment = new settingFragment();

        setCurrentFragment(homeefragment);

        bottomNavigationView.setOnItemSelectedListener(item -> {
            int id = item.getItemId();

            if (id == R.id.home) {
                setCurrentFragment(homeefragment);
                return true;
            } else if (id == R.id.profile) {
                setCurrentFragment(profilefragment);
                return true;
            } else if (id == R.id.settings) {
            setCurrentFragment(settingfragment);
            return true;
            }
            return false;
        });

    }

    private void setCurrentFragment(Fragment fragment) {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.flFragment, fragment)
                .commit();
    }
}