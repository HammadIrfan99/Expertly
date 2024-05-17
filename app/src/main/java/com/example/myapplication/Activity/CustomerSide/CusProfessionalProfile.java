package com.example.myapplication.Activity.CustomerSide;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.myapplication.Model.Notification;
import com.example.myapplication.Model.Professional;
import com.example.myapplication.R;
import com.example.myapplication.Utils.FirebaseUtils;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class CusProfessionalProfile extends AppCompatActivity {

    Professional professional;
    TextView username;
    TextView phoneNumber;
    TextView address;
    TextView skill;
    TextView rating;
    TextView booking;
    Button chatButton;
    Button bookButton;
    ImageButton backButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.cus_professional_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });



        username = findViewById(R.id.cus_Prof_Profile_username);
        phoneNumber = findViewById(R.id.cus_Prof_Profile_PhNo);
        address = findViewById(R.id.cus_Prof_Profile_address);
        skill = findViewById(R.id.cus_Prof_Profile_skill);
        rating = findViewById(R.id.cus_Prof_Profile_rating);
        booking = findViewById(R.id.cus_Prof_Profile_booking);
        chatButton = findViewById(R.id.cusProf_Profile_ChatButton);
        bookButton = findViewById(R.id.cusProf_Profile_BookButton);
        backButton = findViewById(R.id.cus_Prof_Profile_backButton);

        //getting professionals data from searchRecyclerViewAdapter
        professional = FirebaseUtils.getProfessionalModelAsIntent(getIntent());
        //Collectiong professional data to display to customer
        String prof_userId = professional.getUser_id();
        username.setText(professional.getUsername());
        phoneNumber.setText(professional.getPhoneNumber());
        address.setText(professional.getAddress());
        skill.setText(professional.getSkill());
        rating.setText(professional.getRating());
        booking.setText(professional.getBooking());


        //Implementing back Button
        backButton.setOnClickListener(v -> {
            onBackPressed();  // Directly call onBackPressed() to handle back press
        });

        chatButton.setOnClickListener(v->{
            Intent intent = new Intent(this, CusChat.class);
            FirebaseUtils.passProfessionalModelAsIntent(intent,professional);
            startActivity(intent);
        });


        bookButton.setOnClickListener(v -> {
            //Get current users details to add to the notification
            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

            FirebaseFirestore db = FirebaseFirestore.getInstance();
            db.collection("customer").document(user.getUid())
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                            if (task.isSuccessful()) {
                                DocumentSnapshot document = task.getResult();
                                if (document.exists()) {
                                    String cus_UserId = FirebaseAuth.getInstance().getCurrentUser().getUid();
                                    String username = document.getString("username");
                                    String email = document.getString("email");
                                    String phoneNumber = document.getString("phoneNumber");
                                    String address = document.getString("address");

                                    // Create a new Notification object
                                    Notification notification = new Notification();
                                    notification.setCustomerId(cus_UserId); // Assuming you have a method to get the current logged in user
                                    // Format the timestamp
                                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault());
                                    String formattedDate = sdf.format(new Date());
                                    notification.setTimestamp(formattedDate);
                                    notification.setCustomersUsername(username);
                                    notification.setCustomersAddress(address);
                                    notification.setCustomersPhoneNumber(phoneNumber);
                                    notification.setProfessionalId(prof_userId);
                                    notification.setWorkType(professional.getSkill());
                                    notification.setProfessionalUsername(professional.getUsername());
                                    notification.setProfessionalAddress(professional.getAddress());
                                    notification.setProfessionalPhoneNumber(professional.getPhoneNumber());
                                    notification.setBookingStatus("Booking Confirmation Panding");

                                    // Add the notification to Firestore
                                    db.collection("booking")
                                            .add(notification)
                                            .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                                @Override
                                                public void onSuccess(DocumentReference documentReference) {
                                                    // Now add to Notification notification
                                                    notification.setNotificationId(documentReference.getId());
                                                    Log.d("TAG", "onSuccess: " + documentReference.getId());

                                                    // Update the document in Firestore with the new Notification object
                                                    db.collection("booking").document(documentReference.getId())
                                                            .set(notification)
                                                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                                @Override
                                                                public void onSuccess(Void aVoid) {
                                                                    Toast.makeText(CusProfessionalProfile.this, "Booking successful", Toast.LENGTH_SHORT).show();
                                                                }
                                                            })
                                                            .addOnFailureListener(new OnFailureListener() {
                                                                @Override
                                                                public void onFailure(@NonNull Exception e) {
                                                                    Toast.makeText(CusProfessionalProfile.this, "Failed to update booking", Toast.LENGTH_SHORT).show();
                                                                }
                                                            });
                                                }
                                            })
                                            .addOnFailureListener(new OnFailureListener() {
                                                @Override
                                                public void onFailure(@NonNull Exception e) {
                                                    Toast.makeText(CusProfessionalProfile.this, "Booking failed", Toast.LENGTH_SHORT).show();
                                                }
                                            });
                                } else {
                                    Log.d("Document Existance", "No such document");
                                    Toast.makeText(CusProfessionalProfile.this, "Error Occured while Storing details", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Log.d("Is successful", "get failed with ", task.getException());
                                Toast.makeText(CusProfessionalProfile.this, "Task Unsucessful", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        });
}
}