package com.example.camaraderie.admin_screen;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.camaraderie.R;
import com.example.camaraderie.User;
import com.example.camaraderie.databinding.FragmentAdminUserProfileBinding;
import com.example.camaraderie.utilStuff.UserDeleter;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * AdminUserProfileFragment extends Fragment and is used for allows a admin to view different users profile
 * and remove them from the app
 */
public class AdminUserProfileFragment extends Fragment {

    private FragmentAdminUserProfileBinding binding;
    private NavController nav;
    private FirebaseFirestore db;
    private User user;

    /**
     * We require an empty constructor
     */


    /**
     * onCreateView initializes the xml view for the fragment and is run as soon as we navigate to this fragment
     * We initialize our binding to inflate the xml and we return the root of the binding as a view
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     * @return
     * We return a view which is the inflated
     */

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAdminUserProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    /**
     * onViewCreated initializes all the xml elements and allows us to setup our backend for this components
     * We get the latest instance of firebase
     * We check to make sure our arguments are not null and we get the user
     * We make sure user is not null before grabbing all the id, email etc
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();
        nav = NavHostFragment.findNavController(this);

        if (getArguments() != null) {
            String userPath = getArguments().getString("userEventDocRef");

            if (userPath != null) {
                DocumentReference userRef = db.document(userPath);
                userRef.get().addOnSuccessListener(snapshot -> {
                    user = snapshot.toObject(User.class);

                    if (user != null) {
                        binding.UserIDNumber.setText(user.getUserId());
                        binding.UserEmail.setText(user.getEmail());
                        binding.UserName.setText(user.getFirstName());
                        binding.UserPhoneNo.setText(user.getPhoneNumber());
                        binding.UserAddress.setText(user.getAddress());
                    } else {
                        Toast.makeText(getContext(), "User data not found", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(e ->
                        Toast.makeText(getContext(), "Error loading user: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                );
            }
        }

        /**
         * We have a remove button that allows for the admin to remove users who are not following guidelines
         * When the button is clicked we use firebase to remove the user from the Users collection
         * We grab their userID and delete their document and afterwards navigate back to the admin user data screen
         */

        binding.RemoveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user == null) {
                    Toast.makeText(getContext(), "No user selected", Toast.LENGTH_SHORT).show();
                    return;
                }

                UserDeleter deleter = new UserDeleter(user);
                deleter.DeleteUser(() -> {
                    Toast.makeText(getContext(), "User " + user.getFirstName() + " deleted", Toast.LENGTH_SHORT).show();
                    if(!nav.popBackStack(R.id.admin_user_data_screen_view, false)) {
                        nav.navigate(R.id.admin_user_data_screen_view);
                    }
                });
            }
        });

        /**
         * We also have a back button for the admin to navigate back
         */

        binding.BackButton.setOnClickListener( v -> nav.popBackStack());
    }
}