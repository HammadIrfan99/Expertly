package com.example.myapplication.Activity.SignIn_SignUp;

import android.content.Intent;
import android.content.SharedPreferences;
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

import com.example.myapplication.Activity.CustomerSide.CustomerHomepageActivity;
import com.example.myapplication.Activity.ProfessionalSide.ProfessionalHomePg_Activity;
import com.example.myapplication.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class Signin_Activity extends AppCompatActivity {

    EditText editTextemail, editTextpassword;
    Button buttonLogin;
    Spinner spinner;
    ProgressBar progressBar;
    FirebaseAuth mAuth;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

//    // Check if user is already logged in. Try to make use of Flag in SharedPreferences to determine whether user is customer or professional
//    public void onStart(){
//        super.onStart();
//        FirebaseUser user = mAuth.getCurrentUser();
//        if(user != null){
//            startActivity(new Intent(Signin_Activity.this, HomepageActivity.class));
//            finish();
//        }
//    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_signin);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        mAuth = FirebaseAuth.getInstance();
        editTextemail = findViewById(R.id.signin_email_textview);
        editTextpassword = findViewById(R.id.signin_password_password);
        buttonLogin = findViewById(R.id.signin_login_button);
        spinner = findViewById(R.id.signin_spinner);
        progressBar = findViewById(R.id.Signin_progressBar);
        setInProgressBar(false);
        //Implementation for spinner

        // Add options in spinner
        ArrayList<String> spinnerList = new ArrayList<>();
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

        buttonLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String email = String.valueOf(editTextemail.getText().toString());
                String password = String.valueOf(editTextpassword.getText().toString());

                if(TextUtils.isEmpty(email) || TextUtils.isEmpty(password)){
                    setInProgressBar(false);
                    Toast.makeText(Signin_Activity.this, "Fill all the fields", Toast.LENGTH_SHORT).show();
                }else {

                    mAuth.signInWithEmailAndPassword(email, password).addOnCompleteListener(Signin_Activity.this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                setInProgressBar(true);
                                // Here Check for the user in professional and customer collection separately
                                FirebaseUser user = mAuth.getCurrentUser();
                                String userType = spinner.getSelectedItem().toString();
                                assert user != null;
                                String user_id = user.getUid();
                                DocumentReference userRef = null;



                                if (userType.equals("Customer"))
                                    userRef = db.collection("customer").document(user_id);
                                else if (userType.equals("Professional"))
                                    userRef = db.collection("professional").document(user_id);

                                if (userRef != null) {

                                    userRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                                        @Override
                                        public void onSuccess(DocumentSnapshot document) {
                                            Log.d("onSuccess method","Done");
                                            if (document.exists()) {

                                                //Storing Data in Shared Preferences to shoe in Profile section
                                                SharedPreferences sharedPreferences = getSharedPreferences("userData",MODE_PRIVATE);
                                                SharedPreferences.Editor editor = sharedPreferences.edit();

                                                editor.putString("firstName",document.getString("firstName"));
                                                editor.putString("lastName",document.getString("lastName"));
                                                editor.putString("userType",document.getString("userrType"));
                                                editor.putString("userID",document.getString("user_id"));
                                                editor.putString("username",document.getString("username"));
                                                editor.putString("password",document.getString("password"));
                                                editor.putString("phoneNumber",document.getString("phoneNumber"));
                                                editor.putString("address",document.getString("address"));
                                                editor.putString("email",document.getString("email"));
                                                editor.putString("rating",document.getString("rating"));
                                                editor.putString("skill",document.getString("skill"));
                                                editor.putString("booking",document.getString("booking"));
                                                editor.apply();

                                                setInProgressBar(false);

                                                Log.d("user login", "user logged in with email:password");
                                                Toast.makeText(Signin_Activity.this, "Sign in successful", Toast.LENGTH_SHORT).show();

                                                // Directing users to their Activities
                                                if (spinner.getSelectedItem().toString().equals("Customer")) {
                                                    finish();
                                                    Intent i = new Intent(Signin_Activity.this, CustomerHomepageActivity.class);
                                                    startActivity(i);
                                                }else if (spinner.getSelectedItem().toString().equals("Professional")) {
                                                    finish();
                                                    Intent i = new Intent(Signin_Activity.this, ProfessionalHomePg_Activity.class);
                                                    startActivity(i);
                                                }

                                            }else{
                                                setInProgressBar(false);
                                                Log.d("user login", "user login failed with email:password");
                                                Toast.makeText(Signin_Activity.this, "UserType or email is incorrect", Toast.LENGTH_SHORT).show();
                                            }
                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            setInProgressBar(false);
                                            Log.d("user login", "user login failed with email:password");
                                            Toast.makeText(Signin_Activity.this, "Sign in failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                } else {
                                    setInProgressBar(false);
                                    Log.d("user login", "user login failed with email:password");
                                    Toast.makeText(Signin_Activity.this, "Error Occurred. User not Found", Toast.LENGTH_SHORT).show();
                                }
                            }
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            setInProgressBar(false);
                            Log.d("user login", "user login failed with email:password");
                            Toast.makeText(Signin_Activity.this, "Sign in failed " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });
    }


    public void goSignupActivity(View view){
        //GO to Signup Activity
        Intent i = new Intent(this, Signup_Activity.class);
        startActivity(i);
    }

    void setInProgressBar(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            buttonLogin.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            buttonLogin.setVisibility(View.VISIBLE);
        }
    }
}