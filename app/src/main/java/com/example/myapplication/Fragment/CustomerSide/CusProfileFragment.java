package com.example.myapplication.Fragment.CustomerSide;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.myapplication.Activity.CustomerSide.CustomerHomepageActivity;
import com.example.myapplication.Activity.SignIn_SignUp.Signin_Activity;
import com.example.myapplication.Model.Customer;
import com.example.myapplication.Model.UserModel;
import com.example.myapplication.R;
import com.example.myapplication.Utils.FirebaseUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthEmailException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

public class CusProfileFragment extends Fragment {

    ImageView profilePic;
    EditText username;
    EditText phoneNumber;
    EditText password;
    EditText address;
    ProgressBar progressBar;
    String newUsername;
    String newPassword;
    String newPhoneNumber;
    String newAddress;

    Button profileUpdateButton;
    Button logoutButton;



    boolean isProfileUpdateButtonPressed = false;


    public CusProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_customer_profile, container, false);

        progressBar = view.findViewById(R.id.cusProfile_progressBar);
        profilePic = view.findViewById(R.id.cusProfile_Pic);
        username = view.findViewById(R.id.cusProfile_username);
        phoneNumber = view.findViewById(R.id.cusProfile_phNo);
        password = view.findViewById(R.id.cusProfile_password);
        address = view.findViewById(R.id.cusProfile_address);
        profileUpdateButton = view.findViewById(R.id.cusProfile_updateProfile_button);
        logoutButton = view.findViewById(R.id.cusProfile_logout_button);

        getUserData();
        setInProgressBar(false);

        profileUpdateButton.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                isProfileUpdateButtonPressed = true;
                updateProfile();
            }
        } );

        logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseUtils.logout();
                Intent intent = new Intent(getContext(), Signin_Activity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);

            }
        });
        return view;
    }

    void updateProfile(){
        setInProgressBar(true);
        newUsername = username.getText().toString();
        newPassword = password.getText().toString();
        newPhoneNumber = phoneNumber.getText().toString();
        newAddress = address.getText().toString();
        if(newUsername.isEmpty() || newPassword.isEmpty() || newAddress.isEmpty() || newPhoneNumber.isEmpty()){
            setInProgressBar(false);
            Toast.makeText(getActivity(), "Fill all the fields", Toast.LENGTH_SHORT).show();
        } else if (newUsername.length() < 3) {
            setInProgressBar(false);
            Toast.makeText(getActivity(), "Username must have at least 3 characters", Toast.LENGTH_SHORT).show();
        } else if (newPassword.length() < 6 ) {
            setInProgressBar(false);
            Toast.makeText(getActivity(), "Password must have at least 6 characters", Toast.LENGTH_SHORT).show();
        }else if (isProfileUpdateButtonPressed) {
            username.setText(newUsername);
            password.setText(newPassword);
            phoneNumber.setText(newPhoneNumber);
            address.setText(newAddress);

            updateToFireStore();
        }

    }

    void updateToFireStore(){
        FirebaseUser currentUser = FirebaseAuth.getInstance().getCurrentUser();
        if(currentUser != null){
            String uid = currentUser.getUid();
            FirebaseFirestore db = FirebaseFirestore.getInstance();

            // First update password and then profile if password is updated successfully
            currentUser.updatePassword(newPassword)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d("Password Updation", "User password updated successfully");
                                // Create a new HashMap to store updated data
                                Map<String, Object> updatedData = new HashMap<>();
                                updatedData.put("username", newUsername);
                                updatedData.put("password", newPassword);
                                updatedData.put("phoneNumber", newPhoneNumber);
                                updatedData.put("address", newAddress);

                                // Update Firestore document in the appropriate collection
                                db.collection("customer").document(uid)
                                        .update(updatedData)
                                        .addOnSuccessListener(new OnSuccessListener<Void>() {
                                            @Override
                                            public void onSuccess(Void aVoid) {
                                                setInProgressBar(false);
                                                // Data updated successfully
                                                Toast.makeText(getActivity(), "Profile updated successfully", Toast.LENGTH_SHORT).show();
                                                isProfileUpdateButtonPressed = false;
                                            }
                                        })
                                        .addOnFailureListener(new OnFailureListener() {
                                            @Override
                                            public void onFailure(@NonNull Exception e) {
                                                // Failed to update data
                                                Log.e("Data Updation", "Error updating document", e);
                                                Toast.makeText(getActivity(), "Failed to update profile", Toast.LENGTH_SHORT).show();
                                            }
                                        });
                            } else {
                                Log.e("Password Updation", "Error updating user password", task.getException());
                                Toast.makeText(getActivity(), "Failed to update profile", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }
    }


    void getUserData(){
        Context content = requireContext();
            SharedPreferences sharedPreferences = content.getSharedPreferences("userData", Context.MODE_PRIVATE);
            username.setText(sharedPreferences.getString("username","username"));
            password.setText(sharedPreferences.getString("password","password"));
            phoneNumber.setText(sharedPreferences.getString("phoneNumber","phoneNumber"));
            address.setText(sharedPreferences.getString("address","address"));
    }

    void setInProgressBar(boolean inProgress){
        if(inProgress){
            progressBar.setVisibility(View.VISIBLE);
            profileUpdateButton.setVisibility(View.GONE);
        }else{
            progressBar.setVisibility(View.GONE);
            profileUpdateButton.setVisibility(View.VISIBLE);
        }
    }

//    public void logoutUser(View view) {
//        FirebaseUtils.logout();
//        Intent intent = new Intent(getContext(), Signin_Activity.class);
//        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
//        startActivity(intent);
//    }

}
