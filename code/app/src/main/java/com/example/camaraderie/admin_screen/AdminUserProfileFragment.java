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
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;

/**
 * Admin view of a user's profile. Can delete a user.
 */
public class AdminUserProfileFragment extends Fragment {

    private FragmentAdminUserProfileBinding binding;
    private FirebaseFirestore db;
    private User user;

    public AdminUserProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentAdminUserProfileBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();

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

        binding.BackButton.setOnClickListener( v ->
                NavHostFragment.findNavController(this)
                        .navigate(R.id.action_admin_user_profile_to_admin_user_data_screen_view)
        );
    }
}