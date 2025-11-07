package com.example.camaraderie.admin_screen;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.camaraderie.R;
import com.example.camaraderie.User;
import com.example.camaraderie.databinding.FragmentAdminUserProfileBinding;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * AdminUserProfileFragment extends Fragment and is used for allows a admin to view different users profile
 * and remove them from the app
 */
public class AdminUserProfileFragment extends Fragment {

    private FragmentAdminUserProfileBinding binding;
    private FirebaseFirestore db;
    private User user;

    /**
     * We require an empty constructor
     */

    public AdminUserProfileFragment() {
        // Required empty public constructor
    }

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

        if(getArguments() != null){
            user = (User) getArguments().getSerializable("user");

        }

        if (user != null) {
            binding.UserID.setText(user.getUserId());
            binding.UserEmail.setText(user.getEmail());
            binding.UserName.setText(user.getFirstName());
            binding.UserPhoneNo.setText(user.getPhoneNumber());
            binding.UserAddress.setText(user.getAddress());
        } else {
            Toast.makeText(getContext(), "Error: User not found", Toast.LENGTH_SHORT).show();
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

                db.collection("Users")
                        .document(user.getUserId())
                        .delete()
                        .addOnSuccessListener(w -> {
                            Toast.makeText(getContext(), "User " + user.getFirstName() + " deleted", Toast.LENGTH_SHORT).show();

                            NavHostFragment.findNavController(AdminUserProfileFragment.this)
                                    .navigate(R.id.action_admin_user_profile_to_admin_user_data_screen_view);
                        })
                        .addOnFailureListener(e ->
                                Toast.makeText(getContext(), "Failed to delete: " + e.getMessage(), Toast.LENGTH_SHORT).show()
                        );
            }
        });

        /**
         * We also have a back button for the admin to navigate back
         */

        binding.BackButton.setOnClickListener( v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_admin_user_profile_to_admin_user_data_screen_view)
        );
    }
}