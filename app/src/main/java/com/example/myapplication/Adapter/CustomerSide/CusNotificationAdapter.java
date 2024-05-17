package com.example.myapplication.Adapter.CustomerSide;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Activity.CustomerSide.CusProfessionalProfile;
import com.example.myapplication.Fragment.CustomerSide.CusNotificationFragment;
import com.example.myapplication.Model.Notification;
import com.example.myapplication.R;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

public class CusNotificationAdapter extends FirestoreRecyclerAdapter<Notification, CusNotificationAdapter.NotificationViewHolder> {

    Context context;

    public CusNotificationAdapter(@NonNull FirestoreRecyclerOptions<Notification> options, Context context) {
        super(options);
        this.context = context;
    }

    @NonNull
    @Override
    public NotificationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.cus_notification_recycler_row, parent, false);
        return new NotificationViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NotificationViewHolder notificationViewHolder, int position, @NonNull Notification notification) {
        notificationViewHolder.usernameText.setText(notification.getProfessionalUsername());
        notificationViewHolder.timeStamp.setText(notification.getTimestamp());
        notificationViewHolder.addressText.setText(notification.getProfessionalPhoneNumber());
        notificationViewHolder.bookingStatus.setText(notification.getBookingStatus());
        notificationViewHolder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, CusNotificationFragment.class);
            intent.putExtra("professionalId", notification.getProfessionalId());
            intent.putExtra("professionalUsername", notification.getProfessionalUsername());
            intent.putExtra("professionalPhoneNumber", notification.getProfessionalPhoneNumber());
            intent.putExtra("bookingStatus", notification.getBookingStatus());
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent); // Use context to start the activity
        });

    }

    class NotificationViewHolder extends RecyclerView.ViewHolder {

        TextView usernameText;
        TextView timeStamp;
        TextView addressText;
        TextView bookingStatus;

        public NotificationViewHolder(@NonNull View itemView) {
            super(itemView);
            usernameText = itemView.findViewById(R.id.recycler_view_userName_text);
            timeStamp = itemView.findViewById(R.id.recycler_view_timeStamp_text);
            addressText = itemView.findViewById(R.id.recycler_view_address_text);
            bookingStatus = itemView.findViewById(R.id.recycler_view_bookingStatus_text);
        }
    }
}