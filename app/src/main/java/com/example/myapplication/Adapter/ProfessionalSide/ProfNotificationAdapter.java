package com.example.myapplication.Adapter.ProfessionalSide;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Activity.ProfessionalSide.ProfCustomerProfile;
import com.example.myapplication.Model.Notification;
import com.example.myapplication.R;
import com.example.myapplication.Utils.FirebaseUtils;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class ProfNotificationAdapter extends FirestoreRecyclerAdapter<Notification, ProfNotificationAdapter.NotificationViewHolder> {

    Context context;

    public ProfNotificationAdapter(@NonNull FirestoreRecyclerOptions<Notification> options, Context context) {
        super(options);
        this.context = context;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.prof_notification_recycler_row, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder notificationViewHolder, int position, @NonNull Notification notification) {
        notificationViewHolder.usernameText.setText(notification.getCustomersUsername());
        notificationViewHolder.timeStamp.setText(notification.getTimestamp());
        notificationViewHolder.addressText.setText(notification.getCustomersAddress());
        notificationViewHolder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, ProfCustomerProfile.class);
            intent.putExtra("notificationId", notification.getNotificationId());
            intent.putExtra("customerId", notification.getCustomerId());
            intent.putExtra("customerUsername", notification.getCustomersUsername());
            intent.putExtra("customerAddress", notification.getCustomersAddress());
            intent.putExtra("customerPhoneNumber", notification.getCustomersPhoneNumber());
            intent.putExtra("timeStamp", notification.getTimestamp());
            FirebaseUtils.passNotificationModelAsIntent(intent,notification);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent); // Use context to start the activity
        });
    }

    class NotificationViewHolder extends RecyclerView.ViewHolder {

        TextView usernameText;
        TextView timeStamp;
        TextView addressText;
        // Image View

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameText = itemView.findViewById(R.id.recycler_view_userName_text);
            timeStamp = itemView.findViewById(R.id.recycler_view_timeStamp_text);
            addressText = itemView.findViewById(R.id.recycler_view_address_text);
        }

    }
}







