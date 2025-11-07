package com.example.camaraderie.admin_screen;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
    public UserArrayAdaptor(@NonNull Context context, ArrayList<User> user_list){
        super(context, 0, user_list);
        this.db = FirebaseFirestore.getInstance();
        this.users = user_list;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            view = LayoutInflater.from(getContext())
                    .inflate(R.layout.fragment_admin_users_view_item, parent, false);
        }

        User user = getItem(position);
        if (user == null) {
            return view;
        }

        TextView name = view.findViewById(R.id.user_name);
        TextView user_id = view.findViewById(R.id.user_id);
        Button profile = view.findViewById(R.id.UserProfileButton);
        Button remove = view.findViewById(R.id.RemoveButton);

        name.setText(user.getFirstName());
        user_id.setText(user.getUserId());

        /*
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //View profile
                Bundle bundle = new Bundle();
                bundle.putString("userId", user.getUserId());

                NavController navController = Navigation.findNavController(v);
                //navController.navigate(R.id.list_to_detail_view, bundle);
            }
        });*/

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Users")
                        .document(user.getUserId())
                        .delete()
                        .addOnSuccessListener(w -> {
                            Toast.makeText(getContext(), "User " + user.getFirstName() + " deleted", Toast.LENGTH_SHORT).show();

                            users.remove(position);
                            notifyDataSetChanged();
                        })
                        .addOnFailureListener(e ->
                                Toast.makeText(getContext(), "Failed to delete: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                        );
            }
        });
        return view;
    }
}
