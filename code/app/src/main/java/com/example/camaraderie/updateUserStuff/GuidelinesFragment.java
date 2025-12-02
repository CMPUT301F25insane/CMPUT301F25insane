package com.example.camaraderie.updateUserStuff;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.example.camaraderie.databinding.FragmentGuidelinesBinding;

/**
 * displays the guidelines screen
 */
public class GuidelinesFragment extends Fragment {

    private FragmentGuidelinesBinding binding;

    private NavController nav;

    /**
     * creates binding
     * @param inflater The LayoutInflater object that can be used to inflate
     * any views in the fragment,
     * @param container If non-null, this is the parent view that the fragment's
     * UI should be attached to.  The fragment should not add the view itself,
     * but this can be used to generate the LayoutParams of the view.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     *
     * @return the binding root
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentGuidelinesBinding.inflate(getLayoutInflater());
        return binding.getRoot();
    }

    /**
     * set the back button listener
     * @param view The View returned by {@link #onCreateView(LayoutInflater, ViewGroup, Bundle)}.
     * @param savedInstanceState If non-null, this fragment is being re-constructed
     * from a previous saved state as given here.
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        nav = NavHostFragment.findNavController(this);

        binding.returnButtonForGuidelines.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                nav.popBackStack();
            }
        });
    }

    /**
     * sets binding to null
     */
    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }
}
