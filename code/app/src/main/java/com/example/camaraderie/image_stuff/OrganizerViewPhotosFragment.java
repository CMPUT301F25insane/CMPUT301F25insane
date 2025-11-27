package com.example.camaraderie.image_stuff;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;
import android.content.ContentResolver;

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

import java.io.ByteArrayOutputStream;
import java.io.IOException;
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
                        try {
                            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContext().getContentResolver(), uri);
                            ByteArrayOutputStream stream = new ByteArrayOutputStream();
                            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream);
                            byte [] bytes = stream.toByteArray();
                            String imageString = Base64.encodeToString(bytes, Base64.DEFAULT);
                            Log.d("ByteString:", imageString);
                            //Need to update into the event
                            eventDocRef = event.getEventDocRef();

                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                    } else {
                        Log.d("PhotoPicker", "No media selected");
                    }
                });


        svm.getEvent().observe(getViewLifecycleOwner(), evt -> {
            this.event = evt;
            eventDocRef = event.getEventDocRef();
        });

        // Decode and put the image into the imageView
        String imageString = eventDocRef.get().getResult().get("imageString").toString();

        byte[] imageBytes = Base64.decode(imageString, Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
        binding.imageView2.setImageBitmap(bitmap);

        binding.backButton.setOnClickListener(v -> nav.popBackStack());

        binding.addPhotosButton.setOnClickListener(v -> {
            // Launch the photo picker and let the user choose only images.
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());


        });


    }

}
