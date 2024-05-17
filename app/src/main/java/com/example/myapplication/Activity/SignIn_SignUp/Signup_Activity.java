package com.example.myapplication.Activity.SignIn_SignUp;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.Model.Customer;
import com.example.myapplication.Model.Professional;
import com.example.myapplication.R;
import com.example.myapplication.Model.UserModel;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;


import java.util.ArrayList;


public class Signup_Activity extends AppCompatActivity {

    EditText editTextusername, editTextemail, editTextpassword, editTextrepeat_password;
    Button buttonregister;
    Spinner spinner;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    FirebaseFirestore db;

//    // Check if user is already logged in
//    public void onStart(){
//        super.onStart();
//        FirebaseUser user = mAuth.getCurrentUser();
//        if(user != null){
//            startActivity(new Intent(Signup_Activity.this, HomepageActivity.class));
//            finish();
//        }
//    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signup);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Getting all views by their id's
        editTextemail = findViewById(R.id.signup_email_textview);
        editTextusername = findViewById(R.id.signup_username_textview);
        editTextpassword = findViewById(R.id.signup_password_password);
        editTextrepeat_password = findViewById(R.id.signup_repeat_password_password);
        buttonregister = findViewById(R.id.signup_register_button);
        progressBar = findViewById(R.id.Signup_progressBar);
        setInProgressBar(false);
        //Implementation for spinner
        spinner = findViewById(R.id.signup_spinner);

        // Add options in spinner
        ArrayList <String> spinnerList = new ArrayList<>();
        spinnerList.add("Professional");
        spinnerList.add("Customer");
        ArrayAdapter adapter = new ArrayAdapter(this, android.R.layout.simple_spinner_item, spinnerList);
        adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                String item = adapterView.getItemAtPosition(position).toString();
            }
            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        // Initializing mAuth to further proceed with it
        mAuth = FirebaseAuth.getInstance();


        // Implementation when register button is clicked
        buttonregister.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                setInProgressBar(true);
            //  progressbar.setVisibility(View.VISIBLE);
                String email, username, password, repeat_password, userType;
                email = String.valueOf(editTextemail.getText());
                username = String.valueOf(editTextusername.getText());
                password = String.valueOf(editTextpassword.getText());
                repeat_password = String.valueOf(editTextrepeat_password.getText());
                userType = spinner.getSelectedItem().toString();


                // Checking for all fields are filled
                if (TextUtils.isEmpty(email) || TextUtils.isEmpty(username) || username.length() < 3 || TextUtils.isEmpty(password) || TextUtils.isEmpty(repeat_password) || spinner.isSelected()) {
                    setInProgressBar(false);
                    Toast.makeText(Signup_Activity.this, "Fill all the fields", Toast.LENGTH_SHORT).show();
                } else if (password.length() < 6) {
                    setInProgressBar(false);
                    Toast.makeText(Signup_Activity.this, "passward must be atleast 6 characters long", Toast.LENGTH_SHORT).show();
                } else if (!password.equals(repeat_password)) {
                    setInProgressBar(false);
                    Toast.makeText(Signup_Activity.this, "\"repeatPassward\" must match \"password\"", Toast.LENGTH_SHORT).show();
                } else {
                    mAuth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(Signup_Activity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {
                                setInProgressBar(true);

                                // user_id in FireStore documents same as Uid in Authentication documents of each user
                                String user_id = mAuth.getCurrentUser().getUid();
                                //Implementation of firebase db , adding data to firestore
                                db = FirebaseFirestore.getInstance();
                                DocumentReference newUserRef;

                                if(userType.equals("Customer")){
                                    newUserRef = db.collection("customer").document(user_id);
                                }
                                else{
                                    newUserRef = db.collection("professional").document(user_id);
                                }

                                //Initializing UserModel object with available attributes. And storing data in firebase
                                //Initializing UserModel user
                                UserModel user;

                                if(userType.equals("Customer")){
                                    user = new Customer(user_id, username, email, password, userType);
                                }else{
                                    user = new Professional(user_id, username, email, password, userType);
                                }
                                newUserRef.set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                                    @Override
                                    public void onSuccess(Void unused) {
                                        setInProgressBar(false);
                                        Log.d("user Creation", "Create user with email : success");
                                        Toast.makeText(Signup_Activity.this, "Sign-up successful", Toast.LENGTH_SHORT).show();
                                        Toast.makeText(Signup_Activity.this, "User Created Successfullly.", Toast.LENGTH_SHORT).show();
                                        Log.d("Data Updated","Data updated in firestore : successful");
                                        finish();
                                        Intent i = new Intent(Signup_Activity.this, ProfileCreationActivity.class);
                                        i.putExtra("user",user);
                                        startActivity(i);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        setInProgressBar(false);
                                        Toast.makeText(Signup_Activity.this, "Error: User not Created "+e.getMessage(), Toast.LENGTH_SHORT).show();
                                        Log.d("Data Updated","Data updated in firestore : Failed");
                                    }
                                });

                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            // If signup fails , Display message to user
                            setInProgressBar(false);
                            Log.w("user Creation failed", "Create user with email: failed");
                            Toast.makeText(Signup_Activity.this, "Sign-up failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }

    public void goSigninActivity(View v){
        //Go to SignIn Activity
        Intent i = new Intent(this, Signin_Activity.class);
        startActivity(i);
    }

    void setInProgressBar(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            buttonregister.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            buttonregister.setVisibility(View.VISIBLE);
        }
    }

}




