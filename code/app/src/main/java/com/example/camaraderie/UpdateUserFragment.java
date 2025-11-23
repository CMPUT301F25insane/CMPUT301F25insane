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
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;

/**
 * This is the screen for users updating their information.
 */

public class UpdateUserFragment extends Fragment {
    private FirebaseFirestore db;
    private CollectionReference usersRef;
    private DocumentReference userDocRef;
    private FragmentUpdateUserBinding binding;

    /**
     *
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return binding root
     */
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentUpdateUserBinding.inflate(getLayoutInflater());

        return binding.getRoot();
    }

    /**
     * attaches the layout bindings and button listeners
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        db = FirebaseFirestore.getInstance();
        usersRef = db.collection("Users");

        userDocRef = usersRef.document(user.getUserId());

        binding.nameFieldForUpdateUser.setText(user.getFirstName());
        binding.emailFieldForUpdateUser.setText(user.getEmail());
        binding.phoneFieldForUpdateUser3.setText(user.getPhoneNumber());
        binding.addressFieldForUpdateUser2.setText(user.getAddress());


        binding.confirmButtonForUserProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = binding.nameFieldForUpdateUser.getText().toString().trim();
                String email = binding.emailFieldForUpdateUser.getText().toString().trim();
                String address = binding.addressFieldForUpdateUser2.getText().toString().trim();
                String phone_no = binding.phoneFieldForUpdateUser3.getText().toString().trim();

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

        binding.DeleteButtonForUserProfile.setOnClickListener(v -> {
            Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();

            for (DocumentReference ref : user.getSelectedEvents()) {
                ref.update("selectedList", FieldValue.arrayRemove(user.getDocRef()));
            }

            for (DocumentReference ref : user.getAcceptedEvents()) {
                ref.update("acceptedList", FieldValue.arrayRemove(user.getDocRef()));
            }

            for (DocumentReference ref : user.getWaitlistedEvents()) {
                ref.update("waitlist", FieldValue.arrayRemove(user.getDocRef()));
            }

            for (DocumentReference eventDocRef : user.getUserCreatedEvents()) {
                db.collection("Users").get()
                        .addOnSuccessListener(snapshot -> {
                            for (DocumentSnapshot userDoc : snapshot.getDocuments()) {
                                DocumentReference uRef = userDoc.getReference();
                                uRef.update("waitlistedEvents", FieldValue.arrayRemove(eventDocRef));
                                uRef.update("selectedEvents", FieldValue.arrayRemove(eventDocRef));
                                uRef.update("acceptedEvents", FieldValue.arrayRemove(eventDocRef));
                            }
                        });

                user.deleteCreatedEvent(eventDocRef);
            }
            user.getDocRef().delete();
            // goodbye
            getActivity().finish();
            System.exit(0);
//            NavHostFragment.findNavController(this)
//                    .navigate(R.id.fragment_main);
        });

        binding.cancelButtonForUserProfile.setOnClickListener(v -> {
            NavHostFragment.findNavController(this)
                    .navigate(R.id.fragment_main);
        });

        binding.adminButtonForUserProfile2.setOnClickListener(v -> {
            if (binding.adminPasswordForUserProfile.getText().toString().equals("80085")) {
                user.setAdmin(true);
                NavHostFragment.findNavController(this)
                        .navigate(R.id.admin_main_screen);
            }
        });

        binding.guidelinesButtonForUserProfile3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NavHostFragment.findNavController(UpdateUserFragment.this)
                        .navigate(R.id.fragment_guidelines);
            }
        });
    }

    /**
     * destroys view, sets binding to null to avoid memory leaks
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null; // avoid memory leaks
    }
}
