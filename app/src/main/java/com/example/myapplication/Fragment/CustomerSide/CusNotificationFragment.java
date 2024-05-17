package com.example.myapplication.Fragment.CustomerSide;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapter.CustomerSide.CusNotificationAdapter;
import com.example.myapplication.Model.Notification;
import com.example.myapplication.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class CusNotificationFragment extends Fragment {

    RecyclerView recyclerView;
    CusNotificationAdapter adapter;

    public CusNotificationFragment(){
        //Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_customer_notification, container, false);

        recyclerView = view.findViewById(R.id.cusNotificationPg_recyclerView);
        //getting Customer id
        String customerId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        searchNotificationInRecyclerView(customerId);

        return view;
    }

    public void searchNotificationInRecyclerView(String notificationId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Query  query = db.collection("booking")
                .whereEqualTo("notificationId", notificationId)
                .orderBy("timestamp", Query.Direction.DESCENDING);

        FirestoreRecyclerOptions<Notification> options = new FirestoreRecyclerOptions.Builder<Notification>()
                .setQuery(query, Notification.class)
                .build();

        adapter = new CusNotificationAdapter(options, getContext());
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        recyclerView.setAdapter(adapter);
        adapter.startListening();
    }

    @Override
    public void onStart() {
        super.onStart();
        if(adapter != null)
            adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        if(adapter != null)
            adapter.stopListening();
    }

    @Override
    public void onResume() {
        super.onResume();
        if(adapter != null)
            adapter.startListening();
    }
}