package com.example.myapplication.Model;

import com.google.firebase.firestore.auth.User;

public class Customer extends UserModel{

    public Customer(String user_id, String username, String email, String password, String phoneNumber, String firstName, String lastName, String address, String userType, String skill, double rating){
        super(user_id, username, email, password, phoneNumber, firstName, lastName, address, userType);
    }

    public Customer(String user_id, String username, String email, String password, String userType) {
        super(user_id, username, email, password, userType);
    }
    public Customer(){

    }
}
