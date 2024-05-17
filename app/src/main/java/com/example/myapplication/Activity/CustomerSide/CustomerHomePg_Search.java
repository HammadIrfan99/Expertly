package com.example.myapplication.Activity.CustomerSide;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Adapter.CustomerSide.SearchUserRecyclerAdapter;
import com.example.myapplication.Model.Professional;
import com.example.myapplication.Model.UserModel;
import com.example.myapplication.R;
import com.example.myapplication.Utils.FirebaseUtils;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.Query;

import java.util.Queue;

public class CustomerHomePg_Search extends AppCompatActivity {

    ImageButton backButton;
    ImageButton searchButton;
    EditText searchInput;
    RecyclerView recyclerView;
    SearchUserRecyclerAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_customer_home_pg_search);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        backButton = findViewById(R.id.cusSearchPg_back);
        searchButton = findViewById(R.id.cusSearchPg_search);
        searchInput = findViewById(R.id.cusSearchPg_searchInput);
        recyclerView = findViewById(R.id.cusSearchPg_recyclerView);

        //Implementing back Button
        backButton.setOnClickListener(v -> {
            onBackPressed();  // Directly call onBackPressed() to handle back press
        });

        //Implementing Search Button
        searchInput.requestFocus();

            searchButton.setOnClickListener(v ->{
                String searchTerm = searchInput.getText().toString();
                Log.d("searchTerm","reached");
                if(searchTerm.isEmpty()){
                    searchInput.setError("Invalid Skill");
                }
                searchUserInRecyclerView(searchTerm);
            });
    }
    public void searchUserInRecyclerView(String searchTerm){

        Query query = FirebaseUtils.allUserCollectionReference()
                .whereGreaterThanOrEqualTo("skill",searchTerm.toLowerCase());

        FirestoreRecyclerOptions<Professional> options = new FirestoreRecyclerOptions.Builder<Professional>()
                .setQuery(query, Professional.class).build();

            adapter = new SearchUserRecyclerAdapter(options, getApplicationContext());
            recyclerView.setLayoutManager(new LinearLayoutManager(CustomerHomePg_Search.this));
            recyclerView.setAdapter(adapter);
            adapter.startListening();

    }
    @Override
    protected void onStart() {
        super.onStart();
        if(adapter != null)
            adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(adapter != null)
            adapter.stopListening();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if(adapter != null)
            adapter.startListening();
    }
}


