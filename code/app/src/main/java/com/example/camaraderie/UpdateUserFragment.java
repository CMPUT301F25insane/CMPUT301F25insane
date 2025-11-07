package com.example.camaraderie;

import static com.example.camaraderie.MainActivity.user;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.camaraderie.databinding.FragmentUpdateUserBinding;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * This is the screen for users updating their information.
 */

public class UpdateUserFragment extends Fragment {
    private FirebaseFirestore db;
    private CollectionReference usersRef;
    private DocumentReference userDocRef;
    private FragmentUpdateUserBinding binding;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentUpdateUserBinding.inflate(getLayoutInflater());

        return binding.getRoot();
    }

    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();
        usersRef = db.collection("Users");

        userDocRef = usersRef.document(user.getUserId());

        binding.updateName.setText(user.getFirstName());
        binding.updateEmail.setText(user.getEmail());
        binding.updatePhoneNo.setText(user.getPhoneNumber());
        binding.updateAddress.setText(user.getAddress());


        binding.updateSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.updateName.getText().toString().trim();
                String email = binding.updateEmail.getText().toString().trim();
                String address = binding.updateAddress.getText().toString().trim();
                String phone_no = binding.updatePhoneNo.getText().toString().trim();

                userDocRef
                        .update(
                                "firstName",name,
                                "email", email,
                                "phoneNumber", phone_no,
                                "address", address)
                        .addOnSuccessListener(a -> {
                            Toast.makeText(getContext(), "User updated successfully", Toast.LENGTH_SHORT).show();

                            user.setFirstName(name);
                            user.setEmail(email);
                            user.setPhoneNumber(phone_no);
                            user.setAddress(address);})
                        .addOnFailureListener(e -> {
                            Toast.makeText(getContext(), "Failed to update user: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                        });
            }
        });

        binding.userDelete.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();
            userDocRef.delete();
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_update_user_to_fragment_main);
        });

        binding.updateCancel.setOnClickListener(v -> {
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_update_user_to_fragment_main);
        });

        binding.admin.setOnClickListener(v -> {
            NavHostFragment.findNavController(this)
                    .navigate(R.id.action_update_user_to_admin_main_screen);
        });

        binding.seeGuidelinesButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(UpdateUserFragment.this)
                        .navigate(R.id.action_update_user_to_fragment_guidelines);
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // avoid memory leaks
    }
}
