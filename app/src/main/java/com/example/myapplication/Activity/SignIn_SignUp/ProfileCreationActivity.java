package com.example.myapplication.Activity.SignIn_SignUp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.Activity.CustomerSide.CustomerHomepageActivity;
import com.example.myapplication.Activity.ProfessionalSide.ProfessionalHomePg_Activity;
import com.example.myapplication.R;
import com.example.myapplication.Model.UserModel;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import io.grpc.Context;

public class ProfileCreationActivity extends AppCompatActivity {

    EditText editTextFName, editTextLName, editTextPhNo, editTextAddress;
    Button buttonNext;
    ProgressBar progressBar;
    FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile_creation);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        editTextFName = findViewById(R.id.profile_FName);
        editTextLName = findViewById(R.id.profile_LName);
        editTextPhNo = findViewById(R.id.profile_phNo);
        editTextAddress = findViewById(R.id.profile_Address);
        buttonNext = findViewById(R.id.profile_button);
        progressBar = findViewById(R.id.profile_progressBar);
        setInProgressBar(false);


        buttonNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Converting to Info string
                String Fname,Lname, phoneNo,address;

                Fname = String.valueOf(editTextFName.getText());
                Lname = String.valueOf(editTextLName.getText());
                phoneNo = String.valueOf(editTextPhNo.getText());
                address = String.valueOf(editTextAddress.getText());


                if(TextUtils.isEmpty(Fname) || TextUtils.isEmpty(Lname) || TextUtils.isEmpty(phoneNo) || TextUtils.isEmpty(address)){
                    setInProgressBar(false);
                    Toast.makeText(ProfileCreationActivity.this, "Fill all the fields", Toast.LENGTH_SHORT).show();
                }else{

                    UserModel user = getIntent().getParcelableExtra("user");

                    user.setFirstName(Fname);
                    user.setLastName(Lname);
                    user.setPhoneNumber(phoneNo);
                    user.setAddress(address);

                    Map<String, Object> updatedData = new HashMap<>();
                    updatedData.put("firstName",user.getFirstName());
                    updatedData.put("lastName",user.getLastName());
                    updatedData.put("phoneNumber",user.getPhoneNumber());
                    updatedData.put("address",user.getAddress());

                    setInProgressBar(true);
                    db = FirebaseFirestore.getInstance();
                    DocumentReference userRef;
                    if(user.getUserType().equals("Customer"))
                        userRef = db.collection("customer").document(user.getUser_id());
                    else
                        userRef = db.collection("professional").document(user.getUser_id());
                    // Update the document with the new data
                    userRef.update(updatedData)
                            .addOnSuccessListener(aVoid -> {
                                setInProgressBar(false);
                                // Update successful
                                Toast.makeText(ProfileCreationActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                                Toast.makeText(ProfileCreationActivity.this, "SignIn Please", Toast.LENGTH_SHORT).show();
                                    finish();
                                    Intent i = new Intent(ProfileCreationActivity.this, Signin_Activity.class);
                                    startActivity(i);
                            }).addOnFailureListener(e -> {
                                // Handle error
                                Toast.makeText(ProfileCreationActivity.this, "Error updating profile: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    // If profile Creation fails , Display message to user
                                    setInProgressBar(false);
                                    Log.w("user Creation failed", "Create user with email: failed");
                                    Toast.makeText(ProfileCreationActivity.this, "Sign-up failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                }
            }
        });

    }
    void setInProgressBar(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            buttonNext.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            buttonNext.setVisibility(View.VISIBLE);
        }
    }
}