package com.example.camaraderie.image_stuff;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.PickVisualMediaRequest;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.camaraderie.Event;
import com.example.camaraderie.SharedEventViewModel;
import com.example.camaraderie.databinding.FragmentOrganizerViewPhotosBinding;
import com.example.camaraderie.databinding.FragmentViewEventOrganizerBinding;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;

public class OrganizerViewPhotosFragment extends Fragment {

    private NavController nav;
    private FirebaseFirestore db;
    private DocumentReference eventDocRef;
    private SharedEventViewModel svm;

    private Event event;

    private FragmentOrganizerViewPhotosBinding binding;

    /**
     * sets svm, nav, and db.
     *
     * @param savedInstanceState If the fragment is being re-created from
     *                           a previous saved state, this is the state.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        svm = new ViewModelProvider(requireActivity()).get(SharedEventViewModel.class);
        nav = NavHostFragment.findNavController(this);
        db = FirebaseFirestore.getInstance();
    }

    /**
     * set binding
     *
     * @param inflater           The LayoutInflater object that can be used to inflate
     *                           any views in the fragment,
     * @param container          If non-null, this is the parent view that the fragment's
     *                           UI should be attached to.  The fragment should not add the view itself,
     *                           but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     * @return binding root
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentOrganizerViewPhotosBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }

    /**
     * sets bindings for buttons and textviews. svm gets event and updates as appropriate
     *
     * @param view               The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     *                           from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Registers a photo picker activity launcher in single-select mode.
        ActivityResultLauncher<PickVisualMediaRequest> pickMedia =
                registerForActivityResult(new ActivityResultContracts.PickVisualMedia(), uri -> {
                    // Callback is invoked after the user selects a media item or closes the
                    // photo picker.
                    if (uri != null) {
                        Log.d("PhotoPicker", "Selected URI: " + uri);
                        binding.imageView2.setImageURI(uri);
                        //Add code to save the photo into the database
                        uploadImageToFirebase(uri);
                    } else {
                        Log.d("PhotoPicker", "No media selected");
                    }
                });


        svm.getEvent().observe(getViewLifecycleOwner(), evt -> {
            this.event = evt;
            eventDocRef = event.getEventDocRef();
        });

        binding.backButton.setOnClickListener(v -> nav.popBackStack());

        binding.addPhotosButton.setOnClickListener(v -> {
            // Launch the photo picker and let the user choose only images.
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());


        });

    }

    private void uploadImageToFirebase(Uri imageUri) {
        // Show progress indicator

        // Create reference to Firebase Storage
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();
        String filename = "event_photos/" + System.currentTimeMillis() + "_" + UUID.randomUUID().toString() + ".jpg";
        StorageReference imageRef = storageRef.child(filename);

        // Upload file to Firebase Storage
        imageRef.putFile(imageUri)
                .addOnSuccessListener(taskSnapshot -> {
                    // Get download URL
                    imageRef.getDownloadUrl().addOnSuccessListener(downloadUri -> {
                        // Save download URL to Firestore
                        savePhotoUrlToFirestore(downloadUri.toString());
                        Log.d("PhotoPicker", "Image uploaded successfully: " + downloadUri);
                    });
                })
                .addOnFailureListener(e -> {
                    Log.e("PhotoPicker", "Upload failed: " + e.getMessage());
                })
                .addOnProgressListener(snapshot -> {
                    // Show upload progress if needed
                    double progress = (100.0 * snapshot.getBytesTransferred()) / snapshot.getTotalByteCount();
                    Log.d("PhotoPicker", "Upload is " + progress + "% done");
                });
    }

    private void savePhotoUrlToFirestore(String imageUrl) {
        // Add the download URL to the photos array in Firestore
        eventDocRef.update("photos", FieldValue.arrayUnion(imageUrl))
                .addOnSuccessListener(aVoid -> {
                    Log.d("PhotoPicker", "Photo URL saved to Firestore");
                })
                .addOnFailureListener(e -> {
                    Log.e("PhotoPicker", "Error saving photo URL: " + e.getMessage());
                });
    }
}
