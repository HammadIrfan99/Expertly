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
import com.example.myapplication.Model.Professional;
import com.example.myapplication.R;
import com.example.myapplication.Utils.FirebaseUtils;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.common.util.AndroidUtilsLight;

public class SearchUserRecyclerAdapter extends FirestoreRecyclerAdapter<Professional, SearchUserRecyclerAdapter.UserModelViewHolder> {

    Context context;
    public SearchUserRecyclerAdapter(@NonNull FirestoreRecyclerOptions<Professional> options, Context context) {
        super(options);
        this.context = context;
    }

    @NonNull
    @Override
    public UserModelViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.search_professional_recycler_row,parent, false);
        return new UserModelViewHolder(view);
    }

    @Override
    protected void onBindViewHolder(@NonNull UserModelViewHolder userModelViewHolder, int position, @NonNull Professional user) {
        userModelViewHolder.usernameText.setText(user.getUsername());
        userModelViewHolder.addressText.setText(user.getAddress());
        userModelViewHolder.skillText.setText(user.getSkill());
        userModelViewHolder.itemView.setOnClickListener(v -> {
            Intent intent = new Intent(context, CusProfessionalProfile.class);
            FirebaseUtils.passProfessionalModelAsIntent(intent,user);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent); // Use context to start the activity
        });
        }

    class UserModelViewHolder extends RecyclerView.ViewHolder{
        TextView usernameText;
        TextView skillText;
        TextView addressText;
//        ImageView profilePic;

        public UserModelViewHolder(@NonNull View itemView){
            super(itemView);
            usernameText = itemView.findViewById(R.id.recycler_view_userName_text);
            skillText = itemView.findViewById(R.id.recycler_view_skill_text);
            addressText = itemView.findViewById(R.id.recycler_view_address_text);
//            profilePic = itemView.findViewById(R.id.profilePic);
        }
    }
}
