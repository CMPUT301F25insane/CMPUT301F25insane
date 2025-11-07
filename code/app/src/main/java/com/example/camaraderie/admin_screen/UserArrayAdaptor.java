package com.example.camaraderie.admin_screen;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

import com.example.camaraderie.R;
import com.example.camaraderie.User;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class UserArrayAdaptor extends ArrayAdapter<User> {

    private FirebaseFirestore db;
    private ArrayList<User> users;
    public UserArrayAdaptor(@NonNull Context context, ArrayList<User> users){
        super(context, 0, users);
        this.db = FirebaseFirestore.getInstance();
        this.users = users;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_admin_users_view_item, parent, false);
        }

        User user = getItem(position);
        if (user == null) {
            return view;
        }

        TextView name = convertView.findViewById(R.id.user_name);
        TextView user_id = convertView.findViewById(R.id.user_id);
        Button profile = convertView.findViewById(R.id.UserProfileButton);
        Button remove = convertView.findViewById(R.id.RemoveButton);

        name.setText(user.getFirstName());
        user_id.setText(user.getUserId());

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //View profile
                Bundle bundle = new Bundle();
                bundle.putString("userId", user.getUserId());

                NavController navController = Navigation.findNavController(v);
                //navController.navigate(R.id.list_to_detail_view, bundle);
            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Users")
                        .document(user.getUserId())
                        .delete();
            }
        });

        return view;
    }
}
