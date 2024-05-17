package com.example.myapplication.Model;

import android.os.Parcel;
import android.os.Parcelable;

public class UserModel implements Parcelable{
    private String user_id;
    private String username;
    private String email;
    private String password;
    private String phoneNumber;
    private String firstName;
    private String lastName;
    private String address;
    private String userType;


    public UserModel(String user_id, String username, String email, String password, String phoneNumber, String firstName, String lastName, String address, String userType) {
        this.user_id = user_id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        this.firstName = firstName;
        this.lastName = lastName;
        this.address = address;
        this.userType = userType;
    }

    public UserModel() {
    }

    public UserModel(String user_id, String username, String email, String password, String userType) {
        this.user_id = user_id;
        this.username = username;
        this.email = email;
        this.password = password;
        this.userType = userType;
    }

    // Parcelable constructor
    protected UserModel(Parcel in) {
        user_id = in.readString();
        username = in.readString();
        email = in.readString();
        password = in.readString();
        phoneNumber = in.readString();
        firstName = in.readString();
        lastName = in.readString();
        address = in.readString();
        userType = in.readString();
    }

    // Required Parcelable methods
    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(user_id);
        dest.writeString(username);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeString(phoneNumber);
        dest.writeString(firstName);
        dest.writeString(lastName);
        dest.writeString(address);
        dest.writeString(userType);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    // Parcelable CREATOR
    public static final Parcelable.Creator<UserModel> CREATOR = new Parcelable.Creator<UserModel>() {
        @Override
        public UserModel createFromParcel(Parcel in) {
            return new UserModel(in);
        }

        @Override
        public UserModel[] newArray(int size) {
            return new UserModel[size];
        }
    };

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getUserType() {
        return userType;
    }

    public void setUserType(String userType) {
        this.userType = userType;
    }
}
