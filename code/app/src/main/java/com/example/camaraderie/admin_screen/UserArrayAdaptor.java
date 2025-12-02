package com.example.camaraderie.admin_screen;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
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
import androidx.navigation.fragment.NavHostFragment;

import com.example.camaraderie.R;
import com.example.camaraderie.User;
import com.example.camaraderie.utilStuff.UserDeleter;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * UserArrayAdaptor extends array adapter and we use it to display our custom user items in an array adapter
 */
public class UserArrayAdaptor extends ArrayAdapter<User> {

    private FirebaseFirestore db;
    private ArrayList<User> users;
    private NavController nav;

    /**
     * We initialize a constructor to setup or attributes
     * @param context app context
     * @param user_list user list of users in db
     * @param nav nav controller
     */
    public UserArrayAdaptor(@NonNull Context context, ArrayList<User> user_list, NavController nav){
        super(context, 0, user_list);
        this.db = FirebaseFirestore.getInstance();
        this.users = user_list;
        this.nav = nav;
    }

    /**
     * getView is used to initialize the view of users in our array adapter
     *      * We setup the information on the xml to fill in with the user's information
     * @param position The position of the item within the adapter's data set of the item whose view
     *        we want.
     * @param convertView The old view to reuse, if possible. Note: You should check that this view
     *        is non-null and of an appropriate type before using. If it is not possible to convert
     *        this view to display the correct data, this method can create a new view.
     *        Heterogeneous lists can specify their number of view types, so that this View is
     *        always of the right type (see {@link #getViewTypeCount()} and
     *        {@link #getItemViewType(int)}).
     * @param parent The parent that this view will eventually be attached to
     * @return created item view
     */
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
            /**
             * handles on profile click
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
                Bundle bundle = new Bundle();
                bundle.putString("userEventDocRef", user1.getDocRef().getPath());
                //TODO: add admin view of user profile
                nav.navigate(R.id.admin_user_profile, bundle);
            }
        });

        /**
         * We have a remove button that allows the admin to to remove a user from the app and removes them from
         * all waiting lists, accepted lists, and waitlists
         */

        remove.setOnClickListener(new View.OnClickListener() {
            /**
             * handles on user delete
             * @param v The view that was clicked.
             */
            @Override
            public void onClick(View v) {
//                for (DocumentReference ref : user1.getSelectedEvents()) {
//                    ref.update("selectedEvents", FieldValue.arrayRemove(user1.getDocRef()));
//                }
//
//                for (DocumentReference ref : user1.getAcceptedEvents()) {
//                    ref.update("acceptedEvents", FieldValue.arrayRemove(user1.getDocRef()));
//                }
//
//                for (DocumentReference ref : user1.getWaitlistedEvents()) {
//                    ref.update("waitlist", FieldValue.arrayRemove(user1.getDocRef()));
//                }
//
//                for (DocumentReference eventDocRef : user1.getUserCreatedEvents()) {
//                    db.collection("Users").get()
//                            .addOnSuccessListener(snapshot -> {
//                                for (DocumentSnapshot userDoc : snapshot.getDocuments()) {
//                                    DocumentReference uRef = userDoc.getReference();
//                                    uRef.update("waitlistedEvents", FieldValue.arrayRemove(eventDocRef));
//                                    uRef.update("selectedEvents", FieldValue.arrayRemove(eventDocRef));
//                                    uRef.update("acceptedEvents", FieldValue.arrayRemove(eventDocRef));
//                                }
//                            });
//
//                    user1.deleteCreatedEvent(eventDocRef);
//                }
//                user1.getDocRef().delete();
                UserDeleter deleter = new UserDeleter(user1);
                String id = user1.getUserId();
                deleter.DeleteUser(()->{
                    Log.d("Firebase", "Admin has deleted user" + id);
                    notifyDataSetChanged();
                });
            }
        });

        return view;
    }
}
