package com.example.myapplication.Activity.ProfessionalSide;

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

import com.example.myapplication.Activity.CustomerSide.CustomerHomePg_Search;
import com.example.myapplication.Activity.CustomerSide.CustomerHomepageActivity;
import com.example.myapplication.Fragment.CustomerSide.CusNotificationFragment;
import com.example.myapplication.Fragment.CustomerSide.CusProfessionalFragment;
import com.example.myapplication.Fragment.CustomerSide.CusProfileFragment;
import com.example.myapplication.Fragment.ProfessionalSide.ProfCustomerFragment;
import com.example.myapplication.Fragment.ProfessionalSide.ProfNotificationFragment;
import com.example.myapplication.Fragment.ProfessionalSide.ProfProfileFragment;
import com.example.myapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class ProfessionalHomePg_Activity extends AppCompatActivity {

    BottomNavigationView bottomNavigationView;
    ProfCustomerFragment customerFragment;
    ProfNotificationFragment notificationFragment;
    ProfProfileFragment profileFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_professional_home_pg);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        customerFragment = new ProfCustomerFragment();
        profileFragment = new ProfProfileFragment();
        notificationFragment = new ProfNotificationFragment();
        bottomNavigationView = findViewById(R.id.profHomePg_bottomNavigation);

        bottomNavigationView.setOnItemSelectedListener(new NavigationBarView.OnItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
                if(menuItem.getItemId()== R.id.profHomePgMenu_professional){
                    getSupportFragmentManager().beginTransaction().replace(R.id.main, customerFragment).commit();
                } else if (menuItem.getItemId()== R.id.profHomePgMenu_profile) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.main, profileFragment).commit();
                } else if (menuItem.getItemId()== R.id.profHomePgMenu_notifications) {
                    getSupportFragmentManager().beginTransaction().replace(R.id.main, notificationFragment).commit();
                }
                return true;
            }
        });

        bottomNavigationView.setSelectedItemId(R.id.cusHomePgMenu_professional);


    }
}