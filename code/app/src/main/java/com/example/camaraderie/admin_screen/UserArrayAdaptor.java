package com.example.camaraderie.admin_screen;

import static com.example.camaraderie.MainActivity.user;

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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

public class UserArrayAdaptor extends ArrayAdapter<User> {

    private FirebaseFirestore db;
    private NavController nav;

    public UserArrayAdaptor(@NonNull Context context, ArrayList<User> users, NavController nav){
        super(context, 0, users);
        this.db = FirebaseFirestore.getInstance();

        this.nav = nav;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(R.layout.fragment_admin_users_view_item, parent, false);
        }
        else {
            view = convertView;
        }

        User user1 = getItem(position);
        if (user1 == null) {
            return view;
        }

        TextView name = view.findViewById(R.id.user_name);
        TextView user_id = view.findViewById(R.id.user_id);
        Button profile = view.findViewById(R.id.UserProfileButton);
        Button remove = view.findViewById(R.id.RemoveButton);

        name.setText(user1.getFirstName());
        user_id.setText(user1.getUserId());

        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //View profile
                Bundle bundle = new Bundle();
                bundle.putString("userEventDocRef", user1.getDocRef().getPath());
                //TODO: add admin view of user profile
                //nav.navigate(R.id.list_to_detail_view, bundle);
            }
        });

        remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                for (DocumentReference ref : user1.getSelectedEvents()) {
                    ref.update("selectedList", FieldValue.arrayRemove(user1.getDocRef()));
                }

                for (DocumentReference ref : user1.getAcceptedEvents()) {
                    ref.update("acceptedList", FieldValue.arrayRemove(user1.getDocRef()));
                }

                for (DocumentReference ref : user1.getWaitlistedEvents()) {
                    ref.update("waitlist", FieldValue.arrayRemove(user1.getDocRef()));
                }

                for (DocumentReference eventDocRef : user1.getUserCreatedEvents()) {
                    db.collection("Users").get()
                            .addOnSuccessListener(snapshot -> {
                                for (DocumentSnapshot userDoc : snapshot.getDocuments()) {
                                    DocumentReference uRef = userDoc.getReference();
                                    uRef.update("waitlistedEvents", FieldValue.arrayRemove(eventDocRef));
                                    uRef.update("selectedEvents", FieldValue.arrayRemove(eventDocRef));
                                    uRef.update("acceptedEvents", FieldValue.arrayRemove(eventDocRef));
                                }
                            });

                    user1.deleteCreatedEvent(eventDocRef);
                }
                user1.getDocRef().delete();
            }
        });

        return view;
    }
}
