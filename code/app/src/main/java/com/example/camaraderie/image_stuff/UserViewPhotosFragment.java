package com.example.camaraderie.image_stuff;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.camaraderie.Event;
import com.example.camaraderie.SharedEventViewModel;
import com.example.camaraderie.databinding.FragmentUserViewPhotosBinding;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class UserViewPhotosFragment extends Fragment{

    private NavController nav;
    private FirebaseFirestore db;
    private DocumentReference eventDocRef;
    private SharedEventViewModel svm;

    private String imageString;

    private Event event;

    private FragmentUserViewPhotosBinding binding;

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
        binding = FragmentUserViewPhotosBinding.inflate(inflater, container, false);

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

        svm.getEvent().observe(getViewLifecycleOwner(), evt -> {
            this.event = evt;
            eventDocRef = event.getEventDocRef();
            Log.d("Event Doc Ref:", eventDocRef.toString());
            eventDocRef.get().addOnCompleteListener(task -> {
                imageString = task.getResult().getString("imageString");
                // Decode and place the image into the imageView
                if (imageString != null) {
                    byte[] imageBytes = Base64.decode(imageString, Base64.DEFAULT);
                    Log.d("Bytes", imageBytes.toString());
                    Bitmap bitmap = BitmapFactory.decodeByteArray(imageBytes, 0, imageBytes.length);
                    binding.imageView2.setImageBitmap(bitmap);
                }
            });

        });

        binding.backButton.setOnClickListener(v -> nav.popBackStack());


    }
}
