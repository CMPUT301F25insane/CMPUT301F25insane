package com.example.camaraderie.image_stuff;


import static com.example.camaraderie.image_stuff.ImageHandler.deleteEventImage;
import static com.example.camaraderie.image_stuff.ImageHandler.uploadEventImage;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

import com.bumptech.glide.Glide;
import com.example.camaraderie.Event;
import com.example.camaraderie.SharedEventViewModel;
import com.example.camaraderie.databinding.FragmentOrganizerViewPhotosBinding;


/**
 * This is what the organizer sees when they look at their own event's poster.
 * They can add a photo or change an existing photo
 */
/**
 * This class allows the organizer to view their photos of their event
 * @author Tahmid-Parvez
 */
public class OrganizerViewPhotosFragment extends Fragment {

    private NavController nav;
    private SharedEventViewModel svm;
    private Event event;


    private FragmentOrganizerViewPhotosBinding binding;

    /**
     * sets svm and nav
     *
     * @param savedInstanceState If the fragment is being re-created from
     *                           a previous saved state, this is the state.
     */
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        svm = new ViewModelProvider(requireActivity()).get(SharedEventViewModel.class);
        nav = NavHostFragment.findNavController(this);
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
                        deleteEventImage(event);
                        uploadEventImage(event, uri, new ImageHandler.UploadCallback() {
                            /**
                             * defines on success callback for image upload
                             * @param downloadUrl new image url
                             */
                            @Override
                            public void onSuccess(String downloadUrl) {
                                event.setImageUrl(downloadUrl);
                                event.updateDB(() -> Toast.makeText(getContext(), "Image saved", Toast.LENGTH_SHORT).show());
                            }

                            /**
                             * defines onFailure callback for image upload
                             * @param e image error
                             */
                            @Override
                            public void onFailure(Exception e) {
                                Log.e("UPLOAD", "Failed", e);
                                Toast.makeText(getContext(), "Upload failed", Toast.LENGTH_SHORT).show();
                            }
                        });
                    } else {
                        Log.d("PhotoPicker", "No media selected");
                    }
                });

        //Get the event and put it into the imageview
        svm.getEvent().observe(getViewLifecycleOwner(), evt -> {
            this.event = evt;

            Glide.with(this).load(event.getImageUrl()).into(binding.imageView2);

        });

        binding.backButton.setOnClickListener(v -> nav.popBackStack());

        binding.addPhotosButton.setOnClickListener(v -> {
            // Launch the photo picker and let the user choose only images.
            pickMedia.launch(new PickVisualMediaRequest.Builder()
                    .setMediaType(ActivityResultContracts.PickVisualMedia.ImageOnly.INSTANCE)
                    .build());


        });


    }

}
