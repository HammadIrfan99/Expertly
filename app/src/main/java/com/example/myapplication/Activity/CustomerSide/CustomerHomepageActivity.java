package com.example.myapplication.Activity.CustomerSide;

import android.content.Intent;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.Fragment.CustomerSide.CusNotificationFragment;
import com.example.myapplication.Fragment.CustomerSide.CusProfessionalFragment;
import com.example.myapplication.Fragment.CustomerSide.CusProfileFragment;
import com.example.myapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class CustomerHomepageActivity extends AppCompatActivity {

   BottomNavigationView bottomNavigationView;
    ImageButton searchButton;
    EditText searchInput;
    CusProfessionalFragment professionalFragment;
    CusNotificationFragment notificationFragment;
    CusProfileFragment profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer_homepage);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        professionalFragment = new CusProfessionalFragment();
        profileFragment = new CusProfileFragment();
        notificationFragment = new CusNotificationFragment();
        searchButton = findViewById(R.id.cusHomePg_search);
        bottomNavigationView = findViewById(R.id.cusHomePg_bottomNavigation);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId()== R.id.cusHomePgMenu_professional){
                    getSupportFragmentManager().beginTransaction().replace(R.id.main, professionalFragment).commit();
                } else if (menuItem.getItemId()== R.id.cusHomePgMenu_profile) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.main, profileFragment).commit();
                } else if (menuItem.getItemId()== R.id.cusHomePgMenu_notifications) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.main, notificationFragment).commit();
                }
                return true;
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.cusHomePgMenu_professional);

        searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
            startActivity(new Intent(CustomerHomepageActivity.this, CustomerHomePg_Search.class));
            }
        });

    }
}