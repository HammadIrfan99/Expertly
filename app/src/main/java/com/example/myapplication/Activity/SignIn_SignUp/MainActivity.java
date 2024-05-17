package com.example.myapplication.Activity.SignIn_SignUp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.Activity.CustomerSide.CusProfessionalProfile;
import com.example.myapplication.Activity.CustomerSide.CustomerHomepageActivity;
import com.example.myapplication.Fragment.CustomerSide.CusProfileFragment;
import com.example.myapplication.R;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        ProgressBar progressbar;
        progressbar = findViewById(R.id.main_progressBar);
        progressbar.setVisibility(View.VISIBLE);
        // Handler is used to run onCreate Method and the runnable method in parallel
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent i = new Intent(MainActivity.this, Signin_Activity.class);
                startActivity(i);
                finish();
                progressbar.setVisibility(View.GONE);

            }
        }, 2000);

    }
}