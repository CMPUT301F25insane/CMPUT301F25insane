package com.example.camaraderie.updateUserStuff;

import static com.example.camaraderie.main.MainActivity.user;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.camaraderie.R;
import com.example.camaraderie.databinding.FragmentUpdateUserBinding;
import com.example.camaraderie.utilStuff.UserDeleter;
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
    private NavController nav;

    ActivityResultLauncher<String[]> locationPermissionRequest;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        nav = NavHostFragment.findNavController(this);

        //https://developer.android.com/develop/sensors-and-location/location/permissions/runtime
        locationPermissionRequest = locationPermissionRequest =
                registerForActivityResult(new ActivityResultContracts
                                .RequestMultiplePermissions(), result -> {

                            Boolean fineLocationGranted = null;
                            Boolean coarseLocationGranted = null;

                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                fineLocationGranted = result.getOrDefault(
                                        Manifest.permission.ACCESS_FINE_LOCATION, false);
                                coarseLocationGranted = result.getOrDefault(
                                        Manifest.permission.ACCESS_COARSE_LOCATION,false);
                            }

                    if (fineLocationGranted != null && fineLocationGranted) {
                        user.setGeoEnabled(true);
                        Toast.makeText(getContext(),"Precise location permission granted", Toast.LENGTH_SHORT).show();
                    } else if (coarseLocationGranted != null && coarseLocationGranted) {
                        user.setGeoEnabled(true);
                        Toast.makeText(getContext(),"Approximate location permission granted", Toast.LENGTH_SHORT).show();
                    } else {
                        user.setGeoEnabled(false);
                        binding.geoSwitch.setChecked(false); // turn off switch if denied
                        Toast.makeText(getContext(),"Location permission denied", Toast.LENGTH_SHORT).show();
                    }
                });
    }

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

        // TODO: to fix this, we need to use batches and use .commit on the batch (instead of the for loop, then after all as been finished, we can safely exit the app. the asyncronicity of firebase does not let the user get deleted before teh app closes
        binding.DeleteButtonForUserProfile.setOnClickListener(v -> {

            deleteUser(
                    () -> {
                        // goodbye
                        assert getActivity() != null;
                        getActivity().finish();
                        System.exit(0);
                    }
            );


//            NavHostFragment.findNavController(this)
//                    .navigate(R.id.fragment_main);
        });

        binding.cancelButtonForUserProfile.setOnClickListener(v -> {
            nav.navigate(R.id.action_update_user_to_fragment_main);
            //nav.popBackStack();
        });

        binding.adminButtonForUserProfile2.setOnClickListener(v -> {
            if (binding.adminPasswordForUserProfile.getText().toString().equals("80085")) {
                user.setAdmin(true);
                nav.navigate(R.id.admin_main_screen);
            }
        });

        binding.guidelinesButtonForUserProfile3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nav.navigate(R.id.fragment_guidelines);
            }
        });

        binding.geoSwitch.setChecked(user.isGeoEnabled()); // initialize switch

        binding.geoSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
            if (isChecked) {
                //user turned it on
                requestLocationPermissions();
            } else {
                // user turned it off
                user.setGeoEnabled(false);
                Toast.makeText(getContext(), "Geolocation disabled", Toast.LENGTH_SHORT).show();
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

    private void deleteUser(Runnable onComplete) {
        Toast.makeText(getContext(), "Deleted", Toast.LENGTH_SHORT).show();

        UserDeleter deleter = new UserDeleter(user);
        deleter.DeleteUser(onComplete);
    }

    private void requestLocationPermissions() {
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED) {
            user.setGeoEnabled(true);
            Toast.makeText(getContext(), "Precise location permission already granted", Toast.LENGTH_SHORT).show();
            return;
        }

        locationPermissionRequest.launch(new String[]{
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        boolean hasPermission = ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION)
                == PackageManager.PERMISSION_GRANTED;
        binding.geoSwitch.setChecked(user.isGeoEnabled() && hasPermission);
    }
}
