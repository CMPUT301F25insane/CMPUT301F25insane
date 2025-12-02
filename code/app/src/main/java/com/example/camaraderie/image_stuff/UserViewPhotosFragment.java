package com.example.camaraderie.image_stuff;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.bumptech.glide.Glide;
import com.example.camaraderie.Event;
import com.example.camaraderie.SharedEventViewModel;
import com.example.camaraderie.databinding.FragmentUserViewPhotosBinding;


/**
 * This is what the user sees when he wants to view an event's poster
 */
public class UserViewPhotosFragment extends Fragment{

    private NavController nav;
    private SharedEventViewModel svm;
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

            Glide.with(this).load(event.getImageUrl()).into(binding.imageView2);

        });

        binding.backButton.setOnClickListener(v -> nav.popBackStack());


    }
}
