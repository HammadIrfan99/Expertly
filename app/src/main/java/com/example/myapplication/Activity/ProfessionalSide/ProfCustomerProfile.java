package com.example.myapplication.Activity.ProfessionalSide;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.Activity.CustomerSide.CusChat;
import com.example.myapplication.Model.Customer;
import com.example.myapplication.R;
import com.example.myapplication.Utils.FirebaseUtils;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class ProfCustomerProfile extends AppCompatActivity {


    TextView Cus_username;
    TextView Cus_phoneNumber;
    TextView Cus_address;
    TextView Cus_timeStamp;
    Button chatButton;
    Button confirmBookingButton;
    Button rejectBookingButton;
    String customerId;
    String customerUsername;
    String customerAddress;
    String customerPhoneNumber;
    String timeStamp;
    ImageButton backButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_prof_customer_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Cus_username = findViewById(R.id.prof_Cus_Profile_username);
        Cus_phoneNumber = findViewById(R.id.prof_Cus_Profile_PhNo);
        Cus_address = findViewById(R.id.prof_Cus_Profile_address);
        Cus_timeStamp = findViewById(R.id.prof_Cus_Profile_timeStamp);
        chatButton = findViewById(R.id.prof_Cus_Profile_ChatButton);
        confirmBookingButton = findViewById(R.id.prof_Cus_Profile_ConfirmButton);
        rejectBookingButton = findViewById(R.id.prof_Cus_Profile_RejectButton);
        backButton = findViewById(R.id.prof_Cus_Profile_backButton);


        // Getting Customers datail from Intent
        Intent intentGetData = getIntent();
        customerId = intentGetData.getStringExtra("customerId");
        customerUsername = intentGetData.getStringExtra("customerUsername");
        customerAddress = intentGetData.getStringExtra("customerAddress");
        customerPhoneNumber = intentGetData.getStringExtra("customerPhoneNumber");
        timeStamp = intentGetData.getStringExtra("timeStamp");

        //Implementing back Button
        backButton.setOnClickListener(v -> {
            onBackPressed();  // Directly call onBackPressed() to handle back press
        });

        // Setting details
        Cus_username.setText(customerUsername);
        Cus_phoneNumber.setText(customerPhoneNumber);
        Cus_address.setText(customerAddress);
        Cus_timeStamp.setText(timeStamp);

        Customer customer = FirebaseUtils.getCustomerlModelAsIntent(getIntent());

            chatButton.setOnClickListener(v->{
                Intent intent = new Intent(this, ProfChat.class);
                FirebaseUtils.passCustomerModelAsIntent(intent,customer);
                startActivity(intent);
            });

            confirmBookingButton.setOnClickListener(v->{
                confirmBooking(customerId);
            });

            rejectBookingButton.setOnClickListener(v->{
                rejectBooking(customerId);
            });

    }

    public void confirmBooking(String customerId){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Update bookingStatus in notifications
        db.collection("booking")
                .whereEqualTo("customerId", customerId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            db.collection("booking").document(document.getId()).update("bookingStatus", "Booking Request Confirmed");
                            Toast.makeText(this, "Booking Confirmedd Successfully", Toast.LENGTH_SHORT).show();
                            Log.d("TAG", "confirmBooking: " + document.getId());
                        }
                    }
                }).addOnFailureListener(e -> {
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("TAG", "confirmBooking: " + e.getMessage());
                });
    }

    public void rejectBooking(String customerId){
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Update bookingStatus in notifications
        db.collection("booking")
                .whereEqualTo("customerId", customerId)
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (DocumentSnapshot document : task.getResult()) {
                            db.collection("booking").document(document.getId()).update("bookingStatus", "Booking Request Rejected");
                            Toast.makeText(this, "Booking Rejacted Successfully", Toast.LENGTH_SHORT).show();
                            Log.d("TAG", "rejectBooking: " + document.getId());
                        }
                    }
                }).addOnFailureListener(e -> {
                    Toast.makeText(this, "Error: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                    Log.d("TAG", "rejectBooking: " + e.getMessage());
                });
    }
}