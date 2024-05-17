package com.example.myapplication.Model;

public class Professional extends UserModel{

    private String skill;
    private String rating;
    private String booking;
    public Professional(String user_id, String username, String email, String password, String phoneNumber, String firstName, String lastName, String address, String userType, String skill, String rating, String booking){
        super(user_id, username, email, password, phoneNumber, firstName, lastName, address, userType);
        this.skill = skill;
        this.rating = rating;
        this.booking = booking;
    }

    public Professional(String user_id, String username, String email, String password, String userType) {
        super(user_id, username, email, password, userType);
    }

    public Professional(){

    }

    public String getSkill() {
        return skill;
    }

    public String getBooking() {
        return booking;
    }

    public void setBooking(String booking) {
        this.booking = booking;
    }

    public void setSkill(String skill) {
        this.skill = skill;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(String rating) {
        this.rating = rating;
    }
}
