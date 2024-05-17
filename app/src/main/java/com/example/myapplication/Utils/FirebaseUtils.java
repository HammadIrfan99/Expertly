package com.example.myapplication.Utils;

import android.content.Intent;

import com.example.myapplication.Model.Customer;
import com.example.myapplication.Model.Notification;
import com.example.myapplication.Model.Professional;
import com.example.myapplication.Model.UserModel;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.Collection;

public class FirebaseUtils {

    public static String getCurrentUserId(){
        return FirebaseAuth.getInstance().getUid();
    }

    public static boolean IsLoggedIn(){
        if(getCurrentUserId() != null) {
            return true;
        }
        return false;
    }

    public static DocumentReference getProfessionalUserDetails(){
        return FirebaseFirestore.getInstance().collection("professional").document(getCurrentUserId());
    }

    public static CollectionReference allUserCollectionReference(){
        return FirebaseFirestore.getInstance().collection("professional");
    }

    public static void logout() {
    FirebaseAuth.getInstance().signOut();
    }

    // For Professional Profile in Customers Side
    public static void passProfessionalModelAsIntent(Intent intent, Professional model){
        intent.putExtra("userId",model.getUser_id());
        intent.putExtra("username",model.getUsername());
        intent.putExtra("phoneNumber",model.getPhoneNumber());
        intent.putExtra("address",model.getAddress());
        intent.putExtra("skill",model.getSkill());
        intent.putExtra("rating",model.getRating());
        intent.putExtra("booking",model.getBooking());
    }

    // For Professional Profile in Customers Side
    public static Professional getProfessionalModelAsIntent(Intent intent){
        Professional userModel = new Professional();
        userModel.setUser_id(intent.getStringExtra("userId"));
        userModel.setUsername(intent.getStringExtra("username"));
        userModel.setPhoneNumber(intent.getStringExtra("phoneNumber"));
        userModel.setAddress(intent.getStringExtra("address"));
        userModel.setBooking(intent.getStringExtra("booking"));
        userModel.setSkill(intent.getStringExtra("skill"));
        userModel.setRating(intent.getStringExtra("rating"));
        return userModel;
    }

    public static void passNotificationModelAsIntent(Intent intent, Notification model){
        intent.putExtra("customerId",model.getCustomerId());
        intent.putExtra("professionalId",model.getProfessionalId());
        intent.putExtra("customersUsername",model.getCustomersUsername());
        intent.putExtra("customersPhoneNumber",model.getCustomersPhoneNumber());
        intent.putExtra("customersAddress",model.getCustomersAddress());
        intent.putExtra("timeStamp",model.getTimestamp());
        intent.putExtra("workType",model.getWorkType());
    }

    public static void passCustomerModelAsIntent(Intent intent, Customer model){
        intent.putExtra("userId",model.getUser_id());
        intent.putExtra("username",model.getUsername());
        intent.putExtra("phoneNumber",model.getPhoneNumber());
        intent.putExtra("address",model.getAddress());
        intent.putExtra("email",model.getEmail());

    }

    public static Customer getCustomerlModelAsIntent(Intent intent){
        Customer userModel = new Customer();
        userModel.setUser_id(intent.getStringExtra("userId"));
        userModel.setUsername(intent.getStringExtra("username"));
        userModel.setPhoneNumber(intent.getStringExtra("phoneNumber"));
        userModel.setAddress(intent.getStringExtra("address"));
        return userModel;
    }



}
