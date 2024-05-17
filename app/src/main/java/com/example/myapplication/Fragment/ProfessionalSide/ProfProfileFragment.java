package com.example.myapplication.Fragment.ProfessionalSide;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.example.myapplication.Activity.SignIn_SignUp.Signin_Activity;
import com.example.myapplication.R;
import com.example.myapplication.Utils.FirebaseUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class ProfProfileFragment extends Fragment {

    ImageView profilePic;
    EditText username;
    EditText phoneNumber;
    EditText password;
    EditText address;
    EditText skill;
    EditText rating;
    EditText booking;
    String newUsername;
    String newPassword;
    String newPhoneNumber;
    String newAddress;
    String newSkill;
    Button profileUpdateButton;
    Button logoutButton;
    ProgressBar progressBar;

    boolean isProfileUpdateButtonPressed = false;


    public ProfProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view =  inflater.inflate(R.layout.fragment_professional_profile, container, false);

        profilePic = view.findViewById(R.id.profProfile_Pic);
        username = view.findViewById(R.id.profProfile_username);
        phoneNumber = view.findViewById(R.id.profProfile_phNo);
        password = view.findViewById(R.id.profProfile_password);
        address = view.findViewById(R.id.profProfile_address);
        skill = view.findViewById(R.id.profProfile_skill);
        rating = view.findViewById(R.id.profProfile_rating);
        booking = view.findViewById(R.id.profProfile_booking);
        profileUpdateButton = view.findViewById(R.id.profProfile_updateProfile_button);
        logoutButton = view.findViewById(R.id.profProfile_logout_button);
        progressBar = view.findViewById(R.id.profProfile_progressBar);

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
        newSkill = skill.getText().toString();
        if(newUsername.isEmpty() || newPassword.isEmpty() || newAddress.isEmpty() || newPhoneNumber.isEmpty() || newSkill.isEmpty()){
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
            skill.setText(newSkill);

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
                                updatedData.put("skill",newSkill);

                                // Update Firestore document in the appropriate collection
                                db.collection("professional").document(uid)
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
        skill.setText(sharedPreferences.getString("skill","skill"));
        rating.setText(sharedPreferences.getString("rating","rating"));
        booking.setText(sharedPreferences.getString("booking","booking"));

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

