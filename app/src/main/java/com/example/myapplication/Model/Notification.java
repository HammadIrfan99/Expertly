package com.example.myapplication.Model;

public class Notification {
    private String notificationId;
    private String professionalId;
    private String professionalUsername;
    private String professionalAddress;
    private String professionalPhoneNumber;
    private String customerId;
    private String customersUsername;
    private String customersAddress;
    private String customersPhoneNumber;
    private String workType;
    private String timestamp;
    private String bookingStatus;


    // Empty constructor needed for Firestore
    public Notification() {}

    public Notification(String notificationId,String professionalId, String professionalUsername, String professionalAddress, String professionalPhoneNumber, String customerId, String customersUsername, String customersAddress, String customersPhoneNumber, String workType, String timestamp, String bookingStatus) {
        this.notificationId = notificationId;
        this.professionalId = professionalId;
        this.professionalUsername = professionalUsername;
        this.professionalAddress = professionalAddress;
        this.professionalPhoneNumber = professionalPhoneNumber;
        this.customerId = customerId;
        this.customersUsername = customersUsername;
        this.customersAddress = customersAddress;
        this.customersPhoneNumber = customersPhoneNumber;
        this.workType = workType;
        this.timestamp = timestamp;
        this.bookingStatus = bookingStatus;
    }

    public String getNotificationId() {
        return notificationId;
    }

    public void setNotificationId(String notificationId) {
        this.notificationId = notificationId;
    }

    public String getProfessionalId() {
        return professionalId;
    }

    public void setProfessionalId(String professionalId) {
        this.professionalId = professionalId;
    }

    public String getProfessionalUsername() {
        return professionalUsername;
    }

    public void setProfessionalUsername(String professionalUsername) {
        this.professionalUsername = professionalUsername;
    }

    public String getProfessionalAddress() {
        return professionalAddress;
    }

    public void setProfessionalAddress(String professionalAddress) {
        this.professionalAddress = professionalAddress;
    }

    public String getProfessionalPhoneNumber() {
        return professionalPhoneNumber;
    }

    public void setProfessionalPhoneNumber(String professionalPhoneNumber) {
        this.professionalPhoneNumber = professionalPhoneNumber;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCustomersUsername() {
        return customersUsername;
    }

    public void setCustomersUsername(String customersUsername) {
        this.customersUsername = customersUsername;
    }

    public String getCustomersAddress() {
        return customersAddress;
    }

    public void setCustomersAddress(String customersAddress) {
        this.customersAddress = customersAddress;
    }

    public String getCustomersPhoneNumber() {
        return customersPhoneNumber;
    }

    public void setCustomersPhoneNumber(String customersPhoneNumber) {
        this.customersPhoneNumber = customersPhoneNumber;
    }

    public String getWorkType() {
        return workType;
    }

    public void setWorkType(String workType) {
        this.workType = workType;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getBookingStatus() {
        return bookingStatus;
    }

    public void setBookingStatus(String bookingStatus) {
        this.bookingStatus = bookingStatus;
    }
}
