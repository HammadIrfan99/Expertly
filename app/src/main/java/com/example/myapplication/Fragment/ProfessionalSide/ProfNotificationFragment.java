package com.example.myapplication.Fragment.ProfessionalSide;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapter.ProfessionalSide.ProfNotificationAdapter;
import com.example.myapplication.Model.Notification;
import com.example.myapplication.R;
import com.example.myapplication.R;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ProfNotificationFragment extends Fragment {

    RecyclerView recyclerView;
    ProfNotificationAdapter adapter;

    public ProfNotificationFragment(){
        //Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_professional_notification, container, false);

        recyclerView = view.findViewById(R.id.profNotificationPg_recyclerView);
        //getting Professional id
        String professionalId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        searchNotificationInRecyclerView(professionalId);


        return view;
    }

    public void searchNotificationInRecyclerView(String professionalId) {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        Query  query = db.collection("booking")
                    .whereEqualTo("professionalId", professionalId)
                    .orderBy("timestamp", Query.Direction.DESCENDING);


        FirestoreRecyclerOptions<Notification> options = new FirestoreRecyclerOptions.Builder<Notification>()
                .setQuery(query, Notification.class)
                .build();

        adapter = new ProfNotificationAdapter(options, getContext());
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